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

import mariusz.ambroziak.kassistant.model.Problem;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;





public class TescoApiClientParticularProduct_notUsed {
	public static float fakeWeight=1000000;
	private static final String DETAILS_BASE_URL = "https://dev.tescolabs.com/product/";
	private static final String DETAILS_BASE_URL_TO_BE_SAVED_IN_DB= "https://dev.tescolabs.com/product/?tpnb=";

	private static final String headerName="Ocp-Apim-Subscription-Key";
	private static final String headerValue="bb40509242724f799153796d8718c3f3";


	private static String getResponseByUrl(String url) {
		ClientConfig cc = new DefaultClientConfig();
		cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);

		Client c = Client.create();

		WebResource client = c.resource(url);

		Builder clientWithParamsAndHeader = client.header(headerName, headerValue);

		String response1 ="";

		try{
			response1 = clientWithParamsAndHeader.accept("application/json").get(String.class);
			return response1;

		}catch( com.sun.jersey.api.client.UniformInterfaceException e){
			ProblemLogger.logProblem("UniformInterfaceException for url: "+url+". Waiting and retrying");
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
	private static String getResponseById(int id) {
		ClientConfig cc = new DefaultClientConfig();
		cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);

		Client c = Client.create();

		WebResource client = c.resource(DETAILS_BASE_URL);
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("tpnb", Integer.toString(id));


		WebResource clientWithParams = client.queryParams(queryParams);
		Builder clientWithParamsAndHeader = clientWithParams.header(headerName, headerValue);

		String response1 ="";

		try{
			response1 = clientWithParamsAndHeader.accept("application/json").get(String.class);
			return response1;

		}catch( com.sun.jersey.api.client.UniformInterfaceException e){
			ProblemLogger.logProblem("UniformInterfaceException for id: "+id+". Waiting and retrying");
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

	private static void sleep(long milis) {
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Produkt getProduktByUrl(String url){
		String response=getResponseByUrl(url);

		if(response==null||response.equals(""))
			return null;

		JSONObject root=new JSONObject(response);

		JSONArray products=root.getJSONArray("products");

		if(products.length()>0) {
			JSONObject actualProduct = products.getJSONObject(0);
			Produkt createParticularProduct = createParticularProduct(actualProduct);

			return createParticularProduct;

		}
		return null;
	}


	public static Produkt getProduktByShopId(int id){
		String response=getResponseById(id);

		if(response==null||response.equals(""))
			return null;

		JSONObject root=new JSONObject(response);

		JSONArray products=root.getJSONArray("products");

		if(products.length()>0) {
			JSONObject actualProduct = products.getJSONObject(0);
			Produkt createParticularProduct = createParticularProduct(actualProduct);

			return createParticularProduct;

		}
		return null;
	}

	public static Produkt updateParticularProduct(Produkt p) {
		if(p!=null) {
			String response=getResponseByUrl(p.getUrl());


			JSONObject jsonRoot=new JSONObject(response);
			JSONObject metadataJson=null;

			if(p.getMetadata()==null||p.getMetadata().equals("")) {
				metadataJson=new JSONObject();
			}else {
				metadataJson=new JSONObject(p.getMetadata());
			}



			JSONArray jsonProducts=jsonRoot.getJSONArray("products");
			if(jsonProducts.length()>0) {
				JSONObject jsonProduct = jsonProducts.getJSONObject(0);
				if(jsonProduct.has("qtyContents")) {
					JSONObject jsonQtyContents = jsonProduct.getJSONObject("qtyContents");
					if(jsonQtyContents.has("drainedWeight")) {
						metadataJson.put("drainedWeight",jsonQtyContents.get("drainedWeight"));
						p.setMetadata(metadataJson.toString());
					}
				}

			}
		}
		return p;
	}

	private static Produkt createParticularProduct(JSONObject singleProductJson) {
		String name = singleProductJson.getString("description");
		long tpnb = singleProductJson.getLong("tpnb");
		String detailsUrl=DETAILS_BASE_URL_TO_BE_SAVED_IN_DB+tpnb;
		String description = "";//actual description is missing in this api
		float price = -1;//(float)singleProductJson.getDouble("price");//price is missing too
		JSONObject  metadataJson=new JSONObject();
		JSONObject qtyContents = singleProductJson.getJSONObject("qtyContents");
		if(qtyContents.has("drainedWeight")) {
			metadataJson.put("drainedWeight",qtyContents.get("drainedWeight"));
		}
		String quantityString = calculateQuantityJspString(qtyContents, detailsUrl);

		Produkt result=new Produkt(detailsUrl,quantityString,name,"",description,price,false);
		return result;
	}


	private static String calculateQuantityJspString(JSONObject singleProductJson, String detailsUrl) {
		String contentsMeasureType = singleProductJson.getString("quantityUom");
		float contentsQuantity = (float)singleProductJson.getDouble("quantity");

		String quantityString="";
		if("KG".equals(contentsMeasureType)||"kg".equals(contentsMeasureType)) {
			contentsQuantity*=1000;
			contentsMeasureType="G";
		}

		if(!"G".equals(contentsMeasureType)&&!"g".equals(contentsMeasureType)) {
			ProblemLogger.logProblem("Found tesco product with type different than Grams:"+detailsUrl);

		}else {

			if(contentsMeasureType!=null&&(contentsMeasureType.equals("G")||contentsMeasureType.equals("g"))) {
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


	public static void main(String [] args){
		//System.out.println(getProduktsFor("chicken"));
		//System.out.println(getProduktByShopId("81866107"));
	}


}
