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

public class Basic_Ingredient_Nutrient_AmountDAOImpl implements Basic_Ingredient_Nutrient_AmountDAO {
	private SessionFactory sessionFactory;
	private NutrientDAO nutrientDao;
	private Basic_IngredientDAO basicIngredientDao;
	private Basic_Ingredient_Nutrient_Data_SourceDAO basicIngredientNutrientDataSourceDao;



	public static String getNutrientsForBasicIngredientIdQuery=
			"select * from basic_ingredient_nutrient_amount inner join Nutrient on Nutrient.nu_id=basic_ingredient_nutrient_amount.nu_id "+
			"where basic_ingredient_nutrient_amount.bi_id=__bi_id__";
	public static String getNutrientsForBasicIngredientNameQuery=
			"select * from basic_ingredient_nutrient_amount " + 
			"	inner join Nutrient on Nutrient.nu_id=basic_ingredient_nutrient_amount.nu_id " + 
			"	inner join basic_ingredient on basic_ingredient.bi_id=basic_ingredient_nutrient_amount.bi_id " + 
			"where basic_ingredient.name='__name__'";
	



	public Basic_Ingredient_Nutrient_AmountDAOImpl(SessionFactory sessionFactory, NutrientDAO nutrientDao,
			Basic_IngredientDAO basicIngredientDao,
			Basic_Ingredient_Nutrient_Data_SourceDAO basicIngredientNutrientDataSourceDao) {
		super();
		this.sessionFactory = sessionFactory;
		this.nutrientDao = nutrientDao;
		this.basicIngredientDao = basicIngredientDao;
		this.basicIngredientNutrientDataSourceDao = basicIngredientNutrientDataSourceDao;
	}

	@Override
	@Transactional
	public void saveNutrientData(Basic_Ingredient basicIngredient,Basic_Ingredient_Nutrient_Data_Source binds, Map<Nutrient, Float> nutrientsAmounts) {
		if(isAnyOfArgumentsEmpty(basicIngredient,binds,nutrientsAmounts))
			return;
		
		boolean basicIngredientExistsInDb = this.basicIngredientDao.checkIfRecordExistsUpdateArgumentIfNeedBe(basicIngredient);
		
		if(!basicIngredientExistsInDb) {
			basicIngredientDao.saveBasicIngredient(basicIngredient);
		}
		
		for(Entry<Nutrient, Float> e:nutrientsAmounts.entrySet()){
			Nutrient nutrient = e.getKey();
			if(nutrient!=null){
				boolean nutrientIsInDbAndHasId = isNutritientInDb(nutrient);
				
				if(!nutrientIsInDbAndHasId){
					ProblemLogger.logProblem("Nutritient was not found in database: "+nutrient.getName());
				}else{
					Basic_Ingredient_Nutrient_amount basicIngredientNutritientAmount=new Basic_Ingredient_Nutrient_amount();
					basicIngredientNutritientAmount.setBasicIngredient(basicIngredient);
					basicIngredientNutritientAmount.setNutritient(nutrient);
					basicIngredientNutritientAmount.setCoefficient(e.getValue());
					//basicIngredientNutrientDataSourceDao.sa
					basicIngredientNutritientAmount.setDataSource(binds);
					this.basicIngredientNutrientDataSourceDao.saveDataSource(binds);
					this.sessionFactory.getCurrentSession().save(basicIngredientNutritientAmount);
				}
			}
		}
	}

	private boolean isAnyOfArgumentsEmpty(Basic_Ingredient basicIngredient, Basic_Ingredient_Nutrient_Data_Source binds, Map<Nutrient, Float> nutrientsAmounts) {
		if(basicIngredient==null) {
			ProblemLogger.logProblem("Trying to save nutrient data for empty basic ingredient");
			return true;
		}
		if(nutrientsAmounts==null||nutrientsAmounts.isEmpty()) {
			ProblemLogger.logProblem("Trying to save empty nutrient");
			return true;
		}
		if(binds==null) {
			ProblemLogger.logProblem("Trying to save empty nutrient data for basic ingredient: "+basicIngredient);
			return true;
		}
		return false;
	}

	private boolean isNutritientInDb(Nutrient nutrient) {
		boolean nutrientIsInDb=false;
		if(nutrient.getNu_id()==null){
			nutrientIsInDb=false;
		}else{
			Long nu_id=nutrient.getNu_id();
			Nutrient nutrientFromDb=nutrientDao.getNutrientById(nu_id);
			if(nutrientFromDb==null){
				nutrientIsInDb=false;
			}else{
				nutrientIsInDb=true;
			}
			
		}
		return nutrientIsInDb;
	}

	@Override
	@Transactional
	public Map<Nutrient, Float> getNutrientsForBasicIngredientById(Long basicIngredientId) {
		String sql=getNutrientsForBasicIngredientIdQuery.replaceAll("__bi_id__", Long.toString(basicIngredientId));
		
		@SuppressWarnings("unchecked")
		List<Basic_Ingredient_Nutrient_amount> list = 
		(List<Basic_Ingredient_Nutrient_amount>)sessionFactory.getCurrentSession()
		.createSQLQuery(sql)
		.addEntity(Basic_Ingredient_Nutrient_amount.class)
//		.createCriteria(Basic_Ingredient_Nutrient_amount.class)
//		.add(Restrictions.sqlRestriction(sql))
		.list();
		
		Map<Nutrient, Float> retValue=new HashMap<Nutrient, Float>();
		for(Basic_Ingredient_Nutrient_amount bina:list) {
			System.out.println(bina);
			retValue.put(bina.getNutritient(), bina.getCoefficient());
		}
		
		return retValue;
		
	}

	
	@Override
	@Transactional
	public Map<Nutrient, Float> getNutrientsForBasicIngredientByName(String basicIngredientName) {
		basicIngredientName=org.apache.commons.lang.StringEscapeUtils.escapeSql(basicIngredientName);

		String sql=getNutrientsForBasicIngredientNameQuery.replaceAll("__name__", basicIngredientName);

		@SuppressWarnings("unchecked")
		List<Basic_Ingredient_Nutrient_amount> list = 
		(List<Basic_Ingredient_Nutrient_amount>)sessionFactory.getCurrentSession()
		.createSQLQuery(sql)
		.addEntity(Basic_Ingredient_Nutrient_amount.class)
		.list();

		for(Basic_Ingredient_Nutrient_amount bina:list) {
			System.out.println(bina);
		}
		
		return null;
		
	}

	@Override
	@Transactional
	public boolean areNutrientsForBasicIngredient(Long basicIngredientId) {
		return sessionFactory.getCurrentSession()
		.createCriteria(Basic_Ingredient_Nutrient_amount.class)
        .add(Restrictions.eq("basicIngredient.id", basicIngredientId))
        .setProjection(Projections.property("basicIngredient.id"))
        .setFirstResult(0).setMaxResults(1)
        .uniqueResult() != null;
		
		
	}
	
	@Override
	@Transactional
	public boolean areNutrientsForBasicIngredient(String basicIngredientName) {
		basicIngredientName=org.apache.commons.lang.StringEscapeUtils.escapeSql(basicIngredientName);

		return sessionFactory.getCurrentSession()
		.createCriteria(Basic_Ingredient_Nutrient_amount.class)
        .add(Restrictions.eq("basicIngredient.id", basicIngredientName))
        .setProjection(Projections.property("basicIngredient.id"))
        .setFirstResult(0).setMaxResults(1)
        .uniqueResult() != null;
		
		
	}

	@Override
	public List<Basic_Ingredient_Nutrient_amount> getNutrientsForDataSourceById(Long basicIngredientDataSourceId) {
		@SuppressWarnings("unchecked")
		List<Basic_Ingredient_Nutrient_amount> binas = (List<Basic_Ingredient_Nutrient_amount>) sessionFactory.getCurrentSession()
				.createCriteria(Basic_Ingredient_Nutrient_amount.class)
				.add(Restrictions.eq("binds_id", basicIngredientDataSourceId))
				.list();
		return binas;
	}

}
