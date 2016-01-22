package mariusz.ambroziak.kassistant.agents;

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
import webscrappers.przepisy.SkladnikiExtractor;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.QuantityExtractor.AuchanQExtract;
import mariusz.ambroziak.kassistant.dao.Base_WordDAOImpl;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.dao.ProduktDAO;
import mariusz.ambroziak.kassistant.dao.Variant_WordDAOImpl;
import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.Recipe;
import mariusz.ambroziak.kassistant.model.Recipe_Ingredient;
import mariusz.ambroziak.kassistant.model.Variant_Word;
import mariusz.ambroziak.kassistant.model.jsp.SearchResult;
import mariusz.ambroziak.kassistant.model.utils.QuantityProdukt;
import mariusz.ambroziak.kassistant.utils.Converter;
import mariusz.ambroziak.kassistant.utils.MessageTypes;
import mariusz.ambroziak.kassistant.utils.ParameterHolder;
import mariusz.ambroziak.kassistant.utils.StringHolder;

public class RecipeAgent extends BaseAgent{

	static ArrayList<RecipeAgent> agents;
	private boolean busy=false;

	@Autowired
	private ProduktDAO produktDao;
	
	
	public static final String PARSER_NAME = "recipeParser";

	private static final long serialVersionUID = 1L;
	public  static final boolean checkShops = true;

//	DatabaseInterface interfac;

	public boolean isBusy() {
		return busy;
	}


	@Override
	protected void live() {
		
		StringMessage m=(StringMessage) waitNextMessageKA();
		
		processMessage(m);
		
		while(true){
			pause(1000);
		}
	}

	private void processMessage(StringMessage m) {
		Message messageSentEarlier = getMessageSentEarlier(m.getConversationID());
		
		
		
	}


	@Override
	protected void pause(int milliSeconds) {
		// TODO Auto-generated method stub
		super.pause(milliSeconds);
	}

	@Override
	protected void activate() {
		if(!isGroup(AGENT_COMMUNITY,AGENT_GROUP)){
			createGroup(AGENT_COMMUNITY,AGENT_GROUP);
		}
		
		requestRole(AGENT_COMMUNITY, AGENT_GROUP, PARSER_NAME);
//		requestRole(AGENT_COMMUNITY, ShopsListAgent.GUIDANCE_GROUP, AGENT_ROLE);
		
		//		interfac=DatabaseInterface.getDBInterface();

		if(agents==null)agents=new ArrayList<RecipeAgent>();
		agents.add(this);
//		AgentAddress x=getAgentWithRole(AGENT_COMMUNITY, AGENT_GROUP, "manager");

//		pause(500);

		
		setLogLevel(Level.FINEST);
		super.activate();
	}

	public RecipeAgent() {
		super();
		AGENT_COMMUNITY=StringHolder.AGENT_COMMUNITY;
//		AGENT_GROUP = StringHolder.SERVLETS_GROUP;
		AGENT_ROLE=PARSER_NAME;
		
	}


	@Override
	protected void end() {
		// TODO Auto-generated method stub
		super.end();
	}

	public static ArrayList<SearchResult> parse(String url){
		RecipeAgent freeOne = getFreeAgent();
		
		if(freeOne!=null)
		{
			freeOne.busy=true;
			ArrayList<SearchResult> result= freeOne.getFromDbOrParseRecipe(url);
			freeOne.busy=false;
			return result;
		}else
			return null;
		
	
	}

	public static List<Produkt> parseProdukt(String searchPhrase){
		RecipeAgent freeOne = getFreeAgent();
		if(freeOne!=null)
		{
			freeOne.busy=true;
			List<Produkt> result= freeOne.retrieveSkladnik(searchPhrase);
			freeOne.busy=false;
			return result;
		}else
			return null;
	}
	
	
	public static ArrayList<SearchResult> getProdukt(String produktUrl){
		RecipeAgent freeOne = getFreeAgent();
		if(freeOne!=null)
		{
			freeOne.busy=true;
			ArrayList<SearchResult> result= freeOne.getFromDbOrParseProdukt(produktUrl);
			freeOne.busy=false;
			return result;
		}else 
			return null;
		
	}
	
	
	private ArrayList<SearchResult> getFromDbOrParseProdukt(String produktUrl) {
		
		
		Produkt produkts = getProduktFromDbByUrl(produktUrl);
		
		
		if(produkts==null){
			if(ParameterHolder.isCheckShops()){
				JSONObject json = new JSONObject();
				
				json.put(StringHolder.PRODUKT_URL_NAME, produktUrl);
				json.put(StringHolder.MESSAGE_CREATOR_NAME, PARSER_NAME);
				json.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.GetProduktData);
//				json.put(StringHolder.MESSAGE_ID_NAME, MessageCounter.getCount());
				
				AgentAddress x=getAgentWithRole(StringHolder.AGENT_COMMUNITY, AGENT_GROUP, ShopsListAgent.SHOP_LIST_NAME);
		
				StringMessage messageToSend = new StringMessage(json.toString());
				StringMessage response=(StringMessage) 
						sendMessageWithRoleAndWaitForReplyKA(x, messageToSend,PARSER_NAME);
				
				//TODO resume work
	
				
				
			}
		}
		return null;
	}


	public Produkt getProduktFromDbByUrl(String produktUrl) {
		return produktDao.getProduktsByURL(produktUrl);
	}


	private static RecipeAgent getFreeAgent() {
		RecipeAgent freeOne=null;
		
		if(agents==null){
			System.out.println("Agent system not started");
		}
		else{
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
	}
		return freeOne;
	}

	


	private ArrayList<SearchResult> getFromDbOrParseRecipe(String url) {
		
		Recipe recipeByURL = DaoProvider.getInstance().getRecipeDao().getRecipeByURL(url);
		
		if(recipeByURL==null)
			return parseRecipe(url);
		else
			return retrieveFromDb();
	}


	private ArrayList<SearchResult> retrieveFromDb() {
		// TODO Auto-generated method stub
		return null;
	}


	public ArrayList<SearchResult> parseRecipe(String url){
		Recipe recipe=new Recipe();
		
		recipe.setUrl(url);
		
		
		ArrayList<SearchResult> retValue=new ArrayList<SearchResult>();
//		StringBuilder outPage=new StringBuilder();
		try{
			URLConnection connection = new URL(url).openConnection();//connection.getRequestProperties()
			connection.setRequestProperty("Accept-Charset", java.nio.charset.StandardCharsets.UTF_8.toString());
			InputStream detResponse = connection.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(detResponse,java.nio.charset.StandardCharsets.UTF_8.toString());
			BufferedReader detBR=new BufferedReader(inputStreamReader);
			String respLine=null;
			String html="";
			while((respLine=detBR.readLine())!=null){
				html+=respLine;
			}

			Document doc = Jsoup.parse(html);


			Elements ings=doc.select("[itemprop=\"ingredients\"]");


			for(Element e:ings){
//				ArrayList<Produkt> znalezioneProdukty=new ArrayList<Produkt>()
				
				String ingredient = e.text();
//				Recipe_Ingredient quantityRetrieved =null;
				
				
				QuantityProdukt produktAndAmount=retrieveProduktAmountData(e);
				
				 
				List<Produkt> potencjalneSkladniki = retrieveSkladnik(produktAndAmount.getProduktPhrase());
				
				
				retValue.add(new SearchResult(ingredient,produktAndAmount.getProduktPhrase(),produktAndAmount.getAmountType()+"_"+produktAndAmount.getAmount(),
						potencjalneSkladniki));
				
				htmlLog("\n"+ingredient+"->\n");
				
				if(potencjalneSkladniki!=null&&potencjalneSkladniki.size()>0)
					for(Produkt p:potencjalneSkladniki){
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


	private QuantityProdukt retrieveProduktAmountData(Element e) {
		// TODO Auto-generated method stub
		String ingredient = e.text();
		String quan =extractQuantity(e);
		

		QuantityProdukt retValue = SkladnikiExtractor.extract(ingredient, quan);
		return retValue;
	}


	private String extractQuantity(Element e) {
		
		String quantity=e.parent().select(".quantity").text();
		
		
		if(quantity==null||quantity.equals(""))
			quantity=e.parent().parent().select(".quantity").text();
		return quantity;
	}

	private static QuantityProdukt retrieveQuantity(String quantity) {
		return PrzepisyPLQExtract.extractQuantity(quantity);
		
		
		
	}


	private List<Produkt> retrieveSkladnik(String text) {
		List<Produkt> results;
		ArrayList<String> baseWords = null;
		
		
		
		results=checkInDb(text);

		if(results==null||results.size()<1){
			baseWords=getBaseWords(text);
			
			baseWords=removeNiepoprawne(baseWords);
			
			if(baseWords.size()>0)
				results=checkInDb(baseWords);
		}

		if(results==null||results.size()<1){
			
			if(baseWords==null)
				baseWords=getBaseWords(text);
			
			baseWords=removeNiepoprawne(baseWords);
			
			if(baseWords.size()>0)
				results=checkShops(baseWords);
		}
		
		
		if(results==null||results.size()<1){
			results=checkShops(text);
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

//	private List<Produkt> adjustForBaseWordForm(String text) {
//		String[] words=text.split(" ");
//		ArrayList<String> wordsList=new ArrayList<String>();
//		
//		for(String x : words)
//			wordsList.add(x);
//		
//		ProduktDAO produktDao = DaoProvider.getInstance().getProduktDao();
//		
//		return produktDao.getProduktsByVariantNames(wordsList);
//		
//	}

//
//	private Produkt checkShops(String text) {
//		// TODO Auto-generated method stub
//		return null;
//	}

//	 JsonParserFactory factory=JsonParserFactory.getInstance();


	private ArrayList<Produkt> checkShops(String text) {
		if(checkShops){
			JSONObject json = new JSONObject();
			
			json.put(StringHolder.SEARCH4_NAME, text);
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
//
//	private ArrayList<Produkt> adjustForBaseWordForm(String text) {
//		String query=selectProductThroughBaseWord.replaceAll("__v_word__", text);
//		ArrayList<Produkt> results=new ArrayList<Produkt>();
//		ResultSet rs = interfac.runQuery(query);
//
//		try {
//			if(rs.next()){
//				results.add(new Produkt(rs.getString("nazwa"),rs.getString("url")));
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return results;
//
//	}
//
//	private ArrayList<Produkt> checkInDb(String text) {
//		String query=selectSkladnikQ.replaceAll("__nazwa_skladnika__", "%"+text+"%");
//		ArrayList<Produkt> results=new ArrayList<Produkt>();
//		ResultSet rs = interfac.runQuery(query);
//
//		try {
//			while(rs.next()){
//				results.add(new Produkt(rs.getString("nazwa"),rs.getString("url")));
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return results;
//	}

}
