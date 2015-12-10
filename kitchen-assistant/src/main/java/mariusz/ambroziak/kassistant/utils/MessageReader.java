package mariusz.ambroziak.kassistant.utils;

import madkit.message.StringMessage;

import org.json.JSONObject;

public class MessageReader extends MessageUtil {

	public MessageReader(StringMessage m) {
		super();
		this.content = "";
		if(m==null)
			ProblemLogger.logProblem("null message!!!");
		else if(m.getContent().equals("")){
			ProblemLogger.logProblem("Empty content message:id="+m.getConversationID()
					+" sender="+m.getSender()+" receiver="+m.getReceiver());

		}else {
			this.content = m.getContent();
			json=new JSONObject(content);
		}
	}

	public MessageReader(String content) {
		super();

		this.content = "";

		if(content==null||content.equals(""))
			ProblemLogger.logProblem("Empty content message");
		else
		{
			this.content = content;
			json=new JSONObject(content);
		}
	}

	public Object getMessageType(){
		if(json!=null)
			return json.get(StringHolder.MESSAGE_TYPE_NAME);
		else
		{
			ProblemLogger.logProblem("Message type not found.");
			return "";
		}
	}
	
	public Object getProduktUrl(){
		if(json!=null)
			return json.get(StringHolder.PRODUKT_URL_NAME);
		else
		{
			ProblemLogger.logProblem("produkt url not found.");
			return "";
		}
	}
	
}
