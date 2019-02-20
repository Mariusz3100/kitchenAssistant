package mariusz.ambroziak.kassistant.controllers.bootstrap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import api.extractors.EdamanQExtract;
import mariusz.ambroziak.kassistant.Apiclients.edaman.EdamanRecipeApiClient;
import mariusz.ambroziak.kassistant.Apiclients.edaman.ParseableRecipeData;
import mariusz.ambroziak.kassistant.agents.EdamanRecipeAgent;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.controllers.logic.RecipeLogic;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.jsp.MultiProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.jsp.ProduktWithRecountedPrice;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.ApiIngredientAmount;
import mariusz.ambroziak.kassistant.model.utils.GoodBadSkippedResults;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;


@Controller
public class RecipeParsing_controller_bootstrap extends RecipeLogic{


	@RequestMapping(value="/b_apiRecipeParsed")
	public ModelAndView apiRecipeParsed(HttpServletRequest request) throws AgentSystemNotStartedException {
		return parseRecipe(request);

	}





	private ModelAndView parseRecipe(HttpServletRequest request) {
		String recipeID=request.getParameter(JspStringHolder.recipeApiId);
		if(recipeID==null||recipeID.equals("")) {
			ModelAndView model = new ModelAndView(StringHolder.bootstrapFolder+"boot_recipeEngUrlForm");
			return model;
		}
		
		Map<MultiProdukt_SearchResult,PreciseQuantity> result=new HashMap<>(); 
		ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_chooseProducts");

		try {
			result= EdamanRecipeAgent.parseSingleRecipe(recipeID);
		} catch (AgentSystemNotStartedException e) {
			return returnAgentSystemNotStartedPage();
		} catch (Page404Exception e) {
			mav.addObject("erroneusUrl",recipeID);
		}
		mav.addObject("url",recipeID);
		mav.addObject("results",result);

		return mav;
	}
	
	
	
	@RequestMapping(value="/b_correctProducts")
	public ModelAndView b_correctProducts(HttpServletRequest request) {
		setEncoding(request);
		String url=request.getParameter(JspStringHolder.recipeUrl_name);


		GoodBadSkippedResults extractGoodBadSkippedResults;
		try {
			extractGoodBadSkippedResults = extractGoodBadSkippedResults(request);
			if(extractGoodBadSkippedResults.getUsersBadChoice()==null
					||extractGoodBadSkippedResults.getUsersBadChoice().isEmpty()) 
			{
				//working
				return new ModelAndView("List");

			}else {
					ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_correctProducts");
					mav.addObject("badResults",extractGoodBadSkippedResults.getUsersBadChoice());
					mav.addObject("correctResults",extractGoodBadSkippedResults.getGoodResults());
					mav.addObject("skippedResults",extractGoodBadSkippedResults.getSkippedResults());
					return mav;
				
			}
		} catch (AgentSystemNotStartedException e) {
			returnAgentSystemNotStartedPage();
		}
		return null;
		

		
	}
	


}
