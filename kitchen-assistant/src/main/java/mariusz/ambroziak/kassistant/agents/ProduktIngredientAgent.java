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

public class ProduktIngredientAgent extends BaseAgent{

	static ArrayList<ProduktIngredientAgent> agents;
	private boolean busy=false;


//	@Autowired
//	private ProduktDAO produktDao;
	
	
	public static final String PARSER_NAME = "foodIngredientParser";

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
		

		if(agents==null)agents=new ArrayList<ProduktIngredientAgent>();
		agents.add(this);


		
		setLogLevel(Level.FINEST);
		super.activate();
	}

	public ProduktIngredientAgent() {
		super();
		AGENT_COMMUNITY=StringHolder.AGENT_COMMUNITY;
	
	}


	@Override
	protected void end() {
		super.end();
	}


	public static ProduktWithAllIngredients parseFoodIngredient(String name)
			throws AgentSystemNotStartedException, ShopNotFoundException, Page404Exception{
		ProduktIngredientAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.setBusy(true);
			ProduktWithAllIngredients result;
			try{
				result= freeOne.getOrParseFoodIngredient(name);
			}finally{
				freeOne.setBusy(false);
			}
			return result;
		}
	}
	
	





	private ProduktWithAllIngredients getOrParseFoodIngredient(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	private static ProduktIngredientAgent getFreeAgent() throws AgentSystemNotStartedException {
		ProduktIngredientAgent freeOne=null;
		
		if(agents==null){
			System.out.println("Agent system not started");
			throw new AgentSystemNotStartedException();
		}
		else{
			while(freeOne==null){
				for(ProduktIngredientAgent ra:agents){
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
