package mariusz.ambroziak.kassistant;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import madkit.kernel.Madkit;
import mariusz.ambroziak.kassistant.QuantityExtractor.JedzDobrzeExtractor;
import mariusz.ambroziak.kassistant.agents.ClockAgent;
import mariusz.ambroziak.kassistant.agents.ProduktAgent;
import mariusz.ambroziak.kassistant.agents.ReadingAgent;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.agents.config.AgentsSystem;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.dao.ProduktDAO;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.exceptions.ShopNotFoundException;
import mariusz.ambroziak.kassistant.model.Health_Relevant_Ingredient;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.jsp.SearchResult;
import mariusz.ambroziak.kassistant.model.utils.BasicIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.CompoundIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithAllIngredients;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithBasicIngredients;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;

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
	@Autowired
	private ProduktDAO produktDao;
	
	@RequestMapping(value="/get_food_ingredient_data")
	public ModelAndView produkts(HttpServletRequest request) {
		String phrase=request.getParameter("ingredient_phrase");
		
		if(phrase==null||phrase.equals(""))
		{
			return new ModelAndView("");
		}else{
			
			ModelAndView mav=new ModelAndView("List");
			
			DaoProvider.getInstance().getHealthRelevantIngredientsDao().list();
			HashMap<Health_Relevant_Ingredient, PreciseQuantity> scrapSkladnik = JedzDobrzeScrapper.scrapSkladnik(phrase);
			
			ArrayList<String> lista=new ArrayList<String>();
			
			lista.add("Dla skladnika zywno�ciowego: "+phrase+" znaleziono nast�puj�ce wartosci od�ywcze:");
			
			for(Health_Relevant_Ingredient key:scrapSkladnik.keySet()){
				lista.add(key.getName()+" - "+scrapSkladnik.get(key));
				
			}
			
			mav.addObject("list",lista);
	
			
			return mav;
		}
	}
	
	
	


		
	@RequestMapping(value="/get_food_data_banan")
	public ModelAndView produkts11() {

		ModelAndView mav=new ModelAndView("List");
		
		DaoProvider.getInstance().getHealthRelevantIngredientsDao().list();
		HashMap<Health_Relevant_Ingredient, PreciseQuantity> scrapSkladnik = JedzDobrzeScrapper.scrapSkladnik("banan");
		
		ArrayList<String> lista=new ArrayList<String>();
		
		lista.add("Dla skladnika zywno�ciowego: banan znaleziono nast�puj�ce wartosci od�ywcze:");
		
		for(Health_Relevant_Ingredient key:scrapSkladnik.keySet()){
			lista.add(key.getName()+" - "+scrapSkladnik.get(key));
			
		}
		
		mav.addObject("list",lista);

		
		return mav;
	}		
		
	
	
}