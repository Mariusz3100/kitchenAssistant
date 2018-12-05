package mariusz.ambroziak.kassistant.controllers;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import api.extractors.EdamanQExtract;
import mariusz.ambroziak.kassistant.Apiclients.edaman.EdamanRecipeApiClient;
import mariusz.ambroziak.kassistant.Apiclients.edaman.RecipeData;
import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaNutrientApiClient;
import mariusz.ambroziak.kassistant.agents.ProduktAgent;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.api.agents.EdamanRecipeAgent;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.exceptions.ShopNotFoundException;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.jsp.InvalidSearchResult;
import mariusz.ambroziak.kassistant.model.jsp.MultiProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.jsp.ProduktWithRecountedPrice;
import mariusz.ambroziak.kassistant.model.jsp.SearchResult;
import mariusz.ambroziak.kassistant.model.jsp.SingleProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.jsp.SkippedSearchResult;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.NotPreciseQuantity;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.ApiIngredientAmount;
import mariusz.ambroziak.kassistant.model.utils.BasicIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.GoodBadSkippedResults;
import mariusz.ambroziak.kassistant.utils.CompoundMapManipulator;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;

@Controller
public class RecipeController {


	@RequestMapping(value="/recipeForm")

	public ModelAndView recipeForm(HttpServletRequest request) {

		return new ModelAndView("recipeForm");

	}

	@RequestMapping(value="/recipeApiForm")
	public ModelAndView recipeApiForm(HttpServletRequest request) {

		return new ModelAndView("recipeApiForm");

	}
	
	@RequestMapping(value="/recipeUrlApiForm")
	public ModelAndView recipeUrlApiForm(HttpServletRequest request) {

		return new ModelAndView("recipeUrlApiForm");

	}
	
	@RequestMapping(value="/apirecipes")
	public ModelAndView recipeApiResult(HttpServletRequest request) throws AgentSystemNotStartedException {
		String recipeID=request.getParameter(JspStringHolder.recipeApiId);
		List<RecipeData> results;
		
		if(recipeID==null||recipeID.equals("")){
			String phrase=request.getParameter(JspStringHolder.searchPhrase_name);
			try{
				results=EdamanRecipeAgent.searchForRecipe(phrase);
			}catch (AgentSystemNotStartedException e) {
					return returnAgentSystemNotStartedPage();
			}
		}else{
			results=new ArrayList<>();
			results.add(EdamanRecipeApiClient.getSingleRecipe(recipeID));
		}
		ModelAndView modelAndView = new ModelAndView("recipesFromApiList");
		modelAndView.addObject("recipeList", results);
		
		return modelAndView;

	}
	

	@RequestMapping(value="/apiRecipeParsed")
	public ModelAndView apiRecipeParsed(HttpServletRequest request) throws AgentSystemNotStartedException {
		String recipeID=request.getParameter(JspStringHolder.recipeApiId);
		List<Produkt> results;
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
			ModelAndView mav=new ModelAndView("chooseEngProducts");

			mav.addObject("url",singleRecipe.getUrl());
			mav.addObject("results",result);

			return mav;
		}

	}

	
	@RequestMapping(value="/chooseProdukts")
	public ModelAndView chooseProdukts(HttpServletRequest request) {
		setEncoding(request);
		String url=request.getParameter(JspStringHolder.recipeUrl_name);

		if(url==null){
			return new ModelAndView("recipeForm");
		}

		int liczbaSkladnikow=
				Integer.parseInt(request.getParameter(JspStringHolder.liczbaSkladnikow));

		Map<MultiProdukt_SearchResult,PreciseQuantity> result = new HashMap<MultiProdukt_SearchResult,PreciseQuantity>();

		for(int i=0;i<liczbaSkladnikow;i++){
			String searchPhrase=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.searchPhrase_name);
			String produktPhrase=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.produktPhrase_name);
			String quantityAmount=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.quantity_amount);
			String quantityType=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.quantity_type);

			PreciseQuantity quantity=extractQuantity(quantityAmount, quantityType);
			List<Produkt> possibleProdukts=null;
			try {
				possibleProdukts=getProduktsWithRecountedPrice(RecipeAgent.parseProdukt(produktPhrase),quantity);
			} catch (AgentSystemNotStartedException e) {
				return returnAgentSystemNotStartedPage();
			}

			result.put(
					new MultiProdukt_SearchResult(searchPhrase, produktPhrase, quantity.toJspString(), possibleProdukts)
					,quantity);
		}

		if(result==null||result.isEmpty()){

			return returnAgentSystemNotStartedPage();

		}else{
			ModelAndView mav=new ModelAndView("chooseProducts");

			mav.addObject("url",url);
			mav.addObject("results",result);

			return mav;
		}


	}

	private List<Produkt> getProduktsWithRecountedPrice(List<Produkt> parseProdukt,
			PreciseQuantity neededQuantity) {
		return getProduktsWithRecountedPrice(parseProdukt,neededQuantity,"zł");

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



	private PreciseQuantity extractQuantity(String quantityAmount, String quantityType) {
		AmountTypes at=AmountTypes.valueOf(quantityType);
		try{
			float f=Float.parseFloat(quantityAmount);

			if(at!=null){
				return new PreciseQuantity(f, at);
			}

		}catch(NumberFormatException nfe){
			nfe.printStackTrace();
		}

		return null;
	}


	@RequestMapping(value="/correctQuantities")
	public ModelAndView correctQuantities(HttpServletRequest request) {
		setEncoding(request);
		String url=request.getParameter(JspStringHolder.recipeUrl_name);
		if(url==null){
			return new ModelAndView("recipeForm");
		}
		ArrayList<SearchResult> result;
		try {
			result = RecipeAgent.getPhrasesAndQuantitiesFromRecipeUrl(url);
		} catch (AgentSystemNotStartedException e) {
			return returnAgentSystemNotStartedPage();
		} catch (Page404Exception e) {
			return returnPageNotFoundRecipeForm(url);
		}
		return returnResultsProperlyParsedPage(url, result);
	}

	private ModelAndView returnAgentSystemNotStartedPage() {
		return new ModelAndView("agentSystemNotStarted");
	}

	private ModelAndView returnResultsProperlyParsedPage(String url, ArrayList<SearchResult> result) {
		ModelAndView mav=new ModelAndView("correctQuantities");

		mav.addObject(JspStringHolder.recipeUrl_name,url);
		ArrayList<PreciseQuantity> quantities=extractQuantities(result);
		mav.addObject("quantities",quantities);
		mav.addObject("results",result);
		return mav;
	}

	private ModelAndView returnPageNotFoundRecipeForm(String url) {
		ModelAndView mav=returnAgentSystemNotStartedPage();
		mav.addObject("invalidUrlInformation","Url "+url+" nie prowadzi do strony ---adnego przepisu");
		return mav;
	}



	@RequestMapping(value="/correctProdukts")
	public ModelAndView correctProdukts(HttpServletRequest request) {
		setEncoding(request);
		String url=request.getParameter(JspStringHolder.recipeUrl_name);
		GoodBadSkippedResults resultsHolder;
		try {
			resultsHolder = extractGoodBadSkippedResults(request);
		} catch (AgentSystemNotStartedException e1) {
			return returnAgentSystemNotStartedPage();
		}

		if(resultsHolder.getUsersBadChoice().isEmpty()){
			try {
				return createProperResultsNutrientData(resultsHolder);
			} catch (AgentSystemNotStartedException e) {
				return returnAgentSystemNotStartedPage();
			} 

		}else{
			return returnProduktsCorrectingPage(resultsHolder);
		}
	}

	private void setEncoding(HttpServletRequest request) {
		
		try {
			request.setCharacterEncoding(StringHolder.ENCODING);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private ModelAndView createProperResultsNutrientData(GoodBadSkippedResults resultsHolder) throws AgentSystemNotStartedException {

		
		//Nazwa produktu->[nazwa skladnika->ilosc]
		Map<String, Map<String, NotPreciseQuantity>> nutrientsMap = retrieveNutritionDetails(resultsHolder);
		
		

		CompoundMapManipulator<String, String> cmm=new CompoundMapManipulator<String, String>(nutrientsMap);
		Map<String, NotPreciseQuantity> sumOfNutrients = cmm.sumUpInnerMaps();
		List<String> nutrientsList = new ArrayList<String>(cmm.getAllInnerMapsKeys());

		ModelAndView mav=new ModelAndView("recipeNutrientData");		
		mav.addObject("nutrientsMap", nutrientsMap);//[nazwa produktu->[nazwa składnika odżywczego->ilość]]
		mav.addObject("allNutrients",nutrientsList );//[lista składników odżywczych]
		mav.addObject("sumOfNutrients", sumOfNutrients);//suma składników odżywczych (sumowana po wszystkich produktach)


		mav.addObject("ingredientsMap", nutrientsMap);
		mav.addObject("allIngredients",nutrientsList);
		mav.addObject("sumOfIngredients", sumOfNutrients);

		Map<String,Map<Nutrient, PreciseQuantity>> sumsForInfredients=new HashMap<String, Map<Nutrient,PreciseQuantity>>(); 
		Map<String, Map<Nutrient, NotPreciseQuantity>> lastMap=new HashMap<String, Map<Nutrient,NotPreciseQuantity>>();

		mav.addObject("lastMap",nutrientsMap );
		mav.addObject("allLastIngs",nutrientsList);
		mav.addObject("lastSum",  sumOfNutrients);

		
		return mav;
	}

	private Map<String, Map<String, NotPreciseQuantity>> retrieveNutritionDetails(
			GoodBadSkippedResults resultsHolder) {
		
		Map<SingleProdukt_SearchResult, Map<Nutrient, PreciseQuantity>> retrievedNutrientDataForProdukts = 
				UsdaNutrientApiClient.retrieveNutrientDataForProdukts(resultsHolder.getGoodResults());

		Map<String, Map<String, NotPreciseQuantity>> retValue = CompoundMapManipulator.stringifyKeys(retrievedNutrientDataForProdukts);
		
		return retValue;
		
	}

	private Map<String, NotPreciseQuantity> getMapOutOfSum(Collection<BasicIngredientQuantity> sumOfNutrients) {
		Map<String,NotPreciseQuantity >retValue=new HashMap<String, NotPreciseQuantity>();
		for(BasicIngredientQuantity biq:sumOfNutrients){
			retValue.put(biq.getName(), biq.getAmount());
		}
		return retValue;
	}


	
	private ModelAndView returnProduktsCorrectingPage(GoodBadSkippedResults resultsHolder) {
		ModelAndView mav=new ModelAndView("correctProducts");
		mav.addObject("badResults",resultsHolder.getUsersBadChoice());
		mav.addObject("correctResults",resultsHolder.getGoodResults());
		mav.addObject("skippedResults",resultsHolder.getSkippedResults());
		return mav;
	}

	private void moveNotFoundProduktFromGoodToBadChoices(String produktUrl, GoodBadSkippedResults resultsToBeUpdated) throws AgentSystemNotStartedException {
		//TODO correct
		
		//		String shortUrl=AuchanAbstractScrapper.getAuchanShortestWorkingUrl(produktUrl);
//		Iterator<SingleProdukt_SearchResult> goodProduktsIterator = resultsToBeUpdated.getGoodResults().iterator();
//		while(goodProduktsIterator.hasNext()){
//			SingleProdukt_SearchResult produktToBeChecked = goodProduktsIterator.next();
//
//			if(shortUrl.equals(produktToBeChecked.getProdukt().getUrl())){
//				goodProduktsIterator.remove();
//
//				InvalidSearchResult goodResultConverted=convertGoodResultToBadOne(produktToBeChecked);
//
//				resultsToBeUpdated.getUsersBadChoice().add(goodResultConverted);
//			}
//		}

	}

	private InvalidSearchResult convertGoodResultToBadOne(SingleProdukt_SearchResult produktToBeConverted) throws AgentSystemNotStartedException {
		List<Produkt> searchResults= ProduktAgent.searchForProdukt(produktToBeConverted.getProduktPhrase());
		InvalidSearchResult retValue=new InvalidSearchResult(produktToBeConverted.getSearchPhraseAnswered().getSearchPhrase(),
				produktToBeConverted.getProduktPhrase(), 
				produktToBeConverted.getQuantity(), 
				searchResults, 
				generateSorryProduktNotFoundInvalidityReason(produktToBeConverted));

		return retValue;
	}



	private String generateSorryProduktNotFoundInvalidityReason(SingleProdukt_SearchResult produktToBeChecked) {
		return "We are really sorry, it seems url "+produktToBeChecked.getProdukt().getUrl()+" points to a produkt in our database, that is no longer avaible at the shop. Please, choose something else.";
	}



	private GoodBadSkippedResults extractGoodBadSkippedResults(HttpServletRequest request) throws AgentSystemNotStartedException {
		GoodBadSkippedResults resultsHolder=new GoodBadSkippedResults();
		int liczbaSkladnikow=
				Integer.parseInt(request.getParameter(JspStringHolder.liczbaSkladnikow));

		for(int i=1;i<=liczbaSkladnikow;i++){
			String searchPhrase=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.searchPhrase_name);
			String produktPhrase=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.produktPhrase_name);
			String quantityPhrase=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.quantity_name);

			String wybranyProdukt=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.skladnikRadio_name);
			Produkt produkt=null;
			if(JspStringHolder.innaOpcja_name.equals(wybranyProdukt)){
				String innyUrl=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.innyUrl_name);
				try {
					produkt=ProduktAgent.getOrScrapProdukt(innyUrl);
					if(produkt!=null){
						ArrayList<Produkt> produkts=new ArrayList<Produkt>();
						produkts.add(produkt);
						SingleProdukt_SearchResult sr=new SingleProdukt_SearchResult(searchPhrase,produktPhrase, quantityPhrase, produkt);
						resultsHolder.addGoodResult(sr);
					}else{
						//znowu pobieramy produkty
						List<Produkt> possibleProdukts = ProduktAgent.searchForProdukt(produktPhrase);
						InvalidSearchResult isr=new InvalidSearchResult(searchPhrase,produktPhrase,quantityPhrase,possibleProdukts,
								"Wygl---da na to, ---e strona internetowa pod url \""+innyUrl+"\" nie istnieje, lub nie opisuje ---adnego produktu");
						resultsHolder.addUsersBadChoice(isr);
					}
				} catch (ShopNotFoundException e) {
					//invalidShopUrls.put(produktPhrase,innyUrl);
					List<Produkt> searchResults;
					searchResults = ProduktAgent.searchForProdukt(produktPhrase);
					InvalidSearchResult isr=new InvalidSearchResult(searchPhrase,produktPhrase,quantityPhrase,searchResults,
							"Wygl---da na to, ---e url \""+innyUrl
							+"\" nie zosta--- rozpoznany jako pasuj---cy do ---adnego ze wspieranych sklep---w.");
					resultsHolder.addUsersBadChoice(isr);

				} 

			}else if(JspStringHolder.pominOpcja_name.equals(wybranyProdukt)){
				resultsHolder.addSkippedResults(new SkippedSearchResult(searchPhrase));

			}else if(wybranyProdukt!=null&&wybranyProdukt.startsWith(JspStringHolder.radioValuePrefix)){
				if(wybranyProdukt!=null&&wybranyProdukt.length()>0)
					wybranyProdukt=wybranyProdukt.replaceFirst(Pattern.quote(JspStringHolder.radioValuePrefix), "");

				produkt=DaoProvider.getInstance().getProduktDao().getProduktsByURL(wybranyProdukt);

				if(produkt==null){
					ProblemLogger.logProblem(
							"Wygl---da na to, ---e produkt wybrany przez przycisk radio nie istnieje w systemie!: "
									+wybranyProdukt);
					List<Produkt> searchResults;

					searchResults = ProduktAgent.searchForProdukt(produktPhrase);

					InvalidSearchResult isr=new InvalidSearchResult(searchPhrase,produktPhrase,quantityPhrase,searchResults,
							"UPS! Wydaje si---, ---e zaproponowany przez nas produkt nie wyst---puje w naszym systemie!! Powiadom o tym administratora albo co---...");
					resultsHolder.addUsersBadChoice(isr);

				}else{
					SingleProdukt_SearchResult sr=new SingleProdukt_SearchResult(searchPhrase,produktPhrase, quantityPhrase, produkt);
					resultsHolder.addGoodResult(sr);
				}
			}

		}
		return resultsHolder;
	}



	private ArrayList<PreciseQuantity> extractQuantities(ArrayList<SearchResult> result) {
		ArrayList<PreciseQuantity> retValue=new ArrayList<PreciseQuantity>();

		for(int i=0;i<result.size();i++){
			PreciseQuantity pq=new PreciseQuantity(-1, AmountTypes.szt);
			String quanPhrase=result.get(i).getQuantity();
			if(quanPhrase==null||quanPhrase.equals("")){
				ProblemLogger.logProblem("Empty amount from hidden field");
			}else{
				String[] elems=quanPhrase.split(JspStringHolder.QUANTITY_PHRASE_BORDER);
				if(elems.length<2){
					ProblemLogger.logProblem("Empty or too short quantity from hidden field: "+quanPhrase);
				}else{
					float amount=Float.parseFloat(elems[1]);
					AmountTypes at=AmountTypes.retrieveTypeByName(elems[0]);
					pq=new PreciseQuantity(amount,at);
				}
			}

			retValue.add(i,pq);
		}


		return retValue;
	}

}
