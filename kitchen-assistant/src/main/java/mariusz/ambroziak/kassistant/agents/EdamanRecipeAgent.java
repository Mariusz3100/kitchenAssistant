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
import mariusz.ambroziak.kassistant.model.jsp.SearchResult;
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
	
	public static ParseableRecipeData parseSingleRecipe(String urlId) throws AgentSystemNotStartedException, Page404Exception{
		EdamanRecipeAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.setBusy(true);
			ParseableRecipeData result;
			try{
				result= freeOne.parseRecipeOrRetrieveFromDb(urlId);
			}finally{
				freeOne.setBusy(false);
			}
			return result;
		}
	}

	
	private ParseableRecipeData parseRecipeOrRetrieveFromDb(String urlId) {
		// TODO Auto-generated method stub
		return null;
	}


	public static ArrayList<ParseableRecipeData> searchForRecipes(String searchPhrase) throws AgentSystemNotStartedException, Page404Exception{
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
