package mariusz.ambroziak.kassistant.utils;


public class StringHolder {
	public static String servletBasePath="";
	public static String auchanFilename="auchanEntries.txt";
	
	public static final String currentAppName="";
	public static final String SEARCH4_NAME = "search4";
	public static final String PRODUKT_URL_NAME = "produktUrl";
	public static final String PRODUKT_IDS_NAME = "ids";
	public static final String SINGLE_PRODUKT_ID_NAME ="id";

	public static final String EXCEPTION_MESSAGE_NAME="exception_name";
	public static final String EXCEPTION_STACKTRACE_NAME="stacktrace";
	
	
	public static final String SEARCH_PHRASE_BORDERS="_";
	
	public static final String NO_RESULT_INFO_NAME = "errorMessage";
	public static final String NO_RESULT_UNKNOWN_SHOP = "UnknownShop";

	
	
	public static final String MESSAGE_CREATOR_NAME = "Message_creator";
	public static final String MESSAGE_TYPE_NAME = "Message_Type";
	public static final String MESSAGE_ID_NAME = "Message_ID";
	
	public static String  workingDirectory="";
	
	public static String AGENT_GROUP="group";
	public static final String SCRAPPERS_GROUP =AGENT_GROUP;// "webScrappers";
	public static final String GUIDANCE_GROUP = AGENT_GROUP;//"guiders";
	public static final String SERVLETS_GROUP = AGENT_GROUP;//"servlets";
	public static final String AGENT_COMMUNITY ="kitchenAssistant";

	public static final String SERVLET_RESPONSE_ENCODING ="ISO-8859-2";
	public static final String user_name_value_suffix ="_users_value";

	public static final String ENCODING =java.nio.charset.StandardCharsets.UTF_8.toString();
	public static final String googleCredentials="{\n" + 
			"	\"web\": {\n" + 
			"		\"client_id\": \"835547712158-bep59luc1knfl3900qgndl9l6rs6rrda.apps.googleusercontent.com\",\n" + 
			"		\"project_id\": \"kitchenassistantproject\",\n" + 
			"		\"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" + 
			"		\"token_uri\": \"https://www.googleapis.com/oauth2/v3/token\",\n" + 
			"		\"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" + 
			"		\"client_secret\": \"gEOOYDYNxdGKip_ZGiHqeihM\",\n" + 
			"		\"redirect_uris\": [\n" + 
			"			\"http://localhost/oauth2callback\",\n" + 
			"			\"http://localhost:8081/oauth2callback\",\n" + 
			"			\"http://localhost:8081/kitchen-assistant/oauth2callback\",\n" + 
			"			\"http://kitchenassistant.com.pl:8081/oauth2callback\",\n" + 
			"			\"http://kitchenassistant.com.pl:8081/kitchen-assistant/oauth2callback\",\n" + 
			"			\"http://app.kitchenassistant.com.pl/kitchen-assistant/oauth2callback\",\n" + 
			"			\"http://app.kitchenassistant.com.pl/oauth2callback\"\n" + 
			"		],\n" + 
			"		\"javascript_origins\": [\n" + 
			"			\"http://localhost\",\n" + 
			"			\"http://localhost:8081\",\n" + 
			"			\"http://kitchenassistant.com.pl:8081\",\n" + 
			"			\"http://app.kitchenassistant.com.pl\"\n" + 
			"		]\n" + 
			"	}\n" + 
			"}";
	
	public static final String SQPhrasesDivider="|"; 
	
	public static final String baseUrl="http://localhost:8081/kitchen-assistant";
	
	
	static{
		
		if(SystemEnv.isRemoteHost()){
			workingDirectory="/var/lib/openshift/554a7d494382eccb8f000090/jbossews";
			
		}
		else{
			workingDirectory="C:\\agentsWorkspaceMay\\data";
			}
		
		
		
	}
	
	
	public static String[] wordsToOmmit={"łyżeczka","łyżeczki","łyżeczek",
							"szklanka","szklanki","szklanek"
							
	};
	
	
			
}
