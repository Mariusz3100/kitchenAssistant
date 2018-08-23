package mariusz.ambroziak.kassistant.Apiclients.googleAuth;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.JDOHelper;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import madkit.kernel.Madkit;
import mariusz.ambroziak.kassistant.Apiclients.edaman.DietLabels;
import mariusz.ambroziak.kassistant.Apiclients.edaman.HealthLabels;
import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleAuthApiClient;
import mariusz.ambroziak.kassistant.agents.ClockAgent;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.agents.config.AgentsSystem;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
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
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;
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
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;



//import database.holders.StringHolder;

@Controller

public class GoogleAuthApiClientCallbackController extends AbstractAuthorizationCodeCallbackServlet{

	public static final String callbackSuffix="/oauth2callback";
	public static final String callbackBaseUrl=StringHolder.baseUrl+callbackSuffix;


	public static Map<String,Credential> authorisationMap;



	static {
		authorisationMap=new HashMap<>();
	}
	@RequestMapping(value="/oauth2callback")
	public void localDoGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@RequestMapping(value="/google/authorise/successful")
	public ModelAndView authoriseSuccessfull(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ModelAndView model = new ModelAndView("List");

		ArrayList<String> strings=new ArrayList<String>();
		strings.add("Authorisation was successful and saved. "
				+"Now your recipe results will be different");

		model.addObject("list",strings);
		return model;
	}

	@Override
	protected AuthorizationCodeFlow initializeFlow() throws IOException {
		GoogleClientSecrets clientSecrets =
				GoogleClientSecrets.load(GoogleAuthApiParameters.getJsonFactory(), new StringReader(StringHolder.googleCredentials));

		GoogleAuthorizationCodeFlow flow =
				new GoogleAuthorizationCodeFlow.Builder(
						GoogleAuthApiParameters.getHTTP_TRANSPORT(), GoogleAuthApiParameters.getJsonFactory(), clientSecrets, GoogleAuthApiParameters.getScopes())
				.setDataStoreFactory(GoogleAuthApiParameters.getDATA_STORE_FACTORY())
				.setAccessType("offline")
				.build();

		return flow;

	}

	@Override

	protected void onSuccess(HttpServletRequest req, HttpServletResponse resp, Credential credential)
			throws ServletException, IOException {
		System.out.println("success");
//		System.out.println("receiving session: "+req.getSession().getId());
//		System.out.println("received credentials: "+credential);
		authorisationMap.put(getUserId(req), credential);


		resp.sendRedirect("/kitchen-assistant/google/authorise/successful");
	}




	@Override
	protected void onError(
			HttpServletRequest req, HttpServletResponse resp, AuthorizationCodeResponseUrl errorResponse)
					throws ServletException, IOException {
		System.out.println("err");
	}

	@Override
	protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
		GenericUrl url = new GenericUrl(req.getRequestURL().toString());
		url.setRawPath("/kitchen-assistant/oauth2callback");
		return url.build();
	}


	@Override
	protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
		// TODO Auto-generated method stub
		return req.getSession(true).getId(); 
	}



}
