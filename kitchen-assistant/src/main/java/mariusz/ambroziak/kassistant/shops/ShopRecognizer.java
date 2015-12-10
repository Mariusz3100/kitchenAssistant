package mariusz.ambroziak.kassistant.shops;

public class ShopRecognizer {

	public static Shop recognizeShop(String url){
		
		for(Shop s:Shop.values()){
			if(url.indexOf(s.getBaseUrl())>-1)
				return s;
		}
		
		return null;
	}
}
