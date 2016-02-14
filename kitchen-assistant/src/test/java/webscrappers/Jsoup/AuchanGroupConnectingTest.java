package webscrappers.Jsoup;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import mariusz.ambroziak.kassistant.model.Produkt;

import org.junit.BeforeClass;
import org.junit.Test;

import webscrappers.auchan.AuchanAbstractScrapper;
import webscrappers.auchan.AuchanGroup;
import webscrappers.auchan.GA_ProduktScrapped;

public class AuchanGroupConnectingTest {
	static ArrayList<GA_ProduktScrapped> searchFor;


	
	@BeforeClass
	public static void setUp() {
		searchFor = AuchanGroup.searchFor("mro¿one owoce");
	}
	
	@Test
	public void testNotNull() {
		for(GA_ProduktScrapped p:searchFor){
			System.out.println("1"+p.getUrl());
			
			assertNotNull(p.getUrl());
					
			assertNotNull(p.getNazwa());		
			
			assertNotNull(AuchanGroup.getAuchanShortestWorkingUrl(p.getUrl()));

//			assertNotNull(AuchanGroup.getAuchanUrlPattern(p.getUrl()));
		}

	}
	
	
	@Test
	public  void testNowyAuchan() throws MalformedURLException {
		String page= AuchanGroup.getPage("http://www.auchandirect.pl/auchan-warszawa/pl/search?text=mro¿one+owoce");
		
		System.out.println(page);
	
	}
	
	
//	@Test
//	public void test2() {
//		for(GA_ProduktScrapped p:searchFor){
//			
//			
//			
//			String auchanShortestWorkingUrl = AuchanGroup.getAuchanShortestWorkingUrl(p.getUrl());
//
//			String auchanUrlPattern = AuchanGroup.getAuchanUrlPattern(p.getUrl());
//			auchanUrlPattern=auchanUrlPattern.replaceAll("%", ".*");
//			assertTrue(Pattern.matches(auchanUrlPattern, auchanShortestWorkingUrl));
//		
//		}
//	}
	


}
