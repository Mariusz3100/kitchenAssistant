package webscrappers.Jsoup;

import static org.junit.Assert.*;
import mariusz.ambroziak.kassistant.model.Produkt;

import org.junit.Test;

import webscrappers.Jsoup.auchan.AuchanAbstractScrapper;
import webscrappers.Jsoup.auchan.AuchanParticular;
import webscrappers.Jsoup.auchan.Page404Exception;
import webscrappers.Jsoup.auchan.ProduktDetails;
import webscrappers.Jsoup.importio.AuchanWebScrapper;

public class UrlShorteningTest {

	@Test(expected=Page404Exception.class)
	public void test() throws Page404Exception {
		String url="http://www.auchandirect.pl/sklep/artykuly/1163_1213_1478/Dania-Gotowe/Pierogi-Pyzy-Kluski/Ravioli-Tortiglioni";
		
		String shortUrl=AuchanAbstractScrapper.getAuchanShortestWorkingUrl(url);
		
		assertNotNull(shortUrl);
		assertNotEquals(shortUrl,"");
		
	}


	
}
