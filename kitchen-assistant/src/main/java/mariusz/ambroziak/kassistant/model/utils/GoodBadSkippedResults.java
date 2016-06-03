package mariusz.ambroziak.kassistant.model.utils;

import java.util.ArrayList;

import mariusz.ambroziak.kassistant.model.jsp.InvalidSearchResult;
import mariusz.ambroziak.kassistant.model.jsp.SingleProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.jsp.SkippedSearchResult;

public class GoodBadSkippedResults {
	private ArrayList<SingleProdukt_SearchResult> goodResults= new ArrayList<SingleProdukt_SearchResult>();
	private ArrayList<InvalidSearchResult> usersBadChoice= new ArrayList<InvalidSearchResult>();
	private ArrayList<SkippedSearchResult> skippedResults= new ArrayList<SkippedSearchResult>();
	public ArrayList<SingleProdukt_SearchResult> getGoodResults() {
		return goodResults;
	}
	public void addGoodResult(SingleProdukt_SearchResult goodResult) {
		this.goodResults.add(goodResult);
	}
	public ArrayList<InvalidSearchResult> getUsersBadChoice() {
		return usersBadChoice;
	}
	public void addUsersBadChoice(InvalidSearchResult usersBadChoice) {
		this.usersBadChoice.add(usersBadChoice);
	}
	public ArrayList<SkippedSearchResult> getSkippedResults() {
		return skippedResults;
	}
	public void addSkippedResults(SkippedSearchResult skippedSearchResult) {
		this.skippedResults.add(skippedSearchResult);
	}

	
	
	
}
