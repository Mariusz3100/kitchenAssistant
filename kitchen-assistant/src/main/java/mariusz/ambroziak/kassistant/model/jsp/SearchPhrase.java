package mariusz.ambroziak.kassistant.model.jsp;

public class SearchPhrase {

	protected String searchPhrase;

	public SearchPhrase(String searchPhrase) {
		super();
		this.searchPhrase=searchPhrase;
	}

	@Override
	public String toString() {
		return searchPhrase;
	}

	public String getSearchPhrase() {
		return searchPhrase;
	}

	public void setSearchPhrase(String searchPhrase) {
		this.searchPhrase = searchPhrase;
	}

}