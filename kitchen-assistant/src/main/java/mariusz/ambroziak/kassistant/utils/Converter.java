package mariusz.ambroziak.kassistant.utils;

public class Converter {
	private static String[] converts=
		{
		"ł-\\$0142",
		"ą-\\$0105",
		",-\\$002C",
		"ż-\\$017C",
		"ź-\\$017A",
		"ć-\\$0107",
		"ń-\\$0144",
		"ó-\\$00F3",
		" -\\$0020"
		};
	
	
	public static String auchanConvertion(String input){
		String result=input;
		
		for(String change:converts){
			String[] split=change.split("-");
			
			result=result.replaceAll(split[0], split[1]);
			
		}
		
		return result;
		
	}
	
	
	public static void main(String args[]){
		System.out.println(auchanConvertion("sałata lodowa"));
	}
	
	
	
}
