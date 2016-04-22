package webscrappers.auchan;

import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.utils.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.QuantityProdukt;

public class ProduktDetails {
	private String url;
	private String nazwa;
	private float cena;

	private String opis;
	
	private PreciseQuantity amount;
	
	
	public PreciseQuantity getAmount() {
		return amount;
	}
	public void setAmount(PreciseQuantity amount) {
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

	
	public Produkt extractProduktWithoutUrl(){
		Produkt produkt=new Produkt();
		produkt.setCena(getCena());
		produkt.setNazwa(getNazwa());
		produkt.setOpis(getOpis());
		produkt.setPrzetworzony(false);
		
		return produkt;
	}
}
