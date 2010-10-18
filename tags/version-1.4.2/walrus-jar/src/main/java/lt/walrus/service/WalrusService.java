package lt.walrus.service;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import lt.walrus.dao.IWalrusDao;
import lt.walrus.dao.InitHelperDao;
import lt.walrus.model.Banner;
import lt.walrus.model.Box;
import lt.walrus.model.Comment;
import lt.walrus.model.Rubric;
import lt.walrus.model.Site;
import lt.walrus.model.Sites;
import lt.walrus.model.Slide;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

/**
 * Main Walrus service
 */
public class WalrusService implements Serializable, CRUDService<Rubric> {
	private static final long serialVersionUID = 2369543694124835551L;
	/**
	 * All sites managed by Walrus
	 */
	private Sites sites; // private HashMap<String, Site> sites = new
							// HashMap<String, Site>();
	/**
	 * List of languages every site is available in
	 */
	private List<String> availableLanguages;
	/**
	 * Walrus DAO service
	 */
	private IWalrusDao dao;
	/**
	 * Site initialization helper for persistency
	 */
	private InitHelperDao initHelper;

	protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

	/**
	 * Returns rubric by id or url. The search is performed in all sites. TODO:
	 * this makes rubric URL unique for all sites, not just one site. fix this.
	 * 
	 * @param l
	 * @return
	 */
	public Rubric getRubric(long l) {
		return findRubricInAllSites(l);
	}

	private Rubric findRubricInAllSites(long l) {
		for (Site site : sites.getSites().values()) {
			Rubric rubric = getRubric(site.getRootRubric(), l);
			if (null != rubric) {
				return rubric;
			}
		}
		return null;
	}

	private Rubric getRubric(Rubric parent, long l) {
		if (null == parent) {
			return null;
		}
		if (parent.getId() == l) {
			return parent;
		}
		for (Iterator<Rubric> i = parent.getChildren().iterator(); i.hasNext();) {
			Rubric ret = getRubric(i.next(), l);
			if (null != ret) {
				return ret;
			}
		}
		return null;
	}

	/**
	 * Adds a rubric. Rubric must have parent rubric set.
	 * 
	 * @param rubric
	 *            rubric to add
	 * @param rubricIndex
	 *            index at which rubric should be added to parent's child list
	 */
	public void addRubric(Rubric rubric, int rubricIndex) {
		rubric.getParent().addChild(rubric, rubricIndex);
		dao.save(rubric.getParent());
	}

	/**
	 * Deletes a rubric
	 * 
	 * @param r
	 *            rubric to delete
	 */
	public void deleteRubric(Rubric r) {
		if (null != r) {
			if (null != r.getParent()) {
				r.getParent().deleteChild(r);
				dao.deleteRubric(r);
				dao.save(r.getParent());
			}
		}
	}

	/**
	 * Persists an object
	 * 
	 * @param context
	 */
	public void save(Object context) {
		if (context instanceof Rubric) {
			save((Rubric) context);
		} else if (context instanceof Box) {
			save((Box) context);
		} else if (context instanceof Site) {
			save((Site) context);
		} else if (context instanceof Comment) {
			save((Comment) context);
		} else if (context instanceof Slide) {
			save((Slide) context);
		} else {
			throw new IllegalArgumentException("Dont't know how to save object of type " + context.getClass().getCanonicalName());
		}
	}

	private void save(Rubric rubric) {
		dao.save(rubric);
	}

	private void save(Box box) {
		dao.save(box);
	}

	private void save(Site site) {
		dao.save(site);
	}

	private void save(Comment comment) {
		dao.save(comment);
	}

	private void save(Slide slide) {
		dao.save(slide);
	}
	/**
	 * Moves rubric to another parent
	 * 
	 * @param to
	 *            destination rubric
	 * @param subject
	 *            rubric to move
	 * @param rubricIndex
	 *            index at which rubric should be places in destination children
	 *            list
	 */
	public void moveSubrubricToRubric(Rubric to, Rubric subject, int rubricIndex) {
		to.addChild(subject, rubricIndex);
		subject.setOrderno(rubricIndex);
		save(to);
		save(subject);
	}

	/**
	 * Returns a site. A site is characterized by host name and language. i.e.
	 * host and language compose a unique locator for a site.
	 * 
	 * @param host
	 * @param language
	 * @return site for specified host and language
	 */
	public Site getSite(String host, String language) {
		return getSite(host, language, false);
	}

	/**
	 * Returns a site. If site is not found, it can be created for given host
	 * and language using prototype site defined in dummyData.xml
	 * 
	 * @param host
	 * @param language
	 * @param createSite
	 *            if true - site will be created when not found
	 * @return
	 */
	public Site getSite(String host, String language, boolean createSite) {
		if (sites.isEmpty()) {
			loadAllSites();
		}

		Site site = getSites().get(host, language);
		if (null != site) {
			return site;
		} else {
			site = dao.getSite(host, language);
			if (null == site) {
				if (createSite) {
					initSite(host, language);
				} else {
					return null;
				}
			} else {
				getSites().put(host + language, site);
			}
			return getSites().get(host, language);
		}
	}

	private void loadAllSites() {
		List<Site> allSites = dao.getAllSites();
		if (null != allSites) {
			for (Site site : allSites) {
				sites.put(site.getHost() + site.getLanguage(), site);
				if (StringUtils.hasText(site.getHostAliases())) {
					String[] aliases = site.getHostAliases().split(",");
					if (null != aliases && aliases.length > 0) {
						for (String alias : aliases) {
							if (null != alias) {
								alias = alias.trim();
								if (StringUtils.hasText(alias)) {
									sites.put(alias + site.getLanguage(), site);
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Get all sites for given language
	 * 
	 * @param language
	 * @return all sites for given language
	 */
	public List<Site> getSites(String language) {
		return sites.getList(language);
	}

	private void initSite(String host, String language) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("conf/site.xml");
		Site sitePrototype = (Site) context.getBean("sitePrototype");

		if (null == sitePrototype) {
			sitePrototype = new Site();
			sitePrototype.setRootRubric(new Rubric("ROOT - AUTOGENERATED"));
		}

		sitePrototype.getRootRubric().initChildrenOrderNos();
		sitePrototype.getRootRubric().setOrderno(0);
		sitePrototype.setLanguage(language);
		sitePrototype.setHost(host);

		if (null == sitePrototype.getTitle()) {
			sitePrototype.setTitle(host);
		}

		int index = 0;
		if (!getSites().getList(language).isEmpty()) {
			index = getSites().getList(language).get(getSites().getList(language).size() - 1).getIndex() + 1;
		}
		sitePrototype.setIndex(index);

		addSite(sitePrototype);

		initHelper.createAdminIfNeeded();
		logger.info("WARNING! Walrus did not find previous site in database. NEW SITE HAS BEEN CREATED. Your new site ID is: " + sitePrototype.getId());
	}

	public IWalrusDao getDao() {
		return dao;
	}

	public void setDao(IWalrusDao dao) {
		this.dao = dao;
	}

	public List<String> getAvailableLanguages() {
		return availableLanguages;
	}

	public void setAvailableLanguages(List<String> availableLanguages) {
		this.availableLanguages = availableLanguages;
	}

	public void deleteBanner(Banner banner) {
		dao.deleteBanner(banner);
	}

	public void delete(Slide slide) {
		dao.delete(slide);
	}

	public InitHelperDao getInitHelper() {
		return initHelper;
	}

	public void setInitHelper(InitHelperDao initHelper) {
		this.initHelper = initHelper;
	}

	public Site getSiteById(String id) {
		return sites.getSiteById(id);
	}

	public void deleteSite(Site site) {
		sites.remove(site.getHost() + site.getLanguage());
		dao.deleteSite(site);
	}

	/**
	 * Add a site to Walrus
	 * 
	 * @param site
	 */
	public void addSite(Site site) {
		site.getRootRubric().setOrderno(0);
		dao.save(site);
		// load site from db to get initialized ids
		Site site2 = dao.getSite(site.getHost(), site.getLanguage());
		initHelper.fixRootrubricOrderno(site2.getRootRubric());
		sites.put(site.getHost() + site.getLanguage(), site2);
	}

	public Sites getSites() {
		return sites;
	}

	public void setSites(Sites sites) {
		this.sites = sites;
	}

	/**
	 * Adds a rubric, rubric must have parent rubric set.
	 */
	public void add(Rubric o) {
		addRubric(o, 0);
	}

	/**
	 * Deletes a rubric
	 */
	public void delete(Rubric o) {
		deleteRubric(o);
	}

	/**
	 * @return a rubric by id
	 */
	public Rubric load(long id) {
		return getRubric(id);
	}

	/**
	 * Save a rubric
	 */
	public void update(Rubric o) {
		save(o);
	}

	/**
	 * @param commentId
	 * @return comment by id
	 */
	public Comment getComment(String commentId) {
		return dao.getComment(commentId);
	}

	/**
	 * Delete comment
	 * 
	 * @param comment
	 */
	public void delete(Comment comment) {
		if (null == comment) {
			return;
		}
		Rubric rubric = comment.getRubric();
		List<Comment> comments = rubric.getComments();
		comments.remove(comment);
		// sita nesamone darom tam, kad commente akivaizdziai buna kitas rubric
		// instanceas... :-/
		getRubric(rubric.getId()).getComments().remove(comment);
		dao.deleteComment(comment);
		dao.save(comment.getRubric());
	}

	/**
	 * Add a comment
	 * 
	 * @param comment
	 * @param index
	 *            index at which comment is added
	 */
	public void addComment(Comment comment, int index) {
		Rubric rubric = getRubric(comment.getRubric().getId());
		comment.setRubric(rubric);
		rubric.getComments().add(index, comment);
		save(comment);
		save(rubric);
	}

	public Rubric getRubricByUrl(String newValue) {
		// TODO Auto-generated method stub
		return null;
	}
}
