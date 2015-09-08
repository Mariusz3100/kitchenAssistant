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






















import webscrappers.AuchanWebScrapper;
import webscrappers.SJPWebScrapper;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.dao.ProduktDAO;
import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.Variant_Word;
import mariusz.ambroziak.kassistant.model.jsp.SearchResult;
import mariusz.ambroziak.kassistant.utils.Converter;
import mariusz.ambroziak.kassistant.utils.StringHolder;

public class RecipeAgent extends BaseAgent{

	static ArrayList<RecipeAgent> agents;
	private boolean busy=false;

	public static final String PARSER_NAME = "recipeParser";

	private static final long serialVersionUID = 1L;
	private static final String selectSkladnikQ = "select * from product where nazwa ilike '__nazwa_skladnika__'";
	private static final String selectBaseWordQ = 
			"select * from base_word  inner join variant_word on variant_word.base_word_id=base_word.base_word_id where v_word='__variant_word__'";

	private static final String selectProductThroughBaseWord = 
			"select * from base_word \n" + 
					"inner join variant_word on variant_word.base_word_id=base_word.base_word_id \n" + 
					"inner join product on product.nazwa ilike '%'||b_word||'%'\n" + 
					"where v_word='__v_word__'";

//	DatabaseInterface interfac;

	public boolean isBusy() {
		return busy;
	}


	@Override
	protected void live() {
//		AgentAddress other = null;
//		
//		while (other == null) {
//			other = getAgentWithRole(StringHolder.AGENT_COMMUNITY, StringHolder.SERVLETS_GROUP,  PARSER_NAME);
//
//		}
//		
//
//		sendMessage(other, new Message());// Sending the message to the agent we found.
//
//		super.live();

		while(true){
			pause(100000);
		}
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
		
		requestRole(AGENT_COMMUNITY, AGENT_GROUP, AGENT_ROLE);
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
		freeOne.busy=true;
		ArrayList<SearchResult> result= freeOne.parseRecipe(url);
		freeOne.busy=false;
		return result;
	}




	public ArrayList<SearchResult> parseRecipe(String url){
		
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
				
				List<Produkt> potencjalneSkladniki = retrieveSkladnik(e.text());
				retValue.add(new SearchResult(e.text(), potencjalneSkladniki));
				
				htmlLog("\n"+e.text()+"->\n");
				
				if(potencjalneSkladniki!=null&&potencjalneSkladniki.size()>0)
					for(Produkt p:potencjalneSkladniki){
						htmlLog(p.getUrl()+"\n");
					}
				else
					htmlLog("Nothing Found\n");
//				if(potencjalneSkladniki.size()==0)
//				{
//					outPage.append("Nothing found\n<br>\n");
//
//				}
				
//				for(Produkt p:potencjalneSkladniki){
//					outPage.append(p.getNazwa()+" \t "+p.getUrl()+"<br>\n");
//				}

			}



		}catch( IOException e){
			e.printStackTrace();
		}



		return retValue;
	}

	private List<Produkt> retrieveSkladnik(String text) {
		System.out.println(text);
		List<Produkt> results;
		ArrayList<String> baseWords = null;
		
		
		
		results=checkInDb(text);

		if(results==null||results.size()<1){
			baseWords=getBaseWords(text);
			
			results=checkInDb(baseWords);
		}

		if(results==null||results.size()<1){
			
			if(baseWords==null)
				baseWords=getBaseWords(text);
			
			results=checkShops(baseWords);
		}
		
		
		if(results==null||results.size()<1){
			results=checkShops(text);
		}


		
		
		return results;


	}


	private List<Produkt> checkShops(ArrayList<String> baseWords) {
		return checkShops(Converter.listToString(baseWords));
	}


	private ArrayList<String> getBaseWords(String text) {
		String[] words=text.split(" ");
		ArrayList<String> wordsList=new ArrayList<String>();
		
		for(String x : words){
			Base_Word base_Name = DaoProvider.getInstance().getVariantWordDao().getBase_Name(x.toLowerCase());
			if(base_Name!=null)
				wordsList.add(base_Name.getB_word());
			else
				wordsList.add(getAndSaveNewRelation(x.toLowerCase()));
		}
		
		return wordsList;
	}


	private String getAndSaveNewRelation(String x) {
		String retValue=SJPWebScrapper.scrapWord(Converter.getOnlyLetters(x));
		retValue=Converter.getOnlyLetters(retValue);
		if(retValue==null)retValue="";
		
		DaoProvider.getInstance().getVariantWordDao().addRelation(x.toLowerCase(),retValue.toLowerCase());
		
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
		
		JSONObject json = new JSONObject();
		
		json.put(StringHolder.SEARCH4_NAME, text);
		
		
		AgentAddress x=getAgentWithRole(StringHolder.AGENT_COMMUNITY, AGENT_GROUP, ShopsListAgent.SHOP_LIST_NAME);

		StringMessage messageToSend = new StringMessage(json.toString());
		sendMessageWithRole(x, messageToSend,PARSER_NAME);
		
		StringMessage response=(StringMessage) waitNextMessage();

		if(response.getContent().equals(""))
				return null;
		else{
			JSONObject jsonObject = new JSONObject(response.getContent());
			
//			Map y= factory.newJsonParser().parseJson(response.getContent());
			ProduktDAO produktDao = DaoProvider.getInstance().getProduktDao();
			
			String ids=jsonObject.getString("ids");
			
			ArrayList<Produkt> retValue=new ArrayList<Produkt>();
			
			for(String id:ids.split(" ")){
				retValue.add(produktDao.getById(Long.parseLong(id)));
			}
			
			return retValue;
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
