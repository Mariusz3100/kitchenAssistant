package mariusz.ambroziak.kassistant.agents;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import javax.persistence.Basic;

import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaFoodDetails;
import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaFoodId;
import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaNutrientApiClient;
import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaNutrientApiClientParticularFood;
import mariusz.ambroziak.kassistant.dao.Base_WordDAOImpl;
import mariusz.ambroziak.kassistant.dao.Basic_IngredientDAO;
import mariusz.ambroziak.kassistant.dao.Basic_Ingredient_Nutrient_AmountDAO;
import mariusz.ambroziak.kassistant.dao.Basic_Ingredient_Nutrient_Data_SourceDAO;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.dao.ProduktDAO;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.IncopatibleAmountTypesException;
import mariusz.ambroziak.kassistant.exceptions.InvalidArgumentException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.exceptions.ShopNotFoundException;
import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient_Nutrient_Data_Source;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.jsp.SingleProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.NotPreciseQuantity;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.AbstractQuantity;
import mariusz.ambroziak.kassistant.model.utils.BasicIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.NutrientDetailsOfBasicIngredient;
import mariusz.ambroziak.kassistant.model.utils.ProduktIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithAllIngredients;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithBasicIngredients;
import mariusz.ambroziak.kassistant.shopcom.ShopComApiClient;
import mariusz.ambroziak.kassistant.model.utils.PreciseQuantityWithPhrase;
import mariusz.ambroziak.kassistant.shops.ShopRecognizer;
import mariusz.ambroziak.kassistant.utils.MessageTypes;
import mariusz.ambroziak.kassistant.utils.ParameterHolder;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;
import mariusz.ambroziak.kassistant.utils.StringUtils;

import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import webscrappers.SJPWebScrapper;
import webscrappers.przepisy.PrzepisyPLQExtract;

public class ReadingNutritientsUsdaAgent extends BaseAgent{
	public static final String USDA_AGENT_NAME = "usdaAgent";

	//TODO update
	static ArrayList<ReadingNutritientsUsdaAgent> agents;
	public static final String PARSER_NAME = "apiNutritientsParser";

	private static final long serialVersionUID = 1L;
	public  static final boolean checkShops = true;


	@Override
	protected void live() {

		StringMessage m;

		while(true){

			m = nextMessageKA();
			if(m!=null)
				processMessage(m);
			else
				enjoyYourOwn();
		}
	}

	public void enjoyYourOwn() {
	}


	private void processMessage(StringMessage m) {
		if(m!=null){
			String content=((StringMessage)m).getContent();
			JSONObject json=new JSONObject(content);

			if(json.get(StringHolder.MESSAGE_TYPE_NAME)==null
					||json.get(StringHolder.MESSAGE_TYPE_NAME).equals("")){
				ProblemLogger.logProblem("Message has no type (in Shop.com agent): "+content);
			}else if(json.get(StringHolder.MESSAGE_TYPE_NAME).equals(MessageTypes.SearchFor.toString())){
				processSearchForMessage(m);
			}else if(json.get(StringHolder.MESSAGE_TYPE_NAME).equals(MessageTypes.GetProduktData.toString())){
				processGetProduktByNdbno(m);

			}
		}

	}

	private void processGetProduktByNdbno(StringMessage m) {
		// TODO Auto-generated method stub

	}

	private void processSearchForMessage(StringMessage m) {
		JSONObject json=new JSONObject(m.getContent());
		StringMessage messageToSend=null;

		String ids = searchForCorrectEverywhereHandleNulls(json);

		try{
			messageToSend = createResponseMessage(ids);
		}catch(Exception e) {
			ProblemLogger.logProblem(e.toString());
			ProblemLogger.logStackTrace(e.getStackTrace());
			messageToSend=createExceptionResponseMessage(e);

			setBusy(false);
		}

		sendReplyWithRoleKA(m, messageToSend,AGENT_ROLE);
	}

	private StringMessage createResponseMessage(String ids) {
		JSONObject result=new JSONObject();

		result.put(StringHolder.PRODUKT_IDS_NAME, ids);
		result.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.SearchForResponse);
		result.put(StringHolder.MESSAGE_CREATOR_NAME, USDA_AGENT_NAME);

		StringMessage messageToSend = new StringMessage(result.toString());
		return messageToSend;
	}

	private String searchForCorrectEverywhereHandleNulls(JSONObject json) {
		String searchForPhrase=(String) json.get(StringHolder.SEARCH4_NAME);

		NutrientDetailsOfBasicIngredient productDetails = searchForDetailsInDbAndApi(searchForPhrase);
		String id="";
		if(productDetails.isEmpty()){
			htmlLog("Not able to find produkt details for:"+searchForPhrase+" in usda database.\n");
		}else{
			//TODO 
			id=Long.toString(productDetails.getBasicIngredient().getBi_id());
		}

		return id;
	}

	private NutrientDetailsOfBasicIngredient searchForDetailsInDbAndApi(String searchForPhrase) {
		NutrientDetailsOfBasicIngredient productDetails = searchForCorrectInDb(searchForPhrase);

		if(productDetails==null||productDetails.isEmpty()) {
			UsdaFoodDetails usdaDetails = searchForCorrectInUsda(searchForPhrase);
			Basic_Ingredient biFromDb = saveDetailsInDbCheckPrerequisites(usdaDetails);
			
			productDetails=new NutrientDetailsOfBasicIngredient(biFromDb);
			productDetails.setNutrientsMapFromMapWithPreciseQuantityValues(usdaDetails.getNutrietsMap());
		}
		return productDetails;
	}

	private NutrientDetailsOfBasicIngredient searchForCorrectInDb(String searchForPhrase) {
		// TODO write getting from db
		return null;
	}

	private Basic_Ingredient saveDetailsInDbCheckPrerequisites(UsdaFoodDetails productDetails) {

		if(productDetails==null
				||productDetails.getId()==null
				||productDetails.getId().getName()==null
				||productDetails.getId().getName().equals(""))
		{
			ProblemLogger.logProblem("Trying to save an empty basic ingredient");
			return null;
		}else {
			Basic_IngredientDAO basicIngredientDao = DaoProvider.getInstance().getBasicIngredientDao();

			String name = productDetails.getId().getName();
			Basic_Ingredient fromDb = basicIngredientDao.getIngredientByName(name);
			if(fromDb!=null) 
			{
				boolean areNutrientsAlreadyInDb = DaoProvider.getInstance().getBasicIngredientNutrientAmountDao().areNutrientsForBasicIngredient(fromDb.getBi_id());
				
				if(areNutrientsAlreadyInDb) {
					ProblemLogger.logProblem("Trying to save nutrient details for an ingredient, that has already details saved");
					Map<Nutrient, Float> nutrientsForBasicIngredient = DaoProvider.getInstance().getBasicIngredientNutrientAmountDao().getNutrientsForBasicIngredient(fromDb.getBi_id());
					if(checkIfTheseAreTheSameNutrients(productDetails.getNutrietsMap(),nutrientsForBasicIngredient)) {
						return fromDb;
					}else {
						ProblemLogger.logProblem("Trying to save nutrient details for an ingredient, that has already details saved AND the nutrients do not match");
						return null;
					}
				}else {
					Map<Nutrient, Float> nutrietsMap=getRelativeAmountsFromPreciseQuantityFor100g(productDetails.getNutrietsMap());
					
					DaoProvider.getInstance().getBasicIngredientNutrientAmountDao().saveNutrientData(fromDb,nutrietsMap);
					return fromDb;
				}
				
			}else {
				Basic_Ingredient bi=new Basic_Ingredient();
				bi.setName(name);
				basicIngredientDao.saveBasicIngredient(bi);
				Map<Nutrient, Float> nutrietsMap=getRelativeAmountsFromPreciseQuantityFor100g(productDetails.getNutrietsMap());
				//below method should do the above anyway; code above is just in case		
				DaoProvider.getInstance().getBasicIngredientNutrientAmountDao().saveNutrientData(bi,nutrietsMap);
				return fromDb;
			}
		}
	}



	private boolean checkIfTheseAreTheSameNutrients(Map<Nutrient, PreciseQuantity> fromArgument,
			Map<Nutrient, Float> fromDb) {
		if(fromArgument.size()!=fromDb.size()) {
			return false;
		}else {
			for(Entry<Nutrient, Float> e:fromDb.entrySet()) {
				Nutrient fromDbNutrient = e.getKey();
				PreciseQuantity valueFromArgument=fromArgument.get(fromDbNutrient);
				if(valueFromArgument==null
					||fromDb.get(fromDbNutrient)==null
					||fromDb.get(fromDbNutrient).floatValue()!=valueFromArgument.getAmount()) {
						return false;
				}
			}
			return true;
		}
	}

	private HashMap<Nutrient, Float> getRelativeAmountsFromPreciseQuantityFor100g(
			Map<Nutrient, PreciseQuantity> scrapedNutritientData) {
		HashMap<Nutrient, Float> retValue=new HashMap<Nutrient, Float>();
		
		if(scrapedNutritientData==null||scrapedNutritientData.isEmpty())
			return retValue;
		
		for(Entry<Nutrient, PreciseQuantity>  e:scrapedNutritientData.entrySet())
		{
			PreciseQuantity value = e.getValue();
			
			if(!AmountTypes.mg.equals(value.getType())&&!AmountTypes.kalorie.equals(value.getType())){
				ProblemLogger.logProblem(
						"Proba zapisywania w bazie innego typu ilosci skladnika odzywczego niz mg.");
			}else{
				
				
				float coefficient=value.getAmount()/(100*1000);
				
				retValue.put(e.getKey(), coefficient);
			}
			
		}
		
		return retValue;
	}
	@Override
	protected void pause(int milliSeconds) {
		// TODO Auto-generated method stub
		super.pause(milliSeconds);
	}

	private StringMessage createExceptionResponseMessage(Exception e) {
		JSONObject result=new JSONObject();

		result.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.ExceptionOccured);
		result.put(StringHolder.MESSAGE_CREATOR_NAME, USDA_AGENT_NAME);
		result.put(StringHolder.EXCEPTION_MESSAGE_NAME, e.toString());
		result.put(StringHolder.EXCEPTION_STACKTRACE_NAME, StringUtils.stackTraceToString(e));
		StringMessage messageToSend = new StringMessage(result.toString());

		return messageToSend;
	}
	public static String[] splitSearchPhrase(String searchPhrase) {
		return searchPhrase.split(" ");
	}
	public UsdaFoodDetails searchForCorrectInUsda(String searchForPhrase) {
		UsdaFoodDetails produktsFound = UsdaNutrientApiClient.searchForNutritionDetailsOfAProdukt(searchForPhrase);
		return produktsFound;
	}


	@Override
	protected void activate() {
		if(!isGroup(AGENT_COMMUNITY,AGENT_GROUP)){
			createGroup(AGENT_COMMUNITY,AGENT_GROUP);
		}

		requestRole(AGENT_COMMUNITY, AGENT_GROUP, PARSER_NAME);


		if(agents==null)agents=new ArrayList<ReadingNutritientsUsdaAgent>();
		agents.add(this);

		setLogLevel(Level.FINEST);
		super.activate();
	}

	public ReadingNutritientsUsdaAgent() {
		super();
		AGENT_COMMUNITY=StringHolder.AGENT_COMMUNITY;

	}


	@Override
	protected void end() {
		super.end();
	}


	public static UsdaFoodDetails parseProduct(String usdaId)
			throws AgentSystemNotStartedException, ShopNotFoundException, Page404Exception{
		ReadingNutritientsUsdaAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.setBusy(true);
			UsdaFoodDetails result;
			try{
				result= freeOne.parseProductByNdbno(usdaId);
			}finally{
				freeOne.setBusy(false);
			}
			return result;
		}
	}
	
	public static NutrientDetailsOfBasicIngredient searchForSingleProduct(String searchPhrase)
			throws AgentSystemNotStartedException{
		ReadingNutritientsUsdaAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.setBusy(true);
			NutrientDetailsOfBasicIngredient result;
			try{
				result= freeOne.searchForDetailsInDbAndApi(searchPhrase);
			}finally{
				freeOne.setBusy(false);
			}
			return result;
		}
	}

	


	//	private ProduktWithAllIngredients getAllFoodIngredients(String shortUrl)
	//			throws ShopNotFoundException, AgentSystemNotStartedException, Page404Exception {
	//
	////		ProduktWithAllIngredients produktWithIngredinets = AuchanRecipeParser.getAllIngredients(shortUrl);
	//
	//		return produktWithIngredinets;
	//	}


	private UsdaFoodDetails parseProductByNdbno(String usdaId) {

		UsdaFoodDetails nutrientDetailObjectForDbno = UsdaNutrientApiClientParticularFood.getNutrientDetailObjectForDbno(usdaId);
		return nutrientDetailObjectForDbno;
	}

	private static ReadingNutritientsUsdaAgent getFreeAgent() throws AgentSystemNotStartedException {
		ReadingNutritientsUsdaAgent freeOne=null;

		if(agents==null){
			System.out.println("Agent system not started");
			throw new AgentSystemNotStartedException();
		}
		else{
			while(freeOne==null){
				for(ReadingNutritientsUsdaAgent ra:agents){
					if(!ra.isBusy()){
						freeOne=ra;
					}
				}
				if(freeOne==null){
					try {
						System.out.println("Free RecipeParser Not Found");
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return freeOne;
	}

	public static Collection<UsdaFoodId> searchForMultiProduct(String foodName) throws AgentSystemNotStartedException{
		ReadingNutritientsUsdaAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.setBusy(true);
			Collection<UsdaFoodId> result = null;
			try{
				result= freeOne.searchForDetailsInDbAndApi_multiResult(foodName);
			}finally{
				freeOne.setBusy(false);
			}
			return result;
		}
	}

	private Collection<UsdaFoodId> searchForDetailsInDbAndApi_multiResult(String foodName) {
		Collection<UsdaFoodId> productDetails = searchForManyCorrectInDb(foodName);

		if(productDetails==null||productDetails.isEmpty())
			productDetails=searchForManyResultsInApi(foodName);
		
		
		// TODO Auto-generated method stub
		return productDetails;
	}

	private Collection<UsdaFoodId> searchForManyResultsInApi(String foodName) {
		ArrayList<UsdaFoodId> searchForProduktsInUsdaDb = UsdaNutrientApiClient.searchForProduktsInUsdaDb(foodName);
		return searchForProduktsInUsdaDb;
	}

	private Collection<UsdaFoodId> searchForManyCorrectInDb(String foodName) {
		List<Basic_Ingredient> ingredientBySpacedName = DaoProvider.getInstance().getBasicIngredientDao().getIngredientBySpacedName(foodName);
		
		Collection<UsdaFoodId> retValue=new ArrayList<>();
		
		for(Basic_Ingredient bi:ingredientBySpacedName) {
			retValue.add(getUsdaFoodId(bi));
		}
		
		
		// TODO Auto-generated method stub
		return retValue;
	}

	private UsdaFoodId getUsdaFoodId(Basic_Ingredient bi) {
		Basic_Ingredient_Nutrient_Data_SourceDAO basicIngredientNutrientDataSourceDao = DaoProvider.getInstance().getBasicIngredientNutrientDataSourceDao();
		
		Basic_Ingredient_Nutrient_Data_Source dataSourceByBasicIngredientId = basicIngredientNutrientDataSourceDao.getDataSourceByBasicIngredientId(bi);
		UsdaFoodId retValue=new UsdaFoodId(bi.getName(), dataSourceByBasicIngredientId.getId_in_api());
		return retValue;
	}

	private Basic_Ingredient filterBestIngredientForName(List<Basic_Ingredient> ingredientBySpacedName, String foodName) {
		String[] parts= {};
		if(foodName==null||foodName.equals("")) {
			 parts=foodName.split(" ");
		}
		
		Map<String, Basic_Ingredient> map=new HashMap<String, Basic_Ingredient>();
        Collections.sort(ingredientBySpacedName,new CompareByName());
        for(Basic_Ingredient bi:ingredientBySpacedName) {
			boolean allWordsMatchSoFar=true;
        	for(int i=0;i<parts.length&&allWordsMatchSoFar;i++) {
	        	if(bi.getName().indexOf(parts[i])<0) {
	        		allWordsMatchSoFar=false;
	        	}
			}
        	if(allWordsMatchSoFar) {
        		return bi;
        	}
        	
		}
        return new Basic_Ingredient();
		
		
	}
	



}

