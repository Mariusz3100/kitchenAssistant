package mariusz.ambroziak.kassistant.controllers;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import madkit.kernel.Madkit;
import mariusz.ambroziak.kassistant.QuantityExtractor.AuchanQExtract;
import mariusz.ambroziak.kassistant.QuantityExtractor.JedzDobrzeExtractor;
import mariusz.ambroziak.kassistant.agents.ClockAgent;
import mariusz.ambroziak.kassistant.agents.FoodIngredientAgent;
import mariusz.ambroziak.kassistant.agents.ProduktAgent;
import mariusz.ambroziak.kassistant.agents.ReadingAgent;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.agents.config.AgentsSystem;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.dao.ProduktDAO;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.exceptions.ShopNotFoundException;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.jsp.MultiProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.BasicIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.CompoundIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithAllIngredients;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithBasicIngredients;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.StringHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import testing.QuantityTesting;
import webscrappers.JedzDobrzeScrapper;
import webscrappers.auchan.AuchanRecipeParser;



//import database.holders.StringHolder;

@Controller
public class FoodIngredientController {

	@RequestMapping(value="/get_food_ingredient_data")
	public ModelAndView produkts(HttpServletRequest request) {
		try {
			request.setCharacterEncoding(StringHolder.ENCODING);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String phrase=request.getParameter("ingredient_phrase");
		
		if(phrase==null||phrase.equals(""))
		{
			return new ModelAndView("foodIngredientForm");
		}else{
			
			ModelAndView mav=new ModelAndView("List");
			ArrayList<String> lista=new ArrayList<String>();
			
			
			Map<Nutrient, PreciseQuantity> scrapSkladnik = null;
			try {
				scrapSkladnik = FoodIngredientAgent.parseFoodIngredient(phrase);
			} catch (AgentSystemNotStartedException e) {
				return createAgentSystemNotStartedMav();
			}
			
			
			

			
			if(scrapSkladnik!=null&&scrapSkladnik.size()>0)
			{
				lista.add("Dla skladnika zywnoœciowego: "+phrase+" znaleziono nastêpuj¹ce wartosci od¿ywcze:<br>");
				
				for(Nutrient key:scrapSkladnik.keySet()){
					lista.add(key.getName()+" - "+scrapSkladnik.get(key));
				}				
			}else{
				lista.add("Skladnika zywnoœciowego: "+phrase+" nie odnaleziono.<br>");
			}
			
			
			mav.addObject("list",lista);
	
			
			return mav;
		}
	}
	
	
	@RequestMapping(value="/logFood")
	public ModelAndView simpleFoodLogging(HttpServletRequest request) {
		try {
			request.setCharacterEncoding(StringHolder.ENCODING);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String phrase=request.getParameter("foodPhrase");
		
		if(phrase==null||phrase.equals(""))
		{
			return new ModelAndView("foodLogForm");
		}else{
			String invalidEntriesInfo="";
			ModelAndView mav=new ModelAndView("List");
			
			Map<String, Map<Nutrient, PreciseQuantity>> resultsMap=new HashMap<String, Map<Nutrient,PreciseQuantity>>();
			
			String[] entries=phrase.split(",");
			try {
				for(String entry:entries){
					String[] dividedEntry=entry.split(":");
					if(dividedEntry.length<2){
						invalidEntriesInfo+="entry "+entry+" doesn't have proper distinction between quantity and produkt<br>";
					}
					PreciseQuantity extractedQuantity = AuchanQExtract.extractQuantity(dividedEntry[0]);
					String amount = dividedEntry[0];
					String produktPhrase= dividedEntry[1];
					Map<Nutrient, PreciseQuantity> nutrients = FoodIngredientAgent.parseFoodIngredient(amount);
					
					resultsMap.put(amount, nutrients);
					
				}
			} catch (AgentSystemNotStartedException e) {
				return createAgentSystemNotStartedMav();
			}
			
			ArrayList<String> lista=new ArrayList<String>();
			Map<Nutrient, PreciseQuantity> scrapSkladnik = null;
			try {
				scrapSkladnik = FoodIngredientAgent.parseFoodIngredient(phrase);
			} catch (AgentSystemNotStartedException e) {
				
			}
			
			
			

			
			if(scrapSkladnik!=null&&scrapSkladnik.size()>0)
			{
				lista.add("Dla skladnika zywnoœciowego: "+phrase+" znaleziono nastêpuj¹ce wartosci od¿ywcze:<br>");
				
				for(Nutrient key:scrapSkladnik.keySet()){
					lista.add(key.getName()+" - "+scrapSkladnik.get(key));
				}				
			}else{
				lista.add("Skladnika zywnoœciowego: "+phrase+" nie odnaleziono.<br>");
			}
			
			
			mav.addObject("list",lista);
	
			
			return mav;
		}
	}


	private ModelAndView createAgentSystemNotStartedMav() {
		return new ModelAndView("agentSystemNotStarted");
	}
	


		
	@RequestMapping(value="/get_food_data_banan")
	public ModelAndView produkts11() {

		ModelAndView mav=new ModelAndView("List");
		
		HashMap<Nutrient, PreciseQuantity> scrapSkladnik = JedzDobrzeScrapper.scrapSkladnik("banan");
		
		ArrayList<String> lista=new ArrayList<String>();
		
		lista.add("Dla skladnika zywnoœciowego: banan znaleziono nastêpuj¹ce wartosci od¿ywcze:");
		
		for(Nutrient key:scrapSkladnik.keySet()){
			lista.add(key.getName()+" - "+scrapSkladnik.get(key));
			
		}
		
		mav.addObject("list",lista);

		
		return mav;
	}
		
	
	
}
