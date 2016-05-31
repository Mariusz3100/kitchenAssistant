package mariusz.ambroziak.kassistant.model.jsp;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;

public class SingleProdukt_SearchResult extends SearchResult{
	private Produkt produkt;
	
	public SingleProdukt_SearchResult(String searchPhrase, String produktPhrase,
			String quantity, Produkt produkt) {
		super();
		this.searchPhrase = searchPhrase;
		this.produktPhrase = produktPhrase;
		this.quantity = quantity;
		this.produkt=produkt;
	}

	public Produkt getProdukt() {
		return produkt;
	}

	public void setProdukt(Produkt produkt) {
		this.produkt = produkt;
	}
	
	
	

}
