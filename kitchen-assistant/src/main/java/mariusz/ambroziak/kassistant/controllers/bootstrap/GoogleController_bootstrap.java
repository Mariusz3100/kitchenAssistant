package mariusz.ambroziak.kassistant.controllers.bootstrap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.jdo.JDOHelper;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import madkit.kernel.Madkit;
import mariusz.ambroziak.kassistant.Apiclients.edaman.DietLabels;
import mariusz.ambroziak.kassistant.Apiclients.edaman.HealthLabels;
import mariusz.ambroziak.kassistant.agents.ClockAgent;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.agents.config.AgentsSystem;
import mariusz.ambroziak.kassistant.api.agents.GoogleAccessAgent;
import mariusz.ambroziak.kassistant.controllers.GoogleControllerLogic;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
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

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;



//import database.holders.StringHolder;

@Controller
public class GoogleController_bootstrap{
	

	@RequestMapping(value="/b_getGoogleData")
	public ModelAndView problems1(HttpServletRequest request) {
		ArrayList<String> dietList=new ArrayList<>();
		ArrayList<String> healthList=new ArrayList<>();

		try {
			dietList = getHealthLimitations();
			healthList=getDietLimitations();
		} catch (GoogleDriveAccessNotAuthorisedException e) {
			return createGoogleAccessNotGrantedMav();
		} catch (AgentSystemNotStartedException e) {
			return returnAgentSystemNotStartedPage();
		}
		ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_GoogleData");

		mav.addObject("healthLabels",healthList);
		mav.addObject("dietLabels",dietList);

		return mav;


	}

	protected ModelAndView returnAgentSystemNotStartedPage() {
		return new ModelAndView(StringHolder.bootstrapFolder+"boot_agentSystemNotStarted");
	}
	
	private ArrayList<String> getDietLimitations() throws AgentSystemNotStartedException, GoogleDriveAccessNotAuthorisedException {
		ArrayList<DietLabels> dietLimitations = GoogleAccessAgent.getDietLimitations();
		ArrayList<String> retValue=new ArrayList<>();
		for(DietLabels dl:dietLimitations) {
			retValue.add(dl.getParameterName());
		}
		return retValue;
	}

	private ArrayList<String> getHealthLimitations() throws AgentSystemNotStartedException, GoogleDriveAccessNotAuthorisedException {
		ArrayList<HealthLabels> healthLimitations = GoogleAccessAgent.getHealthLimitations();
		ArrayList<String> retValue=new ArrayList<>();
		for(HealthLabels hl:healthLimitations) {
			retValue.add(hl.getParameterName());
		}
		return retValue;
	}

	private ModelAndView createGoogleAccessNotGrantedMav() {
		ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_GoogleDataAccessDeleted");
		return mav;
	}


	
	@RequestMapping(value="/b_googleDelete")
	public ModelAndView b_googleDelete(HttpServletRequest request) throws IOException {

		deleteLocalAuthorisationData();

		ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_GoogleData");
		mav.addObject("deleted",true);
		return mav;


	}

	private void deleteLocalAuthorisationData() {
		// TODO Auto-generated method stub
		
	}

//	
//	protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
//		GenericUrl url = new GenericUrl(req.getRequestURL().toString());
//		url.setRawPath("/oauth2callback");
//		return url.build();
//	}

//
//	private int i=0;
//	@Override
//	protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		return Integer.toString(i);
//	}



}
