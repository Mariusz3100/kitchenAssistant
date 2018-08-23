package mariusz.ambroziak.kassistant.api.agents;

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

import webscrappers.SJPWebScrapper;
import webscrappers.przepisy.PrzepisyPLQExtract;
import webscrappers.przepisy.PrzepisyPlWebscrapper;
import webscrappers.przepisy.SkladnikiExtractor;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.Apiclients.edaman.UsdaNutrientApiClient;
import mariusz.ambroziak.kassistant.Apiclients.edaman.EdamanRecipeApiParameters;
import mariusz.ambroziak.kassistant.Apiclients.edaman.RecipeData;
import mariusz.ambroziak.kassistant.agents.BaseAgent;
import mariusz.ambroziak.kassistant.agents.ShopsListAgent;
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
import mariusz.ambroziak.kassistant.model.jsp.SearchResult;
import mariusz.ambroziak.kassistant.model.utils.PreciseQuantityWithPhrase;
import mariusz.ambroziak.kassistant.utils.Converter;
import mariusz.ambroziak.kassistant.utils.MessageTypes;
import mariusz.ambroziak.kassistant.utils.ParameterHolder;
import mariusz.ambroziak.kassistant.utils.StringHolder;

public class EdamanRecipeAgent extends BaseAgent{

	static ArrayList<EdamanRecipeAgent> agents;
	public static final String PARSER_NAME = "recipeParser";

	private static final long serialVersionUID = 1L;
	public  static final boolean checkShops = true;

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

	public static ArrayList<MultiProdukt_SearchResult> getQuantitiesAndProduktsFromRecipeUrl(String url)
			throws AgentSystemNotStartedException, Page404Exception{
		EdamanRecipeAgent freeOne = getFreeAgent();

		if(freeOne!=null)
		{
			freeOne.setBusy(true);
			ArrayList<MultiProdukt_SearchResult> result= freeOne.getFromDbOrParseRecipe(url);
			freeOne.setBusy(false);
			return result;
		}else
			return null;


	}

	public static ArrayList<SearchResult> getPhrasesAndQuantitiesFromRecipeUrl(String url) throws AgentSystemNotStartedException, Page404Exception{
		EdamanRecipeAgent freeOne = getFreeAgent();

		if(freeOne!=null)
		{
			freeOne.setBusy(true);
			//TODO potem dodac opcjc wyciagania z bazy? Do rozwazenia
			ArrayList<SearchResult> result= freeOne.parsePhrasesAndQuantitiesFromRecipeUrl(url);
			freeOne.setBusy(false);
			return result;
		}else
			return null;
	}


	public static List<Produkt> parseProdukt(String produktPhrase) throws AgentSystemNotStartedException{
		EdamanRecipeAgent freeOne = getFreeAgent();
		if(freeOne!=null)
		{
			freeOne.setBusy(true);
			List<Produkt> result= freeOne.findSkladnik(produktPhrase);
			freeOne.setBusy(false);
			return result;
		}else
			return null;
	}

	public static List<Produkt> searchForProdukt(String searchPhrase) throws AgentSystemNotStartedException{
		BaseAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.setBusy(true);
			List<Produkt> result;
			try{
				result=null;// freeOne.parseRecipe(searchPhrase);
			}finally{
				freeOne.setBusy(false);
			}
			return result;
		}
	}

	public static List<RecipeData> searchForRecipe(String searchPhrase) throws AgentSystemNotStartedException{
		EdamanRecipeAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.setBusy(true);
			List<RecipeData> result;
			try{
				result= freeOne.fetchRecipes(searchPhrase);
			}finally{
				freeOne.setBusy(false);
			}
			return result;
		}
	}

	public static RecipeData getSingleRecipe(String url) throws AgentSystemNotStartedException{
		EdamanRecipeAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.setBusy(true);
			RecipeData result;
			try{
				result= freeOne.fetchRecipe(url);
			}finally{
				freeOne.setBusy(false);
			}
			return result;
		}
	}


	private RecipeData fetchRecipe(String url) {

		return null;// UsdaNutrientApiClient.getSingleRecipe(url);
	}

	private List<RecipeData> fetchRecipes(String searchPhrase) {

		EdamanRecipeApiParameters eap=new EdamanRecipeApiParameters();

		eap.setDietLabels(GoogleAccessAgent.getDietLabels());
		eap.setHealthLabels(GoogleAccessAgent.getHealthLabels());
		eap.setPhrase(searchPhrase);
		ArrayList<RecipeData> recipes = null;//UsdaNutrientApiClient.getRecipesByParameters(eap);
		return recipes;
	}

	//	private RecipeData parseRecipe(String url) {
	//		return EdamanApiClient.getSingleRecipe(url);
	//		
	//	}

	//	public Produkt getProduktFromDbByUrl(String produktUrl) {
	//		return produktDao.getProduktsByURL(produktUrl);
	//	}

	private void sendGetPreferencesMessage() {
		JSONObject json = new JSONObject();


		json.put(StringHolder.MESSAGE_CREATOR_NAME, PARSER_NAME);
		json.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.GetLimitations);


		AgentAddress x=getAgentWithRole(StringHolder.AGENT_COMMUNITY, AGENT_GROUP, GoogleAccessAgent.AGENT_NAME);

		StringMessage messageToSend = new StringMessage(json.toString());
		StringMessage response=(StringMessage) 
				sendMessageWithRoleAndWaitForReplyKA(x, messageToSend,PARSER_NAME);

		System.out.println(response.getContent());
	}


	private static EdamanRecipeAgent getFreeAgent() throws AgentSystemNotStartedException {
		EdamanRecipeAgent freeOne=null;

		if(agents==null){
			System.out.println("Agent system not started");
			throw new AgentSystemNotStartedException();

		}
		else{
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

	private ArrayList<MultiProdukt_SearchResult> getFromDbOrParseRecipe(String url) throws Page404Exception {
		//TODO actual db saving and retrieving.
		//For now I decided to leave it as it is, every time parse, 
		//because I need to make sure if recipes remain the same 
		Recipe recipeByURL = DaoProvider.getInstance().getRecipeDao().getRecipeByURL(url);

		if(recipeByURL==null)
			return parseProduktsPhrasesAndQuantitiesFromRecipeUrl(url);
		else
			return retrieveFromDb();
	}


	private ArrayList<MultiProdukt_SearchResult> retrieveFromDb() {
		// TODO na razie nie robimy
		return null;
	}






















	public ArrayList<MultiProdukt_SearchResult> parseProduktsPhrasesAndQuantitiesFromRecipeUrl(String url) throws Page404Exception{
		ArrayList<MultiProdukt_SearchResult> retValue=new ArrayList<MultiProdukt_SearchResult>();
		try{
			PrzepisyPlWebscrapper scrapperPrzepisu=PrzepisyPlWebscrapper.parse(url);
			Elements ings=scrapperPrzepisu.extractIngredients();

			for(Element e:ings){
				String ingredient = e.text();
				PreciseQuantityWithPhrase produktAndAmount=retrieveProduktAmountData(e);
				MultiProdukt_SearchResult sr = parseSkladnik(ingredient, produktAndAmount);
				retValue.add(sr);

				htmlLog("\n"+ingredient+"->\n");

				if(sr.getProdukts()!=null&&sr.getProdukts().size()>0)
					for(Produkt p:sr.getProdukts()){
						htmlLog(p.getUrl()+"\n");
					}
				else
					htmlLog("Nothing Found\n");
			}
		}catch( IOException e){
			e.printStackTrace();
		}
		return retValue;
	}


	public ArrayList<SearchResult> parsePhrasesAndQuantitiesFromRecipeUrl(String url) throws Page404Exception{
		ArrayList<SearchResult> retValue=new ArrayList<SearchResult>();
		try{
			PrzepisyPlWebscrapper scrapperPrzepisu=PrzepisyPlWebscrapper.parse(url);
			Elements ings=scrapperPrzepisu.extractIngredients();

			for(Element e:ings){
				String ingredient = e.text();
				PreciseQuantityWithPhrase produktAndAmount=retrieveProduktAmountData(e);
				SearchResult sr =new SearchResult(ingredient,produktAndAmount.getProduktPhrase(),
						produktAndAmount.getQuantityJspPhrase());
				retValue.add(sr);

			}
		}catch( IOException e){
			e.printStackTrace();
		}
		return retValue;
	}


	public MultiProdukt_SearchResult parseSkladnik(String searchPhrase, String  quanPhrase) {
		PreciseQuantityWithPhrase qp=retrieveProduktAmountData(searchPhrase,quanPhrase);

		List<Produkt> potencjalneSkladniki = findSkladnik(qp.getProduktPhrase());
		return new MultiProdukt_SearchResult(searchPhrase,qp.getProduktPhrase(),qp.getQuantityJspPhrase(),potencjalneSkladniki);
	}

	public MultiProdukt_SearchResult parseSkladnik(String searchPhrase, PreciseQuantityWithPhrase qp) {
		List<Produkt> potencjalneSkladniki = findSkladnik(qp.getProduktPhrase());
		return new MultiProdukt_SearchResult(searchPhrase,qp.getProduktPhrase(),qp.getQuantityJspPhrase(),potencjalneSkladniki);
	}


	private PreciseQuantityWithPhrase retrieveProduktAmountData(Element e) {
		String ingredient = e.text();
		String quan =extractQuantity(e);

		PreciseQuantityWithPhrase retValue = SkladnikiExtractor.extract(ingredient, quan);
		return retValue;
	}

	private PreciseQuantityWithPhrase retrieveProduktAmountData(String ingredientPhrase, String quantityPhrase) {

		PreciseQuantityWithPhrase retValue = SkladnikiExtractor.extract(ingredientPhrase, quantityPhrase);
		return retValue;
	}

	private String extractQuantity(Element e) {

		String quantity=e.select(".quantity").text();


		if(quantity==null||quantity.equals(""))
			quantity=e.parent().parent().select(".quantity").text();
		return quantity;
	}

	//	private static QuantityProdukt retrieveQuantity(String quantity) {
	//		return PrzepisyPLQExtract.extractQuantity(quantity);
	//		
	//		
	//		
	//	}


	private List<Produkt> findSkladnik(String produktPhrase) {
		List<Produkt> results;
		ArrayList<String> baseWords = null;
		results=checkInDb(produktPhrase);

		if(results==null||results.size()<1){
			baseWords=getBaseWords(produktPhrase);

			baseWords=removeNiepoprawne(baseWords);

			if(baseWords.size()>0)
				results=checkInDb(baseWords);
		}

		if(results==null||results.size()<1){

			if(baseWords==null)
				baseWords=getBaseWords(produktPhrase);

			baseWords=removeNiepoprawne(baseWords);

			if(baseWords.size()>0)
				results=checkShops(baseWords);
		}


		if(results==null||results.size()<1){
			results=checkShops(produktPhrase);
		}
		return results;
	}


	private ArrayList<String> removeNiepoprawne(ArrayList<String> baseWords) {
		ArrayList<String> retValue=new ArrayList<String>();

		for(String x:baseWords){
			if(!x.equals(Base_WordDAOImpl.niepoprawneSlowoBazowe))
				retValue.add(x);
		}

		return retValue;
	}

	private String removeNiepoprawne(String baseWords) {
		return baseWords.replaceAll(Base_WordDAOImpl.niepoprawneSlowoBazowe, 
				"");
	}

	private List<Produkt> checkShops(ArrayList<String> baseWords) {

		String baseWordsString="";


		for(String x:baseWords){
			if(!Base_WordDAOImpl.niepoprawneSlowoBazowe.equals(baseWordsString))
				baseWordsString+=x+" ";
		}


		return checkShops(baseWordsString);
	}


	private ArrayList<String> getBaseWords(String text) {
		String[] words=text.split(" ");
		ArrayList<String> wordsList=new ArrayList<String>();

		for(String x : words){
			Base_Word base_Name = DaoProvider.getInstance().getVariantWordDao().getBase_Name(x.toLowerCase());
			if(base_Name!=null){
				if(!Base_WordDAOImpl.niepoprawneSlowoBazowe.equals(base_Name.getB_word()))
					wordsList.add(base_Name.getB_word());
			}
			else
				wordsList.add(SJPWebScrapper.getAndSaveNewRelation(x.toLowerCase()));
		}

		return wordsList;
	}





	private ArrayList<Produkt> checkInDb(String text) {
		ArrayList<Produkt> retValue=new ArrayList<Produkt>();

		retValue.addAll(
				DaoProvider.getInstance()
				.getProduktDao()
				.getProduktsBySpacedName(text));

		return retValue;
	}

	private ArrayList<Produkt> checkInDb(Collection<String> texts) {
		ArrayList<Produkt> retValue=new ArrayList<Produkt>();

		retValue.addAll(
				DaoProvider.getInstance()
				.getProduktDao()
				.getProduktsByNames(texts));

		return retValue;
	}


	private ArrayList<Produkt> checkShops(String text) {
		if(checkShops&&text!=null){
			JSONObject json = new JSONObject();

			json.put(StringHolder.SEARCH4_NAME, text.trim());
			json.put(StringHolder.MESSAGE_CREATOR_NAME, PARSER_NAME);
			json.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.SearchFor);


			AgentAddress x=getAgentWithRole(StringHolder.AGENT_COMMUNITY, AGENT_GROUP, ShopsListAgent.SHOP_LIST_NAME);

			StringMessage messageToSend = new StringMessage(json.toString());
			StringMessage response=(StringMessage) 
					sendMessageWithRoleAndWaitForReplyKA(x, messageToSend,PARSER_NAME);


			if(response.getContent().equals(""))
				return null;
			else{
				JSONObject jsonObject = new JSONObject(response.getContent());

				//			Map y= factory.newJsonParser().parseJson(response.getContent());
				ProduktDAO produktDao = DaoProvider.getInstance().getProduktDao();

				String ids=jsonObject.getString(StringHolder.PRODUKT_IDS_NAME);

				ArrayList<Produkt> retValue=new ArrayList<Produkt>();

				if(ids!=null&&!ids.equals(""))
					for(String id:ids.split(" ")){
						retValue.add(produktDao.getById(Long.parseLong(id)));
					}

				return retValue;
			}
		}else{
			return null;
		}
	}
}

