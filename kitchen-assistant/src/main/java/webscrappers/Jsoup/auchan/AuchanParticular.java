package webscrappers.Jsoup.auchan;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mariusz.ambroziak.kassistant.dao.Base_WordDAOImpl;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.model.jsp.QuantityProdukt;
import mariusz.ambroziak.kassistant.utils.Converter;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.PrzepisyPLQExtract;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class AuchanParticular extends AuchanAbstractScrapper{


	private static final String OPIS_PRODUKTU_LABEL = "Opis produktu";

	public static ProduktDetails getProduktDetails(String detailsUrl) throws Page404Exception{
		String page="";
		ProduktDetails retValue=new ProduktDetails();
		
		
		try {
			page = getPage(detailsUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Document doc = Jsoup.parse(page);
		
		doc.setBaseUri(baseURL);
		
		if(checkIf404Page(doc))
			throw new Page404Exception( detailsUrl);
		
		
		Elements header = doc.select("h1");
		
		retValue.setNazwa(getOwnTextOrEmpty(header));
		
		
		String opisProduktu="";
		
		Elements opisElement = doc.select("#product-description-section");
		
		String opisProduktuText=getAllTextOrEmpty(opisElement);
		
//		Elements allElements = opisElement.getAllElements();
//		allElements.remove(titleElement);
		
		
		
		opisProduktuText=opisProduktuText.replaceFirst(OPIS_PRODUKTU_LABEL, "");

		retValue.setOpis(opisProduktuText);
		
		
		
		String cenaZl=getOwnTextOrEmpty(doc.select(".price-zl"));
		String cenaGr=getOwnTextOrEmpty(doc.select(".price-gr"));
		
		float cena=combineCena(cenaZl,cenaGr);
		
		retValue.setCena(cena);
		
		retValue.setUrl(detailsUrl);
		
		
		String quantityText=getOwnTextOrEmpty(doc.select(".prod-info"));
		
		QuantityProdukt quantity = PrzepisyPLQExtract.extractQuantity(quantityText);
		
		retValue.setAmount(quantity);
		
		
		
		
		
		return retValue;
	}

	private static float combineCena(String cenaZl, String cenaGr) {
		
		int zl=Integer.parseInt(cenaZl);
		int gr=Integer.parseInt(cenaGr);
			
		float result =zl;
		
		result=result+gr/100f;
		
		return result;
	}

	public static String getOwnTextOrEmpty(Elements opisElement) {
		String nazwaProduktu="";
		if(opisElement!=null&&opisElement.size()>0)
			nazwaProduktu=opisElement.get(0).ownText();
		
		return nazwaProduktu;
	}

	
	public static String getAllTextOrEmpty(Elements opisElement) {
		String nazwaProduktu="";
		if(opisElement!=null&&opisElement.size()>0)
			nazwaProduktu=opisElement.get(0).text();
		
		return nazwaProduktu;
	}
	
	public static void main(String[] args){
		String url="http://www.auchandirect.pl/sklep/artykuly/wyszukiwarka/95100534/A";
		try {
			ProduktDetails pd=getProduktDetails(url);
		} catch (Page404Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
