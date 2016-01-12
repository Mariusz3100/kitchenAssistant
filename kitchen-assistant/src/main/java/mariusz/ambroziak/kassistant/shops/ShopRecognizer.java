package mariusz.ambroziak.kassistant.shops;

import webscrappers.auchan.AuchanAbstractScrapper;

public class ShopRecognizer {

	public static Shops recognizeShop(String url){
		
		for(Shops s:Shops.values()){
			if(url.indexOf(s.getBaseUrl())>-1)
				return s;
		}
		
		return Shops.UnknownShop;
	}
	
	public static String getShortestWorkingUrl(String baseUrl){
		Shops shop=recognizeShop(baseUrl);
		
		if(shop==Shops.Auchan)
		{
			return AuchanAbstractScrapper.getAuchanShortestWorkingUrl(baseUrl);
		}
		
		return "";
	}
	
}
