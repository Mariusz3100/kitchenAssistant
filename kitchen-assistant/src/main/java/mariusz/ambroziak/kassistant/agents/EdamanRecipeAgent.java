package mariusz.ambroziak.kassistant.agents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.json.JSONObject;

import com.sun.jersey.api.client.UniformInterfaceException;

import api.extractors.EdamanQExtract;
import madkit.kernel.AgentAddress;
import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.Apiclients.edaman.DietLabels;
import mariusz.ambroziak.kassistant.Apiclients.edaman.EdamanRecipeApiClient;
import mariusz.ambroziak.kassistant.Apiclients.edaman.EdamanRecipeApiParameters;
import mariusz.ambroziak.kassistant.Apiclients.edaman.HealthAndDietLimitations;
import mariusz.ambroziak.kassistant.Apiclients.edaman.HealthLabels;
import mariusz.ambroziak.kassistant.Apiclients.edaman.ParseableRecipeData;
import mariusz.ambroziak.kassistant.api.agents.GoogleAccessAgent;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.dao.ProduktDAO;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.GoogleDriveAccessNotAuthorisedException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.jsp.MultiProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.jsp.ProduktWithRecountedPrice;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.ApiIngredientAmount;
import mariusz.ambroziak.kassistant.utils.MessageTypes;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;

public class EdamanRecipeAgent extends BaseAgent{

	static ArrayList<EdamanRecipeAgent> agents;


	public static final String PARSER_NAME = "edamanRecipeParser";

	private static final long serialVersionUID = 1L;
	public  static final boolean checkShops = true;


	private static final String edamanAgentDescription = "Agent used by other agents (usually through ShopListAgent) to retrieve information." ;



	@Override
	protected void live() {

		while(true){
			pause(100000);
		}
	}


	@Override
	protected void pause(int milliSeconds) {
		super.pause(milliSeconds);
	}

	@Override
	protected void activate() {
		if(!isGroup(AGENT_COMMUNITY,AGENT_GROUP)){
			createGroup(AGENT_COMMUNITY,AGENT_GROUP);
		}

		requestRole(AGENT_COMMUNITY, AGENT_GROUP, PARSER_NAME);

		if(agents==null)agents=new ArrayList<EdamanRecipeAgent>();
		agents.add(this);

		setLogLevel(Level.FINEST);
		setDescription(edamanAgentDescription);

		super.activate();
	}

	public EdamanRecipeAgent() {
		super();
		AGENT_COMMUNITY=StringHolder.AGENT_COMMUNITY;
		AGENT_ROLE=PARSER_NAME;

	}


	@Override
	protected void end() {
		super.end();
	}






	private static EdamanRecipeAgent getFreeAgent() throws AgentSystemNotStartedException {
		EdamanRecipeAgent freeOne=null;

		if(agents==null){
			System.out.println("Agent system not started");
			throw new AgentSystemNotStartedException();

		}else{
			while(freeOne==null){
				for(EdamanRecipeAgent ra:agents){
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




	//----------------------------------------------------------------	



	public static ParseableRecipeData retrieveSingleRecipeHeaderById(String urlId) throws AgentSystemNotStartedException, Page404Exception{
		EdamanRecipeAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.setBusy(true);
			ParseableRecipeData result;
			try{
				result= freeOne.getFromDbOrApiRecipeHeaderData(urlId);
			}finally{
				freeOne.setBusy(false);
			}
			return result;
		}
	}

	public static Map<MultiProdukt_SearchResult, PreciseQuantity> parseSingleRecipe(String urlId) throws AgentSystemNotStartedException, Page404Exception{
		EdamanRecipeAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.setBusy(true);
			Map<MultiProdukt_SearchResult, PreciseQuantity> result;
			try{
				result= freeOne.parseRecipeOrRetrieveFromDb(urlId);
			}finally{
				freeOne.setBusy(false);
			}
			return result;
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

	private Map<MultiProdukt_SearchResult, PreciseQuantity> parseRecipeOrRetrieveFromDb(String urlId) throws Page404Exception {
		Map<MultiProdukt_SearchResult, PreciseQuantity> retValue=new HashMap<>();
		ParseableRecipeData singleRecipe = EdamanRecipeApiClient.getSingleRecipe(urlId);
		
		if(singleRecipe==null) {
			throw new Page404Exception("Url '"+urlId+"' does not return any valid recipe data");
		}else {
			for(ApiIngredientAmount aia:singleRecipe.getIngredients()) {
				String ingName=aia.getName();
				String ingredientNameWithoutQuantities=EdamanQExtract.correctText(ingName);
				ArrayList<Produkt> found=getFromDbOrApiSingleIngredient(ingredientNameWithoutQuantities);
				List<Produkt> foundProductsWithRecountedPrices=getProduktsWithRecountedPrice(found, aia.getAmount(), "$");
				MultiProdukt_SearchResult singleResult=
						new MultiProdukt_SearchResult(ingName, ingredientNameWithoutQuantities,aia.getAmount().toJspString() , foundProductsWithRecountedPrices);
				retValue.put(singleResult, aia.getAmount());
			}
		}

		return retValue;
	}



	private ProduktWithRecountedPrice getProduktWithRecountedPrice(Produkt p, PreciseQuantity neededQuantity, String curency) {
		String recountedPrice="";
		PreciseQuantity produktQuan=PreciseQuantity.parseFromJspString(p.getQuantityPhrase());
		if(produktQuan==null||neededQuantity==null) {
			ProblemLogger.logProblem("Amount not found for product with id: "+p.getP_id());
			recountedPrice="1->"+p.getCena()+" "+curency;
		}else {
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
		}
		ProduktWithRecountedPrice retValue=new ProduktWithRecountedPrice(p, recountedPrice);
		return retValue;
	}



	private ArrayList<Produkt> getFromDbOrApiSingleIngredient(String ingredientNameWithoutQuantities) {
		//		String parsedSearchPhrase=EdamanQExtract.correctText(ingredientNameWithoutQuantities);
		ArrayList<Produkt> retValue=new ArrayList<>();


		List<Produkt> produktsFromDb = DaoProvider.getInstance().getProduktDao().getProduktsBySpacedName(ingredientNameWithoutQuantities);

		if(produktsFromDb==null||produktsFromDb.isEmpty()) {
			retValue=checkShops(ingredientNameWithoutQuantities);
		}else {
			retValue.addAll(produktsFromDb);
		}
		return retValue;


	}



	private ArrayList<Produkt> checkShops(String text) {
		if(checkShops&&text!=null){
			JSONObject json = createSearchForMessage(text);

			AgentAddress x=getAgentWithRole(StringHolder.AGENT_COMMUNITY, AGENT_GROUP, ShopsListAgent.SHOP_LIST_NAME);

			StringMessage messageToSend = new StringMessage(json.toString());
			StringMessage response=(StringMessage) 
					sendMessageWithRoleAndWaitForReplyKA(x, messageToSend,PARSER_NAME);

			if(response.getContent().equals(""))
				return null;
			else{
				JSONObject jsonObject = new JSONObject(response.getContent());

				if(MessageTypes.ExceptionOccured.toString().equals(jsonObject.getString(StringHolder.MESSAGE_TYPE_NAME))){
					String name=jsonObject.getString(StringHolder.EXCEPTION_MESSAGE_NAME);
					String stackTrace=jsonObject.getString(StringHolder.EXCEPTION_STACKTRACE_NAME);

					throw new RuntimeException("Exception was thrown in agent"+name+":\n"+stackTrace);
				}else {
					if(MessageTypes.SearchForResponse.toString().equals(jsonObject.getString(StringHolder.MESSAGE_TYPE_NAME))){
						String ids=jsonObject.getString(StringHolder.PRODUKT_IDS_NAME);
						return retrieveProducktsByIds(ids);	
					}else {
						ProblemLogger.logProblem("Received message of improper type "+response.getContent());
						return null;
					}

				}

			}
		}else{
			return null;
		}
	}

	private ArrayList<Produkt> retrieveProducktsByIds(String ids) {
		ProduktDAO produktDao = DaoProvider.getInstance().getProduktDao();

		ArrayList<Produkt> retValue=new ArrayList<Produkt>();

		if(ids!=null&&!ids.equals(""))
			for(String id:ids.split(" ")){
				retValue.add(produktDao.getById(Long.parseLong(id)));
			}

		return retValue;
	}


	private JSONObject createSearchForMessage(String text) {
		JSONObject json = new JSONObject();

		json.put(StringHolder.SEARCH4_NAME, text.trim());
		json.put(StringHolder.MESSAGE_CREATOR_NAME, PARSER_NAME);
		json.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.SearchFor);
		return json;
	}

	public static ArrayList<ParseableRecipeData> searchForRecipes(String googleAccessToken,String searchPhrase) throws AgentSystemNotStartedException{
		EdamanRecipeAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.setBusy(true);
			ArrayList<ParseableRecipeData> result;
			try{
				result= freeOne.searchForRecipesByPhrase(googleAccessToken,searchPhrase);
			}finally{
				freeOne.setBusy(false);
			}
			return result;
		}
	}	

	private ArrayList<ParseableRecipeData> searchForRecipesByPhrase(String googleAccessToken,String searchPhrase) {
		HealthAndDietLimitations recipeLimitations;
		try {
			if(googleAccessToken==null||googleAccessToken.equals(""))
				return EdamanRecipeApiClient.getRecipesByPhrase(searchPhrase);
			else
				recipeLimitations = getRecipeLimitations(googleAccessToken);
		} catch (GoogleDriveAccessNotAuthorisedException e) {
			return EdamanRecipeApiClient.getRecipesByPhrase(searchPhrase);

		}
		EdamanRecipeApiParameters eap = packToEap(searchPhrase, recipeLimitations);
		return getAndCheckRecipesByEap(eap);
	}

//[Peanuts, Vegan, No_sugar]
	private ArrayList<ParseableRecipeData> getAndCheckRecipesByEap(EdamanRecipeApiParameters eap) {
		ArrayList<ParseableRecipeData> recipesByParameters=null;
		try {
			recipesByParameters = EdamanRecipeApiClient.getRecipesByParameters(eap);
		}catch (UniformInterfaceException e) {
			recipesByParameters = EdamanRecipeApiClient.getRecipesByPhrase(eap.getPhrase());
		}
		//Peanuts
		ArrayList<ParseableRecipeData> retvalue=new ArrayList<>();
		for(ParseableRecipeData prd:recipesByParameters) {
			if(prd!=null
					&&prd.getDietLabels().containsAll(eap.getDietLabels())
					&&prd.getHealthLabels().containsAll(eap.getHealthLabels()))
				retvalue.add(prd);
		}
		return retvalue;
		
	}


	private EdamanRecipeApiParameters packToEap(String searchPhrase, HealthAndDietLimitations recipeLimitations) {
		EdamanRecipeApiParameters eap=new EdamanRecipeApiParameters();
		if(recipeLimitations!=null) {
			eap.setDietLabels(recipeLimitations.getDietLabels());
			eap.setHealthLabels(recipeLimitations.getHealthLabels());
		}
		eap.setPhrase(searchPhrase);
		return eap;
	}


	private HealthAndDietLimitations getRecipeLimitations(String googleAccessToken) throws GoogleDriveAccessNotAuthorisedException {
		StringMessage messageToSend = createGetLimitationMessage(googleAccessToken);

		AgentAddress x=getAgentWithRole(StringHolder.AGENT_COMMUNITY, AGENT_GROUP, GoogleAccessAgent.GOOGLE_AGENT_NAME);

		StringMessage response=(StringMessage) 
				sendMessageWithRoleAndWaitForReplyKA(x, messageToSend,PARSER_NAME);

		return processResponseMessage(response);
	}


	private HealthAndDietLimitations processResponseMessage(StringMessage response) throws GoogleDriveAccessNotAuthorisedException {
		if(response.getContent().equals(""))
			return new HealthAndDietLimitations();
		else{
			JSONObject jsonObject = new JSONObject(response.getContent());

			if(!jsonObject.has(StringHolder.MESSAGE_TYPE_NAME))
			{
				ProblemLogger.logProblem("Received message without type "+response.getContent());
				return new HealthAndDietLimitations();
			}else {
				if(MessageTypes.ExceptionOccured.toString().equals(jsonObject.getString(StringHolder.MESSAGE_TYPE_NAME))){
					String name=jsonObject.getString(StringHolder.EXCEPTION_MESSAGE_NAME);
					String stackTrace=jsonObject.getString(StringHolder.EXCEPTION_STACKTRACE_NAME);

					throw new RuntimeException("Exception was thrown in agent"+name+":\n"+stackTrace);
				}else {
					if(MessageTypes.GetLimitationsResponseNotAuthorised.toString().equals(jsonObject.getString(StringHolder.MESSAGE_TYPE_NAME))){
						throw new GoogleDriveAccessNotAuthorisedException();
					}else{
						
						if(MessageTypes.GetLimitationsResponse.toString().equals(jsonObject.getString(StringHolder.MESSAGE_TYPE_NAME))){
							HealthAndDietLimitations retValue=new HealthAndDietLimitations();
							String dietLabels=jsonObject.getString(StringHolder.DIET_LIMITATIONS_NAME);

							if(dietLabels!=null&&!dietLabels.equals("")) {
								List<DietLabels> retrievedByName = DietLabels.retrieveByName(Arrays.asList(dietLabels.split("\r\n")));
								retValue.setDietLabels(retrievedByName);
							}
							String healthLabels=jsonObject.getString(StringHolder.HEALTH_LIMITATIONS_NAME);

							if(healthLabels!=null&&!healthLabels.equals("")) {
								List<HealthLabels> retrievedByName = HealthLabels.retrieveByParameterName(Arrays.asList(healthLabels.split("\r\n")));
								retValue.setHealthLabels(retrievedByName);
							}

							return retValue;
						}else {
							ProblemLogger.logProblem("Received message of improper type "+response.getContent());
							return new HealthAndDietLimitations();
						}
					}
				}
			}
		}
	}


	private StringMessage createGetLimitationMessage(String googleAccessToken) {
		JSONObject json = new JSONObject();

		json.put(StringHolder.MESSAGE_CREATOR_NAME, PARSER_NAME);
		json.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.GetLimitations);
		includeAccessToken(json,googleAccessToken);
		StringMessage messageToSend = new StringMessage(json.toString());

		return messageToSend;
	}


	private void includeAccessToken(JSONObject json, String googleAccessToken) {
		GoogleAccessAgent.accessTokenMap
			.put(Long.toString(GoogleAccessAgent.accessTokenIdInMap),
					googleAccessToken);
		
		json.put(StringHolder.GOOGLE_ACCESS_TOKEN_ID_NAME, GoogleAccessAgent.accessTokenIdInMap);
		GoogleAccessAgent.accessTokenIdInMap++;
	}


	private ParseableRecipeData getFromDbOrApiRecipeHeaderData(String urlId) throws Page404Exception {
		//TODO for recipe url

		return EdamanRecipeApiClient.getSingleRecipe(urlId);
		//		Recipe recipeByURL = DaoProvider.getInstance().getRecipeDao().getRecipeByURL(url);
		//
		//		if(recipeByURL==null)
		//			return parseProduktsPhrasesAndQuantitiesFromRecipeUrl(url);
		//		else
		//			return retrieveFromDb();
	}


}
