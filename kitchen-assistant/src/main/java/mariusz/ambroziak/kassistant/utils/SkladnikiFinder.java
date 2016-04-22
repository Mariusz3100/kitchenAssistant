package mariusz.ambroziak.kassistant.utils;

import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient;

public class SkladnikiFinder {
	
	
	public static Basic_Ingredient findBasicIngredientInDbByPhrase(String phrase){
		String name=getBaseName(phrase);
		
		Basic_Ingredient ingredient = DaoProvider.getInstance().getBasicIngredientDao().getIngredientByAnyName(name);
		
		return ingredient;
	}
	
	public static Basic_Ingredient findBasicIngredientInDbByBaseName(String name){
		Basic_Ingredient ingredient = DaoProvider.getInstance().getBasicIngredientDao().getIngredientByAnyName(name);
		
		return ingredient;
	}
	
	public static String getBaseName(String phrase){
		//TODO jakaœ logika
		return phrase;
	}
	
	
	
}
