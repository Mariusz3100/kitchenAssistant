package mariusz.ambroziak.kassistant.Apiclients.wikipedia;

import javax.ws.rs.core.MultivaluedMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import mariusz.ambroziak.kassistant.Apiclients.edaman.DietLabels;
import mariusz.ambroziak.kassistant.Apiclients.edaman.EdamanRecipeApiParameters;
import mariusz.ambroziak.kassistant.Apiclients.edaman.HealthLabels;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;

public class WikipediaApiClient {
	public final static String baseUrl="https://en.wikipedia.org/api/rest_v1/page/title/";


	public static String getRedirectIfAny(String original) throws Page404Exception {
		if(original.endsWith(".")) {
			original=original.substring(0,original.length()-1);
		}
		String url=baseUrl+original;
		try {
		String response=getResponse(url);
		
		JSONObject json=new JSONObject(response);
		
		
		if(json.has("items")) {
			JSONArray jsonArray = json.getJSONArray("items");
			if(jsonArray.length()>0) {
				JSONObject jsonObject = jsonArray.getJSONObject(0);
				if(jsonObject.has("comment")) {
					String comment=jsonObject.getString("comment");
					
					if(comment.startsWith("redirect to [[")) {
						String result=comment.substring(14,comment.length()-2);
						return result;
					}
					
				}
			}
			
		}
			
		}catch(UniformInterfaceException e) {
			e.printStackTrace();
			if(e.getMessage().endsWith("returned a response status of 404 Not Found")) {
				throw new Page404Exception(url);
			}
		}
			
			
		
		
		return null;
	}


	private static String getResponse(String url) {
		ClientConfig cc = new DefaultClientConfig();

		Client c = Client.create();
		WebResource resource = c.resource(url);

		String response1 = resource.accept("application/json").get(String.class);
		return response1;
	}


	public static void main(String[] arg) throws Page404Exception {

		String x=getRedirectIfAny("tbsp");
		String y=getRedirectIfAny("tablespoon");
		String z=getRedirectIfAny("tbsp.");
		
		System.out.println(x+" "+y+" "+z);

		
		
	}
}
