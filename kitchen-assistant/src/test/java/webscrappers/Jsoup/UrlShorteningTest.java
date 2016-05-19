package webscrappers.Jsoup;

import static org.junit.Assert.*;

import java.net.MalformedURLException;

import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.model.Produkt;

import org.junit.Test;

import webscrappers.Jsoup.importio.AuchanWebScrapper;
import webscrappers.auchan.AuchanAbstractScrapper;
import webscrappers.auchan.AuchanParticular;
import webscrappers.auchan.ProduktDetails;

public class UrlShorteningTest {

//	@Test(expected=Page404Exception.class)
//	public void test() throws Page404Exception {
//		String url="http://www.auchandirect.pl/sklep/artykuly/1163_1213_147";
//		
//		String shortUrl=AuchanAbstractScrapper.getAuchanShortestWorkingUrl(url);
//		
//		assertNotNull(shortUrl);
//		assertNotEquals(shortUrl,"");
//		
//	}

//	@Test
//	public void test() {
//		String url="http://www.auchandirect.pl/auchan-warszawa/pl/profi-pasztet-z-drobiu-wielkopolski-z-pieczarkami/p-96900406?fromCategory=true";
//		
//		String shortUrl=AuchanAbstractScrapper.getAuchanShortestWorkingUrl(url);
//		
//		assertNotNull(shortUrl);
//		assertNotEquals(shortUrl,"");
//		
//	}
//	
//	@Test
//	public void test1() {
//		String url="http://www.auchandirect.pl/auchan-warszawa/pl/profi-pasztet-z-drobiu-wielkopolski-z-pieczarkami/p-96900406";
//		
//		String shortUrl=AuchanAbstractScrapper.getAuchanShortestWorkingUrl(url);
//		
//		assertNotNull(shortUrl);
//		assertNotEquals(shortUrl,"");
//		
//	}
	
	
	public void testPage() {
		String url="http://www.auchandirect.pl/p-96900406";
		//http://www.auchandirect.pl/p-97500398
		String content = null;
		try {
			content = AuchanAbstractScrapper.getPage(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		} catch (Page404Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertNotNull(content);
		
	}
	
	
	@Test
	public void testNewShortening() {
		String url="http://www.auchandirect.pl/p-96900406";
		//http://www.auchandirect.pl/p-97500398
		String shortUrl = AuchanAbstractScrapper.getAuchanShortestWorkingUrl(url);
	
		assertEquals(shortUrl,url);

		
		
	}
	
	
	@Test
	public void testNewShortening2() {
		String fullUrl="http://www.auchandirect.pl/auchan-warszawa/pl/profi-pasztet-z-drobiu-wielkopolski-z-pieczarkami/p-96900406";
		String shortUrl="http://www.auchandirect.pl/p-96900406";
		//http://www.auchandirect.pl/p-97500398
		String shortenedUrl = AuchanAbstractScrapper.getAuchanShortestWorkingUrl(fullUrl);
	
		assertEquals(shortUrl,shortenedUrl);

		
		
	}
	
	
	@Test
	public void testNewShorteningTricky() {
		String fullUrl="http://www.auchandirect.pl/auchan-warszawa/pl/profi-pasztet-z-drobiu-wielkopolski-z-pieczarkami/p-96900406/p-97500398";
		String shortUrl="http://www.auchandirect.pl/p-97500398";
		//http://www.auchandirect.pl/p-97500398
		String shortenedUrl = AuchanAbstractScrapper.getAuchanShortestWorkingUrl(fullUrl);
	
		assertEquals(shortUrl,shortenedUrl);

		
		
	}
}
