package mariusz.ambroziak.kassistant.controllers.bootstrap;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaFoodId;
import mariusz.ambroziak.kassistant.agents.ReadingNutritientsUsdaAgent;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
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


	
	@RequestMapping(value=JspStringHolder.NUTRIENT_BY_NAME_SUFFIX)
	public ModelAndView agentNutrientsParsed(HttpServletRequest request) {

		String foodName=request.getParameter(JspStringHolder.foodName);

		if(foodName==null||foodName.equals("")) {
			return new ModelAndView(StringHolder.bootstrapFolder+"boot_foodSearch_byFoodName");
		}else {
			Collection<UsdaFoodId> searchForProduktsInUsdaDb;
			try {
				searchForProduktsInUsdaDb = ReadingNutritientsUsdaAgent.searchForMultiProduct(foodName);
			} catch (AgentSystemNotStartedException e) {
				return returnAgentSystemNotStartedPage();
			} 
					//UsdaNutrientApiClient.searchForProduktsInUsdaDbSortByName(foodName);
			
			
			ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_usdaProducts");
			
			mav.addObject("foodName",foodName);
			mav.addObject("usdaProducts",searchForProduktsInUsdaDb);

			return mav;
		}
	}
	protected ModelAndView returnAgentSystemNotStartedPage() {
		return new ModelAndView(StringHolder.bootstrapFolder+"boot_agentSystemNotStarted");
	}


}
