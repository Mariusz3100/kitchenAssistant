package mariusz.ambroziak.kassistant.utils;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Converter {
	private static String[] converts=
		{
		"≥-\\$0142",
		"π-\\$0105",
		",-\\$002C",
		"ø-\\$017C",
		"ü-\\$017A",
		"Ê-\\$0107",
		"Ò-\\$0144",
		"Û-\\$00F3",
		"Í-\\$0119",
		" -\\$0020",
		"ú-\\$015B",
		"%-\\$0025",
		
		};

	private static String[] ommissions=
		{
		")",
		"(",
		"+",
		};

	public static String auchanConvertion(String input){
		String result=input;
		
		for(String change:converts){
			String[] split=change.split("-");

			result=result.replaceAll(split[0], split[1]);

		}

		for(String change:ommissions){

			result=result.replaceAll(Pattern.quote(change), "");

		}
		
		return result;

	}

	public static String getOnlyLetters(String text){
		return text.replaceAll("[^A-Za-zπÊÍ≥ÒÛúüø• £—”åèØ]", "");
	}


	public static void main(String args[]){
		System.out.println(auchanConvertion("sa≥ata lodowa"));
	}

	public static String listToString(ArrayList<String> words){
		String retValue="";

		for(String x:words){
			retValue+=x+" ";
		}

		return retValue.trim();
	}


	public static String escapeSql(String str) {
		if (str == null) {
			return null;
		}
		return str.replaceAll( "'", "''");
	}
}
