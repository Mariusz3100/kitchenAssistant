package mariusz.ambroziak.kassistant.agents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.regex.Pattern;

import org.json.JSONObject;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.api.agents.ShopComAgent;
import mariusz.ambroziak.kassistant.shops.ShopRecognizer;
import mariusz.ambroziak.kassistant.shops.Shops;
import mariusz.ambroziak.kassistant.utils.MessageTypes;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;


public class ShopsListAgent extends BaseAgent {

	//	public static final String GUIDANCE_GROUP = StringHolder.GUIDANCE_GROUP;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String SHOP_LIST_NAME = "shopsList";

	ArrayList<AgentAddress> adresses;

		private static Map<Shops,AgentAddress> shopUrlMap;


	public ShopsListAgent() {
		super();

		AGENT_COMMUNITY=StringHolder.AGENT_COMMUNITY;
		//	AGENT_GROUP = StringHolder.GUIDANCE_GROUP;
		AGENT_ROLE=SHOP_LIST_NAME;
	}

	public static ShopsListAgent instance;

	@Override
	protected void live() {

		Message m=null;

		while(true){

			m=waitNextMessageKA();


			if(m.getSender().getRole().equals("manager")){
				processDamnManagerMessage(m);
			}

			String content=((StringMessage)m).getContent();
			JSONObject json=new JSONObject(content);

			if(json.get(StringHolder.MESSAGE_TYPE_NAME)==null
					||json.get(StringHolder.MESSAGE_TYPE_NAME).equals("")){
				ProblemLogger.logProblem("Message has no type: "+content);
			}else if(json.get(StringHolder.MESSAGE_TYPE_NAME).equals(MessageTypes.SearchFor.toString())){
				if(!m.getSender().getRole().equals(RecipeAgent.PARSER_NAME)
						&&!m.getSender().getRole().equals(ProduktAgent.PARSER_NAME)){
					ProblemLogger.logProblem("Received searchFor request not from parser");	
				}
				AgentAddress x=getAgentWithRole(AGENT_COMMUNITY, StringHolder.SCRAPPERS_GROUP, ShopComAgent.SHOP_COM_API_AGENT_NAME);
				StringMessage newM=new StringMessage(((StringMessage)m).getContent());
				
				json.put(StringHolder.MESSAGE_CREATOR_NAME, SHOP_LIST_NAME);
				addMessagesRelation((StringMessage) m,newM);
				sendMessageWithRole(x, newM,ShopsListAgent.SHOP_LIST_NAME);

			}else if(json.get(StringHolder.MESSAGE_TYPE_NAME).equals(MessageTypes.SearchForResponse.toString())){
				if(!m.getSender().getRole().equals(ShopComAgent.SHOP_COM_API_AGENT_NAME)){
					ProblemLogger.logProblem("Received searchForResponse Message not from shop agent");

				}
				StringMessage newM=new StringMessage(((StringMessage)m).getContent());
				StringMessage originalOne=getOriginalMessage(m.getConversationID());
				json.put(StringHolder.MESSAGE_CREATOR_NAME, SHOP_LIST_NAME);

				sendReply(originalOne, newM);//(x, newM,ShopsListAgent.SHOP_LIST_NAME);


			}else if(json.get(StringHolder.MESSAGE_TYPE_NAME).equals(MessageTypes.GetProduktData.toString())){
				
				String url = json.getString(StringHolder.PRODUKT_URL_NAME);
				AgentAddress x=getProperAgentAdress(url);
				
				if(x==null){
					JSONObject response=new JSONObject();
					response.put(StringHolder.NO_RESULT_INFO_NAME, StringHolder.NO_RESULT_UNKNOWN_SHOP);
					sendReply(m,new StringMessage(response.toString()));
				}else{
					StringMessage newM=new StringMessage(((StringMessage)m).getContent());
					addMessagesRelation((StringMessage) m,newM);

					sendMessageWithRoleKA(x, newM,ShopsListAgent.SHOP_LIST_NAME);
				}
			}else if(json.get(StringHolder.MESSAGE_TYPE_NAME).equals(MessageTypes.GetProduktDataResponse.toString())){
				StringMessage originalOne=getOriginalMessage(m.getConversationID());
				StringMessage newM=new StringMessage(((StringMessage)m).getContent());
				sendReply(originalOne, newM);//(x, newM,ShopsListAgent.SHOP_LIST_NAME);

			}


		}

	}

	private AgentAddress getProperAgentAdress(String url) {
		if(shopUrlMap==null)
			setUpUrlMap();
			

		Shops shop=ShopRecognizer.recognizeShop(url);
		
		if(shop!=null&&shop!=Shops.UnknownShop)
			return shopUrlMap.get(shop);
			
			
		return null;
	}

	public void processDamnManagerMessage(Message m) {
		String content=((StringMessage)m).getContent();
		JSONObject json=new JSONObject(content);

		if(RecipeAgent.PARSER_NAME.equals(json.get(StringHolder.MESSAGE_CREATOR_NAME))){
			AgentAddress x=getAgentWithRole(AGENT_COMMUNITY, StringHolder.SCRAPPERS_GROUP,ShopComAgent.SHOP_COM_API_AGENT_NAME);
			StringMessage newM=new StringMessage(((StringMessage)m).getContent());
			sendMessageWithRole(x, newM,ShopsListAgent.SHOP_LIST_NAME);

		}else{
			ProblemLogger.logProblem("Message was received and cannot be properly handled:"+content );
		}
	}

	@Override
	protected void activate() {
		// TODO Auto-generated method stub
		super.activate();

		if(!isGroup(AGENT_COMMUNITY,AGENT_GROUP)){
			createGroup(AGENT_COMMUNITY,AGENT_GROUP);
		}

		requestRole(AGENT_COMMUNITY, AGENT_GROUP, AGENT_ROLE);
		setLogLevel(Level.FINEST);


		if(instance==null)
			instance=this;
	}



		public void setUpUrlMap() {
			if(shopUrlMap==null){
				shopUrlMap=new HashMap<Shops, AgentAddress>();
			}
			
			AgentAddress x=getAgentWithRole(AGENT_COMMUNITY, StringHolder.SCRAPPERS_GROUP, AuchanAgent.AUCHAN_WEB_SCRAPPER_NAME);
	
			shopUrlMap.put(Shops.Auchan,x);
			
			AgentAddress y=getAgentWithRole(AGENT_COMMUNITY, StringHolder.SCRAPPERS_GROUP, ShopComAgent.SHOP_COM_API_AGENT_NAME);
			
			shopUrlMap.put(Shops.ShopCom,y);
			
		}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		super.end();
	}


	
	public static void main(String[] arg){
		String x="http://www.auchandirect.pl/sklep/artykuly/1160_1181_1292/93000281/A";
		String y="(http://)?www.auchandirect.pl/sklep/.+";
		
		boolean b=Pattern.matches(y, x);
		
		System.out.println(b);
		
		
	}

}
