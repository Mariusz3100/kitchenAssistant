package mariusz.ambroziak.kassistant.api.agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.json.JSONObject;
import org.mortbay.jetty.security.Credential;



import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.Apiclients.edaman.DietLabels;
import mariusz.ambroziak.kassistant.Apiclients.edaman.HealthLabels;
import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleDriveApiClient;
import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleAuthApiClientCallbackController;
import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleAuthApiClientController;
import mariusz.ambroziak.kassistant.agents.BaseAgent;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.GoogleDriveAccessNotAuthorisedException;
import mariusz.ambroziak.kassistant.model.jsp.MultiProdukt_SearchResult;
import mariusz.ambroziak.kassistant.utils.MessageTypes;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;

public class GoogleAccessAgent extends BaseAgent {
	public static String GOOGLE_AGENT_NAME="Google_agent";

	private static ArrayList<GoogleAccessAgent> agents;

	GoogleAuthApiClientCallbackController googleCallbackController;

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


	private static GoogleAccessAgent getFreeAgent() throws AgentSystemNotStartedException {
		GoogleAccessAgent freeOne=null;

		if(agents==null){
			System.out.println("Agent system not started");
			throw new AgentSystemNotStartedException();

		}
		else{
			while(freeOne==null){
				for(GoogleAccessAgent ra:agents){
					if(!ra.isBusy()){
						freeOne=ra;

					}
				}
				if(freeOne==null){
					try {
						System.out.println("Free RecipeParser Not Found");
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return freeOne;
	}


	private void processMessage(StringMessage m) {


		if(m!=null){
			String content=((StringMessage)m).getContent();
			JSONObject json=new JSONObject(content);

			if(!json.has(StringHolder.MESSAGE_TYPE_NAME)||json.get(StringHolder.MESSAGE_TYPE_NAME)==null
					||json.get(StringHolder.MESSAGE_TYPE_NAME).equals("")){
				ProblemLogger.logProblem("Message has no type (in Google agent): "+content);
			}else if(json.get(StringHolder.MESSAGE_TYPE_NAME).equals(MessageTypes.GetLimitations.toString())){
				processgetLimitationsMessage(m);
			}
		}
	}


	public static ArrayList<DietLabels> getDietLimitations() throws AgentSystemNotStartedException, GoogleDriveAccessNotAuthorisedException{
		try {
			GoogleAccessAgent freeAgent = getFreeAgent();
			return freeAgent.getDietLimitationsPrivately();
		} catch (IOException e) {
			ProblemLogger.logProblem("Exception thrown during retrieving diet data from google drive");
			ProblemLogger.logStackTrace(e.getStackTrace());
			ArrayList<DietLabels> arrayList = new ArrayList<DietLabels>();
			e.printStackTrace();
			return arrayList;
		}

	}

	public static void deleteGoogleAuthorisationData() throws AgentSystemNotStartedException{

		GoogleAccessAgent freeOne = getFreeAgent();
		if(freeOne!=null)
		{
			freeOne.setBusy(true);
			freeOne.deleteAuthorisationData();
			freeOne.setBusy(false);
		}
	}


	private void deleteAuthorisationData() {
		GoogleAuthApiClientCallbackController.authorisationMap.clear();
		GoogleDriveApiClient.deleteCredentials();
		GoogleAuthApiClientController.deletionCounter++;
	}


	private ArrayList<DietLabels> getDietLimitationsPrivately() throws IOException, GoogleDriveAccessNotAuthorisedException {
		return GoogleDriveApiClient.getDietLimitations();
	}

	private ArrayList<HealthLabels> getHealthLimitationsPrivately() throws IOException, GoogleDriveAccessNotAuthorisedException {
		return GoogleDriveApiClient.getHealthLimitations();
	}
	//
	//	public Credential getCredentials() {
	////		googleController.doGet(request, response);
	//		
	//		return null;
	//	}
	//	
	public static ArrayList<HealthLabels> getHealthLimitations() throws AgentSystemNotStartedException, GoogleDriveAccessNotAuthorisedException{
		try {
			GoogleAccessAgent freeAgent = getFreeAgent();
			return freeAgent.getHealthLimitationsPrivately();
		} catch (IOException e) {
			ProblemLogger.logProblem("Exception thrown during retrieving diet data from google drive");
			ProblemLogger.logStackTrace(e.getStackTrace());
			ArrayList<HealthLabels> arrayList = new ArrayList<HealthLabels>();
			e.printStackTrace();
			return arrayList;
		}
	}

	
	public static void saveHealthLimitations(List<HealthLabels> labels) throws AgentSystemNotStartedException, GoogleDriveAccessNotAuthorisedException{
		try {
			GoogleAccessAgent freeAgent = getFreeAgent();
			freeAgent.saveHealthLimitationsPrivately(labels);
		} catch (IOException e) {
			ProblemLogger.logProblem("Exception thrown during retrieving diet data from google drive");
			ProblemLogger.logStackTrace(e.getStackTrace());
			e.printStackTrace();
		}
	}
	
	public static void saveDietLimitations(List<DietLabels> labels) throws AgentSystemNotStartedException, GoogleDriveAccessNotAuthorisedException{
		try {
			GoogleAccessAgent freeAgent = getFreeAgent();
			freeAgent.saveDietLimitationsPrivately(labels);
		} catch (IOException e) {
			ProblemLogger.logProblem("Exception thrown during retrieving diet data from google drive");
			ProblemLogger.logStackTrace(e.getStackTrace());
			e.printStackTrace();
		}
	}
	private void saveDietLimitationsPrivately(List<DietLabels> labels) throws IOException, GoogleDriveAccessNotAuthorisedException {
		GoogleDriveApiClient.writeDietLabelsToDrive(labels);
		
	}


	private void saveHealthLimitationsPrivately(List<HealthLabels> labels) throws IOException, GoogleDriveAccessNotAuthorisedException {
		GoogleDriveApiClient.writeHealthLabelsToDrive(labels);
		
	}


	private void processgetLimitationsMessage(StringMessage m) {
		JSONObject retValue=new JSONObject();

		String dietLimitationsAsString="";
		String healthLimitationsAsString="";
		try {
			dietLimitationsAsString = GoogleDriveApiClient.getDietLimitationsAsString();
			healthLimitationsAsString = GoogleDriveApiClient.getHealthLimitationsAsString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GoogleDriveAccessNotAuthorisedException e) {
			retValue.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.GetLimitationsResponseNotAuthorised);

		}
		
		retValue.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.GetLimitationsResponse);

		retValue.put(StringHolder.HEALTH_LIMITATIONS_NAME,healthLimitationsAsString);
		retValue.put(StringHolder.DIET_LIMITATIONS_NAME,dietLimitationsAsString);


		StringMessage messageToSend = new StringMessage(retValue.toString());

		sendReplyWithRoleKA(m, messageToSend,GOOGLE_AGENT_NAME);


	}


	@Override
	protected void activate() {
		if(!isGroup(AGENT_COMMUNITY,AGENT_GROUP)){
			createGroup(AGENT_COMMUNITY,AGENT_GROUP);
		}

		requestRole(AGENT_COMMUNITY, AGENT_GROUP, GOOGLE_AGENT_NAME);

		if(agents==null)agents=new ArrayList<GoogleAccessAgent>();
		agents.add(this);

		setLogLevel(Level.FINEST);

		googleCallbackController=new GoogleAuthApiClientCallbackController();

		super.activate();
	}

	public GoogleAccessAgent() {
		super();
		AGENT_COMMUNITY=StringHolder.AGENT_COMMUNITY;
		AGENT_ROLE=GOOGLE_AGENT_NAME;

	}
}
