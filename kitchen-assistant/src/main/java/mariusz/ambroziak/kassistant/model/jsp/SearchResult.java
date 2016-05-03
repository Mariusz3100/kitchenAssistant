package mariusz.ambroziak.kassistant.model.jsp;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;

public class SearchResult {
	private String searchPhrase;
	private String produktPhrase;
	private String quantity;

	private ArrayList<Produkt> produkts;
	
	public String getProduktPhrase() {
		return produktPhrase;
	}

	public void setProduktPhrase(String produktPhrase) {
		this.produktPhrase = produktPhrase;
	}

	
//	public SearchResult(String searchPhrase, String quantity,List<Produkt> produkts) {
//		super();
//		this.searchPhrase = searchPhrase;
//		this.quantity=quantity;
//		this.produkts =new ArrayList<Produkt>();
//		if(produkts!=null)
//			this.produkts.addAll(produkts);
//	}
	public String getQuantity() {
		return quantity;
	}
	
	public SearchResult(String searchPhrase, String produktPhrase,
			String quantity, List<Produkt> produkts) {
		super();
		this.searchPhrase = searchPhrase;
		this.produktPhrase = produktPhrase;
		this.quantity = quantity;
		this.produkts =new ArrayList<Produkt>();
		if(produkts!=null)
			this.produkts.addAll(produkts);	
	}
	
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getSearchPhraseEncoded() {
		String retValue=
				Base64.getEncoder().encodeToString(searchPhrase.getBytes());
		return retValue;
	}
	
	public void setSearchPhraseEncoded(String searchPhrase) {
		this.searchPhrase = new String(Base64.getDecoder().decode(searchPhrase.getBytes()));
	}
	
	public String getSearchPhrase() {
		return searchPhrase;
	}
	public void setSearchPhrase(String searchPhrase) {
		this.searchPhrase = searchPhrase;
	}
	public ArrayList<Produkt> getProdukts() {
		return produkts;
	}
	public void setProdukts(ArrayList<Produkt> produkts) {
		this.produkts = produkts;
	}

}
