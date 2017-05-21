package mariusz.ambroziak.kassistant.agents;

import java.util.HashMap;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.ConversationID;
import madkit.kernel.Message;
import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.messages.filters.NotConversationIDsFilter;
import mariusz.ambroziak.kassistant.utils.StringHolder;

import org.springframework.stereotype.Controller;

@Controller
public abstract class BaseAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String AGENT_GROUP="group";

	public String AGENT_COMMUNITY = StringHolder.AGENT_COMMUNITY;

	protected NotConversationIDsFilter filterOfExpectedMessages;

	
	
	
	public Message sendMessageWithRoleAndWaitForReplyKA(AgentAddress receiver,
			Message messageToSend, String senderRole) {
//		addMessageToList(messageToSend);

		filterOfExpectedMessages.addIdToBeOmmitted(messageToSend.getConversationID());
		
		String data="{Agent \""+this.toString()+"\" sent message:\n ";

		data+="id:"+messageToSend.getConversationID()+" content:<"+((StringMessage)messageToSend).getContent()+">\n";
		data+="to agent \""+receiver.toString()+"\"\n ";
					
		data+="at "+ClockAgent.getTimePassed()+"}\n\n";

		htmlLog(data);
		
		return super.sendMessageWithRoleAndWaitForReply(receiver, messageToSend,
				senderRole);
	}


	public ReturnCode sendReplyWithRoleKA(Message messageToReplyTo,
			Message reply, String senderRole) {
//		addMessageToList(reply);
		String data="{Agent \""+this.toString()+"\" sent message:\n ";

		data+="id:"+reply.getConversationID()+" content:<"+((StringMessage)reply).getContent()+">\n";
		data+="as a reply to message: id:"+reply.getConversationID()+" content:<"+((StringMessage)reply).getContent()+">\n ";
					
		data+="at "+ClockAgent.getTimePassed()+"}\n\n";

		htmlLog(data);
		
		return super.sendReplyWithRole(messageToReplyTo, reply,
				senderRole);
	}
	
	public Message sendReplyWithRoleAndWaitForReplyKA(Message messageToReplyTo,
			Message reply, String senderRole, Integer timeOutMilliSeconds) {
//		addMessageToList(reply);
		filterOfExpectedMessages.addIdToBeOmmitted(reply.getConversationID());
		
		String data="{Agent \""+this.toString()+"\" sent message:\n ";

		data+="id:"+reply.getConversationID()+" content:<"+((StringMessage)reply).getContent()+">\n";
		data+="as a reply to message: id:"+reply.getConversationID()+" content:<"+((StringMessage)reply).getContent()+">\n ";
		if(timeOutMilliSeconds!=null)
			data+=" with timeout "+timeOutMilliSeconds+" ";
					
		data+="at "+ClockAgent.getTimePassed()+", waits for reply}\n\n";

		htmlLog(data);
		
		return super.sendReplyWithRoleAndWaitForReply(messageToReplyTo, reply,
				senderRole, timeOutMilliSeconds);
	}

	public Message sendReplyWithRoleAndWaitForReplyKA(Message messageToReplyTo,
			Message reply, String senderRole) {
//		addMessageToList(reply);
		filterOfExpectedMessages.addIdToBeOmmitted(reply.getConversationID());

		String data="{Agent \""+this.toString()+"\" sent message:\n ";

		data+="id:"+reply.getConversationID()+" content:<"+((StringMessage)reply).getContent()+">\n";
		data+="as a reply to message: id:"+reply.getConversationID()+" content:<"+((StringMessage)reply).getContent()+">\n ";

					
		data+="at "+ClockAgent.getTimePassed()+", waits for reply}\n\n";

		htmlLog(data);
		
		return super.sendReplyWithRoleAndWaitForReply(messageToReplyTo, reply,
				senderRole);
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
		filterOfExpectedMessages=new NotConversationIDsFilter();
	}

	private HashMap<ConversationID,StringMessage> messagesSent;
	private HashMap<ConversationID,StringMessage> messagesReceived;
	private HashMap<ConversationID,ConversationID> mesageRelations;

	private boolean busy = false;
	
	
	protected void addMessageSentToList(StringMessage m){
		if(messagesSent==null)
			messagesSent=new HashMap<ConversationID,StringMessage>();
		
		messagesSent.put(m.getConversationID(),m);
	}
	
	protected Message getMessageSentEarlier(ConversationID id){
		if(messagesSent==null)
			return null;
		return messagesSent.get(id);
	}
	
	protected void addMessageReceivedToList(StringMessage m){
		if(messagesReceived==null)
			messagesReceived=new HashMap<ConversationID,StringMessage>();
		
		messagesReceived.put(m.getConversationID(),m);
	}
	
	protected Message getMessageREceivedEarlier(ConversationID id){
		if(messagesReceived==null)
			return null;
		return messagesReceived.get(id);
	}
	
	
	protected void addMessagesRelation(StringMessage originalMessage,
			StringMessage followingMessage){
		if(messagesSent==null)
			messagesSent=new HashMap<ConversationID,StringMessage>();
		
		if(mesageRelations==null)
			mesageRelations=new HashMap<ConversationID,ConversationID>();
		
		if(messagesReceived==null)
			messagesReceived=new HashMap<ConversationID,StringMessage>();
		
		mesageRelations.put(followingMessage.getConversationID(),originalMessage.getConversationID());
		
		messagesReceived.put(originalMessage.getConversationID(), originalMessage);
		messagesSent.put(followingMessage.getConversationID(), followingMessage);
		
	}
	
	protected StringMessage getOriginalMessage(ConversationID followingMessageId){
		ConversationID originalId=mesageRelations.get(followingMessageId);
		
		return messagesReceived.get(originalId);
		
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
		Message m=super.waitNextMessage(this.filterOfExpectedMessages);
		
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

		StringMessage sm=(StringMessage) super.nextMessage(filterOfExpectedMessages);

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
//		addMessageToList(message);
		
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


	public void setBusy(boolean busy) {
		this.busy = busy;
	}


	public boolean isBusy() {
		return busy;
	}



}
