package mariusz.ambroziak.kassistant.dao;

import java.util.List;

import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

public class ProduktDAOImpl implements ProduktDAO {
	private SessionFactory sessionFactory;

	public ProduktDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional
	public List<Produkt> list() {
		@SuppressWarnings("unchecked")
		List<Produkt> listProdukt = (List<Produkt>) sessionFactory.getCurrentSession()
				.createCriteria(Produkt.class)
				.list();

		return listProdukt;
	}

//	@Override
//	public Produkt getProduktByURL(String url) {
//
//		  String SELECT = " SELECT * "
//		                + " FROM hosts"
//		                + " WHERE is_template=false AND id = :id";
//		  SqlParameterSource namedParameters = new MapSqlParameterSource("url", url);
//		  return getSimpleJdbcTemplate().queryForObject(SELECT, new NagiosHostMapper(), namedParameters);
//		return null;
//	}

	@Override
	@Transactional
	public Produkt getProduktByURL(String url) {
		Produkt produkt = (Produkt) sessionFactory.getCurrentSession()
				.createCriteria(Produkt.class)
				.add(Restrictions.eq("url", url))
				.list().get(0);
		
		return produkt;
	}

}
