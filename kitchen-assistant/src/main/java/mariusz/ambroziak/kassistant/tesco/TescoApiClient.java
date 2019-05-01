package mariusz.ambroziak.kassistant.tesco;


import java.util.ArrayList;

import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import api.extractors.AbstractQuantityEngExtractor.QuantityTranslation;
import api.extractors.EdamanQExtract;
import mariusz.ambroziak.kassistant.model.Amount_Type;
import mariusz.ambroziak.kassistant.model.Problem;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;





public class TescoApiClient {
	private static final String DETAILS_BASE_URL = "https://dev.tescolabs.com/product/?tpnb=";
	private static final String baseUrl= "https://dev.tescolabs.com/grocery/products/";
	private static final int  productsReturnedLimit=20;
	
	private static final String headerName="Ocp-Apim-Subscription-Key";
	private static final String headerValue="bb40509242724f799153796d8718c3f3";

//https://dev.tescolabs.com/grocry/products/?query=cucumber&offset=0&limit=10
	private static String getResponse(String phrase) {
		ClientConfig cc = new DefaultClientConfig();
		cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);

		Client c = Client.create();
		WebResource client = c.resource(baseUrl);

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("query", phrase);
		queryParams.add("offset", "0");
		queryParams.add("limit",Integer.toString(productsReturnedLimit));
		WebResource clientWithParams = client.queryParams(queryParams);
		Builder clientWithParamsAndHeader = clientWithParams.header(headerName, headerValue);
		
		String response1 ="";

		try{
			response1 = clientWithParamsAndHeader.accept("application/json").get(String.class);
			return response1;

		}catch( com.sun.jersey.api.client.UniformInterfaceException e){
			ProblemLogger.logProblem("UniformInterfaceException for term: "+phrase+". Waiting and retrying");
			sleep(2000);
			try{
				response1 = clientWithParamsAndHeader.accept("application/json").get(String.class);
				return response1;

			}catch( com.sun.jersey.api.client.UniformInterfaceException ex){
				System.err.println("Double: "+ex);
				ProblemLogger.logProblem("Double: "+ex);
				ex.printStackTrace();
			
			}
		}


		return response1;
	}


	public static ArrayList<Produkt> getProduktsFor(String phrase){
		ArrayList<Produkt> retValue=new ArrayList<Produkt>();
		
		String response=getResponse(phrase);
//		sleep(1000); //just to not exceed limit
		JSONObject root=new JSONObject(response);
		JSONArray produkts=((JSONArray)root.get("products"));
		
		for(int i=0;i<produkts.length();i++){
			JSONObject ApiProdukt=(JSONObject) produkts.get(i);

			retValue.add(TescoApiClientParticularProduct.getProduktByShopId((Integer)ApiProdukt.get("id")));
		}
		
		return retValue;
	}


	private static float getPrice(JSONObject ApiProdukt, String url) {
		String minPrice=(String) ApiProdukt.get("minimumPrice");
		String maxPrice=(String) ApiProdukt.get("maximumPrice");
		
		if(minPrice==null||minPrice.equals("")||maxPrice==null||maxPrice.equals(""))
			ProblemLogger.logProblem("Problem with missing price(s) for produkt: "+url);
		
		if(!minPrice.equals(maxPrice))
			ProblemLogger.logProblem("Problem with max and min price not matching for produkt: "+url);
		
		float maxFloat=extractFloatPrice(maxPrice);

		return maxFloat;
	}


	private static float extractFloatPrice(String stringPrice) {
		stringPrice=stringPrice.replace("$", "");
		float floatPrice=Float.parseFloat(stringPrice);
		return floatPrice;
	}


	public static void main(String [] args){
		//System.out.println(getProduktsFor("chicken"));
		String response = getResponse("chicken");
		
		ArrayList<Produkt> list = parseResponse(response);
		
		System.out.println(list.size());
	}


	private static ArrayList<Produkt> parseResponse(String response) {
		JSONObject jsonRoot=new JSONObject(response);
		
		
		JSONObject ukJson = jsonRoot.getJSONObject("uk");
		
		JSONObject jsonGhs = ukJson.getJSONObject("ghs");
		
		JSONObject jsonProducts =jsonGhs.getJSONObject("products");
		
		JSONArray jsonProductResultsArray=jsonProducts.getJSONArray("results");

		ArrayList<Produkt> resultProductList = calculateProductList(jsonProductResultsArray);
		return resultProductList;
	}


	private static ArrayList<Produkt> calculateProductList(JSONArray jsonProductResultsArray) {
		ArrayList<Produkt> resultList=new ArrayList<Produkt>();
		
		
		for(int i=0;i<jsonProductResultsArray.length();i++) {
			JSONObject singleProductJson = jsonProductResultsArray.getJSONObject(i);
			Produkt result = createParticularProduct(singleProductJson);
			
			resultList.add(result);
		}
		
		return resultList;
	}


	private static Produkt createParticularProduct(JSONObject singleProductJson) {
		String name = singleProductJson.getString("name");
		long tpnb = singleProductJson.getLong("tpnb");
		String detailsUrl=DETAILS_BASE_URL+tpnb;
		String description = calculateDescription(singleProductJson);
		float price = (float)singleProductJson.getDouble("price");

		String quantityString = calculateQuantityJspString(singleProductJson, detailsUrl);
					
		Produkt result=new Produkt(detailsUrl,quantityString,name,"",description,price,false);
		return result;
	}


	private static String calculateDescription(JSONObject singleProductJson) {
		JSONArray jsonArray = singleProductJson.getJSONArray("description");
		String retValue="";
		for(int i=0;i<jsonArray.length();i++) {
			String line = jsonArray.getString(i);
			retValue+=line+"\n";
		}

		return retValue;
	}


	private static String calculateQuantityJspString(JSONObject singleProductJson, String detailsUrl) {
		String contentsMeasureType = singleProductJson.getString("ContentsMeasureType");
		float contentsQuantity = (float)singleProductJson.getDouble("ContentsQuantity");

		String quantityString="";
		
		if(!contentsMeasureType.equals("G")) {
			ProblemLogger.logProblem("Found tesco product with type different than Grams:"+detailsUrl);
			
		}else {
		
			if(contentsMeasureType!=null&&contentsMeasureType.equals("G")) {
				try {
				PreciseQuantity pq=new PreciseQuantity(contentsQuantity*1000, AmountTypes.mg);
				quantityString=pq.toJspString();
				}catch (NumberFormatException e) {
					// Lets just leave empty Quantity string
				}
			}
		}
		return quantityString;
	}

	public Produkt getProduktByShopId(String id){
		return null;
	}

	private static void sleep(long milis) {
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
