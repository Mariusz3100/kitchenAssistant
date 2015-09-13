package mariusz.ambroziak.kassistant.dao;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import mariusz.ambroziak.kassistant.model.Amount_Type;
import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.Recipe_Ingredient;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.Variant_Word;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

public class Recipe_IngredientDAOImpl implements Recipe_IngredientDAO {
	private SessionFactory sessionFactory;
	
	
	private static String joinBaseQ="select base_word.* from base_word  inner join variant_word on variant_word.base_word_id=base_word.id where v_word='__variant_word__'";
	private static String joinManyBasesQ="select base_word.* from base_word  inner join variant_word on variant_word.base_word_id=base_word.id where v_word in (__variant_words__)";
	
	public Recipe_IngredientDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional
	public List<Recipe_Ingredient> list() {
		@SuppressWarnings("unchecked")
		List<Recipe_Ingredient> listProdukt = (List<Recipe_Ingredient>) sessionFactory.getCurrentSession()
				.createCriteria(Recipe_Ingredient.class)
				.list();

		return listProdukt;
	}

	
	
	@Transactional
	@Override
	public List<Recipe_Ingredient> getByRecID(Long recID) {
		@SuppressWarnings("unchecked")
		List<Recipe_Ingredient> listProdukt = (List<Recipe_Ingredient>) sessionFactory.getCurrentSession()
				.createCriteria(Recipe_Ingredient.class)
				.add(Restrictions.eq("rec_id", recID))
				.list();

		return listProdukt;
	
	}

	@Override
	public void addRecipe_Ingredient(Recipe_Ingredient ri) {
		this.sessionFactory.getCurrentSession().save(ri);
		
	}

//	@Override
//	public void addIngredientAndAmount(Recipe_Ingredient ri, String amountType) {
//		@SuppressWarnings("unused")
//		List<Amount_Type> amounts = (List<Amount_Type>) sessionFactory.getCurrentSession()
//				.createCriteria(Amount_Type.class)
//				.add(Restrictions.eq("nazwa", amountType))
//				.list();
//		
//		if(amounts.size()==0||amounts.size()>1)
//			throw new  DataIntegrityViolationException(
//					"In the table Amount_Type there is more than one recipe for name: "+amountType);
//		else{
//			
//		}
//	}


	
	



	

}
