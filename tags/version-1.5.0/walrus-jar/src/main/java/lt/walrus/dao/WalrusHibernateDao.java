package lt.walrus.dao;

import java.io.Serializable;
import java.util.List;

import lt.walrus.model.Banner;
import lt.walrus.model.Box;
import lt.walrus.model.Comment;
import lt.walrus.model.Rubric;
import lt.walrus.model.Site;
import lt.walrus.model.Slide;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

@Repository("dao")
public class WalrusHibernateDao extends HibernateDaoSupport implements IWalrusDao, Serializable {
	private static final long serialVersionUID = 1498683120678869698L;

	public void save(Site site) {
		getHibernateTemplate().merge(site.getRootRubric());
		getHibernateTemplate().merge(site);
		getHibernateTemplate().flush();
	}

	public void save(Rubric rubric) {
		getHibernateTemplate().update(rubric);
		getHibernateTemplate().flush();
	}

	public void save(Box box) {
		getHibernateTemplate().saveOrUpdate(box);
		getHibernateTemplate().flush();
	}

	public void save(Comment c) {
		getHibernateTemplate().saveOrUpdate(c);
		getHibernateTemplate().flush();
	}
	
	public void addRubric(Rubric rubric) {
		getHibernateTemplate().persist(rubric);
		getHibernateTemplate().flush();
	}

	public void deleteRubric(Rubric r) {
		getHibernateTemplate().delete(r);
		getHibernateTemplate().flush();
	}

	public Rubric getRubric(long id) {
		return (Rubric) getHibernateTemplate().get(Rubric.class, id);
	}

	public Site getSite(String host, String language) {
		Site site = new Site();
		site.setLanguage(language);
		site.setHost(host);
		return (Site) getListHead(getHibernateTemplate().findByExample(site, 0, 1));
	}

	private Object getListHead(List<?> list) {
		if (null != list && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public void deleteBanner(Banner banner) {
		getHibernateTemplate().delete(banner);
		getHibernateTemplate().flush();
	}

	@SuppressWarnings("unchecked")
	public List<Site> getAllSites() {
		return getHibernateTemplate().findByCriteria(DetachedCriteria.forClass(Site.class).addOrder(Order.asc("index")));
	}

	public void deleteSite(Site site) {
		getHibernateTemplate().delete(site);
		getHibernateTemplate().flush();
	}

	public void delete(Slide slide) {
		getHibernateTemplate().delete(slide);
		getHibernateTemplate().flush();
	}	

	@Override
	public Comment getComment(long commentId) {
		return (Comment) getHibernateTemplate().get(Comment.class, commentId);
	}

	@Override
	public void deleteComment(Comment comment) {
		getHibernateTemplate().delete(comment);
	}

	@Override
	public void save(Slide slide) {
		getHibernateTemplate().saveOrUpdate(slide);
		getHibernateTemplate().flush();
	}
}
