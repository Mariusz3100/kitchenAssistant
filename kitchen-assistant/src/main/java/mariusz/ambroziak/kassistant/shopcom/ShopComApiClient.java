package mariusz.ambroziak.kassistant.shopcom;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import mariusz.ambroziak.kassistant.model.Problem;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;





public class ShopComApiClient {

	private static String getResponse(String phrase) {
		return getResponse(phrase,20,0);
	}
	private static String getResponse(String phrase,int amount,int offset) {
		ClientConfig cc = new DefaultClientConfig();
		cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);

		Client c = Client.create();
		WebResource resource = c.resource("https://api.shop.com/AffiliatePublisherNetwork/v1/products");

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("term", phrase);
		queryParams.add("publisherID", "TEST");
		queryParams.add("locale","en_US");
		queryParams.add("categoryId","1-32806");
		queryParams.add("perPage",Integer.toString(amount));
		queryParams.add("start",Integer.toString(offset));
		
		
		queryParams.add("apikey", "l7xxeb4363ce0bcc441eb94134734dec9aed");
		
		String response1 ="";

		try{
			response1 = resource.queryParams(queryParams).accept("application/json").get(String.class);
			return response1;

		}catch( com.sun.jersey.api.client.UniformInterfaceException e){
			ProblemLogger.logProblem("UniformInterfaceException for term: "+phrase+". Waiting and retrying");
			sleep(2000);
			try{
				response1 = resource.queryParams(queryParams).accept("application/json").get(String.class);
				return response1;

			}catch( com.sun.jersey.api.client.UniformInterfaceException ex){
				System.err.println("Double: "+ex);
				ProblemLogger.logProblem("Double: "+ex);
				ex.printStackTrace();
			
			}
		}


		return response1;
	}

	public static ArrayList<Produkt> getDistinctProduktsFor(String phrase,int targetAmout){
		Map<String,Produkt> retValue=new TreeMap<String,Produkt>();
		Map<String,Produkt> allProducts=new TreeMap<String,Produkt>();

		int offset=0;
		while(retValue.size()<=targetAmout) {
			ArrayList<Produkt> produktsFor = getProduktsFor(phrase,50,offset);
			for(Produkt p:produktsFor) {
				allProducts.put(p.getUrl(), p);
				if(!retValue.containsKey(p.getUrl())&&p.getNazwa().toLowerCase().indexOf(phrase.toLowerCase())>=0) {
					retValue.put(p.getUrl(), p);
				}
			}
			offset+=20;
		}
		ArrayList<Produkt> ret=new ArrayList<Produkt>();
		ret.addAll(retValue.values());
		return ret;
		
	}
	public static ArrayList<Produkt> getProduktsFor(String phrase){
		return getProduktsFor(phrase, 50, 0);
	}
	public static ArrayList<Produkt> getProduktsFor(String phrase,int amount,int offset){
		ArrayList<Produkt> retValue=new ArrayList<Produkt>();
		
		String response=getResponse(phrase,amount,offset);
//		sleep(1000); //just to not exceed limit
		JSONObject root=new JSONObject(response);
		JSONArray produkts=((JSONArray)root.get("products"));
		
		for(int i=0;i<produkts.length();i++){
			JSONObject ApiProdukt=(JSONObject) produkts.get(i);
			retValue.add(ShopComApiClientParticularProduct.getProduktByShopId((Integer)ApiProdukt.get("id")));
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
