package webscrappers;

import java.io.UnsupportedEncodingException;

public class ShopScrapper {
	String baseUrl="http://www.auchandirect.pl/sklep/wyszukiwarka/__skladnik_name__";

	
	public static void main(String[] args) {
		
		
		String s="sałata lodowa";
		
		try {
			System.out.println(new String(s.getBytes("UTF8"),"cp1250"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
