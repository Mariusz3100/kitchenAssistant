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
public class RecipeParsing_controller_bootstrap {


	@RequestMapping(value="/b_apiRecipeParsed")
	public ModelAndView apiRecipeParsed(HttpServletRequest request) throws AgentSystemNotStartedException {
		String recipeID=request.getParameter(JspStringHolder.recipeApiId);
		RecipeData singleRecipe = EdamanRecipeApiClient.getSingleRecipe(recipeID);
		int liczbaSkladnikow=singleRecipe.getIngredients().size();

		Map<MultiProdukt_SearchResult,PreciseQuantity> result = new HashMap<MultiProdukt_SearchResult,PreciseQuantity>();

		for(int i=0;i<liczbaSkladnikow;i++){
			ApiIngredientAmount aia=singleRecipe.getIngredients().get(i);
			PreciseQuantity quantity=aia.getAmount();
			String produktPhrase=EdamanQExtract.correctText(aia.getName()); //the same for precise quantity
			
			List<Produkt> possibleProdukts=null;
			try {
				possibleProdukts=getProduktsWithRecountedPrice(RecipeAgent.parseProdukt(produktPhrase),quantity,"$");
			} catch (AgentSystemNotStartedException e) {
				return returnAgentSystemNotStartedPage();
			}

			result.put(
					new MultiProdukt_SearchResult(aia.getName(), produktPhrase, quantity.toJspString(), possibleProdukts)
					,quantity);
		}

		if(result==null||result.isEmpty()){

			return returnAgentSystemNotStartedPage();

		}else{
			ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_correctProducts");

			mav.addObject("url",singleRecipe.getUrl());
			mav.addObject("results",result);

			return mav;
		}

	}


	private List<Produkt> getProduktsWithRecountedPrice(List<Produkt> parseProdukt,
			PreciseQuantity neededQuantity, String curency) {

		List<Produkt> retValue=new ArrayList<Produkt>();
		if(parseProdukt!=null){
			for(Produkt p:parseProdukt){
				ProduktWithRecountedPrice pRec=getProduktWithRecountedPrice(p,neededQuantity,curency);
				retValue.add(pRec);
			}
		}

		return retValue;
	}
	
	private ProduktWithRecountedPrice getProduktWithRecountedPrice(Produkt p, PreciseQuantity neededQuantity, String curency) {
		String recountedPrice="";
		PreciseQuantity produktQuan=PreciseQuantity.parseFromJspString(p.getQuantityPhrase());
		if(produktQuan.getType()!=neededQuantity.getType()){
			ProblemLogger.logProblem("Wielkości skladnika w przepisie+"+neededQuantity+" i w sklepie "+produktQuan+"nie są tego samego typu");
		}else{
			if(produktQuan.getAmount()>=neededQuantity.getAmount()){
				recountedPrice=produktQuan+"->"+p.getCena()+" "+curency;
			}else{
				int multiplier = neededQuantity.getMultiplierOfProduktQuantityForNeededQuantity( produktQuan);
				recountedPrice=produktQuan+" x "+multiplier+" -> "
						+p.getCena()+" x "+multiplier+" "+curency+"="+p.getCena()*multiplier+" "+curency;
			}
		}


		ProduktWithRecountedPrice retValue=new ProduktWithRecountedPrice(p, recountedPrice);
		return retValue;
	}


	private ModelAndView returnAgentSystemNotStartedPage() {
		return new ModelAndView("agentSystemNotStarted");
	}
}
