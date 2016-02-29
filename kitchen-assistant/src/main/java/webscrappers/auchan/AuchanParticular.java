package webscrappers.auchan;

import java.net.MalformedURLException;

import mariusz.ambroziak.kassistant.QuantityExtractor.AuchanQExtract;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.model.utils.PreciseQuantity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class AuchanParticular extends AuchanAbstractScrapper{


	private static final String OPIS_PRODUKTU_LABEL = "Opis produktu";

		
		

	
	public static ProduktDetails getProduktDetails(String detailsUrl) throws Page404Exception{
		String page="";
		
		
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
		
		ProduktDetails retValue = extractDataExceptUrl(doc);
		
		
		retValue.setUrl(detailsUrl);
		
		
		return retValue;
	}


	public static ProduktDetails extractDataExceptUrl(Document doc) {
		ProduktDetails retValue=new ProduktDetails();

		Elements header = doc.select(".label");
		
		retValue.setNazwa(getOwnTextOrEmpty(header));
		
		
		
		Elements opisElement = doc.select("#product-desc-0");
		
		String opisProduktuText=getAllTextOrEmpty(opisElement);
		
//		Elements allElements = opisElement.getAllElements();
//		allElements.remove(titleElement);
		
		
		
		opisProduktuText=opisProduktuText.replaceFirst(OPIS_PRODUKTU_LABEL, "");

		retValue.setOpis(opisProduktuText);
		
		
		
		String cenaZl=getOwnTextOrEmpty(doc.select(".p-nb"));
		String cenaGr=getOwnTextOrEmpty(doc.select(".p-cents"));
		
		float cena=combineCena(cenaZl,cenaGr);
		
		retValue.setCena(cena);
		
		
		
		
		String quantityText=getOwnTextOrEmpty(doc.select(".packaging>strong"));
		
		PreciseQuantity quantity = AuchanQExtract.extractQuantity(quantityText);
		
		retValue.setAmount(quantity);
		return retValue;
	}

	
//	public static ProduktWithBasicIngredients getProduktDetailsWithNutritions(String detailsUrl) throws Page404Exception{
//		String page="";
//		ProduktDetails produkt=new ProduktDetails();
//		
//		
//		try {
//			page = getPage(detailsUrl);
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		Document doc = Jsoup.parse(page);
//		
//		doc.setBaseUri(baseURL);
//		
//		if(checkIf404Page(doc))
//			throw new Page404Exception( detailsUrl);
//		
//		
//		Elements header = doc.select(".label");
//		
//		produkt.setNazwa(getOwnTextOrEmpty(header));
//		
//		
//		String opisProduktu="";
//		
//		Elements opisElement = doc.select("#product-desc-0");
//		
//		String opisProduktuText=getAllTextOrEmpty(opisElement);
//		
////		Elements allElements = opisElement.getAllElements();
////		allElements.remove(titleElement);
//		
//		
//		
//		opisProduktuText=opisProduktuText.replaceFirst(OPIS_PRODUKTU_LABEL, "");
//
//		produkt.setOpis(opisProduktuText);
//		
//		
//		
//		String cenaZl=getOwnTextOrEmpty(doc.select(".p-nb"));
//		String cenaGr=getOwnTextOrEmpty(doc.select(".p-cents"));
//		
//		float cena=combineCena(cenaZl,cenaGr);
//		
//		produkt.setCena(cena);
//		
//		produkt.setUrl(detailsUrl);
//		
//		
//		String quantityText=getOwnTextOrEmpty(doc.select(".packaging>strong"));
//		
//		Quantity quantity = AuchanQExtract.extractQuantity(quantityText);
//		
//		produkt.setAmount(quantity);
//		
//		
//		
//		
//		
//		return produkt;
//	}
	
	
	private static float combineCena(String cenaZl, String cenaGr) {
		
		int zl=Integer.parseInt(cenaZl);
		int gr=Integer.parseInt(cenaGr);
			
		float result =zl*100;
		
		result=result+gr;
		result=result/100f;
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
	
	
	
	
	
	
//	public static void main(String[] args){
//		String url="http://www.auchandirect.pl/auchan-warszawa/pl/gellwe-fitella-musli-bananowe-z-kawalkami-czekolady/p-97500398";
//		try {
//			ArrayList<Produkt_Ingredient> ingredients=new ArrayList<Produkt_Ingredient>(); 
//					
//			
//			ProduktDetails pd=getProduktDetailsWithNutritions(ingredients, url);
//		} catch (Page404Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		
//	}
	

}
