package mariusz.ambroziak.kassistant.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mariusz.ambroziak.kassistant.agents.FoodIngredientAgent;
import mariusz.ambroziak.kassistant.agents.ProduktAgent;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.IncopatibleAmountTypesException;
import mariusz.ambroziak.kassistant.exceptions.InvalidArgumentException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.exceptions.ShopNotFoundException;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.jsp.InvalidSearchResult;
import mariusz.ambroziak.kassistant.model.jsp.MultiProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.jsp.ProduktWithRecountedPrice;
import mariusz.ambroziak.kassistant.model.jsp.SearchResult;
import mariusz.ambroziak.kassistant.model.jsp.SingleProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.jsp.SkippedSearchResult;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.NotPreciseQuantity;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.AbstractQuantity;
import mariusz.ambroziak.kassistant.model.utils.BasicIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.GoodBadSkippedResults;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithAllIngredients;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithBasicIngredients;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.CompoundMapManipulator;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

@Controller
public class RecipeConstantNutrientsController {


	/**
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/recipeConstantNutrients")

	public ModelAndView recipeForm(HttpServletRequest request) {
		String recipeUrl=request.getParameter(JspStringHolder.recipeUrl_name);
		
		if(recipeUrl==null||recipeUrl.equals(""))
			return new ModelAndView("recipeConstantNutrientsForm");
		else{
			try {
				ArrayList<SearchResult> searchResultsParsed = RecipeAgent.getPhrasesAndQuantitiesFromRecipeUrl(recipeUrl);
				
				Map<String, Map<Nutrient, PreciseQuantity>> nutrientsMap = new HashMap<String, Map<Nutrient,PreciseQuantity>>();

				
				for(SearchResult sr:searchResultsParsed){
					Map<Nutrient, PreciseQuantity> parsedFoodIngredient = FoodIngredientAgent.parseFoodIngredient(sr.getProduktPhrase());
					
					
					
					if(parsedFoodIngredient!=null&&!parsedFoodIngredient.isEmpty()){
						nutrientsMap.put(sr.getProduktPhrase(), parsedFoodIngredient);
					}else{
						nutrientsMap.put(sr.getProduktPhrase(), parsedFoodIngredient);
					}
				}
				
				CompoundMapManipulator<String, Nutrient> cmm=new CompoundMapManipulator<String, Nutrient>();
				Map<String, Map<Nutrient, NotPreciseQuantity>> preciseToNotPreciseQuantity = cmm.preciseToNotPreciseQuantity(nutrientsMap);
				cmm=new CompoundMapManipulator<String, Nutrient>(preciseToNotPreciseQuantity);
				
				ModelAndView mav=new ModelAndView("recipeNutrientConstantData");		
				mav.addObject("lastMap", preciseToNotPreciseQuantity);
				mav.addObject("lastSum", cmm.sumUpInnerMaps());
				mav.addObject("allLastIngs", cmm.getAllInnerMapsKeys());
				return mav; 				
			} catch (AgentSystemNotStartedException e) {
				return returnAgentSystemNotStartedPage();
			} catch (Page404Exception e) {
				return returnPageNotFoundRecipeForm(recipeUrl);
			}
			
		}
		
		

	}

	private ModelAndView returnAgentSystemNotStartedPage() {
		return new ModelAndView("agentSystemNotStarted");
	}

	private ModelAndView returnPageNotFoundRecipeForm(String url) {
		ModelAndView mav=returnAgentSystemNotStartedPage();
		mav.addObject("invalidUrlInformation","Url "+url+" nie prowadzi do strony �adnego przepisu");
		return mav;
	}
	
}
