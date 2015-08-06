package mariusz.ambroziak.kassistant.utils;

public class SystemEnv {

	public static Boolean AgentsOn=true;

	public static boolean isRemoteHost(){
		//System.out.println(System.getenv("OPENSHIFT_DATA_DIR"));
		
		return System.getenv("OPENSHIFT_DATA_DIR")!=null;
		
	}
}
