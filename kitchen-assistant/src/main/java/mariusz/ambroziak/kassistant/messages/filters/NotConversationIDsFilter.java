package mariusz.ambroziak.kassistant.messages.filters;

import java.util.ArrayList;

import madkit.kernel.ConversationID;
import madkit.kernel.Message;
import madkit.message.MessageFilter;

public class NotConversationIDsFilter implements MessageFilter{

	private ArrayList<ConversationID> ids;
	
	
	
	public NotConversationIDsFilter(){
		ids=new ArrayList<ConversationID>();
	}
	
	
	public void addIdToBeOmmitted(ConversationID id){
		ids.add(id);
	}
	
	
	@Override
	public boolean accept(Message m) {
		System.out.println();
		return !ids.contains(m.getConversationID());
		
	}

}
