package mariusz.ambroziak.kassistant.utils;


public class StringHolder {
	public static String servletBasePath="";
	public static String auchanFilename="auchanEntries.txt";
	public static final String SEARCH4_NAME = "search4";

	public static String  workingDirectory="";
	
	public static String AGENT_GROUP="group";
	public static final String SCRAPPERS_GROUP =AGENT_GROUP;// "webScrappers";
	public static final String GUIDANCE_GROUP = AGENT_GROUP;//"guiders";
	public static final String SERVLETS_GROUP = AGENT_GROUP;//"servlets";
	public static final String AGENT_COMMUNITY ="kitchenAssistant";

	public static final String SERVLET_RESPONSE_ENCODING ="ISO-8859-2";
	public static final String user_name_value_suffix ="_users_value";

	
	
	
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
