package mariusz.ambroziak.kassistant.controllers.bootstrap;

import java.io.UnsupportedEncodingException;
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
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.exceptions.ShopNotFoundException;
import mariusz.ambroziak.kassistant.model.Nutrient;
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
public class ProduktParsing_controller_bootstrap extends RecipeLogic{


	@RequestMapping(value=JspStringHolder.PRODUCT_BY_NAME_SUFFIX)
	public ModelAndView apiRecipeParsed(HttpServletRequest request) throws AgentSystemNotStartedException {
		setEncoding(request);
		String phrase=request.getParameter(JspStringHolder.produktPhrase_name);


		if(phrase==null||phrase.equals(""))
			return new ModelAndView(StringHolder.bootstrapFolder+"boot_produktEngForm");
		else
		{
			List<Produkt> produkts = null;
			Map<Produkt,String> links = null;
			try {
				//				produkts = ShopComAgent.searchForIngredient(url);
				produkts = ProduktAgent.searchForProdukt(phrase);
//				links=getDetailsLinks(produkts);
			} catch (AgentSystemNotStartedException e) {
				return returnAgentSystemNotStartedPage();
			}

			ModelAndView model = new ModelAndView(StringHolder.bootstrapFolder+"boot_produktEngForm");
			model.addObject("produktList", produkts);

			return model;
		}


	}

//	private Map<Produkt, String> getDetailsLinks(List<Produkt> produkts) {
//		Map<Produkt, String> retValue=new HashMap<>();
//		if(produkts!null) {
//			for(Produkt p:produkts) {
//				String link="";
//				retValue.put(p, value)
//			}
//		}
//		return null;
//	}

	protected ModelAndView returnAgentSystemNotStartedPage() {
		return new ModelAndView(StringHolder.bootstrapFolder+"boot_agentSystemNotStarted");
	}



	@RequestMapping(value=JspStringHolder.PRODUCT_BY_URL_SUFFIX)
	public ModelAndView b_correctProducts(HttpServletRequest request) {
		setEncoding(request);
		String url=request.getParameter(JspStringHolder.produktUrl_name);
		ModelAndView mav= new ModelAndView(StringHolder.bootstrapFolder+"boot_produktEngUrlForm");

		if(url!=null&&!url.equals(""))
		{
			try {
				Produkt produkt= ProduktAgent.getOrScrapProdukt(url);
				mav.addObject("produkt", produkt);
				String fullLink=ProduktAgent.getProduktFullLink(url);
				mav.addObject("apiDetailsLink", fullLink);

			} catch (AgentSystemNotStartedException e) {
				return returnAgentSystemNotStartedPage();
			} catch (ShopNotFoundException e) {
				mav.addObject("erroneusUrl",url);
			}

		}
		return mav;
	}


	protected void setEncoding(HttpServletRequest request) {

		try {
			request.setCharacterEncoding(StringHolder.ENCODING);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}


}
