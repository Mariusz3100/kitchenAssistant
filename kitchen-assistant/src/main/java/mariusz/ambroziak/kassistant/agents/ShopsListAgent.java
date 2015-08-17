package mariusz.ambroziak.kassistant.agents;

import java.util.ArrayList;
import java.util.logging.Level;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.utils.StringHolder;


public class ShopsListAgent extends BaseAgent {

	private static final String GUIDANCE_GROUP = StringHolder.GUIDANCE_GROUP;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String SHOP_LIST_NAME = "shopsList";

ArrayList<AgentAddress> adresses;
	
	public ShopsListAgent() {
	super();
	
	AGENT_COMMUNITY=StringHolder.AGENT_COMMUNITY;
	AGENT_GROUP = StringHolder.GUIDANCE_GROUP;
	AGENT_ROLE=SHOP_LIST_NAME;
}

	public static ShopsListAgent instance;
	
	@Override
	protected void live() {

		Message m=null;

		while(true){
			
			m=waitNextMessage();
				
			if(m.getSender().getRole().equals("manager")){
				AgentAddress x=getAgentWithRole(AGENT_COMMUNITY, StringHolder.SCRAPPERS_GROUP, AuchanAgent.AUCHAN_WEB_SCRAPPER_NAME);
				StringMessage newM=new StringMessage(((StringMessage)m).getContent());
				sendMessageWithRole(x, newM,ShopsListAgent.SHOP_LIST_NAME);
//				this.getExistingRoles(
//						this.getExistingCommunities().first()
//						, 
//				this.getExistingGroups(
//				this.getExistingCommunities().first()
//				).first());
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

		}

	}

	@Override
	protected void activate() {
		// TODO Auto-generated method stub
		super.activate();
		
		if(!isGroup(AGENT_COMMUNITY,GUIDANCE_GROUP)){
			createGroup(AGENT_COMMUNITY,GUIDANCE_GROUP);
		}
		
		requestRole(AGENT_COMMUNITY, GUIDANCE_GROUP, AGENT_ROLE);
		setLogLevel(Level.FINEST);

		
		if(instance==null)instance=this;
//		adresses=new ArrayList<AgentAddress>();
//		adresses.add(
//				getAgentWithRole(StringHolder.AGENT_COMMUNITY, StringHolder.SCRAPPERS_GROUP, "auchanWebScrapper"));
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
