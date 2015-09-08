package mariusz.ambroziak.kassistant.agents;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.kernel.AbstractAgent.ReturnCode;
import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.utils.StringHolder;

@Controller
public abstract class BaseAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String AGENT_GROUP="group";

	public String AGENT_COMMUNITY = StringHolder.AGENT_COMMUNITY;

	public String AGENT_ROLE;

	private static HashMap<String,BaseAgent> extent;
	
	public static HashMap<String, BaseAgent> getExtent() {
		return extent;
	}

	//	private static String AgentName;
	protected StringBuilder htmlLogs;
	
	public BaseAgent(){
		htmlLogs=new StringBuilder();
		addNewAgent(this);
	}
	
	
	private void addNewAgent(BaseAgent ba){
		if(extent==null)
			extent=new HashMap<String, BaseAgent>();
		
		String key=ba.getClass().getSimpleName();
		
		int keyCount=0;
		while(extent.containsKey(key)){
			++keyCount;
			key=ba.getClass().getSimpleName()+keyCount;
		}
		
		extent.put(key, ba);
	}
	
	@Override
	public StringMessage nextMessage() {
		
		StringMessage sm=(StringMessage) super.nextMessage();
		
		if(sm!=null){
			String data="Agent \""+this.toString()+"\" received message:\n ";
			
			
			data+=sm.getContent()+" \n";
			
			data+="at "+ClockAgent.getTimePassed()+"}\n\n";
			
			htmlLog(data);
		}
		return sm;
	}
	
	public void htmlLog(String data){
		htmlLogs.append(data);
	}
	
	public String getHtmlLog(){
		return htmlLogs.toString();
	}


	@Override
	public ReturnCode sendMessageWithRole(final AgentAddress receiver, final Message message, final String senderRole) {
		String data="{Agent \""+this.toString()+"\" sent message:\n ";
		
		data+=((StringMessage)message).getContent()+" \n";
		data+="to agent \""+receiver.toString()+"\"\n ";

		data+="at "+ClockAgent.getTimePassed()+"}\n\n";
		
		htmlLog(data);
		
		return super.sendMessageWithRole(receiver,message,senderRole);	
	}
	@Override
	public String toString() {
		return super.toString()+
				" \n[name="+": "+getName()+", "
				+ "AGENT_COMMUNITY:" + AGENT_COMMUNITY + ", "
				+ "AGENT_GROUP:" + AGENT_GROUP + ", "
				+ "AGENT_ROLE="+ AGENT_ROLE + "]";
	}
	
	
	
}
