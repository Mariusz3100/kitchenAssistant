package mariusz.ambroziak.kassistant.Apiclients.wordsapi;

import mariusz.ambroziak.kassistant.exceptions.Page404Exception;

public class WordNotFoundException extends Exception{
	private String phrase;
	
	public WordNotFoundException(String phrase) {
		this.phrase=phrase;
	}

}
