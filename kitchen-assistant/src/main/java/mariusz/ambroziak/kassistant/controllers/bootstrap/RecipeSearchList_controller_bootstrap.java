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
import mariusz.ambroziak.kassistant.Apiclients.edaman.RecipeData;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.api.agents.EdamanRecipeAgent;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.jsp.MultiProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.jsp.ProduktWithRecountedPrice;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.ApiIngredientAmount;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;


@Controller
public class RecipeSearchList_controller_bootstrap {

	
	@RequestMapping(value="/b_engRecipeForm")
	public ModelAndView b_engRecipeForm() {
		ModelAndView model = new ModelAndView(StringHolder.bootstrapFolder+"boot_recipeEngForm");
		
		
		
		return model;
	}


	@RequestMapping(value="/b_apirecipes")
	public ModelAndView b_apirecipes(HttpServletRequest request) {
		String searchPhrase=request.getParameter(JspStringHolder.recipeSearchPhrase_name);
		List<RecipeData> results=EdamanRecipeApiClient.getRecipesByPhrase(searchPhrase);
		ModelAndView modelAndView = new ModelAndView(StringHolder.bootstrapFolder+"boot_RecipeList");
		modelAndView.addObject("recipeList", results);
		return modelAndView;
	}

	

	@RequestMapping(value="/b_engRecipeUrlForm")
	public ModelAndView b_engRecipeUrlForm(HttpServletRequest request) {
		ModelAndView model = new ModelAndView(StringHolder.bootstrapFolder+"boot_recipeEngUrlForm");

		
		
		return model;
	}



	private ModelAndView returnAgentSystemNotStartedPage() {
		return new ModelAndView("agentSystemNotStarted");
	}
}
