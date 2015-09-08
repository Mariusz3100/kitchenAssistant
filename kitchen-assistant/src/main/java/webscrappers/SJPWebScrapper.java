package webscrappers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import mariusz.ambroziak.kassistant.dao.Base_WordDAOImpl;
import mariusz.ambroziak.kassistant.dao.Variant_WordDAOImpl;
import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Variant_Word;
import mariusz.ambroziak.kassistant.utils.StringHolder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class SJPWebScrapper {
	static String baseUrl="http://sjp.pwn.pl/szukaj/_word_.html";
	
	public static String scrapWord(String word){
		String url=baseUrl.replaceAll("_word_", word);
		String retValue=null;
		URLConnection connection;
		try {
			connection = new URL(url).openConnection();
			connection.setRequestProperty("Accept-Charset", java.nio.charset.StandardCharsets.UTF_8.toString());
			InputStream detResponse = connection.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(detResponse,java.nio.charset.StandardCharsets.UTF_8.toString());
			BufferedReader detBR=new BufferedReader(inputStreamReader);
			String respLine=null;
//			detResponse.
			String response="";
			while((respLine=detBR.readLine())!=null){
				response+=respLine;
			}
			
			if(response.indexOf("Nie znaleziono ¿adnych wyników wyszukiwania")>-1)
			{
				retValue="";
			}else{
				Document doc = Jsoup.parse(response);
				
				Element e=doc.select(".anchor-title")
						.select("[href*=http://sjp.pwn.pl/so/]")
						.first();
				if(e==null)
					retValue=Base_WordDAOImpl.niepoprawneSlowoBazowe;
				else
					retValue=e.attr("title");
			}
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//connection.getRequestProperties()
		
		
		return retValue;

	}
	
	public static void main(String[] args){
		scrapWord("asaadad");
	}
}
