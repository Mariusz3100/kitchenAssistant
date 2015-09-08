package mariusz.ambroziak.kassistant.utils;

import java.util.ArrayList;

public class Converter {
	private static String[] converts=
		{
		"�-\\$0142",
		"�-\\$0105",
		",-\\$002C",
		"�-\\$017C",
		"�-\\$017A",
		"�-\\$0107",
		"�-\\$0144",
		"�-\\$00F3",
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
	
	public static String getOnlyLetters(String text){
		return text.replaceAll("[^A-Za-z]", "");
	}
	
	
	public static void main(String args[]){
		System.out.println(auchanConvertion("sałata lodowa"));
	}
	
	public static String listToString(ArrayList<String> words){
		String retValue="";
		
		for(String x:words){
			retValue+=x+" ";
		}
		
		return retValue.trim();
	}
	
}
