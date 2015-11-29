package webscrappers.Jsoup;

import static org.junit.Assert.*;
import mariusz.ambroziak.kassistant.model.Produkt;

import org.junit.Test;

import webscrappers.Jsoup.auchan.AuchanParticular;
import webscrappers.Jsoup.auchan.Page404Exception;
import webscrappers.Jsoup.auchan.ProduktDetails;
import webscrappers.Jsoup.importio.AuchanWebScrapper;

public class AuchanParticularConnectingTest {

	@Test(expected=Page404Exception.class)
	public void test() throws Page404Exception {
		String url="http://www.auchandirect.pl/sklep/artykuly/wyszukiwarka/95";
		ProduktDetails pd=AuchanParticular.getProduktDetails(url);
		
	}

	
	@Test
	public void test2() throws Page404Exception {
		String url="http://www.auchandirect.pl/sklep/artykuly/wyszukiwarka/95100534/A";
		ProduktDetails pd=AuchanParticular.getProduktDetails(url);
		
	}
	
	
	
	@Test
	public void test3() throws Page404Exception {
		String url="http://www.auchandirect.pl/sklep/artykuly/wyszukiwarka/95100534/A";
		ProduktDetails pd=AuchanParticular.getProduktDetails(url);
		
		
		AuchanWebScrapper oldScrapper=AuchanWebScrapper.getAuchanWebScrapper();
		
		Produkt oldScrapperProduktDetails = oldScrapper.getProduktDetails(url);
		
	
		assertEquals("Nazwa niezgodna", oldScrapperProduktDetails.getNazwa(), pd.getNazwa());
		assertEquals("url niezgodny", oldScrapperProduktDetails.getUrl(), pd.getUrl());
		assertEquals("Opis niezgodny", oldScrapperProduktDetails.getOpis(), pd.getOpis());
		
		
	}
	
}
