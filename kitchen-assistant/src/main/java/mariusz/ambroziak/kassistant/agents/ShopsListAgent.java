package mariusz.ambroziak.kassistant.agents;

import java.util.ArrayList;
import java.util.logging.Level;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.utils.StringHolder;


public class ShopsListAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String SHOP_LIST_NAME = "shopsList";

	ArrayList<AgentAddress> adresses;
	
	public static ShopsListAgent instance;
	
	@Override
	protected void live() {

		Message m=null;

		while(true){
			
			m=waitNextMessage();
				
			if(m.getSender().getRole().equals("manager")){
				AgentAddress x=getAgentWithRole(StringHolder.AGENT_COMMUNITY, StringHolder.SCRAPPERS_GROUP, AuchanAgent.AUCHAN_WEB_SCRAPPER_NAME);
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
				AgentAddress x=getAgentWithRole(StringHolder.AGENT_COMMUNITY, StringHolder.SCRAPPERS_GROUP, AuchanAgent.AUCHAN_WEB_SCRAPPER_NAME);
				StringMessage newM=new StringMessage(((StringMessage)m).getContent());
				sendMessageWithRole(x, newM,ShopsListAgent.SHOP_LIST_NAME);

			}

			if(m.getSender().getRole().equals(AuchanAgent.AUCHAN_WEB_SCRAPPER_NAME)){
				AgentAddress x=getAgentWithRole(StringHolder.AGENT_COMMUNITY, StringHolder.SCRAPPERS_GROUP, RecipeAgent.PARSER_NAME);
				StringMessage newM=new StringMessage(((StringMessage)m).getContent());

				sendMessageWithRole(x, newM,SHOP_LIST_NAME);

			}

		}

	}

	@Override
	protected void activate() {
		// TODO Auto-generated method stub
		super.activate();
		
		if(!isGroup(StringHolder.AGENT_COMMUNITY,StringHolder.SCRAPPERS_GROUP)){
			createGroup(StringHolder.AGENT_COMMUNITY,StringHolder.SCRAPPERS_GROUP);
		}
		
		requestRole(StringHolder.AGENT_COMMUNITY, StringHolder.SCRAPPERS_GROUP, ShopsListAgent.SHOP_LIST_NAME);
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
