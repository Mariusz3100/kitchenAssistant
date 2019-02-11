package mariusz.ambroziak.kassistant.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
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

public class NutrientDAOImpl implements NutrientDAO {
	private SessionFactory sessionFactory;
	private Basic_IngredientDAO basicIngredient;
	
//	public static String getNutrientsForBasicIngredientIdQuery=
//			"select * from basic_ingredient_nutrient_amount inner join Nutrient on Nutrient.nu_id=basic_ingredient_nutrient_amount.nu_id "+
//			"where basic_ingredient_nutrient_amount.bi_id=__bi_id__";
	public static String getNutrientsForBasicIngredientIdQuery=
			"select * from basic_ingredient_nutrient_amount";
	public static String getNutrientsForBasicIngredientNameQuery=
			"select * from basic_ingredient_nutrient_amount " + 
			"	inner join Nutrient on Nutrient.nu_id=basic_ingredient_nutrient_amount.nu_id " + 
			"	inner join basic_ingredient on basic_ingredient.bi_id=basic_ingredient_nutrient_amount.bi_id " + 
			"where basic_ingredient.name='__name__'";
	
	public NutrientDAOImpl(SessionFactory sessionFactory, Basic_IngredientDAO basicIngredient) {
		this.sessionFactory = sessionFactory;
		this.basicIngredient=basicIngredient;
	}

	@Override
	@Transactional
	public List<Nutrient> list() {
		@SuppressWarnings("unchecked")
		List<Nutrient> listProdukt = (List<Nutrient>) sessionFactory.getCurrentSession()
				.createCriteria(Nutrient.class)
				.list();

		return listProdukt;
	}

	@Override
	@Transactional
	public Nutrient getNutrientById(Long nutrientId) {
		@SuppressWarnings("unchecked")
		List<Nutrient> listNutrients = (List<Nutrient>) sessionFactory.getCurrentSession()
				.createCriteria(Nutrient.class)
				.add(Restrictions.idEq(nutrientId))
				.list();

		if(listNutrients==null||listNutrients.isEmpty())
			return null;
		else{
				if(listNutrients.size()>1)
					ProblemLogger.logProblem("Two Nutrients for id "+nutrientId);
				
				return listNutrients.get(0);
		}
	}
	
	@Override
	@Transactional
	public Nutrient getNutrientByName(String name) {
		@SuppressWarnings("unchecked")
		List<Nutrient> listNutrients = (List<Nutrient>) sessionFactory.getCurrentSession()
				.createCriteria(Nutrient.class)
				.add(Restrictions.eq("name", name))
				.list();

		if(listNutrients==null||listNutrients.isEmpty())
			return null;
		else{
				if(listNutrients.size()>1)
					ProblemLogger.logProblem("Two Nutrients for name "+name);
				
				return listNutrients.get(0);
		}
	}
	
	@Override
	@Transactional
	public Map<Nutrient,Float> getNutrientsForBasicIngredient(Long basicIngredientId) {
		String sql=getNutrientsForBasicIngredientIdQuery.replaceAll("__bi_id__", Long.toString(basicIngredientId));
		@SuppressWarnings("unchecked")
		List<Basic_Ingredient_Nutrient_amount> amounts = (List<Basic_Ingredient_Nutrient_amount>) sessionFactory.getCurrentSession()
				.createCriteria(Basic_Ingredient_Nutrient_amount.class)
		        .add(Restrictions.sqlRestriction(sql)).list();

		Map<Nutrient,Float> retValue=new HashMap<Nutrient, Float>();
		
		for(Basic_Ingredient_Nutrient_amount nutrient_amount:amounts){
			retValue.put(nutrient_amount.getNutritient(), nutrient_amount.getCoefficient());
		}
		
		return retValue;
	}

	
	
//	@Override
//	@Transactional
//	public void saveNutrientData(Basic_Ingredient basicIngredient, Map<Nutrient, Float> nutrientsAmounts) {
//		for(Entry<Nutrient, Float> e:nutrientsAmounts.entrySet()){
//			Nutrient nutrient = e.getKey();
//			if(nutrient!=null){
//				boolean nutrientIsInDbAndHasId = isNutritientInDb(nutrient);
//				
//				if(!nutrientIsInDbAndHasId){
//					ProblemLogger.logProblem("Nutritient was not found in database: "+nutrient.getName());
//				}else{
//					Basic_Ingredient_Nutrient_amount basicIngredientNutritientAmount=new Basic_Ingredient_Nutrient_amount();
//					basicIngredientNutritientAmount.setBasicIngredient(basicIngredient);
//					basicIngredientNutritientAmount.setNutritient(nutrient);
//					basicIngredientNutritientAmount.setCoefficient(e.getValue());
//					
//					this.sessionFactory.getCurrentSession().save(basicIngredientNutritientAmount);
//				}
//			}
//		}
//	}

	private boolean isNutritientInDb(Nutrient nutrient) {
		boolean nutrientIsInDb=false;
		if(nutrient.getNu_id()==null){
			nutrientIsInDb=false;
		}else{
			Long nu_id=nutrient.getNu_id();
			Nutrient nutrientFromDb=getNutrientById(nu_id);
			if(nutrientFromDb==null){
				nutrientIsInDb=false;
			}else{
				nutrientIsInDb=true;
			}
			
		}
		return nutrientIsInDb;
	}

//	@Override
//	@Transactional
//	public Map<Nutrient, Float> getNutrientsForBasicIngredient(Long basicIngredientId) {
//		@SuppressWarnings("unchecked")
//		List<Basic_Ingredient_Nutrient_amount> list = 
//		(List<Basic_Ingredient_Nutrient_amount>)sessionFactory.getCurrentSession()
//		.createCriteria(Basic_Ingredient_Nutrient_amount.class)
//		.add(Restrictions.eq("bi_id", basicIngredientId))
//		.list();
//		
//		for(Basic_Ingredient_Nutrient_amount bina:list) {
//			System.out.println(bina);
//		}
//		
//		return null;
//		
//	}


//	@Override
//	@Transactional
//	public boolean areNutrientsForBasicIngredient(Long basicIngredientId) {
//		return sessionFactory.getCurrentSession()
//		.createCriteria(Basic_Ingredient_Nutrient_amount.class)
//        .add(Restrictions.eq("basicIngredient.id", basicIngredientId))
//        .setProjection(Projections.property("basicIngredient.id"))
//        .setFirstResult(0).setMaxResults(1)
//        .uniqueResult() != null;
//		
//		
//	}
//	
//	@Override
//	@Transactional
//	public boolean areNutrientsForBasicIngredient(String basicIngredientName) {
//		return sessionFactory.getCurrentSession()
//		.createCriteria(Basic_Ingredient_Nutrient_amount.class)
//        .add(Restrictions.eq("basicIngredient.id", basicIngredientName))
//        .setProjection(Projections.property("basicIngredient.id"))
//        .setFirstResult(0).setMaxResults(1)
//        .uniqueResult() != null;
//		
//		
//	}

}
