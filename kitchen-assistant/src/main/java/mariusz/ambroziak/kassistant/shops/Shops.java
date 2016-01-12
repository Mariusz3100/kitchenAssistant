package mariusz.ambroziak.kassistant.shops;

import mariusz.ambroziak.kassistant.agents.AuchanAgent;

public enum Shops {
	Auchan("Auchan",AuchanAgent.baseUrl),
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
