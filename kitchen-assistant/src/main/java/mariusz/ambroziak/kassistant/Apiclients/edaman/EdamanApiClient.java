package mariusz.ambroziak.kassistant.Apiclients.edaman;


import java.util.ArrayList;
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

import mariusz.ambroziak.kassistant.model.utils.ApiIngredientAmount;
import mariusz.ambroziak.kassistant.model.utils.ProduktIngredientQuantity;





public class EdamanApiClient {


	public static ArrayList<RecipeData> getRecipes(String phrase){
		String response1 = getResponse(phrase);

		ArrayList<RecipeData> rdList=new ArrayList<>();
		
		JSONObject root=new JSONObject(response1);
		
		JSONArray recipeHits = root.getJSONArray("hits");
		for(int i=0;i<recipeHits.length();i++){
			JSONObject recipeHit=(JSONObject) recipeHits.get(i);
			JSONObject recipeUrlList=(JSONObject)recipeHit.get("recipe");
			String url=(String)recipeUrlList.get("shareAs");
			String label=(String)recipeUrlList.get("label");
			String imgUrl=(String)recipeUrlList.get("image");
			
			RecipeData rd=new RecipeData();
			rd.setLabel(label);
			rd.setUrl(url);
			rd.setImageUrl(imgUrl);
			rdList.add(rd);
		}
		
		
		
		return rdList;

	}



	public static RecipeData getSingleRecipe(String url) {
		ClientConfig cc = new DefaultClientConfig();
		cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);

		Client c = Client.create();
		WebResource resource = c.resource("https://api.edamam.com/search");
//
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("r", url);
		//       queryParams.add("app_id", "af08be14");


		String response1 = resource.queryParams(queryParams).accept("text/plain").get(String.class);
		
		if(response1.charAt(0)=='[')
			response1=response1.substring(1);
		
		if(response1.charAt(response1.length()-1)==']')
			response1=response1.substring(0,response1.length()-1);
		
		JSONObject root=new JSONObject(response1);
		
		

			String recipeUrl=(String)root.get("shareAs");
			String label=(String)root.get("label");
			String imgUrl=(String)root.get("image");
			JSONArray recipeIngredients = root.getJSONArray("ingredients");
			
			ArrayList<ApiIngredientAmount> parsedIngredients=new ArrayList<>();
			for(int i=0;i<recipeIngredients.length();i++){
				JSONObject ingredient = (JSONObject) recipeIngredients.get(i);
				Double weight = (Double) ingredient.get("weight");
				
				String name= (String) ingredient.get("food");
				ApiIngredientAmount aia=new ApiIngredientAmount(name,(float)(weight*1000));
				parsedIngredients.add(aia);
			}
			
			
			
			RecipeData rd=new RecipeData();
			rd.setLabel(label);
			rd.setUrl(recipeUrl);
			rd.setImageUrl(imgUrl);
			rd.setIngredients(parsedIngredients);
			return rd;
		
	}



	private static String getResponse(String phrase) {
		ClientConfig cc = new DefaultClientConfig();
		cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);

		Client c = Client.create();
		WebResource resource = c.resource("https://api.edamam.com/search");

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("q", phrase);
		//       queryParams.add("app_id", "af08be14");


		String response1 = resource.queryParams(queryParams).accept("text/plain").get(String.class);
		return response1;
	}





	public static void main(String [] args){
		//getRecipes("chicken");
		System.out.println(getSingleRecipe("https://www.edamam.com/recipe/chicken-mole-72d7a1a4ab4bd65e9a546ce7b3f974b5/chicken"));
		
//		System.out.println(getSingleRecipe("http://www.bonappetit.com/recipe/herbes-de-provence-rotisserie-chickens"));
	}




}
