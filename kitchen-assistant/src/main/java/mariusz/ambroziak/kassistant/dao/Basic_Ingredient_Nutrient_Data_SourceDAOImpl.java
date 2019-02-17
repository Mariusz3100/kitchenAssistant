package mariusz.ambroziak.kassistant.dao;

import java.util.ArrayList;
import java.util.Collection;
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
	private static String selectByIngredientIdSql="SELECT basic_ingredient_nutrient_data_source.* from basic_ingredient_nutrient_data_source where bi_id =__bi_id__";
	private static String selectByIngredientsNameSql="select * from basic_ingredient " + 
			"inner join basic_ingredient_nutrient_data_source on basic_ingredient_nutrient_data_source.bi_id=basic_ingredient.bi_id " + 
			"where __where_clause__";
	
	private static String selectByDataSourceApiIdSql="select * from basic_ingredient_nutrient_data_source  where id_in_api  ='__id_in_api__'";
	@Override
	public void saveDataSource(Basic_Ingredient_Nutrient_Data_Source dataSource) {
		this.sessionFactory.getCurrentSession().save(dataSource);

	}

	@Override
	@Transactional
	public Basic_Ingredient_Nutrient_Data_Source getDataSource(Long Basic_Ingredient_Nutrient_Data_Source_id) {
		Basic_Ingredient_Nutrient_Data_Source list = 
				(Basic_Ingredient_Nutrient_Data_Source)sessionFactory.getCurrentSession()
				.createCriteria(Basic_Ingredient_Nutrient_Data_Source.class)
				.add(Restrictions.idEq(Basic_Ingredient_Nutrient_Data_Source_id))
				.uniqueResult();

		return list;
	}
	@Override
	@Transactional
	public Basic_Ingredient_Nutrient_Data_Source getDataSourceByBasicIngredientId(Basic_Ingredient basic_Ingredient) {
		if(basic_Ingredient==null||basic_Ingredient.getBi_id()==null)
			return null;

		@SuppressWarnings("unchecked")
		Basic_Ingredient_Nutrient_Data_Source ds = (Basic_Ingredient_Nutrient_Data_Source)sessionFactory.getCurrentSession()
		.createSQLQuery(selectByIngredientIdSql.replaceAll("__bi_id__", Long.toString(basic_Ingredient.getBi_id())))
		.addEntity(Basic_Ingredient_Nutrient_Data_Source.class)
		.uniqueResult();

		return ds;		
	}

	@Override
	@Transactional
	public List<Basic_Ingredient_Nutrient_Data_Source> getDataSourceBy_ApiId(String apiId) {
		@SuppressWarnings("unchecked")
		List<Basic_Ingredient_Nutrient_Data_Source> list = 
		(List<Basic_Ingredient_Nutrient_Data_Source>)sessionFactory.getCurrentSession()
		.createCriteria(Basic_Ingredient_Nutrient_Data_Source.class)
		.add(Restrictions.eq("id_in_api", apiId))
		.list();

		return list;
	}

	@Override
	@Transactional
	public List<Basic_Ingredient_Nutrient_Data_Source> getDataSourceBy_ApiId_sql(String apiId) {
		if(apiId==null||apiId.equals("")) {
			return new ArrayList<>();
		}
		
		String sql=selectByDataSourceApiIdSql.replaceAll("__id_in_api__",apiId);		
		@SuppressWarnings("unchecked")
		List<Basic_Ingredient_Nutrient_Data_Source> dataSources = (List<Basic_Ingredient_Nutrient_Data_Source>)sessionFactory.getCurrentSession()
		.createSQLQuery(sql)
		.addEntity(Basic_Ingredient_Nutrient_Data_Source.class)
		.list();

		return dataSources;
	}
	@Override
	public List<Basic_Ingredient_Nutrient_Data_Source> getDataSourcesByIngredientNames(Collection<String> names) {
		String whereClause = getProperIlike_WhereClause(names);
		String sql=selectByIngredientsNameSql.replace("__where_clause__", whereClause);
		@SuppressWarnings("unchecked")
		List<Basic_Ingredient_Nutrient_Data_Source> dataSources = (List<Basic_Ingredient_Nutrient_Data_Source>)sessionFactory.getCurrentSession()
		.createSQLQuery(sql)
		.addEntity(Basic_Ingredient_Nutrient_Data_Source.class)
		.list();

		return dataSources;	
	}

	private String getProperIlike_WhereClause(Collection<String> names) {
		String whereClause=null;
		if(names==null) {

		}else {
			for(String singleName:names) {
				if(whereClause==null)
					whereClause=" basic_ingredient.name ilike '"+singleName+"'";
				else
					whereClause+=" AND basic_ingredient.name ilike '"+singleName+"'";
			}
		}
		if(whereClause==null)whereClause="true";

		return whereClause;
	}



}
