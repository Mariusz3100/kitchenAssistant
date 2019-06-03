package mariusz.ambroziak.kassistant.shopcom;


import java.util.ArrayList;

import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.api.client.json.Json;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import mariusz.ambroziak.kassistant.ai.categorisation.MetadataConstants;
import mariusz.ambroziak.kassistant.model.Problem;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;





public class ShopComApiClientParticularProduct {
	public static float fakeWeight=1000000;

	private static String getResponseById(String id) {
		ClientConfig cc = new DefaultClientConfig();
		cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);

		Client c = Client.create();
		WebResource resource = c.resource("https://api.shop.com/stores/v1/products/"+id);

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("allperms","false");
		queryParams.add("apikey", "l7xxeb4363ce0bcc441eb94134734dec9aed");
		
//		sleep(2000);
		
		try{
			String response1 = resource.queryParams(queryParams).accept("application/json").get(String.class);
			return response1;

		}catch( com.sun.jersey.api.client.UniformInterfaceException e){
			ProblemLogger.logProblem("UniformInterfaceException for id: "+id+". Waiting and retrying");
			sleep(2000);
			try{
				String response1 = resource.queryParams(queryParams).accept("application/json").get(String.class);
				return response1;

			}catch( com.sun.jersey.api.client.UniformInterfaceException ex){
				System.err.println("Double: "+ex);
				ProblemLogger.logProblem("Double: "+ex);
				ex.printStackTrace();
			
			}
		}
		return "";
	}

	private static void sleep(long milis) {
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String getResponseByUrl(String url) {
		ClientConfig cc = new DefaultClientConfig();
		cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);

		Client c = Client.create();
		WebResource resource = c.resource(url);

		String response1 = resource.accept("application/json").get(String.class);
		return response1;
	}

	public static Produkt getProduktByShopId(Integer id){
		return getProduktByShopId(Integer.toString(id));
//		JSONObject root=new JSONObject(response);
//		String description=(String) root.get("description");
//		String name=(String) root.get("caption");
//		Object proceObj = root.get("price");
//		float price=Float.parseFloat(proceObj==null?"0":proceObj.toString());
//		
//		
//		
//		
//		double weight=(double) root.get("weight");
//		
//		PreciseQuantity pq=new PreciseQuantity(fakeWeight,AmountTypes.mg);
//		Produkt resultProdukt =new Produkt();
//		resultProdukt.setNazwa(name);
//		resultProdukt.setOpis(description);
//		resultProdukt.setUrl("https://api.shop.com/stores/v1/products/"+id);
//		resultProdukt.setCena((float)price);
//		resultProdukt.setQuantityPhrase(pq.toJspString());
//
//		return resultProdukt;
	}
	public static Produkt getProduktByShopId(String id){
		String response=getResponseById(id);
		
		if(response==null||response.equals(""))
			return null;
		
		JSONObject root=new JSONObject(response);
		String description=(String) root.get("description");
		String name=(String) root.get("caption");
		Object proceObj = root.get("price");
		float price=Float.parseFloat(proceObj==null?"0":proceObj.toString());
		
		
		double weight=-1;
		if(root.has("weight")) {
			weight=(double) root.get("weight");
		}
		PreciseQuantity pq=new PreciseQuantity((float)weight,AmountTypes.mg);
		Produkt resultProdukt =new Produkt();
		resultProdukt.setNazwa(name);
		resultProdukt.setOpis(description);
		resultProdukt.setUrl("https://api.shop.com/stores/v1/products/"+id);
		resultProdukt.setCena((float)price);
		resultProdukt.setQuantityPhrase(pq.toJspString());
		String metadata=calculateMetadata(root);

		resultProdukt.setMetadata(metadata);
		
		return resultProdukt;
	}


	private static String calculateMetadata(JSONObject root) {
		if(root==null||!root.has("categoryInfo"))
			return new JSONObject().toString();
		else
		{
			JSONObject catInfo = root.getJSONObject("categoryInfo");
			String category1 =catInfo.has("department")?catInfo.getString("department"):"";
			String category2 = catInfo.has("category")?catInfo.getString("category"):"";
			String category3= catInfo.has("productType")?catInfo.getString("productType"):"";
			JSONObject result=new JSONObject();
			String value = category1+MetadataConstants.stringListSeparator+category2+MetadataConstants.stringListSeparator+category3;
			result.put(MetadataConstants.categoryNameJsonName, value);
			return result.toString();
		}
		
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
		String metadata=calculateMetadata(root);

		resultProdukt.setMetadata(metadata);
		return resultProdukt;
	}


	public static void main(String [] args){
		//System.out.println(getProduktsFor("chicken"));
		for(int i=0;i<10;i++)
			System.out.println("\n\n\n\n"+getProduktByShopId("1471164119"));
	}


}
