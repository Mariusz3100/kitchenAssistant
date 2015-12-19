package webscrappers.Jsoup;

import static org.junit.Assert.*;

import java.util.ArrayList;

import mariusz.ambroziak.kassistant.agents.AuchanAgent;
import mariusz.ambroziak.kassistant.model.Produkt;

import org.junit.Test;

import webscrappers.Jsoup.auchan.AuchanAbstractScrapper;
import webscrappers.Jsoup.auchan.AuchanGroup;
import webscrappers.Jsoup.auchan.AuchanParticular;
import webscrappers.Jsoup.auchan.GA_ProduktScrapped;
import webscrappers.Jsoup.auchan.Page404Exception;
import webscrappers.Jsoup.auchan.ProduktDetails;
import webscrappers.Jsoup.importio.AuchanWebScrapper;

public class SearchForTest {

	@Test
	public void test1() throws Page404Exception {
		String searchPhrase = "olej rzepakowy";
		ArrayList<GA_ProduktScrapped> results = AuchanAgent.searchForCorrectRememberOthers(searchPhrase);
		
		
		assertTrue(results!=null&&results.size()>0);
		
		for(GA_ProduktScrapped p:results){
			assertNotNull(p.getUrl());
			
			assertNotNull(p.getNazwa());		
			
			assertNotNull(AuchanGroup.getAuchanShortestWorkingUrl(p.getUrl()));
			
			for(String x:searchPhrase.split(" ")){
				if(!p.getNazwa().toLowerCase().contains(x.toLowerCase())){
					System.out.println();
				}
				assertTrue(p.getNazwa().toLowerCase().contains(x.toLowerCase()));
			}
		}
		
		
		
	}
	
	@Test
	public void test2() throws Page404Exception {
		String searchPhrase = "olej rzepakowy";
		
		ArrayList<GA_ProduktScrapped> results =AuchanGroup.searchFor(searchPhrase);
		
		
		assertTrue(results!=null&&results.size()>0);
		
		for(GA_ProduktScrapped p:results){
			assertNotNull(p.getUrl());
			
			assertNotNull(p.getNazwa());		
			
			assertNotNull(AuchanGroup.getAuchanShortestWorkingUrl(p.getUrl()));
			
			
		}
		
		
		
	}

	
}
