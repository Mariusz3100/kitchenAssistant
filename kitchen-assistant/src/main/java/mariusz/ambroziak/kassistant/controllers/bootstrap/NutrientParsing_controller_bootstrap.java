package mariusz.ambroziak.kassistant.controllers.bootstrap;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaFoodDetails;
import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaFoodId;
import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaNutrientApiClientParticularFood;
import mariusz.ambroziak.kassistant.agents.ReadingNutritientsUsdaAgent;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.StringHolder;


@Controller
public class NutrientParsing_controller_bootstrap {


	@RequestMapping(value=JspStringHolder.NUTRIENT_BY_NDBNO_SUFFIX)
	public ModelAndView apiRecipeParsed(HttpServletRequest request) {
		String ndbno=request.getParameter(JspStringHolder.ndbno);
		//04542

		if(ndbno==null||ndbno.equals(""))
		{
			return new ModelAndView(StringHolder.bootstrapFolder+"boot_foodSearch_byDbno");
		}
		else 
		{
			return getDForNdbno(ndbno);

		}





	}

	private ModelAndView getDForNdbno(String ndbno) {
		UsdaFoodDetails nutrientDetailsForDbno;
		try {
			nutrientDetailsForDbno = ReadingNutritientsUsdaAgent.retrieveSingleProduct(ndbno);
		} catch (AgentSystemNotStartedException e) {
				return returnAgentSystemNotStartedPage();
		}
				
		UsdaNutrientApiClientParticularFood.getNutrientDetailObjectForDbno(ndbno);
		ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_NutrientResultsForSingleFood");
		
		mav.addObject("productIdObject",nutrientDetailsForDbno.getId());
		mav.addObject("nutrients",nutrientDetailsForDbno.getNutrietsMapPer100g());

		return mav;
	}
	
	@RequestMapping(value="/nutrient_get_nutrient")
	public ModelAndView agentNutrientsSmartly(HttpServletRequest request) {

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
			
			if(searchForProduktsInUsdaDb!=null&&searchForProduktsInUsdaDb.size()==1) {
				Iterator<UsdaFoodId> iterator = searchForProduktsInUsdaDb.iterator();
				UsdaFoodId next = iterator.next();
				return getDForNdbno(next.getNdbno());
			}else {
				ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_usdaProducts");
				
				mav.addObject("foodName",foodName);
				mav.addObject("usdaProducts",searchForProduktsInUsdaDb);
				return mav;				
			}
			
		}
	}
	protected ModelAndView returnAgentSystemNotStartedPage() {
		return new ModelAndView(StringHolder.bootstrapFolder+"boot_agentSystemNotStarted");
	}
}
