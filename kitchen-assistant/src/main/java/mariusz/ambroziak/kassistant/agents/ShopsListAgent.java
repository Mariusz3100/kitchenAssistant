package mariusz.ambroziak.kassistant.agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.json.JSONObject;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.utils.MessageTypes;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;


public class ShopsListAgent extends BaseAgent {

//	public static final String GUIDANCE_GROUP = StringHolder.GUIDANCE_GROUP;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String SHOP_LIST_NAME = "shopsList";

	ArrayList<AgentAddress> adresses;
	
	private static Map<String,AgentAddress> shopUrlMap;
	
	
	public ShopsListAgent() {
	super();
	
	AGENT_COMMUNITY=StringHolder.AGENT_COMMUNITY;
//	AGENT_GROUP = StringHolder.GUIDANCE_GROUP;
	AGENT_ROLE=SHOP_LIST_NAME;
}

	public static ShopsListAgent instance;
	
	@Override
	protected void live() {

		Message m=null;

		while(true){
			
			m=waitNextMessage();
				
			String content=((StringMessage)m).getContent();
			JSONObject json=new JSONObject(content);
			
			if(json.get(StringHolder.MESSAGE_TYPE_NAME)==null
					||json.get(StringHolder.MESSAGE_TYPE_NAME).equals("")){
				ProblemLogger.logProblem("Message has no type: "+content);
			}else if(json.get(StringHolder.MESSAGE_TYPE_NAME).equals(MessageTypes.SearchFor)){
		
				if(m.getSender().getRole().equals("manager")){
					processDamnManagerMessage(m);
				}
	
				if(m.getSender().getRole().equals(RecipeAgent.PARSER_NAME)){
					AgentAddress x=getAgentWithRole(AGENT_COMMUNITY, StringHolder.SCRAPPERS_GROUP, AuchanAgent.AUCHAN_WEB_SCRAPPER_NAME);
					StringMessage newM=new StringMessage(((StringMessage)m).getContent());
					sendMessageWithRole(x, newM,ShopsListAgent.SHOP_LIST_NAME);
				}
	
				if(m.getSender().getRole().equals(AuchanAgent.AUCHAN_WEB_SCRAPPER_NAME)){
					AgentAddress x=getAgentWithRole(AGENT_COMMUNITY, StringHolder.SCRAPPERS_GROUP, RecipeAgent.PARSER_NAME);
					StringMessage newM=new StringMessage(((StringMessage)m).getContent());
	
					sendMessageWithRole(x, newM,SHOP_LIST_NAME);
	
				}
				
			}else if(json.get(StringHolder.MESSAGE_TYPE_NAME).equals(MessageTypes.GetProduktData)){
				
			}

		}

	}

	public void processDamnManagerMessage(Message m) {
		String content=((StringMessage)m).getContent();
		JSONObject json=new JSONObject(content);

		if(RecipeAgent.PARSER_NAME.equals(json.get(StringHolder.MESSAGE_CREATOR_NAME))){
			AgentAddress x=getAgentWithRole(AGENT_COMMUNITY, StringHolder.SCRAPPERS_GROUP, AuchanAgent.AUCHAN_WEB_SCRAPPER_NAME);
			StringMessage newM=new StringMessage(((StringMessage)m).getContent());
			sendMessageWithRole(x, newM,ShopsListAgent.SHOP_LIST_NAME);

		}else{
			ProblemLogger.logProblem("Message was received and cannot be properly handled:"+content );
		}
	}

	@Override
	protected void activate() {
		// TODO Auto-generated method stub
		super.activate();
		
		if(!isGroup(AGENT_COMMUNITY,AGENT_GROUP)){
			createGroup(AGENT_COMMUNITY,AGENT_GROUP);
		}
		
		requestRole(AGENT_COMMUNITY, AGENT_GROUP, AGENT_ROLE);
		setLogLevel(Level.FINEST);

		
		if(instance==null)instance=this;
//		adresses=new ArrayList<AgentAddress>();
//		adresses.add(
//				getAgentWithRole(StringHolder.AGENT_COMMUNITY, StringHolder.SCRAPPERS_GROUP, "auchanWebScrapper"));
	
	
	setUpData();
	
	
	
	
	}

	private void setUpData() {
		setUpUrlMap();

	}

	public void setUpUrlMap() {
		if(shopUrlMap==null){
			shopUrlMap=new HashMap<String, AgentAddress>();
		}
		
		AgentAddress x=getAgentWithRole(AGENT_COMMUNITY, StringHolder.SCRAPPERS_GROUP, AuchanAgent.AUCHAN_WEB_SCRAPPER_NAME);

		shopUrlMap.put(AuchanAgent.acceptedURL,x);
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		super.end();
	}

	
//	public static void main(String[] arg){
//		AgentAddress x=getAgentWithRole(StringHolder.AGENT_COMMUNITY, StringHolder.SCRAPPERS_GROUP, AuchanAgent.AUCHAN_WEB_SCRAPPER_NAME);
//		StringMessage newM=new StringMessage(((StringMessage)m).getContent());
//		sendMessage(x, newM);
//	}
}
