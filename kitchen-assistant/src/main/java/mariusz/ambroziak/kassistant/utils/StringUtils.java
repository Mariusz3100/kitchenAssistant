package mariusz.ambroziak.kassistant.utils;

public class StringUtils {

	
	public static String stackTraceToString(Exception e) {
		if(e==null)
			return null;
		StackTraceElement[] stackTrace = e.getStackTrace();
		String retValue="";
		for(StackTraceElement element:stackTrace) {
			retValue=element.toString()+"\n";
		}
		return retValue;
	}
}
