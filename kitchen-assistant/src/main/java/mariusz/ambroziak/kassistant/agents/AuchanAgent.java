package mariusz.ambroziak.kassistant.agents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



//import database.holders.DBOpenshiftInterface;
//import database.holders.DbLocalInterface;
//import database.holders.StringHolder;
import webscrappers.AuchanWebScrapper;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.utils.MessageTypes;
import mariusz.ambroziak.kassistant.utils.StringHolder;
import mariusz.ambroziak.kassistant.utils.SystemEnv;

public class AuchanAgent extends BaseAgent {
	public static final String AUCHAN_WEB_SCRAPPER_NAME = "auchanWebScrapper";
	
	public static final String acceptedURL="http://www.auchandirect.pl/sklep/artykuly/wyszukiwarka/[0-9]+/.+";
	
	private int counter=0;
	
	public static boolean agentOn=true;
	AuchanWebScrapper webScrapper;
	
	public AuchanAgent() {
		super();
		AGENT_ROLE = AUCHAN_WEB_SCRAPPER_NAME;	
		
		AGENT_COMMUNITY=StringHolder.AGENT_COMMUNITY;
//		AGENT_GROUP = StringHolder.SCRAPPERS_GROUP;
	}
	
	@Override
	protected void live() {
		super.live();

		
		StringMessage m;
		
		while(true){
			
			
			m = nextMessage();
			if(m!=null)
				processMessage(m);
			else
				enjoyYourOwn();
		}
		
		
		

	//	BufferedReader br=null;

		
	}

	public void enjoyYourOwn() {
		if(RecipeAgent.checkShops)
			webScrapper.enjoyYourOwn();
	}
	
	@ResponseBody
	@RequestMapping("/agents/"+AUCHAN_WEB_SCRAPPER_NAME)
	public String showData(){
		return htmlLogs.toString().replace("\n", "\n<br>");
	}


	private void processMessage(StringMessage m) {
		JSONObject message=new JSONObject(m.getContent());

		try {
			ArrayList<Produkt> x=webScrapper.lookup(message.getString(StringHolder.SEARCH4_NAME));
			AgentAddress other=getAgentWithRole(AGENT_COMMUNITY, AGENT_GROUP, ShopsListAgent.SHOP_LIST_NAME);
			
			
			JSONObject result=new JSONObject();
			
			if(x==null||x.size()==0){
				result.put("ids", "");
				htmlLog("Nie uda³o siê znaleŸæ ¿adnego produktu dla "+message.getString(StringHolder.SEARCH4_NAME)+" w sklepie auchan.\n");
			}else{
				
				String foundProdukts="";
				
				for(Produkt p:x){
					foundProdukts+=p.getP_id()+" ";
				}
				result.put("ids", foundProdukts);
				
				
				
				
				htmlLog("W sklepie auchan znaleziono produkt(y) o id ["+foundProdukts+"]\n");
				
			}
			
			result.put(StringHolder.MESSAGE_CREATOR_NAME, AUCHAN_WEB_SCRAPPER_NAME);
			result.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.SearchFor);
			result.put(StringHolder.SEARCH4_NAME, message.getString(StringHolder.SEARCH4_NAME));
			StringMessage messageToSend = new StringMessage(result.toString());

			sendMessageWithRole(other, messageToSend,AGENT_ROLE);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	protected void pause(int milliSeconds) {
		// TODO Auto-generated method stub
		super.pause(milliSeconds);
	}

	@Override
	protected void activate() {

		setLogLevel(Level.FINEST);
//		setWarningLogLevel(Level.FINEST);
		
		if(!isGroup(AGENT_COMMUNITY,AGENT_GROUP)){
			createGroup(AGENT_COMMUNITY,AGENT_GROUP);
		}
		
		requestRole(AGENT_COMMUNITY,AGENT_GROUP, AGENT_ROLE);
//		requestRole(AGENT_COMMUNITY, ShopsListAgent.GUIDANCE_GROUP, AGENT_ROLE);

		
		webScrapper=AuchanWebScrapper.getAuchanWebScrapper();
		super.activate();
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		super.end();
	}

}
