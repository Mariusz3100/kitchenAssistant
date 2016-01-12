package mariusz.ambroziak.kassistant.exceptions;

public class ShopNotFoundException extends Exception{
	String url;

	public ShopNotFoundException(String url) {
		super();
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
	
	
}
