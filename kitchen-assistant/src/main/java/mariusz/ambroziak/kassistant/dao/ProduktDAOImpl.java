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
	public List<Produkt> getProduktsByURL(String url) {
		@SuppressWarnings("unchecked")
		List<Produkt> produkt =  sessionFactory.getCurrentSession()
				.createCriteria(Produkt.class)
				.add(Restrictions.eq("url", url))
				.list();
		
		return produkt;
	}

	
	
	@Override
	@Transactional
	public void addProdukt(Produkt produkt) {
		this.sessionFactory.getCurrentSession().save(produkt);
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Produkt> getProduktsBySpacedName(String name) {
		String[] parts=name.split(" ");
		
		Criteria crit=sessionFactory.getCurrentSession()
				.createCriteria(Produkt.class);
		//crit.add(Restrictions.i));
		for(String x:parts){
			crit=crit.add(Restrictions.ilike("nazwa", "%"+x+"%"));
		}
//		crit.add(Restrictions.ilike("x", "y"))
		return crit.list();
	}


//	private ProduktDAO singleton;
//	
//	public ProduktDAO getSingleton(){
//		return singleton;
//	}



}
