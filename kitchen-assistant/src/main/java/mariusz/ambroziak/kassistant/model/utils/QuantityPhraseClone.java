package mariusz.ambroziak.kassistant.model.utils;


public class QuantityPhraseClone {
	private String phrase;
	private AbstractQuantity quan;
	
	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public void setQuan(AbstractQuantity quan) {
		this.quan = quan;
	}

	public QuantityPhraseClone(String phrase, AbstractQuantity quan) {
		super();
		this.phrase = phrase;
		this.quan = quan;
	}

	
	
	public AbstractQuantity getQuan() {
		return quan;
	}

	
}
