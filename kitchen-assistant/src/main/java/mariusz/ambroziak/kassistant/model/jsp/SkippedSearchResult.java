package mariusz.ambroziak.kassistant.model.jsp;

public class SkippedSearchResult{

	protected SearchPhrase phraseAnswered;
	
	public SkippedSearchResult(String phrase) {
		this.phraseAnswered=new SearchPhrase(phrase);
		
	}

}
