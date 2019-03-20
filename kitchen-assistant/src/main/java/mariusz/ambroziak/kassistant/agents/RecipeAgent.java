package mariusz.ambroziak.kassistant.agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import madkit.kernel.AgentAddress;
import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.dao.Base_WordDAOImpl;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.dao.ProduktDAO;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.Recipe;
import mariusz.ambroziak.kassistant.model.jsp.MultiProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.jsp.SearchResult;
import mariusz.ambroziak.kassistant.model.utils.PreciseQuantityWithPhrase;
import mariusz.ambroziak.kassistant.utils.MessageTypes;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;
import webscrappers.przepisy.PrzepisyPlWebscrapper;
import webscrappers.przepisy.SkladnikiExtractor;

public class RecipeAgent extends BaseAgent{

	static ArrayList<RecipeAgent> agents;
	@Autowired
	private ProduktDAO produktDao;


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

		if(agents==null)agents=new ArrayList<RecipeAgent>();
		agents.add(this);

		setLogLevel(Level.FINEST);
		super.activate();
	}

	public RecipeAgent() {
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
		RecipeAgent freeOne = getFreeAgent();

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
		RecipeAgent freeOne = getFreeAgent();

		if(freeOne!=null)
		{
			freeOne.setBusy(true);
			//TODO potem dodac opcje wyciagania z bazy? Do rozwazenia
			ArrayList<SearchResult> result= freeOne.parsePhrasesAndQuantitiesFromRecipeUrl(url);
			freeOne.setBusy(false);
			return result;
		}else
			return null;
	}


	private static List<Produkt> parseProdukt(String produktPhrase) throws AgentSystemNotStartedException{
		RecipeAgent freeOne = getFreeAgent();
		if(freeOne!=null)
		{
			freeOne.setBusy(true);
			List<Produkt> result= freeOne.findSkladnik(produktPhrase);
			freeOne.setBusy(false);
			return result;
		}else
			return null;
	}


	private static List<Produkt> searchForProdukt(String searchPhrase,String quantityPhrase) throws AgentSystemNotStartedException{
		RecipeAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.setBusy(true);
			List<Produkt> result;
			try{
				result= freeOne.parseSkladnik(searchPhrase, quantityPhrase).getProdukts();
			}finally{
				freeOne.setBusy(false);
			}
			return result;
		}
	}


	public Produkt getProduktFromDbByUrl(String produktUrl) {
		return produktDao.getProduktsByURL(produktUrl);
	}


	private static RecipeAgent getFreeAgent() throws AgentSystemNotStartedException {
		RecipeAgent freeOne=null;

		if(agents==null){
			System.out.println("Agent system not started");
			throw new AgentSystemNotStartedException();

		}else{
			while(freeOne==null){
				for(RecipeAgent ra:agents){
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

				htmlLogResults(ingredient, sr);
			}
		}catch( IOException e){
			e.printStackTrace();
		}
		return retValue;
	}


	private void htmlLogResults(String ingredient, MultiProdukt_SearchResult sr) {
		htmlLog("\n"+ingredient+"->\n");

		if(sr.getProdukts()!=null&&sr.getProdukts().size()>0)
			for(Produkt p:sr.getProdukts()){
				htmlLog(p.getUrl()+"\n");
			}
		else
			htmlLog("Nothing Found\n");
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



	private List<Produkt> findSkladnik(String produktPhrase) {
		List<Produkt> results;
		results=checkInDb(produktPhrase);

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
					if(MessageTypes.SearchFor.toString().equals(jsonObject.getString(StringHolder.MESSAGE_TYPE_NAME))){
						String ids=jsonObject.getString(StringHolder.PRODUKT_IDS_NAME);
						return retrieveProducktsByIds(ids);	
					}else {
						ProblemLogger.logProblem("Unknown message type "+response.getContent());
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

	public void main(String[] args) throws AgentSystemNotStartedException, Page404Exception {
		RecipeAgent.getPhrasesAndQuantitiesFromRecipeUrl("https://www.przepisy.pl/przepis/nalesniki-z-cukinia-i-serem");
	}
}
