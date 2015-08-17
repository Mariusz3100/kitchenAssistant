package mariusz.ambroziak.kassistant.agents;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import madkit.kernel.Agent;
import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.utils.StringHolder;

@Controller
public abstract class BaseAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String AGENT_GROUP;

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
			key=ba.getClass().getName()+keyCount;
		}
		
		extent.put(key, ba);
	}
	
	@Override
	public StringMessage nextMessage() {
		
		StringMessage sm=(StringMessage) super.nextMessage();
		
		String data="Agent "+this.toString()+" "+getName()+
				" [role:"+AGENT_ROLE+",group:"+AGENT_GROUP+",community"+AGENT_COMMUNITY+"] received message:\n ";
		
		
		data+=sm.getContent()+" \n";
		
		data+="at "+ClockAgent.getTimePassed();
		
		htmlLog(data);
		
		return sm;
	}
	
	public void htmlLog(String data){
		htmlLogs.append(data);
	}
	
	public String getHtmlLog(){
		return htmlLogs.toString();
	}


	@Override
	public String toString() {
		return super.toString()+
				" \n["
				+ "AGENT_COMMUNITY:" + AGENT_COMMUNITY + ", "
				+ "AGENT_GROUP:" + AGENT_GROUP + ", "
				+ "AGENT_ROLE="+ AGENT_ROLE + "]";
	}
	
	
	
}
