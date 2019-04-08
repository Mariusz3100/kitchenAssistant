package mariusz.ambroziak.kassistant.api.agents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.mortbay.jetty.security.Credential;
import org.springframework.web.servlet.ModelAndView;

import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.Apiclients.edaman.DietLabels;
import mariusz.ambroziak.kassistant.Apiclients.edaman.HealthLabels;
import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleDriveApiClient;
import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleAuthApiClientCallbackController;
import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleAuthApiClientController;
import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleCalendarApiClient;
import mariusz.ambroziak.kassistant.agents.BaseAgent;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.GoogleDriveAccessNotAuthorisedException;
import mariusz.ambroziak.kassistant.model.jsp.MultiProdukt_SearchResult;
import mariusz.ambroziak.kassistant.utils.MessageTypes;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;
import mariusz.ambroziak.kassistant.utils.StringUtils;

public class GoogleAccessAgent extends BaseAgent {
	public static String GOOGLE_AGENT_NAME="Google_agent";
	public static Map<String,String> accessTokenMap=new HashMap<>();
	public static long accessTokenIdInMap=0;
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


	public static ArrayList<DietLabels> getDietLimitations(String accessToken) throws AgentSystemNotStartedException, GoogleDriveAccessNotAuthorisedException{
		try {
			GoogleAccessAgent freeAgent = getFreeAgent();
			return freeAgent.getDietLimitationsPrivately(accessToken);
		} catch (IOException e) {

			if(e.getMessage().contains(StringHolder.INVALID_CREDENTIALS_EXCEPTION_MESSAGE)) {
				throw new GoogleDriveAccessNotAuthorisedException();
			}else {
				ProblemLogger.logProblem("Exception thrown during retrieving diet data from google drive");
				ProblemLogger.logStackTrace(e.getStackTrace());
				ArrayList<DietLabels> arrayList = new ArrayList<DietLabels>();
				e.printStackTrace();
				return arrayList;
			}
		}

	}

	public static void deleteGoogleAuthorisationData(HttpServletResponse response) throws AgentSystemNotStartedException{

		GoogleAccessAgent freeOne = getFreeAgent();
		if(freeOne!=null)
		{
			freeOne.setBusy(true);
			freeOne.deleteAuthorisationData(response);
			freeOne.setBusy(false);
		}
	}


	private void deleteAuthorisationData(HttpServletResponse response) {
		Cookie cookie = new Cookie(StringHolder.CREDENTIAL_COOKIE_NAME, "");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		GoogleDriveApiClient.deleteCredentials();
	}


	private ArrayList<DietLabels> getDietLimitationsPrivately(String accessToken) throws IOException, GoogleDriveAccessNotAuthorisedException {
		ArrayList<DietLabels> calendarLimitations=GoogleCalendarApiClient.getDietLimitations(accessToken);
		ArrayList<DietLabels> driveLimitations=GoogleDriveApiClient.getDietLimitations(accessToken);
		
		ArrayList<DietLabels> retValue=new ArrayList<DietLabels>();
		retValue.addAll(driveLimitations);
		retValue.addAll(calendarLimitations);

		return retValue;
	}

	private ArrayList<HealthLabels> getHealthLimitationsPrivately(String accessToken) throws IOException, GoogleDriveAccessNotAuthorisedException {
		ArrayList<HealthLabels> calendarLimitations=GoogleCalendarApiClient.getHealthLimitations(accessToken);
		ArrayList<HealthLabels> driveLimitations=GoogleDriveApiClient.getHealthLimitations(accessToken);
		
		ArrayList<HealthLabels> retValue=new ArrayList<HealthLabels>();
		retValue.addAll(driveLimitations);
		retValue.addAll(calendarLimitations);

		return retValue;
		
	}
	//
	//	public Credential getCredentials() {
	////		googleController.doGet(request, response);
	//		
	//		return null;
	//	}
	//	
	public static ArrayList<HealthLabels> getHealthLimitations(String accessToken) throws AgentSystemNotStartedException, GoogleDriveAccessNotAuthorisedException{
		try {
			GoogleAccessAgent freeAgent = getFreeAgent();
			return freeAgent.getHealthLimitationsPrivately(accessToken);
		} catch (IOException e) {
			if(e.getMessage().contains(StringHolder.INVALID_CREDENTIALS_EXCEPTION_MESSAGE)) {
				throw new GoogleDriveAccessNotAuthorisedException();
			}else {
				ProblemLogger.logProblem("Exception thrown during retrieving diet data from google drive");
				ProblemLogger.logStackTrace(e.getStackTrace());
				ArrayList<HealthLabels> arrayList = new ArrayList<HealthLabels>();
				e.printStackTrace();
				return arrayList;
			}
		}
	}

	
	public static void saveHealthLimitations(String accessToken,List<HealthLabels> labels) throws AgentSystemNotStartedException, GoogleDriveAccessNotAuthorisedException{
		try {
			GoogleAccessAgent freeAgent = getFreeAgent();
			freeAgent.saveHealthLimitationsPrivately(accessToken,labels);
		} catch (IOException e) {
			if(e.getMessage().contains(StringHolder.INVALID_CREDENTIALS_EXCEPTION_MESSAGE)) {
				throw new GoogleDriveAccessNotAuthorisedException();
			}else {
				ProblemLogger.logProblem("Exception thrown during retrieving diet data from google drive");
				ProblemLogger.logStackTrace(e.getStackTrace());
				e.printStackTrace();
			}
		}
	}
	
	public static void saveDietLimitations(String accessToken,List<DietLabels> labels) throws AgentSystemNotStartedException, GoogleDriveAccessNotAuthorisedException{
		try {
			GoogleAccessAgent freeAgent = getFreeAgent();
			freeAgent.saveDietLimitationsPrivately(accessToken,labels);
		} catch (IOException e) {
			if(e.getMessage().contains(StringHolder.INVALID_CREDENTIALS_EXCEPTION_MESSAGE)) {
				throw new GoogleDriveAccessNotAuthorisedException();
			}else {
				ProblemLogger.logProblem("Exception thrown during retrieving diet data from google drive");
				ProblemLogger.logStackTrace(e.getStackTrace());
				e.printStackTrace();
			}
		}
	}
	private void saveDietLimitationsPrivately(String accessToken,List<DietLabels> labels) throws IOException, GoogleDriveAccessNotAuthorisedException {
		GoogleDriveApiClient.writeDietLabelsToDrive(accessToken,labels);
		
	}


	private void saveHealthLimitationsPrivately(String accessToken,List<HealthLabels> labels) throws IOException, GoogleDriveAccessNotAuthorisedException {
		GoogleDriveApiClient.writeHealthLabelsToDrive(accessToken,labels);
		
	}


	private void processgetLimitationsMessage(StringMessage m) {
		JSONObject retValue=new JSONObject();

		String dietLimitationsAsString="";
		String healthLimitationsAsString="";
		String accessToken=extractAccessTokenFromMessage(m);
		try {
			dietLimitationsAsString = getDietLimitationsString(accessToken);
			healthLimitationsAsString = getHealthLimitationsAsString(accessToken);
		
			retValue.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.GetLimitationsResponse);

			retValue.put(StringHolder.HEALTH_LIMITATIONS_NAME,healthLimitationsAsString);
			retValue.put(StringHolder.DIET_LIMITATIONS_NAME,dietLimitationsAsString);
		} catch (IOException e) {
			if(e.getMessage()!=null&&e.getMessage().contains(StringHolder.INVALID_CREDENTIALS_EXCEPTION_MESSAGE)) {
				retValue.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.GetLimitationsResponseNotAuthorised);


			}else {
				ProblemLogger.logProblem("Exception thrown during retrieving diet data from google drive");
				retValue.put(StringHolder.MESSAGE_CREATOR_NAME, GOOGLE_AGENT_NAME);
				retValue.put(StringHolder.EXCEPTION_MESSAGE_NAME, e.getMessage());
				retValue.put(StringHolder.EXCEPTION_STACKTRACE_NAME, StringUtils.stackTraceToString(e));

			
			}
		} catch (GoogleDriveAccessNotAuthorisedException e) {
			retValue.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.GetLimitationsResponseNotAuthorised);

		}
		


		StringMessage messageToSend = new StringMessage(retValue.toString());

		sendReplyWithRoleKA(m, messageToSend,GOOGLE_AGENT_NAME);


	}


	private String getHealthLimitationsAsString(String accessToken)
			throws IOException, GoogleDriveAccessNotAuthorisedException {
		String healthLimitationsAsString;
		String healthLimitationsFromDrive = GoogleDriveApiClient.getDietLimitationsAsString(accessToken);
		String healthLimitationsFromCalendar = GoogleCalendarApiClient.getHealthLimitationsAsString(accessToken);
		healthLimitationsAsString=(healthLimitationsFromDrive==null?"":healthLimitationsFromDrive)+
				StringHolder.GOOGLE_CALENDAR_LINE_SEPARATOR+
				(healthLimitationsFromCalendar==null?"":healthLimitationsFromCalendar);
		
		
		
		return healthLimitationsAsString;
	}


	private String getDietLimitationsString(String accessToken)
			throws IOException, GoogleDriveAccessNotAuthorisedException {
		String dietLimitationsAsString;
		String dietLimitationsFromDrive = GoogleDriveApiClient.getDietLimitationsAsString(accessToken);
		String dietLimitationsFromCalendar = GoogleCalendarApiClient.getDietLimitationsAsString(accessToken);
		dietLimitationsAsString=(dietLimitationsFromDrive==null?"":dietLimitationsFromDrive)+
				StringHolder.GOOGLE_CALENDAR_LINE_SEPARATOR+
				(dietLimitationsFromCalendar==null?"":dietLimitationsFromCalendar);
		
		return dietLimitationsAsString;
	}


	private String extractAccessTokenFromMessage(StringMessage m) {
		if(m==null||m.getContent()==null)
			return "";
		String content=((StringMessage)m).getContent();
		JSONObject json=new JSONObject(content);

		String accessTokenId = "";
		
		try{
			accessTokenId=Integer.toString((Integer) json.get(StringHolder.GOOGLE_ACCESS_TOKEN_ID_NAME));
		}catch (ClassCastException e) {
			accessTokenId=(String) json.get(StringHolder.GOOGLE_ACCESS_TOKEN_ID_NAME);
			
		}
		String accessToken = accessTokenMap.get(accessTokenId);
		return accessToken;
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


	public static boolean isAccessAuthorised(String accessToken) throws AgentSystemNotStartedException {
		try {
			GoogleAccessAgent freeAgent = getFreeAgent();
			freeAgent.getDietLimitationsPrivately(accessToken);
			return true;
		} catch (IOException e) {
			return false;
		} catch (GoogleDriveAccessNotAuthorisedException e) {
			return false;
		}
		
	}
}
