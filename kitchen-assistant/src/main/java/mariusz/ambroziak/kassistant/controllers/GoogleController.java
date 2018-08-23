package mariusz.ambroziak.kassistant.controllers;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.jdo.JDOHelper;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import madkit.kernel.Madkit;
import mariusz.ambroziak.kassistant.Apiclients.edaman.DietLabels;
import mariusz.ambroziak.kassistant.Apiclients.edaman.HealthLabels;
import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleAuthApiClient;
import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleAuthApiParameters;
import mariusz.ambroziak.kassistant.agents.ClockAgent;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.agents.config.AgentsSystem;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.exceptions.GoogleDriveAccessNotAuthorisedException;
import mariusz.ambroziak.kassistant.model.Problem;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.jsp.MultiProdukt_SearchResult;
import mariusz.ambroziak.kassistant.utils.StringHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;



//import database.holders.StringHolder;

@Controller
public class GoogleController extends AbstractAuthorizationCodeServlet{
	private static final String APPLICATION_NAME =
			"Kitchen Assistant";
//
//	/** Directory to store user credentials for this application. */
//	private static final java.io.File DATA_STORE_DIR = new java.io.File(
//			".credentials/drive-java-quickstart");
//
//	/** Global instance of the {@link FileDataStoreFactory}. */
//	private static FileDataStoreFactory DATA_STORE_FACTORY;
//
//	/** Global instance of the JSON factory. */
//	private static final JacksonFactory JSON_FACTORY =
//			JacksonFactory.getDefaultInstance();
//
//	/** Global instance of the HTTP transport. */
//	private static HttpTransport HTTP_TRANSPORT;
//
	/** Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials
	 * at ~/.credentials/drive-java-quickstart
	 */
	private static final List<String> SCOPES =
			//        Arrays.asList(DriveScopes.DRIVE_METADATA_READONLY);
			Arrays.asList(DriveScopes.DRIVE_FILE,DriveScopes.DRIVE_SCRIPTS,
					DriveScopes.DRIVE_APPDATA,DriveScopes.DRIVE);
	
//	static {
//		try {
//			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
//		} catch (Throwable t) {
//			t.printStackTrace();
//			System.exit(1);
//		}
//	}
	
	public static String problems="";




	@RequestMapping(value="/google/health/get")
	public ModelAndView problems1(HttpServletRequest request) throws IOException {
		ArrayList<HealthLabels> healthLimittions = null;

		try {
			healthLimittions = GoogleAuthApiClient.getHealthLimitations();
		} catch (GoogleDriveAccessNotAuthorisedException e) {
			return createGoogleAccessNotGrantedMav();
		}
		ModelAndView mav=new ModelAndView("List");

		ArrayList<String> list=new ArrayList<String>();
		

		if(healthLimittions==null||healthLimittions.isEmpty())
			list.add("Health labels for a were not found in his google drive.");

		else{
			list.add("Health labels for a user:");
	
			for(HealthLabels s:healthLimittions)
			{	
				list.add(s.getParameterName());
			}
		}
		
		mav.addObject("list",list);

		return mav;


	}
//
//	@RequestMapping(value="/google/test")
//	public ModelAndView test(HttpServletRequest request) throws IOException {
//		
//		getCredential();
//		ArrayList<String> arr=new ArrayList<String>();
//		
//		arr.add("teeesting");
//		ModelAndView mav=new ModelAndView("List");
//		mav.addObject("list",arr);
//		
//		return mav;
//	}


	private ModelAndView createGoogleAccessNotGrantedMav() {
		ArrayList<String> list=new ArrayList<String>();
		ModelAndView mav=new ModelAndView("List");

		list.add("Access to google drive was not authorised yet. Click <a href=\"/kitchen-assistant/google/authorise\">here</a> to authorise.");
		mav.addObject("list",list);

		return mav;
	}
	
	
	@RequestMapping(value="/google/diet/get")
	public ModelAndView getDiet(HttpServletRequest request) throws IOException {
		ArrayList<DietLabels> healthLimittions = null;
		try {
			healthLimittions = GoogleAuthApiClient.getDietLimitations();
		} catch (GoogleDriveAccessNotAuthorisedException e) {
			return createGoogleAccessNotGrantedMav();
		}
		
		ModelAndView mav=new ModelAndView("List");
		ArrayList<String> list=new ArrayList<String>();

		if(healthLimittions==null||healthLimittions.isEmpty())
			list.add("Diet labels for a user not found.");
		else{
			list.add("Diet labels for a user:");
	
			for(DietLabels s:healthLimittions)
			{	
				list.add(s.getParameterName());
			}
		}
		
		mav.addObject("list",list);

		return mav;


	}
	
	
	@RequestMapping(value="/google/health/delete")
	public ModelAndView googleDelete(HttpServletRequest request) throws IOException {
		
		GoogleAuthApiClient.deleteCredentials();
		
		ModelAndView mav=new ModelAndView("List");
		ArrayList<String> list=new ArrayList<String>();
		
		list.add("renewed");
		
		mav.addObject("list",list);
		return mav;
		
		
	}

//	@Override
//	protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
//		GoogleClientSecrets clientSecrets =
//				GoogleClientSecrets.load(JSON_FACTORY, new StringReader(StringHolder.googleIds));
//		
//		return new GoogleAuthorizationCodeFlow.Builder(
//				HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//		.setDataStoreFactory(DATA_STORE_FACTORY)
//		.setAccessType("offline")
//		.build();
//	}
	  @Override
	  protected AuthorizationCodeFlow initializeFlow() throws IOException {
		  GoogleClientSecrets clientSecrets =
					GoogleClientSecrets.load(GoogleAuthApiParameters.getJsonFactory(), new StringReader(StringHolder.googleCredentials));
			// Build flow and trigger user authorization request.
			GoogleAuthorizationCodeFlow flow =
					new GoogleAuthorizationCodeFlow.Builder(
							GoogleAuthApiParameters.getHTTP_TRANSPORT(), GoogleAuthApiParameters.getJsonFactory(), clientSecrets, SCOPES)
					.setDataStoreFactory(GoogleAuthApiParameters.getDATA_STORE_FACTORY())
					.setAccessType("offline")
					.build();
			
			return flow;
		  
//		  return new AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(),
//	        HTTP_TRANSPORT,
//	        new JacksonFactory(),
//	        new GenericUrl("https://server.example.com/token"),
//	        new BasicAuthentication("s6BhdRkqt3", "7Fjfp0ZBr1KtDRbnfVdmIw"),
//	        "s6BhdRkqt3",
//	        "https://server.example.com/authorize").setDataStoreFactory(
//	        DATA_STORE_FACTORY)
//	        .build();
	  }
	  @Override
	  protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
	    GenericUrl url = new GenericUrl(req.getRequestURL().toString());
	    url.setRawPath("/oauth2callback");
	    return url.build();
	  }

	  
	  private int i=0;
	@Override
	protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
		// TODO Auto-generated method stub
		return Integer.toString(i);
	}



}
