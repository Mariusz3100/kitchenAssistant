package webscrappers.Jsoup;

public class GA_ProduktScrapped {
	private String nazwa;
	private String url;
	
	
	public String getNazwa() {
		return nazwa;
	}
	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}
	public GA_ProduktScrapped(String nazwa, String url) {
		super();
		this.nazwa = nazwa;
		this.url = url;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}
