package mariusz.ambroziak.kassistant.model.jsp;

import java.util.List;

import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;

public class InvalidSearchResult extends MultiProdukt_SearchResult {

	private String invalidityReason;
	private PreciseQuantity quantityNeeded;


	public PreciseQuantity getQuantityNeeded() {
		quantityNeeded=PreciseQuantity.parseFromJspString(quantityPhrase);
		return quantityNeeded;
	}

	public void setQuantityNeeded(PreciseQuantity quantityNeeded) {
		this.quantityNeeded = quantityNeeded;
	}

	public String getInvalidityReason() {
		return invalidityReason;
	}

	public void setInvalidityReason(String invalidUrl) {
		this.invalidityReason = invalidUrl;
	}

	public InvalidSearchResult(String searchPhrase,String produktPhrase, String quantity,
			List<Produkt> produkts,String invalidityReason) {
		super(searchPhrase, produktPhrase, quantity, produkts);
		
		this.invalidityReason=invalidityReason;
	}

}
