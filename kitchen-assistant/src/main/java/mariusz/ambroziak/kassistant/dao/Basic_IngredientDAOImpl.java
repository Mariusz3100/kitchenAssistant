package mariusz.ambroziak.kassistant.dao;

import java.util.List;

import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.Variant_Word;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

public class Basic_IngredientDAOImpl implements Basic_IngredientDAO {
	private SessionFactory sessionFactory;
	
	private static final String selectBase="select basic_ingredient.* from basic_ingredient_name right join basic_ingredient on basic_ingredient_name.bi_id=basic_ingredient.bi_id\r\n" + 
			" where possible_name  ='__name__' or name='__name__' limit 1";

	public Basic_IngredientDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

	}

	@Override
	@Transactional
	public List<Basic_Ingredient> list() {
		@SuppressWarnings("unchecked")
		List<Basic_Ingredient> listProdukt = (List<Basic_Ingredient>) sessionFactory.getCurrentSession()
				.createSQLQuery("SELECT * FROM Basic_Ingredient")
				.addEntity(Basic_Ingredient.class)
				.list();

		return listProdukt;
	}

	@Override
	@Transactional
	public Basic_Ingredient getIngredientByName(String name) {
		@SuppressWarnings("unchecked")
		List<Basic_Ingredient> listProdukt = (List<Basic_Ingredient>) sessionFactory.getCurrentSession()
				.createCriteria(Basic_Ingredient.class)
				.add(Restrictions.eq("name", name))
				.list();

		if(listProdukt==null||listProdukt.size()<1)
			return null;
		else
			return listProdukt.get(0);
	}
	
	@Override
	@Transactional
	public Basic_Ingredient getIngredientByAnyName(String name) {
		@SuppressWarnings("unchecked")
		List<Basic_Ingredient> ingredients = (List<Basic_Ingredient>) sessionFactory.getCurrentSession()
				.createSQLQuery(selectBase.replaceAll("__name__", name))
				.addEntity(Basic_Ingredient.class)
				.list();

		if(ingredients==null||ingredients.size()<1)
			return null;
		else
			return ingredients.get(0);
	}

	@Override
	@Transactional
	public void saveBasicIngredient(Basic_Ingredient ingredient) {
		sessionFactory.getCurrentSession().save(ingredient);
		
	}

}
