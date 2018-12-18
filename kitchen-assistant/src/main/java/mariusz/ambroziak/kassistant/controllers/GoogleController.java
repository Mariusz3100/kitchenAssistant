package mariusz.ambroziak.kassistant.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.jdo.JDOHelper;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import madkit.kernel.Madkit;
import mariusz.ambroziak.kassistant.Apiclients.edaman.HealthLabels;
import mariusz.ambroziak.kassistant.agents.ClockAgent;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.agents.config.AgentsSystem;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.exceptions.GoogleDriveAccessNotAuthorisedException;
import mariusz.ambroziak.kassistant.model.Problem;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.jsp.MultiProdukt_SearchResult;

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
public class GoogleController extends GoogleControllerLogic{
	

	@RequestMapping(value="/google/health/get")
	public ModelAndView problems1(HttpServletRequest request) throws IOException {
		ArrayList<String> list=new ArrayList<>();
		try {
			list = getHealthLimitationLogic();
		} catch (GoogleDriveAccessNotAuthorisedException e) {
			return createGoogleAccessNotGrantedMav();
		}
		ModelAndView mav=new ModelAndView("List");

		mav.addObject("list",list);

		return mav;


	}

	private ModelAndView createGoogleAccessNotGrantedMav() {
		ArrayList<String> list=new ArrayList<String>();
		ModelAndView mav=new ModelAndView("List");

		list.add("Access to google drive was not authorised yet. Click <a href=\"/google/authorise\">here</a> to authorise.");
		mav.addObject("list",list);

		return mav;
	}


	@RequestMapping(value="/google/diet/get")
	public ModelAndView getDiet(HttpServletRequest request) throws IOException {
		ArrayList<String> list = null;
		try {
			list = getDietLimitationsLogic();
		} catch (GoogleDriveAccessNotAuthorisedException e) {
			return createGoogleAccessNotGrantedMav();
		}
		ModelAndView mav=new ModelAndView("List");

		mav.addObject("list",list);

		return mav;


	}
	@RequestMapping(value="/google/delete")
	public ModelAndView googleDelete(HttpServletRequest request) throws IOException {

		deleteLocalAuthorisationData();

		ModelAndView mav=new ModelAndView("List");
		ArrayList<String> list=new ArrayList<String>();

		list.add("renewed");

		mav.addObject("list",list);
		return mav;


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
