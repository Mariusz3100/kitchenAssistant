package mariusz.ambroziak.kassistant.agents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.QuantityExtractor.AuchanQExtract;
import mariusz.ambroziak.kassistant.dao.Base_WordDAOImpl;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.dao.ProduktDAO;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.exceptions.ShopNotFoundException;
import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.utils.ProduktIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithAllIngredients;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithBasicIngredients;
import mariusz.ambroziak.kassistant.model.utils.QuantityProdukt;
import mariusz.ambroziak.kassistant.shops.ShopRecognizer;
import mariusz.ambroziak.kassistant.utils.MessageTypes;
import mariusz.ambroziak.kassistant.utils.ParameterHolder;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;

import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;

//import com.codesnippets4all.json.parsers.JsonParserFactory;















import webscrappers.SJPWebScrapper;
import webscrappers.auchan.AuchanAbstractScrapper;
import webscrappers.auchan.AuchanRecipeParser;
import webscrappers.przepisy.PrzepisyPLQExtract;

public class ReadingAgent extends BaseAgent{

	static ArrayList<ReadingAgent> agents;
	private boolean busy=false;

	public static final String PARSER_NAME = "labelParser";

	private static final long serialVersionUID = 1L;
	public  static final boolean checkShops = true;


	public void setBusy(boolean busy) {
		this.busy = busy;
	}
	
	public boolean isBusy() {
		return busy;
	}


	@Override
	protected void live() {
				
		while(true){
			pause(1000);
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
		
		requestRole(AGENT_COMMUNITY, AGENT_GROUP, PARSER_NAME);
		

		if(agents==null)agents=new ArrayList<ReadingAgent>();
		agents.add(this);
		
		setLogLevel(Level.FINEST);
		super.activate();
	}

	public ReadingAgent() {
		super();
		AGENT_COMMUNITY=StringHolder.AGENT_COMMUNITY;
	
	}


	@Override
	protected void end() {
		super.end();
	}


	public static ProduktWithAllIngredients parseFullSklad(String shortUrl)
			throws AgentSystemNotStartedException, ShopNotFoundException, Page404Exception{
		ReadingAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.setBusy(true);
			ProduktWithAllIngredients result;
			try{
				result= freeOne.getOrParseAllFoodIngredients(shortUrl);
			}finally{
				freeOne.setBusy(false);
			}
			return result;
		}
	}

	private ProduktWithAllIngredients getOrParseAllFoodIngredients(String shortUrl)
			throws ShopNotFoundException, AgentSystemNotStartedException, Page404Exception {
		
		ProduktWithAllIngredients produktWithIngredinets = AuchanRecipeParser.getAllIngredients(shortUrl);
				
		return produktWithIngredinets;
	}


	private static ReadingAgent getFreeAgent() throws AgentSystemNotStartedException {
		ReadingAgent freeOne=null;
		
		if(agents==null){
			System.out.println("Agent system not started");
			throw new AgentSystemNotStartedException();
		}
		else{
			while(freeOne==null){
				for(ReadingAgent ra:agents){
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

	public static ProduktWithBasicIngredients parseBasicSklad(String shortUrl)
			throws AgentSystemNotStartedException, ShopNotFoundException, Page404Exception {
		ReadingAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.setBusy(true);
			ProduktWithBasicIngredients result;
			try{
				result= freeOne.getOrParseBasicFoodIngredients(shortUrl);
			}finally{
				freeOne.setBusy(false);
			}
			return result;
		}
	}

	private ProduktWithBasicIngredients getOrParseBasicFoodIngredients(String shortUrl) throws Page404Exception {

		return  AuchanRecipeParser.getBasics(shortUrl);
	}

	


}
