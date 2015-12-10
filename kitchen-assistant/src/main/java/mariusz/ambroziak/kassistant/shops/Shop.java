package mariusz.ambroziak.kassistant.shops;

public enum Shop {
	Auchan("Auchan","");
	
	
	String shopName;
	public String getShopName() {
		return shopName;
	}


	public String getBaseUrl() {
		return baseUrl;
	}


	String baseUrl;
	
	
	private Shop(String shopName, String baseUrl) {
		this.shopName = shopName;
		this.baseUrl = baseUrl;
	}
	
	
	

}
