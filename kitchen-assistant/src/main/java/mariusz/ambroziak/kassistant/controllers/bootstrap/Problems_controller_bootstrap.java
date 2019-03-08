package mariusz.ambroziak.kassistant.controllers.bootstrap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import api.extractors.EdamanQExtract;
import mariusz.ambroziak.kassistant.Apiclients.edaman.EdamanRecipeApiClient;
import mariusz.ambroziak.kassistant.Apiclients.edaman.ParseableRecipeData;
import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaNutrientApiClient;
import mariusz.ambroziak.kassistant.agents.EdamanRecipeAgent;
import mariusz.ambroziak.kassistant.agents.ProduktAgent;
import mariusz.ambroziak.kassistant.agents.ReadingNutritientsUsdaAgent;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.controllers.logic.RecipeLogic;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.exceptions.ShopNotFoundException;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.Problem;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.jsp.MultiProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.jsp.ProduktWithRecountedPrice;
import mariusz.ambroziak.kassistant.model.jsp.SingleProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.quantity.NotPreciseQuantity;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.ApiIngredientAmount;
import mariusz.ambroziak.kassistant.model.utils.GoodBadSkippedResults;
import mariusz.ambroziak.kassistant.utils.CompoundMapManipulator;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;


@Controller
public class Problems_controller_bootstrap extends RecipeLogic{


	@RequestMapping(value="/b_problems")
	public ModelAndView getAllProblems(HttpServletRequest request) throws AgentSystemNotStartedException {
		
		Map<Long, String> problems = DaoProvider.getInstance().getProblemDao().listWholeMessages();
		
		ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_problemsList");
		mav.addObject("problemsMap", problems);
		return mav;

	}


	@RequestMapping(value="/b_problem")
	public ModelAndView getAProblem(HttpServletRequest request) throws AgentSystemNotStartedException {
		String problemId=request.getParameter(JspStringHolder.problemId_name);
		ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_singleProblem");
		
		
		if(problemId!=null) {
			long parsedProblemId = Long.parseLong(problemId);
			
			
			List<Problem> byId = DaoProvider.getInstance().getProblemDao().getWholeMessage(parsedProblemId);
			String message="";
			boolean solved=true;
			String idsList="";
			if(byId==null||byId.isEmpty()) {
				message="Problem with such id was not found";
			}else {
				for(Problem p:byId) {
					message+=p.getMessage();
					solved&=p.isSolved();
					idsList+=p.getP_id()+", ";
				}
			}
			mav.addObject("message", message);
			mav.addObject("solved", solved);
			mav.addObject("idsList", idsList);
		}
		
		return mav;

	}

	


}
