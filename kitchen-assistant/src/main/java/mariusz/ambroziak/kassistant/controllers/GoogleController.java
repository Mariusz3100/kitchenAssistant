package mariusz.ambroziak.kassistant.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import madkit.kernel.Madkit;
import mariusz.ambroziak.kassistant.Apiclients.edaman.DietLabels;
import mariusz.ambroziak.kassistant.Apiclients.edaman.HealthLabels;
import mariusz.ambroziak.kassistant.Apiclients.google.GoogleDriveApiClient;
import mariusz.ambroziak.kassistant.agents.ClockAgent;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.agents.config.AgentsSystem;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.model.Problem;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.jsp.MultiProdukt_SearchResult;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;



//import database.holders.StringHolder;

@Controller
public class GoogleController {
	public static String problems="";




	@RequestMapping(value="/google/health/get")
	public ModelAndView problems1(HttpServletRequest request) throws IOException {
		ArrayList<HealthLabels> healthLimittions = GoogleDriveApiClient.getHealthLimitations();
		
		ModelAndView mav=new ModelAndView("List");
		ArrayList<String> list=new ArrayList<String>();

		if(healthLimittions==null||healthLimittions.isEmpty())
			list.add("Health labels for a user not found.");
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

	@RequestMapping(value="/google/diet/get")
	public ModelAndView getDiet(HttpServletRequest request) throws IOException {
		ArrayList<DietLabels> healthLimittions = GoogleDriveApiClient.getDietLimitations();
		
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
	
	
	@RequestMapping(value="/google/health/renew")
	public ModelAndView googleDelete(HttpServletRequest request) throws IOException {
		
		GoogleDriveApiClient.deleteCredentials();
		
		ModelAndView mav=new ModelAndView("List");
		ArrayList<String> list=new ArrayList<String>();
		
		list.add("renewed");
		
		mav.addObject("list",list);
		return mav;
		
		
	}



}
