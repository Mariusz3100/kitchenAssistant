package webscrappers.Jsoup.auchan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.cfg.NotYetImplementedException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import mariusz.ambroziak.kassistant.utils.ProblemLogger;

public abstract class AuchanAbstractScrapper{

	public static final String singleCharAtTheEndOfEveryUrl="A";
	public static final String UrlPattern = "\\Qhttp://www.auchandirect.pl/sklep/artykuly/\\E(\\Qwyszukiwarka/\\E|[0-9_]+\\/)[LV]*[0-9]+\\/";
//	public static final String workingUrlPattern = "http://www.auchandirect.pl/sklep/artykuly/[[wyszukiwarka/]|[[0-9_]+/]][LV]?[0-9]+/[a-zA-Z_0-9-]+?";
//	public static final String shortestPattern = "http://www.auchandirect.pl/sklep/artykuly/[[wyszukiwarka/]|[[0-9_]+/]][LV]?[0-9]+/";
	public static final String baseURL = "http://www.auchandirect.pl";
	
	static final String emptyContentString = "404";

	public static String getPage(String finalUrl) throws MalformedURLException
			 {
					URLConnection connection;
					InputStream detResponse = null;
					boolean successfull=false;
					int timeOut=1000;
					InputStreamReader inputStreamReader = null;
					while(!successfull){
						try {
							connection = new URL(finalUrl).openConnection();
							connection.setConnectTimeout(timeOut);
							connection.setRequestProperty("Accept-Charset", java.nio.charset.StandardCharsets.UTF_8.toString());
							
							detResponse = connection.getInputStream();
							
							successfull=true;
						
					
							inputStreamReader = new InputStreamReader(detResponse,java.nio.charset.StandardCharsets.UTF_8.toString());
						} catch (IOException e) {
							ProblemLogger.logProblem(
									"There was a problem with accessing url '"+finalUrl+"' exception: "+e.getMessage());
							
							//e.printStackTrace();
						}
					}
					
					
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

//	public static String getAuchanUrlPattern(String url) {
//			
//	//		boolean z = Pattern.matches(b,a);
//		    Pattern p = Pattern.compile(shortestPattern);
//	
//		    
//		    
//			Matcher m=p.matcher(url);
//			
//			if(m.find()){
//				
//				
//				String shortUrl=m.group();
//				return shortUrl+"%";
//		    }
//			return null;
//		}
	

	
	public static String getAuchanShortestWorkingUrl(String url) {
		
		boolean z = Pattern.matches(UrlPattern,url);
	    Pattern p = Pattern.compile(UrlPattern);
	
	    
	    
		Matcher m=p.matcher(url);
		
		if(m.find()){
			
			
			String shortUrl=m.group();
			return shortUrl+singleCharAtTheEndOfEveryUrl;
	    }
		ProblemLogger.logProblem("Short url not found");
		return "";
	}

	public static boolean checkIf404Page(String pageContent) {

	Document doc = Jsoup.parse(pageContent);
	
//	doc.setBaseUri(baseURL);
	
	return checkIf404Page(doc);
		
	
	}
	
	public static boolean checkIf404Page(Document doc) {
		Elements content=doc.select("#content");
		
		//pusta strona raczej te¿ jest 404
		if(content==null
				||content.size()==0
				||content.get(0)==null)
			return true;
		
		Element element = content.get(0);
	
		
		if(element.ownText()==null
				||element.getAllElements().size()<1
				||element.ownText().equals(emptyContentString))
			return true;
		
		return false;
	}

}
