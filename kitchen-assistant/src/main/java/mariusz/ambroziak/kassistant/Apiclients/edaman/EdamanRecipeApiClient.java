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





public class EdamanRecipeApiClient {


	public static ArrayList<ParseableRecipeData> getRecipesByPhrase(String phrase){
		String response1 = getResponse(phrase);

		ArrayList<ParseableRecipeData> rdList = parseResponseIntoRecipes(response1);



		return rdList;

	}

	public static ArrayList<ParseableRecipeData> getRecipesByParameters(EdamanRecipeApiParameters eap){
		String response1 = getResponse(eap);
		convertEapToLink(eap);
		ArrayList<ParseableRecipeData> rdList = parseResponseIntoRecipes(response1);

		//		extractRightRecipes(rdList,eap);

		return rdList;

	}


	//	private static void extractRightRecipes(ArrayList<RecipeData> rdList, EdamanRecipeApiParameters eap) {
	//		Iterator<RecipeData> iterator = rdList.iterator();
	//		while(iterator.hasNext()){
	//			RecipeData next = iterator.next();
	//			
	//			if(next.getDietLabels().contains(o))
	//		}
	//	}

	private static ArrayList<ParseableRecipeData> parseResponseIntoRecipes(String response1) {
		ArrayList<ParseableRecipeData> rdList=new ArrayList<>();

		JSONObject root=new JSONObject(response1);

		JSONArray recipeHits = root.getJSONArray("hits");
		for(int i=0;i<recipeHits.length();i++){
			ParseableRecipeData rd=new ParseableRecipeData();

			JSONObject recipeHit=(JSONObject) recipeHits.get(i);
			JSONObject recipeUrlList=(JSONObject)recipeHit.get("recipe");
			String url=(String)recipeUrlList.get("shareAs");
			String label=(String)recipeUrlList.get("label");
			String imgUrl=(String)recipeUrlList.get("image");
			JSONArray healthLabels=(JSONArray)recipeUrlList.getJSONArray("healthLabels");

			String id=(String)recipeUrlList.get("uri");

			rd.setEdamanId(id);
			rd.setLabel(label);
			rd.setUrl(url);
			rd.setImageUrl(imgUrl);
			for(int j=0;j<healthLabels.length();j++){
				HealthLabels retrievedHL = HealthLabels.tryRetrieving((String) healthLabels.get(j));
				if(retrievedHL!=null)
					rd.addHealthLabels(retrievedHL);
			}

			JSONArray dietLabels=(JSONArray)recipeUrlList.getJSONArray("dietLabels");

			for(int j=0;j<dietLabels.length();j++){
				DietLabels retrievedDL = DietLabels.tryRetrieving((String) dietLabels.get(j));
				if(retrievedDL!=null)
					rd.addDietLabel(retrievedDL);
			}
			
			
			ArrayList<ApiIngredientAmount> parsedIngredients = parseIngredientListFromRoot(recipeUrlList);

			rd.setIngredients(parsedIngredients);
			rdList.add(rd);
		}
		return rdList;
	}



	public static ParseableRecipeData getSingleRecipe(String urlID) {
		ClientConfig cc = new DefaultClientConfig();
		cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);

		Client c = Client.create();
		WebResource resource = c.resource(EdamanRecipeApiParameters.getBaseUrl());
		//
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("r", urlID);//URLEncoder.encode(urlID="http://www.edamam.com/ontologies/edamam.owl#recipe_1f8a034117737c47cd89d227e67be98d");
		queryParams.add("app_key",EdamanRecipeApiParameters.getApp_key());

		//       queryParams.add("app_id", "af08be14");
		MultivaluedMap<String, String> queryParams_appId = new MultivaluedMapImpl();
		queryParams_appId.add("app_id",EdamanRecipeApiParameters.getApp_id());



		String response1 = resource.queryParams(queryParams_appId).queryParams(queryParams).accept("text/plain").get(String.class);
		return parseResponseIntoObject(response1);
	}

	private static ParseableRecipeData parseResponseIntoObject(String response1) {
		if(response1==null||response1.length()<2) {
			return null; 
		}else {

			if(response1.charAt(0)=='[')
				response1=response1.substring(1);

			if(response1.charAt(response1.length()-1)==']')
				response1=response1.substring(0,response1.length()-1);
			JSONObject root=new JSONObject(response1);
			String recipeUrl=(String)root.get("shareAs");
			String label=(String)root.get("label");
			String imgUrl=(String)root.get("image");
			String uri=(String)root.get("uri");
			ArrayList<ApiIngredientAmount> parsedIngredients = parseIngredientListFromRoot(root);



			ParseableRecipeData rd=new ParseableRecipeData();
			rd.setEdamanId(uri);
			rd.setLabel(label);
			rd.setUrl(recipeUrl);
			rd.setImageUrl(imgUrl);
			rd.setIngredients(parsedIngredients);
			return rd;
		}
	}

	private static ArrayList<ApiIngredientAmount> parseIngredientListFromRoot(JSONObject root) {
		JSONArray recipeIngredients = root.getJSONArray("ingredients");

		ArrayList<ApiIngredientAmount> parsedIngredients = parseIngredientsFromArray(recipeIngredients);
		return parsedIngredients;
	}

	private static ArrayList<ApiIngredientAmount> parseIngredientsFromArray(JSONArray recipeIngredients) {
		ArrayList<ApiIngredientAmount> parsedIngredients=new ArrayList<>();
		for(int i=0;i<recipeIngredients.length();i++){
			JSONObject ingredient = (JSONObject) recipeIngredients.get(i);
			Double weight = (Double) ingredient.get("weight");
			String name= (String) ingredient.get("text");

			//name=EdamanQExtract.correctText(name);
			ApiIngredientAmount aia=new ApiIngredientAmount(name,(float)(weight*1000));
			parsedIngredients.add(aia);
		}
		return parsedIngredients;
	}

	private static String getResponse(String phrase) {
		EdamanRecipeApiParameters eap=new EdamanRecipeApiParameters();
		eap.setPhrase(phrase);
		return getResponse(eap);
	}

	private static String getResponse(EdamanRecipeApiParameters eap) {
		ClientConfig cc = new DefaultClientConfig();
		cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);

		Client c = Client.create();
		WebResource resource = c.resource("https://api.edamam.com/search");

		MultivaluedMap<String, String> queryParams_appId = new MultivaluedMapImpl();
		queryParams_appId.add("app_id",EdamanRecipeApiParameters.getApp_id());

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("app_key",EdamanRecipeApiParameters.getApp_key());

		queryParams.add("q", eap.getPhrase());

		if(eap.getHealthLabels()!=null)
			for(HealthLabels hl:eap.getHealthLabels())
				queryParams.add("health", hl.getParameterName());

		if(eap.getDietLabels()!=null)
			for(DietLabels dl:eap.getDietLabels())
				queryParams.add("diet", dl.getParameterName());



		String response1 = resource.queryParams(queryParams_appId).queryParams(queryParams).accept("text/plain").get(String.class);
		return response1;
	}


	private static String getResponse(EdamanRecipeApiParameters eap, int querySize) {
		ClientConfig cc = new DefaultClientConfig();
		cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);

		Client c = Client.create();
		WebResource resource = c.resource("https://api.edamam.com/search");

		MultivaluedMap<String, String> queryParams_appId = new MultivaluedMapImpl();
		queryParams_appId.add("app_id",EdamanRecipeApiParameters.getApp_id());

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("app_key",EdamanRecipeApiParameters.getApp_key());

		queryParams.add("q", eap.getPhrase());

		if(eap.getHealthLabels()!=null)
			for(HealthLabels hl:eap.getHealthLabels())
				queryParams.add("health", hl.getParameterName());

		if(eap.getDietLabels()!=null)
			for(DietLabels dl:eap.getDietLabels())
				queryParams.add("diet", dl.getParameterName());



		String response1 = resource.queryParams(queryParams_appId).queryParams(queryParams).accept("text/plain").get(String.class);
		return response1;
	}

	private static String convertEapToLink(EdamanRecipeApiParameters eap) {
		String link="https://api.edamam.com/search?app_id="+eap.getApp_id()
		+"&app_key="+EdamanRecipeApiParameters.getApp_key()
		+"&q="+eap.getPhrase();

		if(eap.getHealthLabels()!=null)
			for(HealthLabels hl:eap.getHealthLabels())
				link+="&health="+hl.getParameterName();

		if(eap.getDietLabels()!=null)
			for(DietLabels dl:eap.getDietLabels())
				link+="&diet="+dl.getParameterName();

		return link;
	}



	public static void main(String [] args){
		//getRecipes("chicken");
		//		System.out.println(getSingleRecipe("https://www.edamam.com/recipe/chicken-mole-72d7a1a4ab4bd65e9a546ce7b3f974b5/chicken"));

		//		System.out.println(getSingleRecipe("http://www.bonappetit.com/recipe/herbes-de-provence-rotisserie-chickens"));

		//		System.out.println(getRecipesByPhrase("chicken"));
		EdamanRecipeApiParameters eap=new EdamanRecipeApiParameters();

		eap.addHealthLabels(HealthLabels.Alcohol_free);
		eap.setPhrase("cake");
		ArrayList<ParseableRecipeData> recipesByParameters = getRecipesByParameters(eap);
		
		ParseableRecipeData singleRecipe = getSingleRecipe("http://www.edamam.com/ontologies/edamam.owl%23recipe_2f9199d70e40e7bf77625c93e08ccae2");
		System.out.println(recipesByParameters);
	}




}
