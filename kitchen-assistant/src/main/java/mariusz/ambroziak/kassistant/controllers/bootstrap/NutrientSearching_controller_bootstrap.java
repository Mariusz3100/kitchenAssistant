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
public class NutrientSearching_controller_bootstrap {


	@RequestMapping(value="/b_nutritientForFoodName")
	public ModelAndView apiRecipeParsed(HttpServletRequest request) throws AgentSystemNotStartedException {
		
		String foodName=request.getParameter(JspStringHolder.recipeApiId);
		
		String recipeID=request.getParameter(JspStringHolder.recipeApiId);
		RecipeData singleRecipe = EdamanRecipeApiClient.getSingleRecipe(recipeID);
		int liczbaSkladnikow=singleRecipe.getIngredients().size();

		ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_correctProducts");

			mav.addObject("url",singleRecipe.getUrl());
			mav.addObject("results",null);

			return mav;
		

	}

	private ModelAndView returnAgentSystemNotStartedPage() {
		return new ModelAndView("agentSystemNotStarted");
	}
}
