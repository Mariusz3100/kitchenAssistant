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

	//	private static Map<String,AgentAddress> shopUrlMap;


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

			m=waitNextMessageKA();


			if(m.getSender().getRole().equals("manager")){
				processDamnManagerMessage(m);
			}

			String content=((StringMessage)m).getContent();
			JSONObject json=new JSONObject(content);

			if(json.get(StringHolder.MESSAGE_TYPE_NAME)==null
					||json.get(StringHolder.MESSAGE_TYPE_NAME).equals("")){
				ProblemLogger.logProblem("Message has no type: "+content);
			}else if(json.get(StringHolder.MESSAGE_TYPE_NAME).equals(MessageTypes.SearchFor.toString())){
				if(!m.getSender().getRole().equals(RecipeAgent.PARSER_NAME)
						&&!m.getSender().getRole().equals(ProduktAgent.PARSER_NAME)){
					ProblemLogger.logProblem("Received searchFor request not from parser");	
				}
				AgentAddress x=getAgentWithRole(AGENT_COMMUNITY, StringHolder.SCRAPPERS_GROUP, AuchanAgent.AUCHAN_WEB_SCRAPPER_NAME);
				StringMessage newM=new StringMessage(((StringMessage)m).getContent());
				addMessagesRelation((StringMessage) m,newM);
				sendMessageWithRole(x, newM,ShopsListAgent.SHOP_LIST_NAME);





			}else if(json.get(StringHolder.MESSAGE_TYPE_NAME).equals(MessageTypes.SearchForResponse.toString())){
				if(!m.getSender().getRole().equals(AuchanAgent.AUCHAN_WEB_SCRAPPER_NAME)){
					ProblemLogger.logProblem("Received searchForResponse Message not from shop agent");

				}
				StringMessage newM=new StringMessage(((StringMessage)m).getContent());
				StringMessage originalOne=getOriginalMessage(m.getConversationID());

				
				sendReply(originalOne, newM);//(x, newM,ShopsListAgent.SHOP_LIST_NAME);


			}else if(json.get(StringHolder.MESSAGE_TYPE_NAME).equals(MessageTypes.GetProduktData.toString())){
				AgentAddress x=getAgentWithRole(AGENT_COMMUNITY, StringHolder.SCRAPPERS_GROUP, AuchanAgent.AUCHAN_WEB_SCRAPPER_NAME);
				StringMessage newM=new StringMessage(((StringMessage)m).getContent());
				addMessagesRelation((StringMessage) m,newM);

				sendMessageWithRoleKA(x, newM,ShopsListAgent.SHOP_LIST_NAME);
			}else if(json.get(StringHolder.MESSAGE_TYPE_NAME).equals(MessageTypes.GetProduktDataResponse.toString())){
				//				AgentAddress x=getAgentWithRole(AGENT_COMMUNITY, StringHolder.SCRAPPERS_GROUP, AuchanAgent.AUCHAN_WEB_SCRAPPER_NAME);
				StringMessage originalOne=getOriginalMessage(m.getConversationID());
				StringMessage newM=new StringMessage(((StringMessage)m).getContent());
				sendReply(originalOne, newM);//(x, newM,ShopsListAgent.SHOP_LIST_NAME);

				//				StringMessage newM=new StringMessage(((StringMessage)m).getContent());
				//				sendMessageWithRole(x, newM,ShopsListAgent.SHOP_LIST_NAME);
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


		//	setUpData();




	}

	//	private void setUpData() {
	//		setUpUrlMap();
	//
	//	}

	//	public void setUpUrlMap() {
	//		if(shopUrlMap==null){
	//			shopUrlMap=new HashMap<String, AgentAddress>();
	//		}
	//		
	//		AgentAddress x=getAgentWithRole(AGENT_COMMUNITY, StringHolder.SCRAPPERS_GROUP, AuchanAgent.AUCHAN_WEB_SCRAPPER_NAME);
	//
	//		shopUrlMap.put(AuchanAgent.acceptedURL,x);
	//	}

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
