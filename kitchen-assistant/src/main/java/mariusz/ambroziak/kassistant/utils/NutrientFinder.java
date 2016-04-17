package mariusz.ambroziak.kassistant.utils;

import java.util.Map;

import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.utils.PreciseQuantity;

public class NutrientFinder {

	public static Map<Nutrient,PreciseQuantity> findNutrientInDb(String name){
		Basic_Ingredient ingredientByAnyName = DaoProvider.getInstance().getBasicIngredientDao().getIngredientByAnyName(name);
		
		
		
		
		
		return null;
	}
}
