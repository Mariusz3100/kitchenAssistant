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
import mariusz.ambroziak.kassistant.exceptions.ShopNotFoundException;
import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.utils.FoodIngredientQuantity;
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
import webscrappers.przepisy.PrzepisyPLQExtract;

public class ReadingAgent extends BaseAgent{

	static ArrayList<ReadingAgent> agents;
	private boolean busy=false;


//	@Autowired
//	private ProduktDAO produktDao;
	
	
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
		
		StringMessage m=(StringMessage) waitNextMessageKA();
		
		
		processMessage(m);
		
		while(true){
			pause(1000);
		}
	}

	private void processMessage(StringMessage m) {
		setBusy(true);
				
		
		
		
		
//		Message messageSentEarlier = getMessageSentEarlier(m.getConversationID());
//		
//		if(messageSentEarlier==null){
//			//TODO message from recipe parser
//
//		}else{
//			//message from shoplist with some results
//			JSONObject message=new JSONObject(m.getContent());
//			
//			
//			
//		}
		
		setBusy(false);
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


	public static List<FoodIngredientQuantity> searchForProdukt(String produktUrl)
			throws AgentSystemNotStartedException, ShopNotFoundException{
		ReadingAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.setBusy(true);
			List<FoodIngredientQuantity> result;
			try{
				result= freeOne.getOrParseFoodIngredients(produktUrl);
			}finally{
				freeOne.setBusy(false);
			}
			return result;
		}
	}
	
	



	private List<FoodIngredientQuantity> getOrParseFoodIngredients(
			String produktUrl) throws ShopNotFoundException, AgentSystemNotStartedException {
		
		Produkt produkt = DaoProvider.getInstance().getProduktDao().getProduktsByURL(produktUrl);
		
		if(produkt==null){
			produkt=scrapProdukt(produktUrl);
			//a mo¿e get produkt i od razu parse?
		}
		
		if(produkt.isPrzetworzony()){
			//TODO retrieve from DB 
		}else{
			
		}
		
		
		return null;
	}

	private Produkt scrapProdukt(String produktUrl) {
		String shortUrl=ShopRecognizer.getShortestWorkingUrl(produktUrl);
		
		
		
		return null;
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

	


}
