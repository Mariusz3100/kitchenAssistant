package mariusz.ambroziak.kassistant.Apiclients;


import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class TestApiClient {


	public static String getResponse(){
		ClientConfig cc = new DefaultClientConfig();
		cc.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);

		Client c = Client.create();
		WebResource resource = c.resource("https://api.edamam.com/search");

		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("q", "chicken");
		//       queryParams.add("app_id", "af08be14");


		String response1 = resource.queryParams(queryParams).accept("text/plain").get(String.class);

		System.out.println();
		return response1;

	}





	public static void main(String [] args){
		new TestApiClient();
	}




}
