package webscrappers.auchan;

import mariusz.ambroziak.kassistant.model.utils.QuantityProdukt;

public class ProduktDetails {
	private String url;
	private String nazwa;
	private float cena;

	private String opis;
	
	private QuantityProdukt amount;
	
	
	public QuantityProdukt getAmount() {
		return amount;
	}
	public void setAmount(QuantityProdukt amount) {
		this.amount = amount;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getNazwa() {
		return nazwa;
	}
	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}
	public float getCena() {
		return cena;
	}
	public void setCena(float cena) {
		this.cena = cena;
	}
	public String getOpis() {
		return opis;
	}
	public void setOpis(String opis) {
		this.opis = opis;
	}
	
	public ProduktDetails() {
	}
		
	public ProduktDetails(String url, String nazwa, float cena, String opis) {
		super();
		this.url = url;
		this.nazwa = nazwa;
		this.cena = cena;
		this.opis = opis;
	}
	
}
