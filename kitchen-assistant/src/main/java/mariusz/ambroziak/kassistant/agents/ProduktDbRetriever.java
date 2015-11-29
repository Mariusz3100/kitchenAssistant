package mariusz.ambroziak.kassistant.agents;

import java.util.ArrayList;
import java.util.Collection;

import madkit.kernel.AgentAddress;
import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.dao.ProduktDAO;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.jsp.SearchResult;
import mariusz.ambroziak.kassistant.utils.MessageCounter;
import mariusz.ambroziak.kassistant.utils.MessageTypes;
import mariusz.ambroziak.kassistant.utils.StringHolder;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

public class ProduktDbRetriever {
	@Autowired
	private ProduktDAO produktDao;

	
	private ArrayList<Produkt> checkInDb(Collection<String> texts) {
		ArrayList<Produkt> retValue=new ArrayList<Produkt>();

		retValue.addAll(
				DaoProvider.getInstance()
				.getProduktDao()
				.getProduktsByNames(texts));



		return retValue;
	}

	private ArrayList<Produkt> checkInDb(String text) {
		ArrayList<Produkt> retValue=new ArrayList<Produkt>();

		retValue.addAll(
				DaoProvider.getInstance()
				.getProduktDao()
				.getProduktsBySpacedName(text));



		return retValue;
	}

	private ArrayList<SearchResult> getFromDbProduktbyUrl(String produktUrl) {
		//do uzupe³nienia sprawdzaniem z db
		
		
		produktDao.getProduktsByURL(produktUrl);
		
		
//		if(checkShops){
//			JSONObject json = new JSONObject();
//			
//			json.put(StringHolder.PRODUKT_URL_NAME, produktUrl);
//			json.put(StringHolder.MESSAGE_CREATOR_NAME, PARSER_NAME);
//			json.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.GetProduktData);
//			json.put(StringHolder.MESSAGE_ID_NAME, MessageCounter.getCount());
//			
//			AgentAddress x=getAgentWithRole(StringHolder.AGENT_COMMUNITY, AGENT_GROUP, ShopsListAgent.SHOP_LIST_NAME);
//	
//			StringMessage messageToSend = new StringMessage(json.toString());
//			sendMessageWithRole(x, messageToSend,PARSER_NAME);
//			
//			StringMessage response=(StringMessage) waitNextMessage();
//
//			
//			
//		}
		
		return null;
	}
	
	
}
