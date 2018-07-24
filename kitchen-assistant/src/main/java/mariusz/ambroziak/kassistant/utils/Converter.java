package mariusz.ambroziak.kassistant.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Converter {
	private static String[] converts=
		{
		"----\\$0142",
		"----\\$0105",
		",-\\$002C",
		"----\\$017C",
		"----\\$017A",
		"----\\$0107",
		"----\\$0144",
		"----\\$00F3",
		"----\\$0119",
		" -\\$0020",
		"----\\$015B",
		"%-\\$0025",
	 
		
		};
	
	private static String[] ommissions=
		{
		")",
		"(",
		"+",
		};
	private static String[] encodingConverts={
	 "----&#260;",
	 "----&#262;",
	 "----&#280;",
	 "----&#321;",
	 "----&#323;",
	 "----&#211;",
	 "----&#346;",
	 "----&#377;",
	 "----&#379;",
	 "----&#261;",
	 "----&#263;",
	 "----&#281;",
	 "----&#322;",
	 "----&#324;",
	 "----&#243;",
	 "----&#347;",
	 "----&#378;",
	 "----&#380;",
		};
	
	

	public static String auchanConvertion(String input){
		String result=input;
		
		

			try {
				result=URLEncoder.encode(input, StringHolder.ENCODING);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return result;
		}


	public static String getOnlyLetters(String text){
		return text.replaceAll("[^A-Za-z------------󜟿---ʣ---ӌ------]", "");
	}


	public static void main(String args[]){
		System.out.println(auchanConvertion("sa---ata lodowa"));
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
	
	
	
	public static String temporaryConvertEncoding(String in){
		if(in==null)
			return null;
		
		String out=in;
		
		for(String change:encodingConverts){
			String[] split=change.split("-");

			out=out.replaceAll(split[1], split[0]);

		}
		return out;
		
		
	}
}
