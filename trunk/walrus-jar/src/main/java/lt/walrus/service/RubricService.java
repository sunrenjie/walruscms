package lt.walrus.service;

import java.io.Serializable;
import java.util.Iterator;

import lt.walrus.dao.IWalrusDao;
import lt.walrus.model.Rubric;
import lt.walrus.model.Site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service("rubricService")
public class RubricService implements Serializable, CRUDService<Rubric> {
	private static final long serialVersionUID = 2369543694124835551L;
	protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

	@Autowired
	private SiteService siteService;

	/**
	 * Walrus DAO service
	 */
	@Autowired
	IWalrusDao dao;

	/**
	 * Returns rubric by id. The search is performed in all sites.
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Rubric get(long id) {
		return findRubricInAllSites(id);
	}

	private Rubric findRubricInAllSites(long id) {
		for (Site site : siteService.getAllSites()) {
			return getRubric(site.getRootRubric(), id, null);
		}
		return null;
	}

	/**
	 * Finds rubric recursively by it's id or permalink
	 * 
	 * @param parent
	 *            parent rubric to start search from
	 * @param id
	 *            id of rubric we are looking
	 * @param permalink
	 *            permalink of rubric we are looking
	 * @return
	 */
	private Rubric getRubric(Rubric parent, long id, String permalink) {
		if (null == parent) {
			return null;
		}
		if ((StringUtils.hasText(permalink) && permalink.equals(parent.getUrl())) || parent.getId() == id) {
			return parent;
		}
		for (Iterator<Rubric> i = parent.getChildren().iterator(); i.hasNext();) {
			Rubric ret = getRubric(i.next(), id, permalink);
			if (null != ret) {
				return ret;
			}
		}
		return null;
	}

	/**
	 * Gets rubric from site by rubrics permalink
	 * 
	 * @param site
	 * @param permalink
	 * @return
	 */
	public Rubric getRubric(Site site, String permalink) {
		if (null == site || !StringUtils.hasText(permalink)) {
			return null;
		}
		return getRubric(site.getRootRubric(), 0, permalink);
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
	 * Adds a rubric, rubric must have parent rubric set.
	 */
	public void add(Rubric o) {
		addRubric(o, 0);
	}

	/**
	 * Deletes a rubric
	 * 
	 * @param r
	 *            rubric to delete
	 */
	public void delete(Rubric r) {
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

	public void save(Rubric rubric) {
		dao.save(rubric);
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

	public IWalrusDao getDao() {
		return dao;
	}

	public void setDao(IWalrusDao dao) {
		this.dao = dao;
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}
}
