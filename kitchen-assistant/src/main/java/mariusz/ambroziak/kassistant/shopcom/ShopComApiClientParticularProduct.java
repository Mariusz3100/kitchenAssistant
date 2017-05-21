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
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;





public class ShopComApiClientParticularProduct {


	private static String getResponseById(String id) {
		ClientConfig cc = new DefaultClientConfig();
		cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);

		Client c = Client.create();
		WebResource resource = c.resource("https://api.shop.com/stores/v1/products/"+id);

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("allperms","false");
		queryParams.add("apikey", "l7xxeb4363ce0bcc441eb94134734dec9aed");
		String response1 = resource.queryParams(queryParams).accept("application/json").get(String.class);
		return response1;
	}

	private static String getResponseByUrl(String url) {
		ClientConfig cc = new DefaultClientConfig();
		cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);

		Client c = Client.create();
		WebResource resource = c.resource(url);

		String response1 = resource.accept("application/json").get(String.class);
		return response1;
	}


	public static Produkt getProduktByShopId(String id){
		String response=getResponseById(id);
		JSONObject root=new JSONObject(response);
		String description=(String) root.get("description");
		String name=(String) root.get("caption");
		double price=(double) root.get("price");
		double weight=(double) root.get("weight");
		
		PreciseQuantity pq=new PreciseQuantity((float)(weight*1000),AmountTypes.mg);
		Produkt resultProdukt =new Produkt();
		resultProdukt.setNazwa(name);
		resultProdukt.setOpis(description);
		resultProdukt.setUrl("https://api.shop.com/stores/v1/products/"+id);
		resultProdukt.setCena((float)price);
		resultProdukt.setQuantityPhrase(pq.toJspString());

		return resultProdukt;
	}


	public static Produkt getProduktByUrl(String url){
		String response=getResponseByUrl(url);
		JSONObject root=new JSONObject(response);
		String description=(String) root.get("description");
		String name=(String) root.get("caption");
		double price=(double) root.get("price");
		double weight=(double) root.get("weight");
		
		PreciseQuantity pq=new PreciseQuantity((float)(weight*1000),AmountTypes.mg);
		Produkt resultProdukt =new Produkt();
		resultProdukt.setNazwa(name);
		resultProdukt.setOpis(description);
		resultProdukt.setUrl(url);
		resultProdukt.setCena((float)price);
		resultProdukt.setQuantityPhrase(pq.toJspString());

		return resultProdukt;
	}


	public static void main(String [] args){
		//System.out.println(getProduktsFor("chicken"));
		System.out.println(getProduktByShopId("1471164119"));
	}


}
