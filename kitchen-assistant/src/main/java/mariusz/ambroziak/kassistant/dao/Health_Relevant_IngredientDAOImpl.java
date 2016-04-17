package mariusz.ambroziak.kassistant.dao;

import java.util.List;

import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient_Name;
import mariusz.ambroziak.kassistant.model.Nutrient;
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
		public List<Nutrient> list() {
			@SuppressWarnings("unchecked")
			List<Nutrient> listUser = (List<Nutrient>) sessionFactory.getCurrentSession()
					.createCriteria(Nutrient.class)
					.list();

			return listUser;
		}


	@Override
	public List<Nutrient> listForSkladnik(int skladnikId) {
		@SuppressWarnings("unchecked")
		List<Nutrient> listProdukt = (List<Nutrient>) sessionFactory.getCurrentSession()
				.createSQLQuery(selectBasicIngredientsQuery.replaceAll("__basic_ingredient_id__", Integer.toString(skladnikId)))
				.addEntity(Nutrient.class)
				.list();

		return listProdukt;
	}

	

}
