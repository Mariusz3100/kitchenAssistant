package mariusz.ambroziak.kassistant.dao;

import java.util.List;

import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient_Name;
import mariusz.ambroziak.kassistant.model.Health_Relevant_Ingredient;
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

public class Health_Relevant_IngredientDAOImpl implements Health_Relevant_IngredientDAO {
	private static final String selectBasicIngredientsQuery = 
			"select Health_Relevant_Ingredient.* from Basic_Ingredient_Health_Relevance inner join Health_Relevant_Ingredient on Health_Relevant_Ingredient.hr_id=Basic_Ingredient_Health_Relevance.hr_id where Basic_Ingredient_Health_Relevance.bi_id=__basic_ingredient_id__";
	private SessionFactory sessionFactory;
	
	
	
	
	public Health_Relevant_IngredientDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

	}




		@Override
		@Transactional
		public List<Health_Relevant_Ingredient> list() {
			@SuppressWarnings("unchecked")
			List<Health_Relevant_Ingredient> listUser = (List<Health_Relevant_Ingredient>) sessionFactory.getCurrentSession()
					.createCriteria(Health_Relevant_Ingredient.class)
					.list();

			return listUser;
		}


	@Override
	public List<Health_Relevant_Ingredient> listForSkladnik(int skladnikId) {
		@SuppressWarnings("unchecked")
		List<Health_Relevant_Ingredient> listProdukt = (List<Health_Relevant_Ingredient>) sessionFactory.getCurrentSession()
				.createSQLQuery(selectBasicIngredientsQuery.replaceAll("__basic_ingredient_id__", Integer.toString(skladnikId)))
				.addEntity(Health_Relevant_Ingredient.class)
				.list();

		return listProdukt;
	}

	

}
