package mariusz.ambroziak.kassistant.controllers.bootstrap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaFoodDetails;
import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaNutrientApiClientParticularFood;
import mariusz.ambroziak.kassistant.agents.ReadingNutritientsUsdaAgent;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.StringHolder;


@Controller
public class NutrientParsing_controller_bootstrap {


	@RequestMapping(value="/b_nutritientForNdbno")
	public ModelAndView apiRecipeParsed(HttpServletRequest request) {
		String ndbno=request.getParameter(JspStringHolder.ndbno);
		//04542

		if(ndbno==null||ndbno.equals(""))
		{
			return new ModelAndView(StringHolder.bootstrapFolder+"boot_foodSearch_byDbno");
		}
		else 
		{
			UsdaFoodDetails nutrientDetailsForDbno;
			try {
				nutrientDetailsForDbno = ReadingNutritientsUsdaAgent.retrieveSingleProduct(ndbno);
			} catch (AgentSystemNotStartedException e) {
					return returnAgentSystemNotStartedPage();
			}
					
					UsdaNutrientApiClientParticularFood.getNutrientDetailObjectForDbno(ndbno);
			ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_NutrientResultsForSingleFood");
			
			mav.addObject("productIdObject",nutrientDetailsForDbno.getId());
			mav.addObject("nutrients",nutrientDetailsForDbno.getNutrietsMap());

			return mav;

		}





	}
	protected ModelAndView returnAgentSystemNotStartedPage() {
		return new ModelAndView(StringHolder.bootstrapFolder+"boot_agentSystemNotStarted");
	}
}
