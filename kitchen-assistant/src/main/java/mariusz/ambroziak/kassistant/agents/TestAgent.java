package mariusz.ambroziak.kassistant.agents;

import java.util.ArrayList;
import java.util.logging.Level;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.utils.StringHolder;


public class TestAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TestAgent_NAME = "test";

	ArrayList<AgentAddress> adresses;
	
	public static TestAgent instance;
	
	@Override
	protected void live() {

		Message m=null;

		while(true){
			
			m=waitNextMessage();

		}

	}

	@Override
	protected void activate() {
		// TODO Auto-generated method stub
		super.activate();
	//	createGroup(StringHolder.AGENT_COMMUNITY,StringHolder.SCRAPPERS_GROUP);// Create the group GroupTest in the community communication.

		requestRole(StringHolder.AGENT_COMMUNITY, StringHolder.SCRAPPERS_GROUP, TestAgent.TestAgent_NAME);
		setLogLevel(Level.FINEST);

		instance=this;
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
	
	public String testMessage(){
		AgentAddress x=getAgentWithRole(StringHolder.AGENT_COMMUNITY, StringHolder.SCRAPPERS_GROUP, ShopsListAgent.SHOP_LIST_NAME);
		
		sendMessage(x, new StringMessage("testing message"));
		
		return "success";
	}
	
	
	public static void main(String args[]){
		System.out.println(TestAgent.instance.testMessage());
	}
}
