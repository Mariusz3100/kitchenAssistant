package webscrappers.google;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import mariusz.ambroziak.kassistant.QuantityExtractor.AmountTypes;
import mariusz.ambroziak.kassistant.model.utils.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.QuantityPhraseClone;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;

public class TranslateScrapper {

	public static final String baseUrl="http://www.dict.pl/dict?word=__words__&words=&lang=PL"; 
	
	public static String getPage(String finalUrl) throws MalformedURLException
	 {
			initCookieHandler();

			URLConnection connection;
			InputStream detResponse = null;
			boolean successfull=false;
			int timeOut=1000;
			InputStreamReader inputStreamReader = null;
			while(!successfull){
				try {
					URL url = new URL(finalUrl);
					connection = url.openConnection();
					
					connection.setConnectTimeout(1000000);
					connection.setRequestProperty("Accept-Charset", "UTF-8");
			        
					
					//argh
					connection.connect();
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
	
	public static void initCookieHandler() {
		if(CookieHandler.getDefault()==null)
			CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
	}
	

	public static void checkifHeadIsOk(Element table) {
		Elements row=table.select("tr");
		
		if(row!=null&&row.size()>0){
			Elements ths = row.select("th.resWordCol");
			
			if(ths!=null&&ths.size()>1){
				if(!"POLSKI".equalsIgnoreCase(ths.get(0).text())
						||!"ANGIELSKI".equalsIgnoreCase(ths.get(1).text())){
					ProblemLogger.logProblem("Jêzyki w s³owniku siê nie zgadzaj¹: "+row);
				}
			}
			
		}
		
		

	}


	public static String getTranslation(String word){
		if(word==null)
			return null;
		
		if(word.equals(""))
			return "";
		
		String wordEncoded="";
		try {
			wordEncoded = URLEncoder.encode(word, StringHolder.ENCODING);
			String url=baseUrl.replaceAll("__words__", wordEncoded);

			String page=getPage(url);
			Document doc = Jsoup.parse(page);

			Elements table = doc.select(".resTable");
			
			
			
			
			if(table!=null&&table.size()>0){
				checkifHeadIsOk(table.get(0));
				
				Elements rows = table.get(0).select(".resRow");
				
				if(rows!=null&&rows.size()>0){
					for(Element row:rows){
						
						Elements tds = row.select("td.resWordCol");
							
						if(tds!=null&&tds.size()>1){
							String originalWord = tds.get(0).text();
							
							originalWord=originalWord.replaceAll("\\{m\\}", "");
							if(originalWord.equals(originalWord)){
								return tds.get(1).text();
							}
						}
						
					}
				}

				
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args){

		String sklad="cukier";
		
		String result=getTranslation(sklad);
		
		System.out.println(result);
	
	}
	
}
