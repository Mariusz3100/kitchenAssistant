package mariusz.ambroziak.kassistant.Apiclients.walmart;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class WalmartApiClient {

	
	
	
	
	
	
	
	
	
	private static String getResponse(String phrase) {
		ClientConfig cc = new DefaultClientConfig();
		cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);

		Client c = Client.create();
		WebResource resource = c.resource("http://api.walmartlabs.com/v1/items/10447841?format=json");

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("q", phrase);
		//       queryParams.add("app_id", "af08be14");


		String response1 = resource.queryParams(queryParams).accept("text/plain").get(String.class);
		return response1;
	}
}
