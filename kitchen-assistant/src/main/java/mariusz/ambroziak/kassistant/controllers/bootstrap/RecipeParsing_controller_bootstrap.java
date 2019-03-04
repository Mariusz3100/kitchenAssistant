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
	
	
	
	@RequestMapping(value="/b_productsChosen")
	public ModelAndView b_correctProducts(HttpServletRequest request) {
		setEncoding(request);
		String url=request.getParameter(JspStringHolder.recipeUrl_name);


		GoodBadSkippedResults extractGoodBadSkippedResults;
		try {
			extractGoodBadSkippedResults = extractGoodBadSkippedResults(request);
			if(extractGoodBadSkippedResults.getUsersBadChoice()==null
					||extractGoodBadSkippedResults.getUsersBadChoice().isEmpty()) 
			{
				Map<SingleProdukt_SearchResult, Map<Nutrient, PreciseQuantity>> parseNutrientDataOfIngredients = parseNutrientDataOfIngredients(extractGoodBadSkippedResults.getGoodResults());
				
				//parseNutrientDataOfIngredients.values().iterator().next()
				
				ModelAndView nutrientsMav = getNutrientsMav(parseNutrientDataOfIngredients);
				//working
				return nutrientsMav;

			}else {
					ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_correctProducts");
					mav.addObject("badResults",extractGoodBadSkippedResults.getUsersBadChoice());
					mav.addObject("correctResults", extractGoodBadSkippedResults.getGoodResults());
					mav.addObject("skippedResults",extractGoodBadSkippedResults.getSkippedResults());
					return mav;
				
			}
		} catch (AgentSystemNotStartedException e) {
			returnAgentSystemNotStartedPage();
		}
		return null;
		

		
	}

	private ModelAndView getNutrientsMav(
			Map<SingleProdukt_SearchResult, Map<Nutrient, PreciseQuantity>> parseNutrientDataOfIngredients) {
				


		Map<String, Map<String, NotPreciseQuantity>> nutrientMap = CompoundMapManipulator.stringifyKeys(parseNutrientDataOfIngredients);
		

		CompoundMapManipulator<String, String> cmm=new CompoundMapManipulator<String, String>(nutrientMap);
		Map<String, NotPreciseQuantity> sumOfNutrients = cmm.sumUpInnerMaps();
		List<String> nutrientsList = new ArrayList<String>(cmm.getAllInnerMapsKeys());


		ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_NutrientResultsForRecipe");		
		mav.addObject("nutrientsMap", nutrientMap);//[nazwa produktu->[nazwa składnika odżywczego->ilość]]
		mav.addObject("allNutrients",nutrientsList );//[lista składników odżywczych]
		mav.addObject("sumOfNutrients", sumOfNutrients);//suma składników odżywczych (sumowana po wszystkich produktach)


		return mav;
	}





	private Map<SingleProdukt_SearchResult, Map<Nutrient, PreciseQuantity>> parseNutrientDataOfIngredients(ArrayList<SingleProdukt_SearchResult> goodResults) throws AgentSystemNotStartedException {
		
		Map<SingleProdukt_SearchResult, Map<Nutrient, PreciseQuantity>> listOfProductNutrients = ReadingNutritientsUsdaAgent.searchForListOfProductAndRecountAmounts(goodResults);
		return listOfProductNutrients;
	}





	@Override
	protected Produkt retieveProdukts(String innyUrl, PreciseQuantity neededQuantity, String curency) throws ShopNotFoundException, AgentSystemNotStartedException {
		Produkt orScrapProdukt = ProduktAgent.getOrScrapProdukt(innyUrl);
		return retrieveProdukt(neededQuantity, curency, orScrapProdukt);
	}




	@Override
	protected Produkt retrieveProdukt(PreciseQuantity neededQuantity, String curency, Produkt orScrapProdukt) {
		if(orScrapProdukt==null||neededQuantity==null)
			return null;
		String recountedPrice="";
		PreciseQuantity produktQuan=PreciseQuantity.parseFromJspString(orScrapProdukt.getQuantityPhrase());
		if(produktQuan.getType()!=neededQuantity.getType()){
			ProblemLogger.logProblem("Wielkości skladnika w przepisie+"+neededQuantity+" i w sklepie "+produktQuan+"nie są tego samego typu");
		}else{
			if(produktQuan.getAmount()>=neededQuantity.getAmount()){
				recountedPrice=produktQuan+"->"+orScrapProdukt.getCena()+" "+curency;
			}else{
				int multiplier = neededQuantity.getMultiplierOfProduktQuantityForNeededQuantity( produktQuan);
				recountedPrice=produktQuan+" x "+multiplier+" -> "
						+orScrapProdukt.getCena()+" x "+multiplier+" "+curency+"="+orScrapProdukt.getCena()*multiplier+" "+curency;
			}
		}
		
		ProduktWithRecountedPrice recounted=new ProduktWithRecountedPrice(orScrapProdukt, recountedPrice);
		return recounted;
	}

	@Override
	protected List<Produkt> recountPrices(List<Produkt> searchResults, PreciseQuantity preciseQuantity, String currency) {
		if(searchResults==null) {
			return null;
		}else {
			List<Produkt> retValue=new ArrayList<>();
			
			for(Produkt p:searchResults) {
				retValue.add(retrieveProdukt(preciseQuantity,currency,p));
			}
			return  retValue;
					
		}
	}

//	private void calculateRecountedPrices(GoodBadSkippedResults extractGoodBadSkippedResults) {
//		if(extractGoodBadSkippedResults!=null) {
//			ArrayList<SingleProdukt_SearchResult> goodResults = extractGoodBadSkippedResults.getGoodResults();
//			if(goodResults!=null)
//		
//		
//		}
//		
//	}
	


}
