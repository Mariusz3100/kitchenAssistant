package mariusz.ambroziak.kassistant.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
	private static String[] encodingConverts={
	 "•-&#260;",
	 "∆-&#262;",
	 " -&#280;",
	 "£-&#321;",
	 "—-&#323;",
	 "”-&#211;",
	 "å-&#346;",
	 "è-&#377;",
	 "Ø-&#379;",
	 "π-&#261;",
	 "Ê-&#263;",
	 "Í-&#281;",
	 "≥-&#322;",
	 "Ò-&#324;",
	 "Û-&#243;",
	 "ú-&#347;",
	 "ü-&#378;",
	 "ø-&#380;",
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
