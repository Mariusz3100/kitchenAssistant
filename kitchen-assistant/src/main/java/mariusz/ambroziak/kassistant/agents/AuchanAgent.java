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
import mariusz.ambroziak.kassistant.utils.StringHolder;
import mariusz.ambroziak.kassistant.utils.SystemEnv;

public class AuchanAgent extends BaseAgent {
	public static final String AUCHAN_WEB_SCRAPPER_NAME = "auchanWebScrapper";
	
	private int counter=0;
	
	public static boolean agentOn=true;
	AuchanWebScrapper webScrapper;
	
	public AuchanAgent() {
		super();
		AGENT_ROLE = AUCHAN_WEB_SCRAPPER_NAME;	
		
		AGENT_COMMUNITY=StringHolder.AGENT_COMMUNITY;
		AGENT_GROUP = StringHolder.SCRAPPERS_GROUP;
	}
	
	@Override
	protected void live() {
//		File f=new File(filename);

		
		StringMessage m;
		
		while(true){
			
			
			m = nextMessage();
			if(m!=null)
				processMessage(m);
			else
				webScrapper.enjoyYourOwn();
		}
		
		
		

	//	BufferedReader br=null;

		
	}
	
	@ResponseBody
	@RequestMapping("/agents/"+AUCHAN_WEB_SCRAPPER_NAME)
	public String showData(){
		return htmlLogs.toString().replace("\n", "\n<br>");
	}


	private Produkt processMessage(StringMessage m) {
		JSONObject message=new JSONObject(m.getContent());

		try {
			ArrayList<Produkt> x=webScrapper.lookup(message.getString(StringHolder.SEARCH4_NAME));
			AgentAddress other=getAgentWithRole(AGENT_COMMUNITY, AGENT_GROUP, AGENT_ROLE);

			if(x==null||x.size()==0){
				sendMessage(other, new StringMessage(""));

			}else{
				JSONObject result=new JSONObject();
				result.put("nazwa", x.get(0).getNazwa());
				result.put("url", x.get(0).getUrl());
				result.put(StringHolder.SEARCH4_NAME, message.getString(StringHolder.SEARCH4_NAME));
				
				StringMessage messageToSend = new StringMessage(result.toString());
				messageToSend.getSender();
				sendMessageWithRole(other, messageToSend,AGENT_ROLE);
			}
			
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
		
		return null;
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

		webScrapper=AuchanWebScrapper.getAuchanWebScrapper();
		super.activate();
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		super.end();
	}

}
