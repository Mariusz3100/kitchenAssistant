package mariusz.ambroziak.kassistant.ai.categorisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import java.util.TreeMap;

import mariusz.ambroziak.kassistant.Apiclients.edaman.EdamanRecipeApiClient;
import mariusz.ambroziak.kassistant.Apiclients.edaman.ParseableRecipeData;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.utils.ApiIngredientAmount;
import mariusz.ambroziak.kassistant.shopcom.ShopComApiClient;
import mariusz.ambroziak.kassistant.shopcom.ShopComApiClientParticularProduct;
import mariusz.ambroziak.kassistant.tesco.TescoApiClient;
import mariusz.ambroziak.kassistant.tesco.TescoApiClientParticularProduct_notUsed;

public class IngredientCategoriser {
	private static IngredientCategoriser singleton;
	//	private Category root;







	public static void main(String [] args){


		System.out.println();
	}





	public static IngredientCategoriser getSingleton() {
		if(singleton==null) {
			singleton=new IngredientCategoriser();

		}
		return singleton;

	}



	private IngredientCategoriser() {
		super();

	}

	
	public static ArrayList<ParseableRecipeData> getRelevantIngredientsFor(String phrase){
		ArrayList<ParseableRecipeData> recipesByPhrase = EdamanRecipeApiClient.getRecipesByPhrase(phrase);
		
//		Map<ParseableRecipeData,ArrayList<ApiIngredientAmount>> retValue=new HashMap<ParseableRecipeData, ArrayList<ApiIngredientAmount>>();
//		
//		for(ParseableRecipeData recipeToBeChecked:recipesByPhrase) {
//			ArrayList<ApiIngredientAmount> relevantProductList=new ArrayList<ApiIngredientAmount>();
//			retValue.put(recipeToBeChecked, relevantProductList);
//			for(int i=0;recipeToBeChecked.getIngredients()!=null&&i<recipeToBeChecked.getIngredients().size();i++) {
//				ApiIngredientAmount apiIngredientAmount = recipeToBeChecked.getIngredients().get(i);
//				if(apiIngredientAmount.getName().contains(phrase)) {
//					relevantProductList.add(apiIngredientAmount);
//				}
//			}
//			
//		}
		
		
		return recipesByPhrase;
		
	}

}
