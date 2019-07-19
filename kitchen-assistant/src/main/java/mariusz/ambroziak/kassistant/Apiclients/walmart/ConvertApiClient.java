package mariusz.ambroziak.kassistant.Apiclients.walmart;

import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import api.extractors.AbstractQuantityEngExtractor.QuantityTranslation;
import mariusz.ambroziak.kassistant.Apiclients.RapidApiClient;
import mariusz.ambroziak.kassistant.Apiclients.wordsapi.WordNotFoundException;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

public class ConvertApiClient extends RapidApiClient{

	public static final String baseUrl="https://community-neutrino-currency-conversion.p.rapidapi.com/convert";
	private static final String header1Value="community-neutrino-currency-conversion.p.rapidapi.com";




	private static String getResponse(String phrase,AmountTypes targetType) throws WordNotFoundException {
		if(phrase==null||targetType==null)
			return "";

		ClientConfig cc = new DefaultClientConfig();
		cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);
		Client c = Client.create();
		WebResource client = c.resource(baseUrl);
		Builder clientWithParamsAndHeader=client.header(header1Name, header1Value).header(header2Name, header2Value);


		MultivaluedMap<String,String> formData = new MultivaluedMapImpl();
		formData.add("from-type", phrase);
		formData.add("to-type", targetType.toString());
		formData.add("from-value", "1");



		String response1 ="";

		try{
			response1 = clientWithParamsAndHeader.accept("application/json").post(String.class,formData);
			return response1;

		}catch( com.sun.jersey.api.client.UniformInterfaceException e){
			if(e.getMessage().contains("404")) {
				throw new WordNotFoundException(phrase);
			}else {
				ProblemLogger.logProblem("UniformInterfaceException for words api, term: "+phrase+". Waiting and retrying");
			}

		}


		return response1;
	}



	public static QuantityTranslation checkForTranslation(String phrase) throws WordNotFoundException {
		QuantityTranslation fromLocalCache=checkLocalCache(phrase);
		if(fromLocalCache!=null)
			return fromLocalCache;
		
		String response=getResponse(phrase, AmountTypes.mg);

		System.out.println(response);
		if(response!=null&&!response.isEmpty()) {
			JSONObject json=new JSONObject(response);
			String double1 = json.getString("result");
			if(!double1.isEmpty()) {
				float f=Float.parseFloat(double1);
				return new QuantityTranslation(AmountTypes.mg, f);
			}
		}

		response=getResponse(phrase, AmountTypes.ml);

		System.out.println(response);
		if(response!=null&&!response.isEmpty()) {
			JSONObject json=new JSONObject(response);
			String double1 = json.getString("result");
			if(!double1.isEmpty()) {

				float f=Float.parseFloat(double1);
				return new QuantityTranslation(AmountTypes.ml, f);
			}
		}


		return null;


	}



	private static QuantityTranslation checkLocalCache(String phrase) {
		if("tsp".equals(phrase))
			return new QuantityTranslation(AmountTypes.ml,(float)4.92892159375);
		
		return null;
	}

}
