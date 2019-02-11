package mariusz.ambroziak.kassistant.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient_Nutrient_Data_Source;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient_Nutrient_amount;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.Variant_Word;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

public class Basic_Ingredient_Nutrient_Data_SourceDAOImpl implements Basic_Ingredient_Nutrient_Data_SourceDAO {
	public Basic_Ingredient_Nutrient_Data_SourceDAOImpl(SessionFactory sessionFactory) {
		super();
		this.sessionFactory = sessionFactory;
	}
	private SessionFactory sessionFactory;
	private static String selectSql="SELECT basic_ingredient_nutrient_data_source.* from basic_ingredient_nutrient_data_source where bi_id =__bi_id__";
	
	@Override
	public void saveDataSource(Basic_Ingredient_Nutrient_Data_Source dataSource) {
		this.sessionFactory.getCurrentSession().save(dataSource);

	}
	
	@Override
	public Basic_Ingredient_Nutrient_Data_Source getDataSource(Long Basic_Ingredient_Nutrient_Data_Source_id) {
		Basic_Ingredient_Nutrient_Data_Source list = 
				(Basic_Ingredient_Nutrient_Data_Source)sessionFactory.getCurrentSession()
				.createCriteria(Basic_Ingredient_Nutrient_Data_Source.class)
				.add(Restrictions.idEq(Basic_Ingredient_Nutrient_Data_Source_id))
				.uniqueResult();
				
		return list;
	}
	@Override
	public Basic_Ingredient_Nutrient_Data_Source getDataSourceByBasicIngredientId(Basic_Ingredient basic_Ingredient) {
		if(basic_Ingredient==null||basic_Ingredient.getBi_id()==null)
			return null;
		
		@SuppressWarnings("unchecked")
		Basic_Ingredient_Nutrient_Data_Source ds = (Basic_Ingredient_Nutrient_Data_Source)sessionFactory.getCurrentSession()
		.createSQLQuery(selectSql.replaceAll("__bi_id__", Long.toString(basic_Ingredient.getBi_id())))
		.addEntity(Basic_Ingredient_Nutrient_Data_Source.class)
		.uniqueResult();
		
		return ds;		
	}
	


}
