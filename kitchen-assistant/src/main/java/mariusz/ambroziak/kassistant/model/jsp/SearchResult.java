package mariusz.ambroziak.kassistant.model.jsp;

import java.util.ArrayList;
import java.util.List;

import mariusz.ambroziak.kassistant.model.Produkt;

public class SearchResult {
	private String searchPhrase;
	private ArrayList<Produkt> produkts;
	
	public SearchResult(String searchPhrase, List<Produkt> produkts) {
		super();
		this.searchPhrase = searchPhrase;
		this.produkts =new ArrayList<Produkt>();
		if(produkts!=null)
			this.produkts.addAll(produkts);
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
