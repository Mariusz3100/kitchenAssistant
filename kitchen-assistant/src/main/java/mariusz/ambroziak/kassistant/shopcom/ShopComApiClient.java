package mariusz.ambroziak.kassistant.shopcom;


import java.util.ArrayList;

import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.org.glassfish.external.probe.provider.annotations.Probe;

import mariusz.ambroziak.kassistant.model.Problem;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;





public class ShopComApiClient {


	private static String getResponse(String phrase) {
		ClientConfig cc = new DefaultClientConfig();
		cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);

		Client c = Client.create();
		WebResource resource = c.resource("https://api.shop.com/AffiliatePublisherNetwork/v1/products");

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("term", phrase);
		queryParams.add("publisherID", "TEST");
		queryParams.add("locale","en_US");
		queryParams.add("categoryId","1-32806");
		queryParams.add("perPage","50");
		queryParams.add("apikey", "l7xxeb4363ce0bcc441eb94134734dec9aed");
		
		

		
		String response1 = resource.queryParams(queryParams).accept("application/json").get(String.class);
		return response1;
	}


	public static ArrayList<Produkt> getProduktsFor(String phrase){
		ArrayList<Produkt> retValue=new ArrayList<Produkt>();
		
		String response=getResponse(phrase);
		
		JSONObject root=new JSONObject(response);
		JSONArray produkts=((JSONArray)root.get("products"));
		
		for(int i=0;i<produkts.length();i++){
			JSONObject ApiProdukt=(JSONObject) produkts.get(0);
			String description=(String) ApiProdukt.get("description");
			String name=(String) ApiProdukt.get("name");
			String url=(String) ApiProdukt.get("referralUrl");
			float price=getPrice(ApiProdukt,url);
			
			Produkt resultProdukt =new Produkt();
			resultProdukt.setNazwa(name);
			resultProdukt.setOpis(description);
			resultProdukt.setUrl(url);
			resultProdukt.setCena(price);
			retValue.add(resultProdukt);
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


}
