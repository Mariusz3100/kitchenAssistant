package mariusz.ambroziak.kassistant.utils;

import org.json.JSONObject;

public class MessageWriter extends MessageUtil {

	public MessageWriter() {
		super();
		content="";
		json=new JSONObject(content);
		
	}

	public void setMessageType(MessageTypes type){
		json.put(StringHolder.MESSAGE_TYPE_NAME, type);

	}

}
