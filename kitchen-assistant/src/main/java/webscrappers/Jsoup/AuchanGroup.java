package webscrappers.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import mariusz.ambroziak.kassistant.dao.Base_WordDAOImpl;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.utils.Converter;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AuchanGroup {
	private static String auchanSearchUrl="http://www.auchandirect.pl/sklep/wyszukiwarka/__search__";
	String filename="\\classes\\auchanEntries.txt";
	public static final String workingUrlPattern="http://www.auchandirect.pl/sklep/artykuly/wyszukiwarka/[0-9]+/[a-zA-Z_0-9-]+";
	public static final String shortestPattern="http://www.auchandirect.pl/sklep/artykuly/wyszukiwarka/[0-9]+/";

	public static final String noResults="Przykro nam ale nie znaleziono wyników dla podanego zapytania"; 
	public static final String validLinkPart="/sklep/artykuly/wyszukiwarka/";
	public static final String baseURL="http://www.auchandirect.pl";

	public static ArrayList<ProduktScrapped> searchFor(String searchPhrase){
		ArrayList<ProduktScrapped> retValue=new ArrayList<ProduktScrapped>();
		
		String search4=Converter.auchanConvertion(searchPhrase);
//		String tempSearch4=search4.replaceAll("\\\\$","\\\\\\\\$");
		
	//	Pattern.quote(lookFor);
		String search4withSpaces=search4.replaceAll("\\$", " ");
				
		
		String urlWithSpaces=auchanSearchUrl.replaceAll("__search__",search4withSpaces);
		
		String finalUrl=urlWithSpaces.replaceAll(" ","\\$");
		
		try {
			String response = getPage(finalUrl);
			
			if(response.indexOf(noResults)>-1)
			{
				//no results, return empty list?
			}else{
				Document doc = Jsoup.parse(response);
				
				doc.setBaseUri(baseURL);
				
				Elements produkts=doc.select(".single-prod-box");
						
				for(Element produkt: produkts){
					Elements links = produkt.select(".prod-box-text").get(0).getElementsByTag("a");
					
					for(Element link: links){
						if(link.attr("href").indexOf(validLinkPart)>-1){
							String l=link.absUrl("href");
							String nazwa=link.ownText();
							ProduktScrapped produktFound=new ProduktScrapped(nazwa,l);
							
							retValue.add(produktFound);

						}
						
					}
					
				}

			}
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return retValue;

	}


	public static String getPage(String finalUrl) throws
			MalformedURLException, UnsupportedEncodingException {
		URLConnection connection;
		InputStream detResponse = null;
		boolean successfull=false;
		int timeOut=1000;

		while(!successfull){
			try {
				connection = new URL(finalUrl).openConnection();
				connection.setConnectTimeout(timeOut);
				connection.setRequestProperty("Accept-Charset", java.nio.charset.StandardCharsets.UTF_8.toString());
				
				detResponse = connection.getInputStream();
				
				successfull=true;
			} catch (IOException e) {
				ProblemLogger.logProblem(
						"There was a problem with accessing url '"+finalUrl+"' Waiting "+timeOut+" ms");
				
				//e.printStackTrace();
			}
		}
		
		InputStreamReader inputStreamReader = new InputStreamReader(detResponse,java.nio.charset.StandardCharsets.UTF_8.toString());
		BufferedReader detBR=new BufferedReader(inputStreamReader);
		
		String respLine=null;
//			detResponse.
		StringBuilder response=new StringBuilder();
		try {
			while((respLine=detBR.readLine())!=null){
				response.append(respLine);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response.toString();
	}
	
//	
//	public static  String getAndSaveNewRelation(String x) {
//		String retValue=SJPWebScrapper.scrapWord(Converter.getOnlyLetters(x));
//		retValue=Converter.getOnlyLetters(retValue);
//		if(retValue==null)retValue="";
//		
//		DaoProvider.getInstance().getVariantWordDao().addRelation(x.toLowerCase(),retValue.toLowerCase());
//		
//		return retValue;
//	}
//	
//	
	public static void main(String[] args){
		searchFor("mro¿one owoce");
	}
}
