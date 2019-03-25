package mariusz.ambroziak.kassistant.Apiclients.googleAuth;



import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
//import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
//import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;
import com.google.api.services.drive.model.*;

import mariusz.ambroziak.kassistant.Apiclients.edaman.DietLabels;
import mariusz.ambroziak.kassistant.Apiclients.edaman.HealthLabels;
import mariusz.ambroziak.kassistant.api.agents.GoogleAccessAgent;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.StringHolder;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files.Export;
import com.google.api.services.drive.DriveScopes;

import static org.hamcrest.CoreMatchers.containsString;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GoogleAuthApiClientController extends AbstractAuthorizationCodeServlet {

	  
	  private GoogleAuthorizationCodeFlow flow;

		@RequestMapping(value=JspStringHolder.GOOGLE_DELETION_SUFFIX)
		public ModelAndView b_googleDelete(HttpServletRequest request,HttpServletResponse response) throws IOException {

			try {
				deleteLocalAuthorisationData(response);
				Cookie kitchenAssistantCookie = getKitchenAssistantCookie(request);
				if(kitchenAssistantCookie!=null&&kitchenAssistantCookie.getValue()!=null)
				{
					Cookie cookie = new Cookie(StringHolder.CREDENTIAL_COOKIE_NAME, "");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
				DataStore<StoredCredential> credentialDataStore = flow.getCredentialDataStore().clear();

				credentialDataStore.clear();
			} catch (AgentSystemNotStartedException e) {
				// TODO Auto-generated catch block
				return returnAgentSystemNotStartedPage();
			}

			ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_GoogleDataAccessDeleted");
			mav.addObject("deleted",true);
			return mav;


		}

		private void deleteLocalAuthorisationData(HttpServletResponse response) throws AgentSystemNotStartedException {
			GoogleAccessAgent.deleteGoogleAuthorisationData(response);
			
		}
	  
	@RequestMapping(value=JspStringHolder.GOOGLE_AUTHORISATION_SUFFIX)
	  protected void localDoGet(HttpServletRequest request, HttpServletResponse response)
	      throws IOException, ServletException {
	    // do stuff
		  Cookie c=getKitchenAssistantCookie(request);
//		  try {
//			  boolean accessAuthorised = GoogleAccessAgent.isAccessAuthorised();
//			  if(accessAuthorised) {
//				  response.sendRedirect(JspStringHolder.GOOGLE_AUTHORISATION_SUCCESSFUL_SUFFIX); 
//			  }else {
//				  super.service(request, response);
//			  }
//			
//		} catch (AgentSystemNotStartedException e) {
//			response.sendRedirect(JspStringHolder.START_AGENT_SYSTEM_NOT_STARTED_SUFFIX);
//		}
		  if(c==null) {
			  super.service(request, response);
		  }else {
			  try {
				if(GoogleAccessAgent.isAccessAuthorised(c.getValue())) {
				    response.sendRedirect(JspStringHolder.GOOGLE_AUTHORISATION_SUCCESSFUL_SUFFIX);
				  }else {
					  super.service(request, response);

				  }
			} catch (AgentSystemNotStartedException e) {
			    response.sendRedirect(JspStringHolder.START_AGENT_SYSTEM_NOT_STARTED_SUFFIX);
			}
		  }
		  

		  System.out.println("stuff");

	  }


	  
	  @Override
	  protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
	    GenericUrl url = new GenericUrl(req.getRequestURL().toString());
	    url.setRawPath("/oauth2callback");
	    return url.build();
	  }

	  @Override
	  protected AuthorizationCodeFlow initializeFlow() throws IOException {
			GoogleClientSecrets clientSecrets =
					GoogleClientSecrets.load(GoogleAuthApiParameters.getJsonFactory(), new StringReader(StringHolder.googleCredentials));

		  
		  flow = new GoogleAuthorizationCodeFlow.Builder(
				GoogleAuthApiParameters.getHTTP_TRANSPORT(), GoogleAuthApiParameters.getJsonFactory(), clientSecrets, GoogleAuthApiParameters.getScopes())
		.setDataStoreFactory(GoogleAuthApiParameters.getDATA_STORE_FACTORY())
		.setAccessType("online")
		.build();
		  return flow;

	  }

		protected ModelAndView returnAgentSystemNotStartedPage() {
			return new ModelAndView(StringHolder.bootstrapFolder+"boot_agentSystemNotStarted");
		}
		
	  @Override
	  protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
	    // return user ID
		//  Cookie c=getKitchenAssistantCookie(req);
			  
			  
		  return req.getSession(true).getId(); 
	  }



	private Cookie getKitchenAssistantCookie(HttpServletRequest req) {
		Cookie[] cookies = req.getCookies();
		  
		  if(cookies!=null) {
			  for(Cookie c:cookies) {
				  if(StringHolder.CREDENTIAL_COOKIE_NAME.equals(c.getName())){
					  return c;
				  }
			  }
		  }
		  return null;
	}
	}
 