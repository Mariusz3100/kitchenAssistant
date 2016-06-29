package mariusz.ambroziak.kassistant.utils;

import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import webscrappers.SJPWebScrapper;

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
		//na razie sprowadzamy do formy bazowej pojedyncze s³owo
		if(phrase.contains(" "))
			return phrase;
		else
			return SJPWebScrapper.retrieveFromDbOrScrapAndSaveInDb(phrase);
		
	}
	
	
	
}
