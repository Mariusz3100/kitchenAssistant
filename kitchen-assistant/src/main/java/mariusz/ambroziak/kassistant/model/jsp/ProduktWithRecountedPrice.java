package mariusz.ambroziak.kassistant.model.jsp;

import mariusz.ambroziak.kassistant.model.Produkt;

public class ProduktWithRecountedPrice extends Produkt{

	private String recountedPrice;

	public String getRecountedPrice() {
		return recountedPrice;
	}

	public void setRecountedPrice(String recountedPrice) {
		this.recountedPrice = recountedPrice;
	}

	public ProduktWithRecountedPrice(String nazwa2, String detailsUrl, String recountedPrice) {
		super(nazwa2, detailsUrl);
		this.recountedPrice = recountedPrice;
	}

	public ProduktWithRecountedPrice(Produkt produkt, String recountedPrice) {
		super(produkt.getUrl(),produkt.getQuantityPhrase(),produkt.getNazwa(),produkt.getSklad(),produkt.getOpis(),
				produkt.getCena(),produkt.isPrzetworzony());
		this.recountedPrice = recountedPrice;
	}
	
	public ProduktWithRecountedPrice(String url,String quantityPhrase, String nazwa, String sklad, String opis,
			float cena, boolean przetworzony,String recountedPrice){
		super( url,quantityPhrase,nazwa,sklad,opis,cena,przetworzony);
		this.recountedPrice = recountedPrice;
	} 
}
