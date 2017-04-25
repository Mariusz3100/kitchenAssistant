package webscrappers.auchan;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.cfg.NotYetImplementedException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.utils.StringHolder;
import webscrappers.AbstractScrapper;

public abstract class AuchanAbstractScrapper extends AbstractScrapper{

//	public static final String singleCharAtTheEndOfEveryUrl="A";
	public static final String UrlPattern = "\\Qhttp://www.auchandirect.pl\\E.*\\/p-[0-9]+(\\?.*)?";
	public static final String UrlPatternP_codePattern = "/p-[0-9]+";
	public static final String urlStart = "http://www.auchandirect.pl";

//	public static final String workingUrlPattern = "http://www.auchandirect.pl/sklep/artykuly/[[wyszukiwarka/]|[[0-9_]+/]][LV]?[0-9]+/[a-zA-Z_0-9-]+?";
//	public static final String shortestPattern = "http://www.auchandirect.pl/sklep/artykuly/[[wyszukiwarka/]|[[0-9_]+/]][LV]?[0-9]+/";
	public static final String baseURL = "http://www.auchandirect.pl";
	
	static final String emptyContentString = "Przepraszamy , strona o tym adresie nie istnieje";
	
	private static int tickets=0;
	
	
//	public static String getPage(String finalUrl) throws MalformedURLException, Page404Exception {
//		super.
//	
//	}

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
//		http://www.auchandirect.pl/auchan-warszawa/pl/profi-pasztet-z-drobiu-wielkopolski-z-pieczarkami/p-96900406?fromCategory=true
		if(!url.startsWith("http://"))
			url="http://"+url;
		if(Pattern.matches(UrlPattern,url)){
		    Pattern p = Pattern.compile(UrlPatternP_codePattern);
		
		    String p_code=null;
		    
			Matcher m=p.matcher(url);
			
			while(m.find()){
				
				p_code=m.group();

		    }
			
			return baseURL+p_code;
		}
//		ProblemLogger.logProblem("Short url not found");
		return url;
	}

	public static boolean checkIf404Page(String pageContent) {

	Document doc = Jsoup.parse(pageContent);
	
//	doc.setBaseUri(baseURL);
	
	return checkIf404Page(doc);
		
	
	}
	
	
//	public static String getPage(String finalUrl) throws MalformedURLException, Page404Exception {
//		getPa
//	}

		
	
	
	public static boolean checkIf404Page(Document doc) {
		Elements pageError=doc.select(".page.error");//.get(0).;
		
		//pusta strona raczej te¿ jest 404
//		if(content==null
//				||content.size()==0
//				||content.get(0)==null)
//			return true;
		
	
		if(pageError!=null
				&&pageError.size()>0
				&&pageError.get(0)!=null
				&&pageError.get(0)
				.getElementsContainingOwnText(emptyContentString).size()>0)
			return true;
		
		return false;
	}

	
	
	public static void main(String[] args){
		String x="http://www.auchandirect.pl/auchan-warszawa/pl/hipp-obiadek-dynia-i-ziemniaki-bio/p-93900005";
		
		String shortUrl=getAuchanShortestWorkingUrl(x);
		
	}
}
