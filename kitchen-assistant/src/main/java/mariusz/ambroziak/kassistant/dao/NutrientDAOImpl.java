package mariusz.ambroziak.kassistant.dao;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.Variant_Word;
import mariusz.ambroziak.kassistant.model.utils.PreciseQuantity;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

public class NutrientDAOImpl implements NutrientDAO {
	private SessionFactory sessionFactory;
	private Basic_Ingredient basicIngredient;
	
	
	public NutrientDAOImpl(SessionFactory sessionFactory, Basic_Ingredient basicIngredient) {
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
	public Map<Nutrient,PreciseQuantity> getNutrientsOfBasicIngredient(Long basicIngredientId) {
//		@SuppressWarnings("unchecked")
//		List<Basic_Ingredient> ingredients = (List<Basic_Ingredient>) sessionFactory.getCurrentSession()
//				.createSQLQuery(selectBase.replaceAll("__name__", name))
//				.addEntity(Basic_Ingredient.class)
//				.list();
//
//		
//		
//		
//		
//		if(ingredients==null||ingredients.size()<1)
//			return null;
//		else
//			return ingredients.get(0);
		return null;
	}

	
	
	@Override
	public void saveNutrientData(Long basicIngredientId, Map<Nutrient, PreciseQuantity> nutrientsAmounts) {
		for(Entry<Nutrient, PreciseQuantity> e:nutrientsAmounts.entrySet()){
			Nutrient nutrient = e.getKey();
			if(nutrient!=null){
				boolean nutrientIsInDb = isNutritientInDb(nutrient);
				
				if(!nutrientIsInDb){
					ProblemLogger.logProblem("Nutritient was not found in database: "+nutrient.getName());
				}else{
					
				}
			}
		}
	}

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

}
