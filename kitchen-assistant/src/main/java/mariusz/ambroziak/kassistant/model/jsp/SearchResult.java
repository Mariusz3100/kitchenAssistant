package mariusz.ambroziak.kassistant.model.jsp;

import java.util.ArrayList;
import java.util.List;

import mariusz.ambroziak.kassistant.model.Produkt;

public class SearchResult {
	private String searchPhrase;
	private String quantity;
	private ArrayList<Produkt> produkts;
	
	public SearchResult(String searchPhrase, String quantity,List<Produkt> produkts) {
		super();
		this.searchPhrase = searchPhrase;
		this.quantity=quantity;
		this.produkts =new ArrayList<Produkt>();
		if(produkts!=null)
			this.produkts.addAll(produkts);
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
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
