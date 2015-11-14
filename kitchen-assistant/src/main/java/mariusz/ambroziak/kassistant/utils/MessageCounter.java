package mariusz.ambroziak.kassistant.utils;

public class MessageCounter {

	private static int count=0;
	
	
	
	public static int getCount(){
		return ++count;
	}
}
