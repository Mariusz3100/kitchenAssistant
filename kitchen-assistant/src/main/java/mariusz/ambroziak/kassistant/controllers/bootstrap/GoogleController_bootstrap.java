package mariusz.ambroziak.kassistant.controllers.bootstrap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.JDOHelper;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import madkit.kernel.Madkit;
import mariusz.ambroziak.kassistant.Apiclients.edaman.DietLabels;
import mariusz.ambroziak.kassistant.Apiclients.edaman.HealthLabels;
import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleDriveApiClient;
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
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
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
	

	@RequestMapping(value=JspStringHolder.GOOGLE_GET_DATA_SUFFIX)
	public ModelAndView getGoogleData(HttpServletRequest request) {
		return create_GetLabelsFromGoogleDrive_Mav();


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


	@RequestMapping(value=JspStringHolder.GOOGLE_AUTHORISATION_SUCCESSFUL_SUFFIX)
	public ModelAndView b_googleAuthorized(HttpServletRequest request) throws IOException {
		ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_GoogleDataAccessGranted");
		return mav;


	}
	@RequestMapping(value=JspStringHolder.GOOGLE_DELETION_SUFFIX)
	public ModelAndView b_googleDelete(HttpServletRequest request) throws IOException {

		try {
			deleteLocalAuthorisationData();
		} catch (AgentSystemNotStartedException e) {
			// TODO Auto-generated catch block
			return returnAgentSystemNotStartedPage();
		}

		ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_GoogleDataAccessDeleted");
		mav.addObject("deleted",true);
		return mav;


	}

	private void deleteLocalAuthorisationData() throws AgentSystemNotStartedException {
		GoogleAccessAgent.deleteGoogleAuthorisationData();
		
	}
	
	@RequestMapping(value="boot_getAllLabels")
	public ModelAndView b_getAllLabels(HttpServletRequest request) throws IOException {
		DietLabels[] diets=DietLabels.values();
		HealthLabels[] healths=HealthLabels.values();
		
		ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_allLabelsAvailable");

		mav.addObject("healthLabels",healths);
		mav.addObject("dietLabels",diets);

		return mav;
		
	}
	
	
	@RequestMapping(value="b_editLabelsDone")
	public ModelAndView b_editedLabels(HttpServletRequest request) throws IOException {
		String[] dietLabelsChosen=request.getParameterValues(JspStringHolder.dietCheckboxName);
		String[] healthLabelsChosen=request.getParameterValues(JspStringHolder.healthCheckboxName);

		List<DietLabels> chosenDietLabels = dietLabelsChosen==null?new ArrayList<>():DietLabels.tryRetrieving(Arrays.asList(dietLabelsChosen));
		List<HealthLabels> chosenHealthLabels = healthLabelsChosen==null?new ArrayList<>():HealthLabels.tryRetrieving(Arrays.asList(healthLabelsChosen));
		
		try {
			GoogleAccessAgent.saveDietLimitations(chosenDietLabels);
			GoogleAccessAgent.saveHealthLimitations(chosenHealthLabels);
		} catch (GoogleDriveAccessNotAuthorisedException e) {
			return createGoogleAccessNotGrantedMav();
		} catch (AgentSystemNotStartedException e) {
			return returnAgentSystemNotStartedPage();
		}
		
		return create_GetLabelsFromGoogleDrive_Mav();		
	}

	private ModelAndView create_GetLabelsFromGoogleDrive_Mav() {
		ArrayList<String> dietList=new ArrayList<>();
		ArrayList<String> healthList=new ArrayList<>();

		try {
			dietList = getDietLimitations();
			healthList=getHealthLimitations();
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
	
	@RequestMapping(value="b_editLabels")
	public ModelAndView b_editLabels(HttpServletRequest request) throws IOException {
		DietLabels[] diets=DietLabels.values();
		HealthLabels[] healths=HealthLabels.values();
		
		ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_editLabels");

		mav.addObject("healthLabels",healths);
		mav.addObject("dietLabels",diets);

		try {
			Map<String,String> dietLimitations = getDietLimitationsAsMap();
			Map<String,String> healthLimitations = getHealthLimitationsAsMap();
			
			
			mav.addObject("selectedHealthLabels",healthLimitations);
			mav.addObject("selectedDietLabels",dietLimitations);
			
		} catch (AgentSystemNotStartedException e) {
			return returnAgentSystemNotStartedPage();
		} catch (GoogleDriveAccessNotAuthorisedException e) {
			return createGoogleAccessNotGrantedMav();
		}
		
		
		return mav;
	}

	private Map<String, String> getDietLimitationsAsMap() throws AgentSystemNotStartedException, GoogleDriveAccessNotAuthorisedException {
		Map<String, String> retValue=new HashMap<String, String>();
		ArrayList<String> dietLimitations = getDietLimitations();
		
		for(String dl: dietLimitations) {
			retValue.put(dl, dl);
		}
		
		return retValue;
	}

	private Map<String, String> getHealthLimitationsAsMap() throws AgentSystemNotStartedException, GoogleDriveAccessNotAuthorisedException {
		Map<String, String> retValue=new HashMap<String, String>();
		ArrayList<String> healtLimitations = getHealthLimitations();
		
		for(String dl: healtLimitations) {
			retValue.put(dl, dl);
		}
		
		return retValue;
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
