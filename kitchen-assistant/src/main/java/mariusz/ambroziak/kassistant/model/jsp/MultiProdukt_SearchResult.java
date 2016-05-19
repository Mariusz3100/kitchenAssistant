package mariusz.ambroziak.kassistant.model.jsp;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;

public class MultiProdukt_SearchResult extends SearchResult{
	private ArrayList<Produkt> produkts;
	
	public MultiProdukt_SearchResult(String searchPhrase, String produktPhrase,
			String quantity, List<Produkt> produkts) {
		super();
		this.searchPhrase = searchPhrase;
		this.produktPhrase = produktPhrase;
		this.quantity = quantity;
		this.produkts =new ArrayList<Produkt>();
		if(produkts!=null)
			this.produkts.addAll(produkts);	
	}
	
	
	
//	public void setSearchPhraseEncoded(String searchPhrase) {
//		this.searchPhrase = new String(Base64.getDecoder().decode(searchPhrase.getBytes()));
//	}
	
	public ArrayList<Produkt> getProdukts() {
		return produkts;
	}
	public void setProdukts(ArrayList<Produkt> produkts) {
		this.produkts = produkts;
	}

}
