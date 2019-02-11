package mariusz.ambroziak.kassistant.controllers.bootstrap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import api.extractors.EdamanQExtract;
import mariusz.ambroziak.kassistant.Apiclients.edaman.EdamanRecipeApiClient;
import mariusz.ambroziak.kassistant.Apiclients.edaman.RecipeData;
import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaFoodDetails;
import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaFoodId;
import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaNutrientApiClient;
import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaNutrientApiClientParticularFood;
import mariusz.ambroziak.kassistant.agents.ReadingNutritientsUsdaAgent;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.api.agents.EdamanRecipeAgent;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.jsp.MultiProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.jsp.ProduktWithRecountedPrice;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.ApiIngredientAmount;
import mariusz.ambroziak.kassistant.model.utils.NutrientDetailsOfBasicIngredient;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;


@Controller
public class NutrientSearching_controller_bootstrap {


//	@RequestMapping(value="/b_nutritientForFoodName")
//	public ModelAndView apiRecipeParsed(HttpServletRequest request) throws AgentSystemNotStartedException {
//
//		String foodName=request.getParameter(JspStringHolder.foodName);
//
//		if(foodName==null||foodName.equals("")) {
//			return new ModelAndView(StringHolder.bootstrapFolder+"boot_foodSearch_byFoodName");
//		}else {
//			Collection<UsdaFoodId> searchForProduktsInUsdaDb = UsdaNutrientApiClient.searchForProduktsInUsdaDbSortByName(foodName);
//			
//			
//			ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_usdaProducts");
//			
//			mav.addObject("foodName",foodName);
//			mav.addObject("usdaProducts",searchForProduktsInUsdaDb);
//
//			return mav;
//		}
//		
//		
//
//	}
	
	
	@RequestMapping(value="/b_nutritientForFoodName")
	public ModelAndView agentNutrientsParsed(HttpServletRequest request) {

		String foodName=request.getParameter(JspStringHolder.foodName);

		if(foodName==null||foodName.equals("")) {
			return new ModelAndView(StringHolder.bootstrapFolder+"boot_foodSearch_byFoodName");
		}else {
			Collection<UsdaFoodId> searchForProduktsInUsdaDb;
			try {
				searchForProduktsInUsdaDb = ReadingNutritientsUsdaAgent.searchForMultiProduct(foodName);
			} catch (AgentSystemNotStartedException e) {
				return new ModelAndView("agentSystemNotStarted");
			} 
					//UsdaNutrientApiClient.searchForProduktsInUsdaDbSortByName(foodName);
			
			
			ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_usdaProducts");
			
			mav.addObject("foodName",foodName);
			mav.addObject("usdaProducts",searchForProduktsInUsdaDb);

			return mav;
		}
	}


	private ModelAndView returnAgentSystemNotStartedPage() {
		return new ModelAndView("agentSystemNotStarted");
	}
}
