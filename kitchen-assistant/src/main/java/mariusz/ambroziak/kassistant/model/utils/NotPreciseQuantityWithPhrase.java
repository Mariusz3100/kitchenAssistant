package mariusz.ambroziak.kassistant.model.utils;

import mariusz.ambroziak.kassistant.model.quantity.NotPreciseQuantity;

public class NotPreciseQuantityWithPhrase {
	private String phrase;
	private NotPreciseQuantity quan;
	
	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public void setQuan(NotPreciseQuantity quan) {
		this.quan = quan;
	}

	public NotPreciseQuantityWithPhrase(String phrase, NotPreciseQuantity quan) {
		super();
		this.phrase = phrase;
		this.quan = quan;
	}

	
	
	public NotPreciseQuantity getQuan() {
		return quan;
	}

	@Override
	public String toString() {
		return "QuantityPhraseClone [phrase=" + phrase + ", quan=" + quan + "]";
	}
}
