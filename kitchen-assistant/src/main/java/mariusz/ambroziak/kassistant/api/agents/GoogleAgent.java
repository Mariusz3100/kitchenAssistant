package mariusz.ambroziak.kassistant.api.agents;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.Apiclients.edaman.ParseableRecipeData;
import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleAuthApiClient;
import mariusz.ambroziak.kassistant.agents.BaseAgent;
import mariusz.ambroziak.kassistant.agents.ProduktAgent;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.GoogleDriveAccessNotAuthorisedException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.shopcom.ShopComApiClient;
import mariusz.ambroziak.kassistant.shopcom.ShopComApiClientParticularProduct;
import mariusz.ambroziak.kassistant.utils.MessageTypes;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;
import webscrappers.auchan.GA_ProduktScrapped;
import webscrappers.auchan.ProduktDetails;

public class GoogleAgent extends BaseAgent {
	public static final String GOOGLE_API_AGENT_NAME = "googleApiAgent";

	static ArrayList<GoogleAgent> agents;
	int tickets=0;
	int lastCheckedClock=0;

	public static boolean agentOn=true;
	private static ArrayList<GA_ProduktScrapped> produktsToScrap;

	public static String baseUrl="https://api.shop.com/stores/v1/products/";

	public GoogleAgent() {
		super();
		AGENT_ROLE = GOOGLE_API_AGENT_NAME;	

		AGENT_COMMUNITY=StringHolder.AGENT_COMMUNITY;
		//		AGENT_GROUP = StringHolder.SCRAPPERS_GROUP;
	}

	@Override
	protected void live() {
		//		super.live();
		StringMessage m;

		while(true){

			m = nextMessageKA();
			if(m!=null)
				processMessage(m);

		}
	}

	private void processMessage(StringMessage m) {
		
		
		if(m!=null){
			String content=((StringMessage)m).getContent();
			JSONObject json=new JSONObject(content);
			
			if(json.get(StringHolder.MESSAGE_TYPE_NAME)==null
					||json.get(StringHolder.MESSAGE_TYPE_NAME).equals("")){
				ProblemLogger.logProblem("Message has no type (in Google agent): "+content);
			}else if(json.get(StringHolder.MESSAGE_TYPE_NAME).equals(MessageTypes.GetLimitations.toString())){
				processgetLimitationsMessage(m);
			}
		}
	}




	private void processgetLimitationsMessage(StringMessage m) {
		String dietLimitationsAsString="";
		String healthLimitationsAsString="";
		try {
			dietLimitationsAsString = GoogleAuthApiClient.getDietLimitationsAsString();
			healthLimitationsAsString = GoogleAuthApiClient.getHealthLimitationsAsString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GoogleDriveAccessNotAuthorisedException e) {
			// TODO Auto-generated catch block
			//this means google account was not connected. We can ignore this.
		}
		
		JSONObject retValue=new JSONObject();
		retValue.put("health",healthLimitationsAsString);
		retValue.put("diet",dietLimitationsAsString);
		
		
		StringMessage messageToSend = new StringMessage(retValue.toString());

		sendReplyWithRoleKA(m, messageToSend, GOOGLE_API_AGENT_NAME);
		
		
	}

	@ResponseBody
	@RequestMapping("/agents/"+GOOGLE_API_AGENT_NAME)
	public String showData(){
		return htmlLogs.toString().replace("\n", "\n<br>");
	}

	@Override
	protected void pause(int milliSeconds) {
		// TODO Auto-generated method stub
		super.pause(milliSeconds);
	}

	@Override
	protected void activate() {

		setLogLevel(Level.FINEST);
		//		setWarningLogLevel(Level.FINEST);

		if(!isGroup(AGENT_COMMUNITY,AGENT_GROUP)){
			createGroup(AGENT_COMMUNITY,AGENT_GROUP);
		}
		if(agents==null)agents=new ArrayList<GoogleAgent>();
		agents.add(this);
		
		requestRole(AGENT_COMMUNITY,AGENT_GROUP, AGENT_ROLE);
		super.activate();
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		super.end();
	}

}
