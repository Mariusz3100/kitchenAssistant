package mariusz.ambroziak.kassistant.model.jsp;

import java.util.List;

import mariusz.ambroziak.kassistant.model.Produkt;

public class InvalidSearchResult extends MultiProdukt_SearchResult {

	private String invalidityReason;
	
	public String getInvalidityReason() {
		return invalidityReason;
	}

	public void setInvalidityReason(String invalidUrl) {
		this.invalidityReason = invalidUrl;
	}

	public InvalidSearchResult(String searchPhrase,String produktPhrase, String quantity,
			List<Produkt> produkts,String invalidUrl) {
		super(searchPhrase, produktPhrase, quantity, produkts);
		
		this.invalidityReason=invalidUrl;
	}

}
