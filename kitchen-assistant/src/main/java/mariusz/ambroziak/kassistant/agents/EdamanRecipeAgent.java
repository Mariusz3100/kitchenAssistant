package mariusz.ambroziak.kassistant.agents;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.hibernate.Session;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//import com.codesnippets4all.json.parsers.JsonParserFactory;




import org.postgresql.translation.messages_bg;
import org.springframework.beans.factory.annotation.Autowired;

import api.extractors.EdamanQExtract;
import webscrappers.SJPWebScrapper;
import webscrappers.przepisy.PrzepisyPLQExtract;
import webscrappers.przepisy.PrzepisyPlWebscrapper;
import webscrappers.przepisy.SkladnikiExtractor;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.Apiclients.edaman.EdamanRecipeApiClient;
import mariusz.ambroziak.kassistant.Apiclients.edaman.ParseableRecipeData;
import mariusz.ambroziak.kassistant.dao.Base_WordDAOImpl;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.dao.ProduktDAO;
import mariusz.ambroziak.kassistant.dao.Variant_WordDAOImpl;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.Recipe;
import mariusz.ambroziak.kassistant.model.Recipe_Ingredient;
import mariusz.ambroziak.kassistant.model.Variant_Word;
import mariusz.ambroziak.kassistant.model.jsp.MultiProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.jsp.ProduktWithRecountedPrice;
import mariusz.ambroziak.kassistant.model.jsp.SearchResult;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.ApiIngredientAmount;
import mariusz.ambroziak.kassistant.model.utils.PreciseQuantityWithPhrase;
import mariusz.ambroziak.kassistant.utils.Converter;
import mariusz.ambroziak.kassistant.utils.MessageTypes;
import mariusz.ambroziak.kassistant.utils.ParameterHolder;
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
	
	private Map<MultiProdukt_SearchResult, PreciseQuantity> parseRecipeOrRetrieveFromDb(String urlId) {
		Map<MultiProdukt_SearchResult, PreciseQuantity> retValue=new HashMap<>();
		ParseableRecipeData singleRecipe = EdamanRecipeApiClient.getSingleRecipe(urlId);

		for(ApiIngredientAmount aia:singleRecipe.getIngredients()) {
			String ingName=aia.getName();
			String ingredientNameWithoutQuantities=EdamanQExtract.correctText(ingName);
			ArrayList<Produkt> found=getFromDbOrApiSingleIngredient(ingredientNameWithoutQuantities);
			List<Produkt> foundProductsWithRecountedPrices=getProduktsWithRecountedPrice(found, aia.getAmount(), "$");
			MultiProdukt_SearchResult singleResult=
					new MultiProdukt_SearchResult(ingName, ingredientNameWithoutQuantities,aia.getAmount().toJspString() , foundProductsWithRecountedPrices);
			retValue.put(singleResult, aia.getAmount());
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

	public static ArrayList<ParseableRecipeData> searchForRecipes(String searchPhrase) throws AgentSystemNotStartedException{
		EdamanRecipeAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.setBusy(true);
			ArrayList<ParseableRecipeData> result;
			try{
				result= freeOne.searchForRecipesByPhrase(searchPhrase);
			}finally{
				freeOne.setBusy(false);
			}
			return result;
		}
	}	
	
	private ArrayList<ParseableRecipeData> searchForRecipesByPhrase(String searchPhrase) {
		
		return EdamanRecipeApiClient.getRecipesByPhrase(searchPhrase);
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
