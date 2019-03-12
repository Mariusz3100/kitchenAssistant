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
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.exceptions.ShopNotFoundException;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.jsp.MultiProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.jsp.PhraseWithMultiplier;
import mariusz.ambroziak.kassistant.model.jsp.ProduktWithRecountedPrice;
import mariusz.ambroziak.kassistant.model.jsp.SingleProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
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
		ParseableRecipeData recipeHeader=null;
		try {
			result= EdamanRecipeAgent.parseSingleRecipe(recipeID);
			recipeHeader = EdamanRecipeAgent.retrieveSingleRecipeHeaderById(recipeID);
		} catch (AgentSystemNotStartedException e) {
			return returnAgentSystemNotStartedPage();
		} catch (Page404Exception e) {
			ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_recipeEngUrlForm");
			mav.addObject("erroneusUrl",recipeID);
			return mav;
		}
		ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_chooseProducts");

		if(recipeHeader!=null&&recipeHeader.getLabel()!=null)
			mav.addObject("recipeName",recipeHeader.getLabel());

		mav.addObject("recipeUrl",recipeID);
		mav.addObject("results",result);

		return mav;
	}
	
	protected ModelAndView returnAgentSystemNotStartedPage() {
		return new ModelAndView(StringHolder.bootstrapFolder+"boot_agentSystemNotStarted");
	}
	
	@RequestMapping(value="/b_productsChosen")
	public ModelAndView b_correctProducts(HttpServletRequest request) {
		setEncoding(request);
		String url=request.getParameter(JspStringHolder.recipeUrl_name);
		String recipeName=request.getParameter(JspStringHolder.recipeName_name);
		

		GoodBadSkippedResults extractGoodBadSkippedResults;
		try {
			extractGoodBadSkippedResults = extractGoodBadSkippedResults(request);
			if(extractGoodBadSkippedResults.getUsersBadChoice()==null
					||extractGoodBadSkippedResults.getUsersBadChoice().isEmpty()) 
			{
				Map<SingleProdukt_SearchResult, Map<Nutrient, PreciseQuantity>> parseNutrientDataOfIngredients = parseNutrientDataOfIngredients(extractGoodBadSkippedResults.getGoodResults());
				
				//parseNutrientDataOfIngredients.kySet().iterator().next()
				
				ModelAndView nutrientsMav = getNutrientsMav(parseNutrientDataOfIngredients);
				nutrientsMav.addObject(JspStringHolder.recipeUrl_name, url);
				nutrientsMav.addObject("recipeName",recipeName);

				
				//working
				return nutrientsMav;

			}else {
					ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_correctProducts");
					mav.addObject("badResults",extractGoodBadSkippedResults.getUsersBadChoice());
					mav.addObject("correctResults", extractGoodBadSkippedResults.getGoodResults());
					mav.addObject("skippedResults",extractGoodBadSkippedResults.getSkippedResults());
					mav.addObject("recipeName",recipeName);
					return mav;
				
			}
		} catch (AgentSystemNotStartedException e) {
			return returnAgentSystemNotStartedPage();
		}


		
	}

	private ModelAndView getNutrientsMav(
			Map<SingleProdukt_SearchResult, Map<Nutrient, PreciseQuantity>> parseNutrientDataOfIngredients) {
				


		Map<PhraseWithMultiplier, Map<String, NotPreciseQuantity>> nutrientMap = CompoundMapManipulator.stringifyKeys(parseNutrientDataOfIngredients);
		

		CompoundMapManipulator<PhraseWithMultiplier, String> cmm=new CompoundMapManipulator<PhraseWithMultiplier, String>(nutrientMap);
		Map<String, NotPreciseQuantity> sumOfNutrients = cmm.sumUpInnerMaps();
		List<String> nutrientsList = new ArrayList<String>(cmm.getAllInnerMapsKeys());

		Map<String,String> produktParsingLinks=createLinksToParseProdukts(parseNutrientDataOfIngredients.keySet());
//		nutrientMap.keySet().iterator().next().get("potassium").setType(AmountTypes.ml);
		ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_NutrientResultsForRecipe");		
		mav.addObject("nutrientsMap", nutrientMap);//[nazwa produktu->[nazwa składnika odżywczego->ilość]]
		mav.addObject("allNutrients",nutrientsList );//[lista składników odżywczych]
		mav.addObject("sumOfNutrients", sumOfNutrients);//suma składników odżywczych (sumowana po wszystkich produktach)
		mav.addObject("produktLinksMap", produktParsingLinks);

		return mav;
	}





	private Map<String, String> createLinksToParseProdukts(Set<SingleProdukt_SearchResult> keySet) {
		Map<String, String> retMap=new HashMap<>();
		for(SingleProdukt_SearchResult sp_sr:keySet) {
//			String produktParsingUrl=
			retMap.put(sp_sr.getProdukt().getNazwa(), sp_sr.getProdukt().getUrl());
		}
		return retMap;
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
		if(produktQuan==null||!produktQuan.isValid()) {
			if(orScrapProdukt.getP_id()!=null) {
				ProblemLogger.logProblem("Amount for product with id: "+orScrapProdukt.getP_id()+" is empty");
			}else {
				ProblemLogger.logProblem("Amount for product with no id and url: "+orScrapProdukt.getUrl()+" is empty");
			}
		}else if(produktQuan.getType()!=neededQuantity.getType()){
			ProblemLogger.logProblem("Amount of ingredient "+neededQuantity+" in the recipe is different from amount in the shop: "+produktQuan);
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
