package mariusz.ambroziak.kassistant.Apiclients.usda;


import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
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
import mariusz.ambroziak.kassistant.Apiclients.edaman.UsdaApiParameters;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.jsp.SingleProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.ApiIngredientAmount;
import mariusz.ambroziak.kassistant.model.utils.ProduktIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithBasicIngredients;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;





public class UsdaNutrientApiClient {

	private static String searchBaseUrl="https://api.nal.usda.gov/ndb/search";


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


	private static ArrayList<UsdaFoodId> parseIntoNutrientDetails(String response) {
		ArrayList<UsdaFoodId> retValue=new ArrayList<>();
		JSONObject root=new JSONObject(response);

		JSONObject list_root = root.getJSONObject("list");

		JSONArray items = list_root.getJSONArray("item");

		for(int i=0;i<items.length();i++) {
			JSONObject item=(JSONObject) items.get(i);
			String name=(String) item.get("name");
			String ndbno=(String) item.get("ndbno");

			UsdaFoodId usdaId=new UsdaFoodId(name, ndbno);
			retValue.add(usdaId);
		}

		return retValue;
	}

	//TODO Obviously, get some better choosing
	private static UsdaFoodId filterBest(ArrayList<UsdaFoodId> parseIntoNutrientDetails, String searchPhrase) {
		if(searchPhrase==null)
			searchPhrase="";
		String[] searchPhraseSplitted=searchPhrase.split(" ");

		UsdaFoodId best=null;

		for(UsdaFoodId id:parseIntoNutrientDetails) {
			if(id.getName()!=null&&serialContains(id,searchPhraseSplitted)) {
				if(best==null)
					best=id;
				else
				{
					if(id.getName().length()<best.getName().length()) {
						best=id;
					}
				}
			}

		}
		return best;				


	}


	private static boolean serialContains( UsdaFoodId id,String[] searchPhraseSplitted) {
		if(id==null||id.getName()==null||id.getName().equals(""))
			return false;
		for(String x:searchPhraseSplitted) {
			if(x!=null) {
				if(id.getName()==null||!id.getName().toLowerCase().contains(x)) {
					return false;
				}
			}
		}

		return true;

	}

	
	
	
	public static UsdaFoodDetails searchForNutritionDetails(String foodName){
		String response=getResponse(foodName);

		JSONObject root=new JSONObject(response);
		if(root.has("errors")) {
			ProblemLogger.logProblem("Error occured when searching for \""+foodName+": "+response);
			return UsdaFoodDetails.getEmptyOne();
		}else{

			ArrayList<UsdaFoodId> parseIntoNutrientDetails = parseIntoNutrientDetails(response);

			UsdaFoodId best = filterBest(parseIntoNutrientDetails,foodName);

			UsdaFoodDetails nutrientDetails = UsdaNutrientApiClientParticularFood.getNutrientDetails(best);

			return nutrientDetails;
		}
	}


	public static void main(String [] args){
		String searchPhrase = "chicken";
		String response=getResponse(searchPhrase);
		System.out.println(response);

		ArrayList<UsdaFoodId> parseIntoNutrientDetails = parseIntoNutrientDetails(response);

		UsdaFoodId best = filterBest(parseIntoNutrientDetails,searchPhrase);


		System.out.println("\nbest: "+best);


	}


//	public static Map<SingleProdukt_SearchResult, ProduktWithBasicIngredients> searchForNutritionDetails(
//			ArrayList<SingleProdukt_SearchResult> goodResults) {
//
//		Map<SingleProdukt_SearchResult, ProduktWithBasicIngredients> retValue=new HashMap<>();
//		retrieveNutrientDataForProdukts
//		ProduktWithBasicIngredients pwbi=new ProduktWithBasicIngredients(sp_sr.getProdukt(), searchForNutritionDetails(goodResults));
//
//
//
//		return null;
//	}

//	public static Map<SingleProdukt_SearchResult, ProduktWithBasicIngredients> searchForBasicNutrients(
//			ArrayList<SingleProdukt_SearchResult> goodResults) {
//
//		Map<SingleProdukt_SearchResult, ProduktWithBasicIngredients> retValue=new HashMap<>();
//
//		for(SingleProdukt_SearchResult sp_sr:goodResults) {
//			retrieveNutrientDataForProdukts(g)
//			ProduktWithBasicIngredients pwbi=new ProduktWithBasicIngredients(sp_sr.getProdukt(), )
//		}
//
//
//		return null;
//	}

	public static Map<SingleProdukt_SearchResult, Map<Nutrient, PreciseQuantity>> retrieveNutrientDataForProdukts(ArrayList<SingleProdukt_SearchResult> goodResults) {
		Map<SingleProdukt_SearchResult, Map<Nutrient,PreciseQuantity>> nutrientMap=new HashMap<SingleProdukt_SearchResult, Map<Nutrient,PreciseQuantity>>();
		
		
		for(SingleProdukt_SearchResult sp_sr:goodResults) {
			Produkt produkt=sp_sr.getProdukt();
			UsdaFoodDetails searchForNutritionDetails = searchForNutritionDetails(produkt.getNazwa());
			if(searchForNutritionDetails.getId()==null) {
				//TODO could be better right now we just assume produkt name is just search phrase with some unnecessary additives
				searchForNutritionDetails = searchForNutritionDetails(sp_sr.getProduktPhrase());
			}
			nutrientMap.put(sp_sr, searchForNutritionDetails.getNutrietsMap());
		}
		return nutrientMap;
	}


	public static Map<SingleProdukt_SearchResult, ProduktWithBasicIngredients> searchForNutritionDetails(
			ArrayList<SingleProdukt_SearchResult> goodResults) {
		//TODO zaślepka
		return new HashMap<SingleProdukt_SearchResult, ProduktWithBasicIngredients>();
		//		Map<SingleProdukt_SearchResult, Map<Nutrient,PreciseQuantity>> nutrientMap=new HashMap<SingleProdukt_SearchResult, Map<Nutrient,PreciseQuantity>>();
//		
//		
//		for(SingleProdukt_SearchResult sp_sr:goodResults) {
//			Produkt produkt=sp_sr.getProdukt();
//			UsdaFoodDetails searchForNutritionDetails = searchForNutritionDetails(produkt.getNazwa());
//			if(searchForNutritionDetails.getId()==null) {
//				//TODO could be better right now we just assume produkt name is just search phrase with some unnecessary additives
//				searchForNutritionDetails = searchForNutritionDetails(sp_sr.getProduktPhrase());
//			}
//			
//			ProduktWithBasicIngredients pwbi=new ProduktWithBasicIngredients(produkt, basicsFor100g)
//			
//			nutrientMap.put(sp_sr, searchForNutritionDetails.getNutrietsMap());
//		}
//		return nutrientMap;
	}








}
