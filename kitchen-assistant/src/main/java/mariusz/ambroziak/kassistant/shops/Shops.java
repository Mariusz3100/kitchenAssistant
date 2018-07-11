package mariusz.ambroziak.kassistant.shops;

import mariusz.ambroziak.kassistant.api.agents.ShopComAgent;

public enum Shops {
	ShopCom("ShopCom",ShopComAgent.baseUrl),
	UnknownShop("Unknown Shop","");
	
	

	
	
	
	String shopName;
	String baseUrl;
	public String getShopName() {
		return shopName;
	}


	public String getBaseUrl() {
		return baseUrl;
	}
	private Shops(String shopName, String baseUrl) {
		this.shopName = shopName;
		this.baseUrl = baseUrl;
	}
	
	
	

}
