package mariusz.ambroziak.kassistant.model.jsp;

public class SearchResult {

	protected SearchPhrase searchPhraseAnswered;
	public SearchPhrase getSearchPhraseAnswered() {
		return searchPhraseAnswered;
	}

	public void setSearchPhraseAnswered(SearchPhrase searchPhraseAnswered) {
		this.searchPhraseAnswered = searchPhraseAnswered;
	}
	protected String produktPhrase;
	protected String quantityPhrase;

	public String getProduktPhrase() {
		return produktPhrase;
	}

	public void setProduktPhrase(String produktPhrase) {
		this.produktPhrase = produktPhrase;
	}

	public String getQuantity() {
		return quantityPhrase;
	}

	public void setQuantity(String quantity) {
		this.quantityPhrase = quantity;
	}
	public SearchResult(String searchPhrase, String produktPhrase, String quantityPhrase) {
		this.searchPhraseAnswered=new SearchPhrase(searchPhrase);
		this.produktPhrase = produktPhrase;
		this.quantityPhrase = quantityPhrase;
	}

}
