package mariusz.ambroziak.kassistant.Apiclients.edaman;


import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import api.extractors.EdamanQExtract;
import mariusz.ambroziak.kassistant.model.utils.ApiIngredientAmount;
import mariusz.ambroziak.kassistant.model.utils.ProduktIngredientQuantity;





public class UsdaNutrientApiClient {

	private static String particularProduktBaseUrl="https://api.edamam.com/api/nutrition-details";

	private static String searchBaseUrl="https://api.nal.usda.gov/ndb/search";

//	public static ArrayList<RecipeData> getNutrientsByPhrase(String phrase){
//		String response1 = getResponse(phrase);
//
////		ArrayList<RecipeData> rdList = parseResponseIntoRecipes(response1);
//		
//		
//		
//		return rdList;
//
//	}
//
//	public static ArrayList<RecipeData> getRecipesByParameters(EdamanRecipeApiParameters eap){
//		String response1 = getResponse(eap);
//
//		ArrayList<RecipeData> rdList = parseResponseIntoRecipes(response1);
//		
////		extractRightRecipes(rdList,eap);
//		
//		return rdList;
//
//	}
//
//
////	private static void extractRightRecipes(ArrayList<RecipeData> rdList, EdamanApiParameters eap) {
////		Iterator<RecipeData> iterator = rdList.iterator();
////		while(iterator.hasNext()){
////			RecipeData next = iterator.next();
////			
////			if(next.getDietLabels().contains(o))
////		}
////	}
//
//	private static ArrayList<RecipeData> parseResponseIntoRecipes(String response1) {
//		ArrayList<RecipeData> rdList=new ArrayList<>();
//		
//		JSONObject root=new JSONObject(response1);
//		
//		JSONArray recipeHits = root.getJSONArray("hits");
//		for(int i=0;i<recipeHits.length();i++){
//			RecipeData rd=new RecipeData();
//
//			JSONObject recipeHit=(JSONObject) recipeHits.get(i);
//			JSONObject recipeUrlList=(JSONObject)recipeHit.get("recipe");
//			String url=(String)recipeUrlList.get("shareAs");
//			String label=(String)recipeUrlList.get("label");
//			String imgUrl=(String)recipeUrlList.get("image");
//			JSONArray healthLabels=(JSONArray)recipeUrlList.getJSONArray("healthLabels");
//			
//			String id=(String)recipeUrlList.get("uri");
//			
//			rd.setEdamanId(id);
//			rd.setLabel(label);
//			rd.setUrl(url);
//			rd.setImageUrl(imgUrl);
//			for(int j=0;j<healthLabels.length();j++){
//				HealthLabels retrievedHL = HealthLabels.retrieveByName((String) healthLabels.get(j));
//				if(retrievedHL!=null)
//					rd.addHealthLabels(retrievedHL);
//			}
//			
//			JSONArray dietLabels=(JSONArray)recipeUrlList.getJSONArray("dietLabels");
//
//			for(int j=0;j<dietLabels.length();j++){
//				DietLabels retrievedDL = DietLabels.retrieveByName((String) dietLabels.get(j));
//				if(retrievedDL!=null)
//					rd.addDietLabel(retrievedDL);
//			}
//			
//			rdList.add(rd);
//		}
//		return rdList;
//	}
//
//
//
//	public static RecipeData getSingleRecipe(String urlID) {
//		ClientConfig cc = new DefaultClientConfig();
//		cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);
//
//		Client c = Client.create();
//		WebResource resource = c.resource(EdamanRecipeApiParameters.getBaseUrl());
////
//		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
//		queryParams.add("r", urlID);//URLEncoder.encode(urlID="http://www.edamam.com/ontologies/edamam.owl#recipe_1f8a034117737c47cd89d227e67be98d");
//		queryParams.add("app_key",EdamanRecipeApiParameters.getApp_key());
//
//		//       queryParams.add("app_id", "af08be14");
//		MultivaluedMap<String, String> queryParams_appId = new MultivaluedMapImpl();
//		queryParams_appId.add("app_id",EdamanRecipeApiParameters.getApp_id());
//
//
//
//		String response1 = resource.queryParams(queryParams_appId).queryParams(queryParams).accept("text/plain").get(String.class);
//		
//		if(response1.charAt(0)=='[')
//			response1=response1.substring(1);
//		
//		if(response1.charAt(response1.length()-1)==']')
//			response1=response1.substring(0,response1.length()-1);
//		
//		JSONObject root=new JSONObject(response1);
//		
//		
//
//			String recipeUrl=(String)root.get("shareAs");
//			String label=(String)root.get("label");
//			String imgUrl=(String)root.get("image");
//			String uri=(String)root.get("uri");
//			JSONArray recipeIngredients = root.getJSONArray("ingredients");
//			
//			ArrayList<ApiIngredientAmount> parsedIngredients=new ArrayList<>();
//			for(int i=0;i<recipeIngredients.length();i++){
//				JSONObject ingredient = (JSONObject) recipeIngredients.get(i);
//				Double weight = (Double) ingredient.get("weight");
//				
//				String name= (String) ingredient.get("text");
//				
//				//name=EdamanQExtract.correctText(name);
//				ApiIngredientAmount aia=new ApiIngredientAmount(name,(float)(weight*1000));
//				parsedIngredients.add(aia);
//			}
//			
//			
//			
//			RecipeData rd=new RecipeData();
//			rd.setEdamanId(uri);
//			rd.setLabel(label);
//			rd.setUrl(recipeUrl);
//			rd.setImageUrl(imgUrl);
//			rd.setIngredients(parsedIngredients);
//			return rd;
//		
//	}
//
//	private static String getResponse(String phrase) {
//		EdamanRecipeApiParameters eap=new EdamanRecipeApiParameters();
//		eap.setPhrase(phrase);
//		return getResponse(eap);
//	}

	private static String getResponse(String searchPhrase) {
		ClientConfig cc = new DefaultClientConfig();
		cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);

		Client c = Client.create();
		WebResource resource = c.resource(searchBaseUrl);

		MultivaluedMap<String, String> queryParams_appId = new MultivaluedMapImpl();
		queryParams_appId.add("api_key",UsdaApiParameters.getApp_key());


		queryParams_appId.add("q", searchPhrase);
		String response1 = resource.queryParams(queryParams_appId).accept("text/plain").get(String.class);
		return response1;
	}






	public static void main(String [] args){
		String response=getResponse("chicken");
		System.out.println(response);
	
	
	}




}
