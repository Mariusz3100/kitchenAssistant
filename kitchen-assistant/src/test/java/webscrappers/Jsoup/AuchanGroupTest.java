package webscrappers.Jsoup;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class AuchanGroupTest {

	@Test
	public void test() {
		ArrayList<GA_ProduktScrapped> searchFor = AuchanGroup.searchFor("mro¿one owoce");

		for(GA_ProduktScrapped p:searchFor){
			
			assertNotNull(p.getUrl());
					
			assertNotNull(p.getNazwa());		
			
			
			assertNotNull(AuchanGroup.getAuchanShortestWorkingUrl(p.getUrl()));

			assertNotNull(AuchanGroup.getAuchanUrlPattern(p.getUrl()));

			

		}



	}

}
