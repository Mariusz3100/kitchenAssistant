package webscrappers.auchan;

public class Page404Exception extends Exception {
	private String url="";
	
	
	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public Page404Exception(String url) {
		super("Zwrócona strona nie zawiera ¿adnych informacji.");
		this.url=url;
	}

}
