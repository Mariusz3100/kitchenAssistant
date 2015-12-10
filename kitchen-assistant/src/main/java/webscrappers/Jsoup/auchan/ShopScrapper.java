package webscrappers.Jsoup.auchan;

import org.hibernate.cfg.NotYetImplementedException;

public abstract class ShopScrapper {
	
	
	public static String getShortestWorkingUrl(String url){
		throw new NotYetImplementedException(
				"This metod is used only to provide handle for inheriting ones, please use some particular scrapper");
	}
}
