package webscrappers;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import mariusz.ambroziak.kassistant.QuantityExtractor.AuchanQExtract;
import mariusz.ambroziak.kassistant.QuantityExtractor.JedzDobrzeExtractor;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;

public class IleWazyScrapper extends AbstractScrapper {
	private static final String BASE_URI = "http://www.ilewazy.pl";
	public static final String urlTemplate="http://www.ilewazy.pl/produkty/page/1/q/__search_phrase__";

	public static PreciseQuantity getUsualAmountOfSztuka(String fraza){
		String url=getProduktUrl(fraza);

		if(url!=null&&url.length()>0){
			try {
				String page=getPage(url);
				Document doc = Jsoup.parse(page);
				Elements dropDownList= doc.select("#selector_unit_weight");
				String dropDown1SztOption=dropDownList.select("[data-label=\"1  sztuka\"]").text();
				
				String probablyAmountInGrams=dropDown1SztOption.replaceAll("1 sztuka", "").trim();
				probablyAmountInGrams=probablyAmountInGrams.replaceAll("\\(", "");
				probablyAmountInGrams=probablyAmountInGrams.replaceAll("\\)", "");
				
				//TODO na razie AuchanQuanExtract
				return AuchanQExtract.extractQuantity(probablyAmountInGrams);
			} catch (MalformedURLException e) {
				ProblemLogger.logProblem("Malformed url: "+url);
				ProblemLogger.logStackTrace(e.getStackTrace());
			} catch (Page404Exception e) {
				ProblemLogger.logProblem("PageNotFound url: "+url);
				ProblemLogger.logStackTrace(e.getStackTrace());
			}

		}

		return  null;
	}

	private static String getProduktUrl(String produktName) {
		String convertedProduktName = null;
		try {
			convertedProduktName = URLEncoder.encode(produktName.trim(),StringHolder.ENCODING);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String finalUrl=urlTemplate.replaceAll("__search_phrase__", convertedProduktName);
		try {
			String page=getPage(finalUrl);

			Document doc = Jsoup.parse(page);
			doc.setBaseUri(BASE_URI);
			String bestUrlFound=null;
			Elements produkts = doc.select(".thumb-subtitle");

			for(Element produktOnPage:produkts){
				boolean found = checkIfFoundsProduktNameMatches(produktName, produktOnPage);

				if(found){
					String urlFound=produktOnPage.attr("href");
					if(bestUrlFound==null||bestUrlFound.length()>urlFound.length()){
						bestUrlFound=urlFound;
					}
				}
			}
			return bestUrlFound;
		} catch (MalformedURLException e) {
			ProblemLogger.logProblem("Malformed url: "+finalUrl);
			ProblemLogger.logStackTrace(e.getStackTrace());
		} catch (Page404Exception e) {
			ProblemLogger.logProblem("PageNotFound url: "+finalUrl);
			ProblemLogger.logStackTrace(e.getStackTrace());
		}

		return null;
	}

	private static boolean checkIfFoundsProduktNameMatches(String produktName, Element produktOnPage) {
		String produktOnPageName=produktOnPage.text();

		String[] produktNameSplitted=produktName.split(" ");
		boolean found=true;
		for(String produktNameFragment:produktNameSplitted){
			if(!produktOnPageName.toLowerCase().contains(produktNameFragment.toLowerCase())){
				found=false;
			}
		}
		return found;
	}




	public static void main(String[] args){
		System.out.println(getUsualAmountOfSztuka("jab³ko"));
	}

}
