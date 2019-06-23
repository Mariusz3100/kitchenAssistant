package mariusz.ambroziak.kassistant.Apiclients.wordsapi;

import java.util.ArrayList;

import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

public class WordsApiClient {

	private static final String baseUrl= "https://wordsapiv1.p.mashape.com/words/";
	private static final String header1Name="X-RapidAPI-Host";
	private static final String header1Value="wordsapiv1.p.rapidapi.com";
	private static final String header2Name="X-RapidAPI-Key";
	private static final String header2Value="0a92a0113fmsh7837636f04b7a89p135639jsnf817375cb72c";

	//https://dev.tescolabs.com/grocry/products/?query=cucumber&offset=0&limit=10
	private static String getResponse(String phrase) throws WordNotFoundException {
		ClientConfig cc = new DefaultClientConfig();
		cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);
		String url=baseUrl+phrase;
		Client c = Client.create();
		WebResource client = c.resource(url);
		Builder clientWithParamsAndHeader=client.header(header1Name, header1Value).header(header2Name, header2Value);

		String response1 ="";

		try{
			response1 = clientWithParamsAndHeader.accept("application/json").get(String.class);
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

	public static ArrayList<WordsApiResult> searchFor(String phrase) throws WordNotFoundException {
		String response=getResponse(phrase);

		if(phrase==null|phrase.equals(""))
			return new ArrayList<WordsApiResult>();

		ArrayList<WordsApiResult> retValue=new ArrayList<WordsApiResult>();
		JSONObject jsonRoot=new JSONObject(response);
		try {
			String baseWord=jsonRoot.getString("word");
			if(jsonRoot.has("results")) {
				JSONArray resultsArray = jsonRoot.getJSONArray("results");

				for(int i=0;i<resultsArray.length();i++) {
					JSONObject jsonSingleResult = resultsArray.getJSONObject(i);
					String partOfSpeech="";
					if(jsonSingleResult.has("partOfSpeech")&&!jsonSingleResult.get("partOfSpeech").equals(JSONObject.NULL)) {
						partOfSpeech=jsonSingleResult.getString("partOfSpeech");
					}
					ArrayList<String> childTypes=new ArrayList<String>();

					if(jsonSingleResult.has("hasTypes")) {
						try {
							JSONArray jsonChildTypes=jsonSingleResult.getJSONArray("hasTypes");
							for(int j=0;j<jsonChildTypes.length();j++) {
								childTypes.add(jsonChildTypes.getString(j));
							}
						}catch(ClassCastException e) {
							ProblemLogger.logProblem("'hasTypes' is of different type than list for "+phrase);
						}
					}
					String definition=jsonSingleResult.getString("definition");

					WordsApiResult parsingResult=new WordsApiResult(phrase, baseWord, definition, partOfSpeech);
					retValue.add(parsingResult);


				}
			}
		}catch(JSONException e) {
			ProblemLogger.logProblem("Problem parsing words api response for "+phrase);
		}
		return retValue;

	}


	private static void sleep(long milis) {
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}





	public static void main(String[] arg) {
		String x;
		try {
			x = getResponse("tomato");
			System.out.println(x);

		} catch (WordNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
