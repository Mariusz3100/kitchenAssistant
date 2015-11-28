package webscrappers.Jsoup.auchan;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;

import mariusz.ambroziak.kassistant.dao.Base_WordDAOImpl;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.utils.Converter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class AuchanGroup extends AuchanAbstractScrapper{
	private static String auchanSearchUrl="http://www.auchandirect.pl/sklep/wyszukiwarka/__search__";
	String filename="\\classes\\auchanEntries.txt";
	public static final String noResults="Przykro nam ale nie znaleziono wyników dla podanego zapytania"; 
	public static final String validLinkPart="/sklep/artykuly/wyszukiwarka/";
	public static ArrayList<GA_ProduktScrapped> searchFor(String searchPhrase){
		ArrayList<GA_ProduktScrapped> retValue=new ArrayList<GA_ProduktScrapped>();
		
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
							GA_ProduktScrapped produktFound=new GA_ProduktScrapped(nazwa,l);
							
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

	
	
	public static void main(String[] args){
		ArrayList<GA_ProduktScrapped> searchFor = searchFor("mro¿one owoce");
		
		for(GA_ProduktScrapped p:searchFor){
			String a=getAuchanShortestWorkingUrl(p.getUrl());
			
			String b=getAuchanShortestWorkingUrl(p.getUrl());
			
			System.out.println(a+" "+b);
			
		}
		
	}
	
}
