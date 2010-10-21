package lt.walrus.dao;

import java.io.Serializable;

import lt.walrus.model.Rubric;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * Pasirupina, kad root rubrikos orderno nebutu null. Hibernate to negali
 * užtikrinti, nes root rubrika nėra sąraše ir orderno jai neseivinamas.
 * 
 * @author mm
 */
@Repository("initHelperDao")
public class InitHelperDao extends JdbcDaoSupport implements Serializable{
	private static final long serialVersionUID = 3442325578143185575L;

	public void fixRootrubricOrderno(Rubric r) {
		getJdbcTemplate().execute("update rubric set orderno = " + r.getOrderno() + " where id = " + r.getId());
	}

	public void createAdminIfNeeded() {
		int userCount = getJdbcTemplate().queryForInt("select count(*) from walrususer");
		if (0 == userCount) {
			getJdbcTemplate().execute(
					"insert into walrususer (id, email, password, role) values(42, 'admin', '21232f297a57a5a743894a0e4a801fc3', 'ROLE_ADMIN')");
		}

	}
}
