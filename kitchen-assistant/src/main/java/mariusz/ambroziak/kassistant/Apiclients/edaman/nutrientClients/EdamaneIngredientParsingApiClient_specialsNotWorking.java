package mariusz.ambroziak.kassistant.Apiclients.edaman.nutrientClients;


import static org.hamcrest.CoreMatchers.nullValue;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mortbay.util.UrlEncoded;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import api.extractors.AbstractQuantityEngExtractor.QuantityTranslation;
import api.extractors.EdamanQExtract;
import mariusz.ambroziak.kassistant.Apiclients.edaman.EdamanRecipeApiParameters;
import mariusz.ambroziak.kassistant.Apiclients.edaman.ParseableRecipeData;
import mariusz.ambroziak.kassistant.ai.categorisation.edaman.IngredientParsed;
import mariusz.ambroziak.kassistant.ai.categorisation.edaman.IngredientUnparsedApiDetails;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.ApiIngredientAmount;
import mariusz.ambroziak.kassistant.model.utils.ProduktIngredientQuantity;





public class EdamaneIngredientParsingApiClient_specialsNotWorking {
	public static final String baseUrl = "https://api.edamam.com/api/nutrition-data";
	public static final String appId = "47379841";
	public static final String appKey = "d28718060b8adfd39783ead254df7f92";


	//	https://api.edamam.com/api/nutrition-details?app_id=47379841&app_key=d28718060b8adfd39783ead254df7f92
	//	https://api.edamam.com/api/nutrition-data?app_id=47379841&app_key=d28718060b8adfd39783ead254df7f92&nutrition-type=logging&ingr=1%20large%20apple	

	static String   fullBaseUrl="https://api.edamam.com/api/nutrition-data?app_id=47379841&app_key=d28718060b8adfd39783ead254df7f92&ingr=";

	private static String getResponse(String ingredientPhrase) {
		Client c = Client.create();
		
		String url=fullBaseUrl+UrlEncoded.encodeString(ingredientPhrase);
		WebResource resource = c.resource(url);
		String response1 = resource.accept("text/html").get(String.class);
		resource.getProperties().entrySet();
		return response1;

		//
		//
		//		String encodedPhrase=UrlEncoded.encodeString(ingredientPhrase);
		//		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		//		queryParams.add("ingr", encodedPhrase);//URLEncoder.encode(urlID="http://www.edamam.com/ontologies/edamam.owl#recipe_1f8a034117737c47cd89d227e67be98d");
		//		queryParams.add("app_key",appKey);
		//
		//		//       queryParams.add("app_id", "af08be14");
		//		MultivaluedMap<String, String> queryParams_appId = new MultivaluedMapImpl();
		//		queryParams_appId.add("app_id",appId);
		//
		//
		//
		//
		//
		//
		//		String response1 = resource.queryParams(queryParams_appId).queryParams(queryParams).accept("text/plain").get(String.class);
		//		return response1;
	}


	public static IngredientUnparsedApiDetails parseIngredient(String Ingredientphrase) {
		String response1=getResponse(Ingredientphrase);

		if(response1==null||response1.length()<2) {
			return null; 
		}else {

			if(response1.charAt(0)=='[')
				response1=response1.substring(1);

			if(response1.charAt(response1.length()-1)==']')
				response1=response1.substring(0,response1.length()-1);
			JSONObject root=new JSONObject(response1);
			JSONArray jsonIngredients=root.getJSONArray("ingredients");

			//	ArrayList<ApiIngredientAmount> parsedIngredients = parseIngredientListFromRoot(root);

			for(int i=0;i<jsonIngredients.length();i++){
				JSONObject ingredient = (JSONObject) jsonIngredients.get(i);
				String name= (String) ingredient.getString("text");
				if(name.equals(Ingredientphrase)) {
					JSONArray parsedIngredientsArrayJson= (JSONArray) ingredient.getJSONArray("parsed");

					for(int j=0;j<parsedIngredientsArrayJson.length();j++) {
						JSONObject parsedJson=parsedIngredientsArrayJson.getJSONObject(0);

						double quantity = parsedJson.getDouble("quantity");
						String measure = parsedJson.getString("measure");
						String productPhrase = parsedJson.getString("foodMatch");

						
						IngredientUnparsedApiDetails retValue=new IngredientUnparsedApiDetails();
						retValue.setOriginalPhrase(Ingredientphrase);
						retValue.setProductPhrase(productPhrase);
						retValue.setAmountTypePhrase(measure);
						retValue.setAmount(quantity);
						return retValue;
					}
				}

			}
		}
		return null;
	}


	private static PreciseQuantity getAmountType(String measure,double quantity) {
		QuantityTranslation extractQuantity = EdamanQExtract.getTranslationToBaseType(measure);


		return extractQuantity.getQuantity((float)quantity);
	}


	public static void main(String[] args) {
		IngredientUnparsedApiDetails parseIngredient = parseIngredient("1 medium tomato");

		System.out.println(parseIngredient);
	}


}
