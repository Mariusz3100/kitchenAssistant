package mariusz.ambroziak.kassistant.model.jsp;

public class SkippedSearchResult{

	protected SearchPhrase phraseAnswered;
	
	public SearchPhrase getPhraseAnswered() {
		return phraseAnswered;
	}

	public void setPhraseAnswered(SearchPhrase phraseAnswered) {
		this.phraseAnswered = phraseAnswered;
	}

	public SkippedSearchResult(String phrase) {
		this.phraseAnswered=new SearchPhrase(phrase);
		
	}

}
