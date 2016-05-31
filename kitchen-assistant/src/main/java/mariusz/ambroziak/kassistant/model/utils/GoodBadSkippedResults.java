package mariusz.ambroziak.kassistant.model.utils;

import java.util.ArrayList;

import mariusz.ambroziak.kassistant.model.jsp.InvalidSearchResult;
import mariusz.ambroziak.kassistant.model.jsp.SingleProdukt_SearchResult;

public class GoodBadSkippedResults {
	private ArrayList<SingleProdukt_SearchResult> goodResults= new ArrayList<SingleProdukt_SearchResult>();
	private ArrayList<InvalidSearchResult> usersBadChoice= new ArrayList<InvalidSearchResult>();
	private ArrayList<String> skippedResults= new ArrayList<String>();
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
	public ArrayList<String> getSkippedResults() {
		return skippedResults;
	}
	public void addSkippedResults(ArrayList<String> skippedResults) {
		this.skippedResults = skippedResults;
	}

	
	
	
}
