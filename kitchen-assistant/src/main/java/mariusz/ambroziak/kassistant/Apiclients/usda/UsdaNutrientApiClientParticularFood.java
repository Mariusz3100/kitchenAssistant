package mariusz.ambroziak.kassistant.Apiclients.usda;


import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

import api.extractors.AbstractQuantityEngExtractor.QuantityTranslation;
import api.extractors.EdamanQExtract;
import mariusz.ambroziak.kassistant.Apiclients.edaman.UsdaApiParameters;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.Nutrient_Name;
import mariusz.ambroziak.kassistant.model.Problem;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.ApiIngredientAmount;
import mariusz.ambroziak.kassistant.model.utils.ProduktIngredientQuantity;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;





public class UsdaNutrientApiClientParticularFood {


	private static String productBaseUrl="https://api.nal.usda.gov/ndb/V2/reports";


	private static String getResponse(String ndbno) {
		ClientConfig cc = new DefaultClientConfig();
		cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);

		Client c = Client.create();
		WebResource resource = c.resource(productBaseUrl);

		MultivaluedMap<String, String> queryParams_appId = new MultivaluedMapImpl();
		queryParams_appId.add("api_key",UsdaApiParameters.getApp_key());


		queryParams_appId.add("ndbno", ndbno);
		String response1 = resource.queryParams(queryParams_appId).accept("text/plain").get(String.class);
		return response1;
	}


	private static Map<Nutrient, PreciseQuantity> parseIntoNutrientDetailsMap(String response,String ndbno) {

		Map<Nutrient,PreciseQuantity> retValueMap=null;
		JSONObject root=new JSONObject(response);
		JSONArray list_root = root.getJSONArray("foods");

		if(list_root.length()!=1) {
			ProblemLogger.logProblem("For ndbno="+ndbno+" returned more than one food");
			return new HashMap<Nutrient, PreciseQuantity>();
		}else {
			JSONObject items = (JSONObject) list_root.get(0);
			JSONObject foodDetails = (JSONObject) items.get("food");
			JSONArray nutrientList =  foodDetails.getJSONArray("nutrients");
			
			retValueMap=parseNutrientsList(nutrientList);
			return retValueMap;
		}
	}
	//returns nutrient values per 100g of basic ingredient
	private static UsdaFoodDetails parseIntoNutrientDetailsObject(String response,String ndbno) {

		Map<Nutrient,PreciseQuantity> retValueMap=null;
		JSONObject root=new JSONObject(response);
		JSONArray list_root = root.getJSONArray("foods");

		if(list_root.length()!=1) {
			ProblemLogger.logProblem("For ndbno="+ndbno+" returned more than one food");
			return UsdaFoodDetails.getEmptyOne();
		}else {
			JSONObject items = (JSONObject) list_root.get(0);
			JSONObject foodDetails = (JSONObject) items.get("food");
			JSONArray nutrientList =  foodDetails.getJSONArray("nutrients");
			JSONObject desc = (JSONObject)foodDetails.get("desc");
			String productName=(String) desc.get("name");
			
			retValueMap=parseNutrientsList(nutrientList);
			UsdaFoodDetails retValue=new UsdaFoodDetails(new UsdaFoodId(productName, ndbno),retValueMap);
			return retValue;
		}
	}

	private static List<Nutrient_Name> getListOfNutrients() {
		return DaoProvider.getInstance().getNutrientNameDao().list();
	}


	private static HashMap<Nutrient, PreciseQuantity> parseNutrientsList(JSONArray nutrientList) {
		List<Nutrient_Name> listOfNutrients = getListOfNutrients();

		HashMap<Nutrient, PreciseQuantity> retValue=new HashMap<Nutrient, PreciseQuantity>();
		for(int i=0;i<nutrientList.length();i++) {
			JSONObject apiNutrient=(JSONObject) nutrientList.get(i);

			String apiNutName = (String) apiNutrient.get("name");
			for(Nutrient_Name dbNutrient:listOfNutrients) {
				if(dbNutrient.getPossible_name().equalsIgnoreCase(apiNutName))
				{
					String apiValue = (String) apiNutrient.get("value");
					String apiUnit = (String) apiNutrient.get("unit");
					float floatApiValue =Float.parseFloat(apiValue);

					if(!"IU".equals(apiUnit)) {//IU is old type of measuring, going out of use
						PreciseQuantity pq = getProperAmountType(apiUnit, floatApiValue);

						if(pq==null) {
							ProblemLogger.logProblem("Unit type "+apiUnit+" not found");
						}else {
							retValue.put(dbNutrient.getBase(), pq);
						}
					}
				}
			}
		}
		return retValue;
	}

	public static  Map<Nutrient, PreciseQuantity> getNutrientDetailsForDbno(String ndbno){
		String response=getResponse(ndbno);
		Map<Nutrient, PreciseQuantity> parseIntoNutrientDetails = parseIntoNutrientDetailsMap(response, ndbno);
		return parseIntoNutrientDetails;
	}

	public static  UsdaFoodDetails getNutrientDetailObjectForDbno(String ndbno){
		String response=getResponse(ndbno);
		UsdaFoodDetails parseIntoNutrientDetails = parseIntoNutrientDetailsObject(response, ndbno);
		return parseIntoNutrientDetails;
	}
	
	public static  UsdaFoodDetails getNutrientDetails(UsdaFoodId id){
		if(id==null) {
			return null;
		}else {
			Map<Nutrient, PreciseQuantity> nutrientDetailsForDbno = getNutrientDetailsForDbno(id.getNdbno());
			return new UsdaFoodDetails(id,nutrientDetailsForDbno);
		}
	}

	private static PreciseQuantity getProperAmountType(String apiUnit, float floatApiValue) {
		QuantityTranslation extractedQuantity = EdamanQExtract.getTranslationToBaseType(apiUnit);

		PreciseQuantity quantity = extractedQuantity.getQuantity(floatApiValue);

		return quantity;
	}





	public static void main(String [] args){

		Map<Nutrient, PreciseQuantity> nutrientDetailsForDbno = getNutrientDetailsForDbno("04542");

		System.out.println("Details:");

		for(Nutrient n:nutrientDetailsForDbno.keySet()) {
			System.out.println(n+"->"+nutrientDetailsForDbno.get(n));
		}



	}






}
