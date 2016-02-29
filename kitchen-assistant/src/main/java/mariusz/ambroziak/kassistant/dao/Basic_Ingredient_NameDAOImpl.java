package mariusz.ambroziak.kassistant.dao;

import java.util.List;

import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient_Name;
import mariusz.ambroziak.kassistant.model.Problem;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.Recipe_Ingredient;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.Variant_Word;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

public class Basic_Ingredient_NameDAOImpl implements Basic_Ingredient_NameDAO {
	private SessionFactory sessionFactory;
	
	public static String selectBasicIngredientsQuery="select * from basic_ingredient_name  order by char_length(possible_name) desc";
	public static String countBasicIngredientsQuery="select count(*) from basic_ingredient_name";
	
	private static  List<Basic_Ingredient_Name> cache;
	
	
	public Basic_Ingredient_NameDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

	}

	
	
	public List<Basic_Ingredient_Name> getListFromDb() {
		@SuppressWarnings("unchecked")
		List<Basic_Ingredient_Name> listProdukt = (List<Basic_Ingredient_Name>) sessionFactory.getCurrentSession()
				.createSQLQuery(selectBasicIngredientsQuery)
				.addEntity(Basic_Ingredient_Name.class)
				.list();

		return listProdukt;
	}
	
	
	public Long listSize() {
		return ( (Long) (sessionFactory.getCurrentSession().createQuery("select count(*) from Basic_Ingredient_Name bin").iterate().next()));
	}

	@Override
	@Transactional
	public List<Basic_Ingredient_Name> list() {
		if(cache==null||cache.size()==0||cache.size()<listSize()){
			cache=getListFromDb();
		}
		
		return cache;

	}
}
