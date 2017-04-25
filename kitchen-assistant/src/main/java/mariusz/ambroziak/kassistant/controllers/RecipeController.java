package mariusz.ambroziak.kassistant.controllers;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mariusz.ambroziak.kassistant.agents.FoodIngredientAgent;
import mariusz.ambroziak.kassistant.agents.ProduktAgent;
import mariusz.ambroziak.kassistant.agents.ReadingAgent;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.IncopatibleAmountTypesException;
import mariusz.ambroziak.kassistant.exceptions.InvalidArgumentException;
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
import mariusz.ambroziak.kassistant.model.utils.AbstractQuantity;
import mariusz.ambroziak.kassistant.model.utils.BasicIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.GoodBadSkippedResults;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithAllIngredients;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithBasicIngredients;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.CompoundMapManipulator;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;
import webscrappers.auchan.AuchanAbstractScrapper;
import webscrappers.auchan.AuchanParticular;

@Controller
public class RecipeController {


	@RequestMapping(value="/recipeForm")

	public ModelAndView recipeForm(HttpServletRequest request) {

		return new ModelAndView("recipeForm");

	}


	@RequestMapping(value="/chooseProdukts")
	public ModelAndView chooseProdukts(HttpServletRequest request) {
		setEncoding(request);
		String url=request.getParameter("recipeurl");

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

		List<Produkt> retValue=new ArrayList<Produkt>();
		if(parseProdukt!=null){
			for(Produkt p:parseProdukt){
				ProduktWithRecountedPrice pRec=getProduktWithRecountedPrice(p,neededQuantity);
				retValue.add(pRec);
			}
		}

		return retValue;
	}

	private ProduktWithRecountedPrice getProduktWithRecountedPrice(Produkt p, PreciseQuantity neededQuantity) {
		String recountedPrice="";
		PreciseQuantity produktQuan=PreciseQuantity.parseFromJspString(p.getQuantityPhrase());
		if(produktQuan.getType()!=neededQuantity.getType()){
			ProblemLogger.logProblem("Wielkoœci skladnika w przepisie+"+neededQuantity+" i w sklepie "+produktQuan+"nie s¹ tego samego typu");
		}else{
			if(produktQuan.getAmount()>=neededQuantity.getAmount()){
				recountedPrice=produktQuan+"->"+p.getCena()+" z³";
			}else{
				int multiplier = getMultiplierOfProduktQuantityForNeededQuantity(neededQuantity, produktQuan);
				recountedPrice=produktQuan+" x "+multiplier+" -> "
						+p.getCena()+" x "+multiplier+" z³="+p.getCena()*multiplier+" z³";
			}
		}


		ProduktWithRecountedPrice retValue=new ProduktWithRecountedPrice(p, recountedPrice);
		return retValue;
	}

	private int getMultiplierOfProduktQuantityForNeededQuantity(NotPreciseQuantity neededQuantity,
			PreciseQuantity produktQuan) {
		int multiplier=1;

		if(!(neededQuantity instanceof PreciseQuantity)){
			ProblemLogger.logProblem("Calculating coefficient for NotPreciseQuantity");
		}
		//raczej zawsze bêdzie PreciseQuantity dwa razy, ale zawsze lepiej wzi¹æ pod uwagê max
		while(produktQuan.getAmount()*multiplier<neededQuantity.getMaximalAmount()){
			++multiplier;
		}
		return multiplier;
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
		mav.addObject("invalidUrlInformation","Url "+url+" nie prowadzi do strony ¿adnego przepisu");
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
		Map<SingleProdukt_SearchResult, ProduktWithBasicIngredients> retrievedBasicNutrientValues = null;
		try {
			retrievedBasicNutrientValues = scrapNutrientValuesDataHandleUnlikelyExceptions(resultsHolder);
		} catch (AgentSystemNotStartedException e) {
			returnAgentSystemNotStartedPage();
		} catch (Page404Exception e) {
			try {
				moveNotFoundProduktFromGoodToBadChoices(e.getUrl(),resultsHolder);
			} catch (AgentSystemNotStartedException e1) {
				returnAgentSystemNotStartedPage();
			}
			return returnProduktsCorrectingPage(resultsHolder);
		}

		//Nazwa produktu->[nazwa sk³adnika->iloœæ]
		Map<String, Map<String, NotPreciseQuantity>> nutrientsMap = getProduktToSkladnikToAmountMap(retrievedBasicNutrientValues);

		CompoundMapManipulator<String, String> cmm=new CompoundMapManipulator<String, String>(nutrientsMap);
		Map<String, NotPreciseQuantity> sumOfNutrients = cmm.sumUpInnerMaps();
		List<String> nutrientsList = new ArrayList<String>(cmm.getAllInnerMapsKeys());

		ModelAndView mav=new ModelAndView("recipeNutrientData");		
		mav.addObject("nutrientsMap", nutrientsMap);
		mav.addObject("allNutrients",nutrientsList );
		mav.addObject("sumOfNutrients", sumOfNutrients);



		Map<SingleProdukt_SearchResult, ProduktWithAllIngredients> retrievedAllNutrientValues = null;
		try {
			retrievedAllNutrientValues = scrapAllNutrientsDataHandleUnlikelyExceptions(resultsHolder);
		} catch (AgentSystemNotStartedException e) {
			returnAgentSystemNotStartedPage();
		} catch (Page404Exception e) {
			try {
				moveNotFoundProduktFromGoodToBadChoices(e.getUrl(),resultsHolder);
			} catch (AgentSystemNotStartedException e1) {
				returnAgentSystemNotStartedPage();
			}
			return returnProduktsCorrectingPage(resultsHolder);

		}


		Map<String, Map<String, NotPreciseQuantity>> simplerAllMap = getSimplerAllMap(retrievedAllNutrientValues);
		CompoundMapManipulator<String, String> cmm2=new CompoundMapManipulator<String, String>(simplerAllMap);

		Map<String, NotPreciseQuantity> sumsForEachNutrient = cmm2.sumUpInnerMaps();
		List<String> allNutrientsList = new ArrayList<String>(cmm2.getAllInnerMapsKeys());


		mav.addObject("ingredientsMap", simplerAllMap);
		mav.addObject("allIngredients",allNutrientsList);
		mav.addObject("sumOfIngredients", sumsForEachNutrient);

		Map<String,Map<Nutrient, PreciseQuantity>> sumsForInfredients=new HashMap<String, Map<Nutrient,PreciseQuantity>>(); 
		Map<String, Map<Nutrient, NotPreciseQuantity>> lastMap=new HashMap<String, Map<Nutrient,NotPreciseQuantity>>();



		for(Entry<String, Map<String, NotPreciseQuantity>> e:simplerAllMap.entrySet()){
			Map<Nutrient, NotPreciseQuantity> map = lastMap.get(e.getKey());

			if(map==null){
				map=new HashMap<Nutrient, NotPreciseQuantity>();
				lastMap.put(e.getKey(), map);
			}

			for(Entry<String, NotPreciseQuantity> e2:e.getValue().entrySet()){

				Map<Nutrient, PreciseQuantity> singleIngredientNutrients =
						FoodIngredientAgent.parseFoodIngredient(e2.getKey());

				for(Entry <Nutrient, PreciseQuantity> e3:singleIngredientNutrients.entrySet()){
					NotPreciseQuantity sum = map.get(e3.getKey());

					if(sum==null){
						sum=e3.getValue();
						map.put(e3.getKey(), sum);
					}else{
						try {
							sum.add(e3.getValue());
						} catch (IncopatibleAmountTypesException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (InvalidArgumentException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}

			}
		}


		CompoundMapManipulator<String,Nutrient> cmm3=new CompoundMapManipulator<String, Nutrient>(lastMap);

		Map<Nutrient,NotPreciseQuantity> lastSum=cmm3.sumUpInnerMaps();
		List<Nutrient> allLastIngs = new ArrayList<Nutrient>(cmm3.getAllInnerMapsKeys());
		mav.addObject("lastMap", lastMap);
		mav.addObject("lastSum", lastSum);
		mav.addObject("allLastIngs", allLastIngs);

		return mav;
	}

	private Map<SingleProdukt_SearchResult, ProduktWithAllIngredients> scrapAllNutrientsDataHandleUnlikelyExceptions(
			GoodBadSkippedResults resultsHolder) throws AgentSystemNotStartedException, Page404Exception {
		Map<SingleProdukt_SearchResult, ProduktWithAllIngredients> retrievedBasicNutrientValues=null;
		try {
			retrievedBasicNutrientValues = 
					ReadingAgent.retrieveOrScrapAllNutrientValues(resultsHolder.getGoodResults());
		} catch (ShopNotFoundException e) {
			//pozostawione, nie powinno wyst¹piæ
			e.printStackTrace();
		}
		return retrievedBasicNutrientValues; 

	}

	private Map<String, NotPreciseQuantity> getMapOutOfSum(Collection<BasicIngredientQuantity> sumOfNutrients) {
		Map<String,NotPreciseQuantity >retValue=new HashMap<String, NotPreciseQuantity>();
		for(BasicIngredientQuantity biq:sumOfNutrients){
			retValue.put(biq.getName(), biq.getAmount());
		}
		return retValue;
	}

	private Map<String, Map<String, NotPreciseQuantity>> getProduktToSkladnikToAmountMap(
			//SearchResult->(ProduktWithListOfNutrient)
			Map<SingleProdukt_SearchResult, ProduktWithBasicIngredients> retrievedBasicNutrientValues) {
		//IngredientName->(NutrientName->amount)
		Map<String, Map<String,NotPreciseQuantity>> amountsMap=new HashMap<String, Map<String,NotPreciseQuantity>>();

		for(Entry<SingleProdukt_SearchResult, ProduktWithBasicIngredients> e:retrievedBasicNutrientValues.entrySet()){
			Map<String, NotPreciseQuantity> produktNutrients = amountsMap.get(e.getKey());

			produktNutrients=new HashMap<String,NotPreciseQuantity>();
			amountsMap.put(e.getKey().getProduktNameAndSearchPhrase(),produktNutrients );


			for(BasicIngredientQuantity biq:e.getValue().getBasicsFromLabelTable()){
				produktNutrients.put(biq.getName(),getAmountMulltipliedByProduktAmountOver100g(biq.getAmount(),e));
			}

		}
		return amountsMap;
	}

	private NotPreciseQuantity getAmountMulltipliedByProduktAmountOver100g(NotPreciseQuantity amountPer100gOfProdukt,
			Entry<SingleProdukt_SearchResult, ProduktWithBasicIngredients> e) {

		PreciseQuantity produktAmount = e.getValue().getProdukt().getAmount();


		int multiplier=getMultiplierOfProduktQuantityForNeededQuantity(PreciseQuantity.parseFromJspString(e.getKey().getQuantity()), produktAmount);

		amountPer100gOfProdukt.multiplyBy(produktAmount.getFractionOf100g()*multiplier);

		return amountPer100gOfProdukt;	


	}


	private Map<String, Map<String, NotPreciseQuantity>> getSimplerAllMap(
			Map<SingleProdukt_SearchResult, ProduktWithAllIngredients> retrievedAllNutrientValues) {
		//IngredientName->(FoodIngredientName->amount)
		Map<String, Map<String, NotPreciseQuantity>> amountsMap=new HashMap<String, Map<String,NotPreciseQuantity>>();

		for(Entry<SingleProdukt_SearchResult, ProduktWithAllIngredients> e:retrievedAllNutrientValues.entrySet()){
			Map<String, NotPreciseQuantity> produktNutrients = amountsMap.get(e.getKey());
			if(produktNutrients==null){
				Map<String, NotPreciseQuantity> mapOutOfList = new HashMap<String, NotPreciseQuantity>();
				amountsMap.put(e.getKey().getProdukt().getNazwa(),mapOutOfList);
				produktNutrients=mapOutOfList;
			}

			if(e.getValue().getProduktAsIngredient()!=null)
			for(BasicIngredientQuantity biq:e.getValue().getProduktAsIngredient().getAllBasicIngredients()){
				NotPreciseQuantity ingredientAmountSoFar = produktNutrients.get(biq.getName());

				NotPreciseQuantity amount = biq.getAmount();
				amount.multiplyBy(
						getMultiplierOfProduktQuantityForNeededQuantity(PreciseQuantity.parseFromJspString(e.getKey().getQuantity()),
								e.getValue().getProdukt().getAmount()));;

								if(ingredientAmountSoFar==null){
									ingredientAmountSoFar=amount;
									produktNutrients.put(biq.getName(), amount);

								}else{
									try {
										ingredientAmountSoFar.add(amount);
									} catch (IncopatibleAmountTypesException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} catch (InvalidArgumentException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
			}

		}
		return amountsMap;
	}

	private ModelAndView returnProduktsCorrectingPage(GoodBadSkippedResults resultsHolder) {
		ModelAndView mav=new ModelAndView("correctProducts");
		mav.addObject("badResults",resultsHolder.getUsersBadChoice());
		mav.addObject("correctResults",resultsHolder.getGoodResults());
		mav.addObject("skippedResults",resultsHolder.getSkippedResults());
		return mav;
	}

	private void moveNotFoundProduktFromGoodToBadChoices(String produktUrl, GoodBadSkippedResults resultsToBeUpdated) throws AgentSystemNotStartedException {
		String shortUrl=AuchanAbstractScrapper.getAuchanShortestWorkingUrl(produktUrl);
		Iterator<SingleProdukt_SearchResult> goodProduktsIterator = resultsToBeUpdated.getGoodResults().iterator();
		while(goodProduktsIterator.hasNext()){
			SingleProdukt_SearchResult produktToBeChecked = goodProduktsIterator.next();

			if(shortUrl.equals(produktToBeChecked.getProdukt().getUrl())){
				goodProduktsIterator.remove();

				InvalidSearchResult goodResultConverted=convertGoodResultToBadOne(produktToBeChecked);

				resultsToBeUpdated.getUsersBadChoice().add(goodResultConverted);
			}
		}

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

	private Map<SingleProdukt_SearchResult, ProduktWithBasicIngredients> scrapNutrientValuesDataHandleUnlikelyExceptions(
			GoodBadSkippedResults resultsHolder) throws AgentSystemNotStartedException, Page404Exception {
		Map<SingleProdukt_SearchResult, ProduktWithBasicIngredients> retrievedBasicNutrientValues=null;
		try {
			retrievedBasicNutrientValues = 
					ReadingAgent.retrieveOrScrapBasicNutrientValues(resultsHolder.getGoodResults());
		} catch (ShopNotFoundException e) {
			//pozostawione, nie powinno wyst¹piæ
			e.printStackTrace();
		}
		return retrievedBasicNutrientValues;
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
								"Wygl¹da na to, ¿e strona internetowa pod url \""+innyUrl+"\" nie istnieje, lub nie opisuje ¿adnego produktu");
						resultsHolder.addUsersBadChoice(isr);
					}
				} catch (ShopNotFoundException e) {
					//invalidShopUrls.put(produktPhrase,innyUrl);
					List<Produkt> searchResults;
					searchResults = ProduktAgent.searchForProdukt(produktPhrase);
					InvalidSearchResult isr=new InvalidSearchResult(searchPhrase,produktPhrase,quantityPhrase,searchResults,
							"Wygl¹da na to, ¿e url \""+innyUrl
							+"\" nie zosta³ rozpoznany jako pasuj¹cy do ¿adnego ze wspieranych sklepów.");
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
							"Wygl¹da na to, ¿e produkt wybrany przez przycisk radio nie istnieje w systemie!: "
									+wybranyProdukt);
					List<Produkt> searchResults;

					searchResults = ProduktAgent.searchForProdukt(produktPhrase);

					InvalidSearchResult isr=new InvalidSearchResult(searchPhrase,produktPhrase,quantityPhrase,searchResults,
							"UPS! Wydaje siê, ¿e zaproponowany przez nas produkt nie wystêpuje w naszym systemie!! Powiadom o tym administratora albo coœ...");
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
