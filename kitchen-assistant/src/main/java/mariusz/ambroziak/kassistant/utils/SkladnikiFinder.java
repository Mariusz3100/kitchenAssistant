package mariusz.ambroziak.kassistant.utils;

import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient;

public class SkladnikiFinder {
	
	
	public static Basic_Ingredient findIngredient(String name){
		Basic_Ingredient ingredientByAnyName = DaoProvider.getInstance().getBasicIngredientDao().getIngredientByAnyName(name);
		
		return ingredientByAnyName;
	}
}
