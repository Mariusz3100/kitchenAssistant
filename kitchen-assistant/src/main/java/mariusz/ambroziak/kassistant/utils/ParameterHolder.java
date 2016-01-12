package mariusz.ambroziak.kassistant.utils;

public class ParameterHolder {
	private static boolean checkShops=true;
	private static boolean agentsOn=true;
	
	
	
	public static boolean isCheckShops() {
		return checkShops;
	}
	public static void setCheckShops(boolean checkShops) {
		ParameterHolder.checkShops = checkShops;
	}
	public static boolean isAgentsOn() {
		return agentsOn;
	}
	public static void setAgentsOn(boolean agentsOn) {
		ParameterHolder.agentsOn = agentsOn;
	}

	
	
	
	
}
