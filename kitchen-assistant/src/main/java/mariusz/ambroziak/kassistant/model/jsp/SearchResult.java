package mariusz.ambroziak.kassistant.model.jsp;

public abstract class SearchResult {

	protected String searchPhrase;
	protected String produktPhrase;
	protected String quantity;

	public String getProduktPhrase() {
		return produktPhrase;
	}

	public void setProduktPhrase(String produktPhrase) {
		this.produktPhrase = produktPhrase;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
			this.quantity = quantity;
		}
	//	public String getSearchPhraseEncoded() {
	//		String retValue=
	//				Base64.getEncoder().encodeToString(searchPhrase.getBytes());
	//		return retValue;
	//	}

	public String getSearchPhrase() {
		return searchPhrase;
	}

	public void setSearchPhrase(String searchPhrase) {
		this.searchPhrase = searchPhrase;
	}

}
