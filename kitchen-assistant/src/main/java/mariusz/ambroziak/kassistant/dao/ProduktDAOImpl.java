package mariusz.ambroziak.kassistant.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

public class ProduktDAOImpl implements ProduktDAO {
	private SessionFactory sessionFactory;
	private Variant_WordDAO variantWordDao;
	public ProduktDAOImpl(SessionFactory sessionFactory,Variant_WordDAO variantWordDao) {
		this.sessionFactory = sessionFactory;
		this.variantWordDao=variantWordDao;

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
	public Produkt getProduktsByURL(String url) {
		@SuppressWarnings("unchecked")
		List<Produkt> produkts =  sessionFactory.getCurrentSession()
				.createCriteria(Produkt.class)
				.add(Restrictions.eq("url", url))
				.list();
		
		if(produkts.size()>1)
			throw new  DataIntegrityViolationException(
					"In the table Produkt there is more than one produkt for url: "+url);

		
		if(produkts==null || produkts.size()==0)
			return null;
		else
			return produkts.get(0);		
	}
	
	@Override
	@Transactional
	public List<Produkt> getProduktsByUrlILike(String url) {
		@SuppressWarnings("unchecked")
		List<Produkt> produkt =  sessionFactory.getCurrentSession()
				.createCriteria(Produkt.class)
				.add(Restrictions.ilike("url", url))
				.list();
		
		return produkt;
	}

	@Override
	@Transactional
	public List<Produkt> getProduktsByNazwa(String nazwa) {
		nazwa=org.apache.commons.lang.StringEscapeUtils.escapeSql(nazwa);

		@SuppressWarnings("unchecked")
		List<Produkt> produkt =  sessionFactory.getCurrentSession()		
				.createSQLQuery("SELECT * FROM Produkt where nazwa='"+nazwa+"'")
				.addEntity(Produkt.class)
				.list();
		
		return produkt;
	}
	
	@Override
	@Transactional
	public void saveProdukt(Produkt produkt) {
		if(produkt!=null&&(produkt.getOpis()==null||produkt.getOpis().length()<Produkt.opis_length)){
			this.sessionFactory.getCurrentSession().save(produkt);
		}else{
			if(produkt.getOpis()!=null&&produkt.getOpis().length()>Produkt.opis_length)
				ProblemLogger.logProblem("Opis produktu"+produkt.getUrl()+" by--- za d---ugi, by go zapisa---");
		}
		
		
	}

	@Transactional
	@Override
	public List<Produkt> getProduktsBySpacedName(String name) {
		String[] parts=name.split(" ");
		
		ArrayList<String> x=new ArrayList<String>();
		
		for(String y:parts){
			y=org.apache.commons.lang.StringEscapeUtils.escapeSql(y);

			x.add(y);
		}
		
		return getProduktsByNames(x);
	}

	@Transactional
	@Override
	public List<Produkt> getProduktsByNames(Collection<String> parts) {

		if(parts==null||parts.size()==0){
			return new ArrayList<Produkt>();
				
		}else{
			Criteria crit=sessionFactory.getCurrentSession()
					.createCriteria(Produkt.class);
			//crit.add(Restrictions.i));
			for(String x:parts){
				x=org.apache.commons.lang.StringEscapeUtils.escapeSql(x);

				crit=crit.add(Restrictions.ilike("nazwa", "%"+x+"%"));
			}
			//		crit.add(Restrictions.ilike("x", "y"))
			return crit.list();
		}
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Produkt> getProduktsBySpacedVariantName(String name) {
		String[] x=name.split(" ");
		ArrayList<String> namesSet=new ArrayList<String>();
		
		for(int i=0;i<x.length;i++){
			String temp=org.apache.commons.lang.StringEscapeUtils.escapeSql(x[i]);

			namesSet.add(temp);
			
		}
		
		return getProduktsByVariantNames(namesSet);
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public List<Produkt> getProduktsByVariantNames(Collection<String> names) {
		
		List<String> base_Names = variantWordDao.getBase_NamesAsStrings(names);
		
		List<Produkt> produkts = getProduktsByNames(base_Names);
		
		return produkts;
	}
	
	@Transactional
	@Override
	public Produkt getById(Long id) {

		@SuppressWarnings("unchecked")
		List<Produkt> produkts =  sessionFactory.getCurrentSession()
				.createCriteria(Produkt.class)
				.add(Restrictions.idEq(id))
				.list();
		
		if(produkts.size()>1)
			throw new  DataIntegrityViolationException(
					"In the table Produkt there is more than one produkt for id: "+id);

		
		if(produkts==null || produkts.size()==0)
			return null;
		else
			return produkts.get(0);
	}
	
}
