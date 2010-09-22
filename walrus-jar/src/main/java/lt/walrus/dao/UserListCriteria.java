package lt.walrus.dao;

import lt.walrus.model.WalrusUser;
import net.mlw.vlh.ValueListInfo;
import net.mlw.vlh.adapter.hibernate3.AbstractCriteriaContentProvider;

import org.hibernate.Criteria;
import org.hibernate.Session;

public class UserListCriteria extends AbstractCriteriaContentProvider {
	protected Criteria getCriteria(ValueListInfo info, Session session) {
		Criteria criteria = session.createCriteria(WalrusUser.class);
		return criteria;
	}
}
