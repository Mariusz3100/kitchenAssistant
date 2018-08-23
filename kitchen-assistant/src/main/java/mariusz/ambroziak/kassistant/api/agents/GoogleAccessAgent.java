package mariusz.ambroziak.kassistant.api.agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.json.JSONObject;
import org.mortbay.jetty.security.Credential;



import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.Apiclients.edaman.DietLabels;
import mariusz.ambroziak.kassistant.Apiclients.edaman.HealthLabels;
import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleAuthApiClient;
import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleAuthApiClientCallbackController;
import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleAuthApiClientController;
import mariusz.ambroziak.kassistant.agents.BaseAgent;
import mariusz.ambroziak.kassistant.exceptions.GoogleDriveAccessNotAuthorisedException;
import mariusz.ambroziak.kassistant.utils.MessageTypes;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;

public class GoogleAccessAgent extends BaseAgent {
	public static String AGENT_NAME="Google_agent";
	
	static ArrayList<GoogleAccessAgent> agents;

	GoogleAuthApiClientCallbackController googleCallbackController;
	GoogleAuthApiClientController googleController;
	
	@Override
	protected void live() {
		//		super.live();
		StringMessage m;

		while(true){

			m = nextMessageKA();
			if(m!=null)
				processMessage(m);
			
				
		}
	}




	private void processMessage(StringMessage m) {
		
		
		if(m!=null){
			String content=((StringMessage)m).getContent();
			JSONObject json=new JSONObject(content);
			
			if(json.get(StringHolder.MESSAGE_TYPE_NAME)==null
					||json.get(StringHolder.MESSAGE_TYPE_NAME).equals("")){
				ProblemLogger.logProblem("Message has no type (in Google agent): "+content);
			}else if(json.get(StringHolder.MESSAGE_TYPE_NAME).equals(MessageTypes.GetLimitations.toString())){
				processgetLimitationsMessage(m);
			}
		}
	}


	public static ArrayList<DietLabels> getDietLabels(){
		try {
			return GoogleAuthApiClient.getDietLimitations();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GoogleDriveAccessNotAuthorisedException e) {
			// TODO Auto-generated catch block
			//this means google account was not connected. We can ignore this.
		}
		return null;
	}

	
	public Credential getCredentials() {
//		googleController.doGet(request, response);
		
		return null;
	}
	
	public static ArrayList<HealthLabels> getHealthLabels(){
		try {
			
			return GoogleAuthApiClient.getHealthLimitations();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GoogleDriveAccessNotAuthorisedException e) {
			// TODO Auto-generated catch block
			//this means google account was not connected. We can ignore this.
		}
		return null;
	}
	
	private void processgetLimitationsMessage(StringMessage m) {
		String dietLimitationsAsString="";
		String healthLimitationsAsString="";
		try {
			dietLimitationsAsString = GoogleAuthApiClient.getDietLimitationsAsString();
			healthLimitationsAsString = GoogleAuthApiClient.getHealthLimitationsAsString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GoogleDriveAccessNotAuthorisedException e) {
			// TODO Auto-generated catch block
			//this means google account was not connected. We can ignore this.
		}
		
		JSONObject retValue=new JSONObject();
		retValue.put("health",healthLimitationsAsString);
		retValue.put("diet",dietLimitationsAsString);
		
		
		StringMessage messageToSend = new StringMessage(retValue.toString());

		sendReplyWithRoleKA(m, messageToSend,AGENT_NAME);
		
		
	}
	
	
	@Override
	protected void activate() {
		if(!isGroup(AGENT_COMMUNITY,AGENT_GROUP)){
			createGroup(AGENT_COMMUNITY,AGENT_GROUP);
		}

		requestRole(AGENT_COMMUNITY, AGENT_GROUP, AGENT_NAME);

		if(agents==null)agents=new ArrayList<GoogleAccessAgent>();
		agents.add(this);

		setLogLevel(Level.FINEST);
		
		googleController=new GoogleAuthApiClientController();
		googleCallbackController=new GoogleAuthApiClientCallbackController();
		
		super.activate();
	}
	
	public GoogleAccessAgent() {
		super();
		AGENT_COMMUNITY=StringHolder.AGENT_COMMUNITY;
		AGENT_ROLE=AGENT_NAME;

	}
}
