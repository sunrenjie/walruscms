package lt.walrus.dao;

import java.util.List;

import lt.walrus.model.WalrusUser;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.security.core.userdetails.UserDetails;

public class UserServiceDao extends HibernateDaoSupport {
	HibernateTransactionManager transactionManager;

	@SuppressWarnings("unchecked")
	public List<WalrusUser> getAll() {
		return getHibernateTemplate().loadAll(WalrusUser.class);
	}
	@SuppressWarnings("unchecked")
	public List<WalrusUser> findUser(String q) {
		DetachedCriteria criteria = DetachedCriteria.forClass(WalrusUser.class).add(Restrictions.ilike("lastName", q, MatchMode.START));
		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	public UserDetails getUserByEmail(String username) {
		List<UserDetails> list = getHibernateTemplate().findByCriteria(DetachedCriteria.forClass(WalrusUser.class).add(Property.forName("email").eq(username)));
		if (null != list && list.size() > 0) {
			return (UserDetails) list.get(0);
		} else {
			logger.debug("UserServiceDao.getUserByEmail: unsuccessful login attempt: " + username);
			return null;
		}
	}

	public HibernateTransactionManager getTransactionManager() {
		return transactionManager;
	}

	public void setTransactionManager(HibernateTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public WalrusUser loadUser(String id) {
		return (WalrusUser) getHibernateTemplate().load(WalrusUser.class, id);
	}

	public void delete(WalrusUser user) {
		getHibernateTemplate().delete(user);
		getHibernateTemplate().flush();
	}

	public void save(WalrusUser user) {
		getHibernateTemplate().update(user);
		getHibernateTemplate().flush();
	}

	public void persist(WalrusUser user) {
		getHibernateTemplate().persist(user);
		getHibernateTemplate().flush();
	}

	@SuppressWarnings("unchecked")
	public WalrusUser getByInviteKey(String inviteKey) {
		List<UserDetails> list = getHibernateTemplate().findByCriteria(
				DetachedCriteria.forClass(WalrusUser.class).add(Property.forName("inviteKey").eq(inviteKey)));
		if (null != list && list.size() > 0) {
			return (WalrusUser) list.get(0);
		} else {
			logger.debug("UserServiceDao.getUserByEmail: unsuccessful registration attempt: " + inviteKey);
			return null;
		}
	}

	public void evict(WalrusUser userFromDao) {
		getHibernateTemplate().evict(userFromDao);
		getHibernateTemplate().flush();
	}
}
