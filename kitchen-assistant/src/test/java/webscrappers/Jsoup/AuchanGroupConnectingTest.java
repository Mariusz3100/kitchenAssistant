package webscrappers.Jsoup;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.junit.BeforeClass;
import org.junit.Test;

import webscrappers.Jsoup.auchan.AuchanAbstractScrapper;
import webscrappers.Jsoup.auchan.AuchanGroup;
import webscrappers.Jsoup.auchan.GA_ProduktScrapped;

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
