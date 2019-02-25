package mariusz.ambroziak.kassistant.controllers.bootstrap;

import java.io.UnsupportedEncodingException;
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
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;


@Controller
public class RecipeParsedDetails_controller_bootstrap extends RecipeLogic{


	@RequestMapping(value="/b_recipeNutrients")
	public ModelAndView apiRecipeParsed(HttpServletRequest request) {
		setEncoding(request);
		String url=request.getParameter(JspStringHolder.recipeUrl_name);

		if(url==null){
			return new ModelAndView("recipeForm");
		}

//		
//		int liczbaSkladnikow=
//				Integer.parseInt(request.getParameter(JspStringHolder.liczbaSkladnikow));
//
//		Map<MultiProdukt_SearchResult,PreciseQuantity> result = new HashMap<MultiProdukt_SearchResult,PreciseQuantity>();
//
//		for(int i=0;i<liczbaSkladnikow;i++){
//			String searchPhrase=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.searchPhrase_name);
//			String produktPhrase=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.produktPhrase_name);
//			String quantityAmount=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.quantity_amount);
//			String quantityType=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.quantity_type);
//
//			PreciseQuantity quantity=extractQuantity(quantityAmount, quantityType);
//			List<Produkt> possibleProdukts=null;
//			try {
//				possibleProdukts=getProduktsWithRecountedPrice(RecipeAgent.parseProdukt(produktPhrase),quantity,"$");
//			} catch (AgentSystemNotStartedException e) {
//				return returnAgentSystemNotStartedPage();
//			}
//
//			result.put(
//					new MultiProdukt_SearchResult(searchPhrase, produktPhrase, quantity.toJspString(), possibleProdukts)
//					,quantity);
//		}
//
//		if(result==null||result.isEmpty()){
//
//			return returnAgentSystemNotStartedPage();
//
//		}else{
//			ModelAndView mav=new ModelAndView("chooseProducts");
//
//			mav.addObject("url",url);
//			mav.addObject("results",result);
//
//			return mav;
//		}
		
		ModelAndView mav=new ModelAndView("boot_NutrientResultsForRecipe");
		
		
		
		return mav;
		
		
	}
	protected ModelAndView returnAgentSystemNotStartedPage() {
		return new ModelAndView(StringHolder.bootstrapFolder+"boot_agentSystemNotStarted");
	}

}
