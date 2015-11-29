package mariusz.ambroziak.kassistant.agents;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.ConversationID;
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



	
	public Message sendMessageWithRoleAndWaitForReplyKA(AgentAddress receiver,
			Message messageToSend, String senderRole) {
	
		String data="{Agent \""+this.toString()+"\" sent message:\n ";

		data+="id:"+messageToSend.getConversationID()+" content:<"+((StringMessage)messageToSend).getContent()+">\n";
		data+="to agent \""+receiver.toString()+"\"\n ";
					
		data+="at "+ClockAgent.getTimePassed()+"}\n\n";

		htmlLog(data);
		
		return super.sendMessageWithRoleAndWaitForReply(receiver, messageToSend,
				senderRole);
	}



	
	public Message sendReplyWithRoleAndWaitForReplyKA(Message messageToReplyTo,
			Message reply, String senderRole, Integer timeOutMilliSeconds) {
		// TODO Auto-generated method stub
		
		String data="{Agent \""+this.toString()+"\" sent message:\n ";

		data+="id:"+reply.getConversationID()+" content:<"+((StringMessage)reply).getContent()+">\n";
		data+="as a reply to message: id:"+reply.getConversationID()+" content:<"+((StringMessage)reply).getContent()+">\n ";
		if(timeOutMilliSeconds!=null)
			data+=" with timeout "+timeOutMilliSeconds+" ";
					
		data+="at "+ClockAgent.getTimePassed()+"}\n\n";

		htmlLog(data);
		
		return super.sendReplyWithRoleAndWaitForReply(messageToReplyTo, reply,
				senderRole, timeOutMilliSeconds);
	}

	public String AGENT_ROLE;

	private static HashMap<String,BaseAgent> extent;
	

	public static HashMap<String, BaseAgent> getExtent() {
		return extent;
	}

//	protected Map<Integer, StringMessage> messages;
	
	//	private static String AgentName;
	protected StringBuilder htmlLogs;

	public BaseAgent(){
		htmlLogs=new StringBuilder();
		addNewAgent(this);
	}

	private HashMap<ConversationID,Message> messagesSent;
	
	protected void addMessageToList(Message m){
		if(messagesSent==null)
			messagesSent=new HashMap<ConversationID,Message>();
		
		messagesSent.put(m.getConversationID(),m);
	}
	
	protected Message getMessageSentEarlier(ConversationID id){
		return messagesSent.get(id);
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

	

	
	public Message waitNextMessageKA() {
		Message m=super.waitNextMessage();
		
		if(m.getSender().getRole().equals("manager"))
			System.out.println("ALERT!!!!");
			
		StringMessage sm=(StringMessage) m;

		if(sm!=null){
			String data="Agent \""+this.toString()+"\" received message:\n ";


			data+="<<"+sm.getContent()+">>\n";

			data+="at "+ClockAgent.getTimePassed()+"}\n\n";

			htmlLog(data);
		}
		return sm;



	}


	
	public StringMessage nextMessageKA() {

		StringMessage sm=(StringMessage) super.nextMessage();

		
		
		if(sm!=null){
			if(sm.getSender().getRole().equals("manager"))
				System.out.println("ALERT!!!!");
			
			
			String data="Agent \""+this.toString()+"\" received message:\n ";


			data+="id:"+sm.getConversationID()+" content:<"+sm.getContent()+">\n";

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


	
	public ReturnCode sendMessageWithRoleKA(final AgentAddress receiver, final Message message, final String senderRole) {
		addMessageToList(message);
		
		String data="{Agent \""+this.toString()+"\" sent message:\n ";

		data+="<"+((StringMessage)message).getContent()+">\n";
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
