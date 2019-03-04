package mariusz.ambroziak.kassistant.Apiclients.usda;


import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import mariusz.ambroziak.kassistant.model.Problem;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.jsp.SingleProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.ApiIngredientAmount;
import mariusz.ambroziak.kassistant.model.utils.ProduktIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithBasicIngredients;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;





public class UsdaNutrientApiClient {




	private static String searchBaseUrl="https://api.nal.usda.gov/ndb/search";
	private static int maxRows=20;

	private static String getResponse(String searchPhrase) {
		ClientConfig cc = new DefaultClientConfig();
		cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);

		Client c = Client.create();
		WebResource resource = c.resource(searchBaseUrl);

		MultivaluedMap<String, String> queryParams_appId = new MultivaluedMapImpl();
		queryParams_appId.add("api_key",UsdaApiParameters.getApp_key());


		queryParams_appId.add("q", searchPhrase);
		queryParams_appId.add("max", Integer.toString(maxRows));

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
				if(id.getName()==null||!id.getName().toLowerCase().contains(x.toLowerCase())) {
					return false;
				}
			}
		}

		return true;

	}

	
	
	
	public static UsdaFoodDetails searchForNutritionDetailsOfAProdukt(String foodName){
		long start=System.currentTimeMillis();
		
		if(foodName==null||foodName.equals(""))
			return UsdaFoodDetails.getEmptyOne();
		
		String response=getResponse(foodName);

		JSONObject root=new JSONObject(response);
		if(root.has("errors")) {
			ProblemLogger.logProblem("Error occured when searching for \""+foodName+": "+response);
			return UsdaFoodDetails.getEmptyOne();
		}else{

			ArrayList<UsdaFoodId> parseIntoNutrientDetails = parseIntoNutrientDetails(response);

			UsdaFoodId best = filterBest(parseIntoNutrientDetails,foodName);

			UsdaFoodDetails nutrientDetails = UsdaNutrientApiClientParticularFood.getNutrientDetails(best);
			
			ProblemLogger.logProblem("Getting data in searchForNutritionDetailsOfAProdukt from usda api took "+(System.currentTimeMillis()-start)+" miliseconds at "+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
			
			return nutrientDetails;
		}
	}

	public static ArrayList<UsdaFoodId> searchForProduktsInUsdaDb(String foodName){
		long start=System.currentTimeMillis();

		String response=getResponse(foodName);

		JSONObject root=new JSONObject(response);
		if(root.has("errors")) {
			ProblemLogger.logProblem("Error occured when searching for \""+foodName+": "+response);
			return new ArrayList<>();
		}else{
			ArrayList<UsdaFoodId> parseIntoNutrientDetails = parseIntoNutrientDetails(response);
			ProblemLogger.logProblem("Getting data in searchForProduktsInUsdaDb from usda api took "+(System.currentTimeMillis()-start)+" miliseconds at "+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

			return parseIntoNutrientDetails;
		}
	}
	
	public static Collection<UsdaFoodId> searchForProduktsInUsdaDbSortByName(String foodName){
		long start=System.currentTimeMillis();

		ArrayList<UsdaFoodId> searchForProduktsInUsdaDb = searchForProduktsInUsdaDb(foodName);
		
		TreeMap<UsdaFoodId, UsdaFoodId> map=new TreeMap<UsdaFoodId, UsdaFoodId>();
		for(UsdaFoodId id:searchForProduktsInUsdaDb) {
			map.put(id, id);
		}
		ProblemLogger.logProblem("Getting data in searchForProduktsInUsdaDbSortByName from usda api took "+(System.currentTimeMillis()-start)+" miliseconds at"+ new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		return map.values();
		
	}


	public static void main(String [] args){
		String searchPhrase = "chicken";
		String response=getResponse(searchPhrase);
		System.out.println(response);

		ArrayList<UsdaFoodId> parseIntoNutrientDetails = parseIntoNutrientDetails(response);

		UsdaFoodId best = filterBest(parseIntoNutrientDetails,searchPhrase);


		System.out.println("\nbest: "+best);


	}


	public static String getSearchBaseUrl() {
		return searchBaseUrl;
	}

	public static Map<SingleProdukt_SearchResult, Map<Nutrient, PreciseQuantity>> retrieveNutrientDataForProdukts(List<SingleProdukt_SearchResult> goodResults) {
		Map<SingleProdukt_SearchResult, Map<Nutrient,PreciseQuantity>> nutrientMap=new HashMap<SingleProdukt_SearchResult, Map<Nutrient,PreciseQuantity>>();
		
		
		for(SingleProdukt_SearchResult sp_sr:goodResults) {
			Produkt produkt=sp_sr.getProdukt();
			UsdaFoodDetails searchForNutritionDetails = searchForNutritionDetailsOfAProdukt(produkt.getNazwa());
			if(searchForNutritionDetails.getId()==null) {
				//TODO could be better right now we just assume produkt name is just search phrase with some unnecessary additives
				searchForNutritionDetails = searchForNutritionDetailsOfAProdukt(sp_sr.getProduktPhrase());
			}
			addNutrientDetailsToResultMap(nutrientMap, sp_sr, searchForNutritionDetails);
		}
		return nutrientMap;
	}


	private static void addNutrientDetailsToResultMap(
			Map<SingleProdukt_SearchResult, Map<Nutrient, PreciseQuantity>> nutrientMap,
			SingleProdukt_SearchResult sp_sr, UsdaFoodDetails searchForNutritionDetails) {
		
		if(searchForNutritionDetails!=null&&searchForNutritionDetails.getNutrietsMapPer100g()!=null)
			nutrientMap.put(sp_sr, searchForNutritionDetails.getNutrietsMapPer100g());
		else
			nutrientMap.put(sp_sr, new HashMap<Nutrient, PreciseQuantity>());
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
