package mariusz.ambroziak.kassistant.dao;

import java.util.List;

import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient_Name;
import mariusz.ambroziak.kassistant.model.Nutrient_Name;
import mariusz.ambroziak.kassistant.model.Problem;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.Recipe_Ingredient;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.Variant_Word;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

public class Nutrient_NameDAOImpl implements Nutrient_NameDAO {
	private SessionFactory sessionFactory;
	
	public static String selectNutrientNameQuery="select * from nutrient_name  order by char_length(possible_name) desc";
	//public static String countBasicIngredientsQuery="select count(*) from nutrient_name";
	
	private static  List<Nutrient_Name> cache;
	
	
	public Nutrient_NameDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

	}

	
	@Transactional
	public List<Nutrient_Name> getListFromDb() {
		@SuppressWarnings("unchecked")
		List<Nutrient_Name> listProdukt = (List<Nutrient_Name>) sessionFactory.getCurrentSession()
				.createSQLQuery(selectNutrientNameQuery)
				.addEntity(Nutrient_Name.class)
				.list();

		return listProdukt;
	}
	
	@Transactional
	public Long listSize() {

		
		Number x=(Number) (sessionFactory.getCurrentSession().createCriteria(Nutrient_Name.class)
                .setProjection(Projections.rowCount()).uniqueResult());
		System.out.println(x);
		return x.longValue();
	}

	
	
	@Override
	@Transactional
	public List<Nutrient_Name> list() {
		if(cache==null||cache.size()==0||cache.size()<listSize()){
			cache=getListFromDb();
		}
		
		return cache;

	}
}
