package mariusz.ambroziak.kassistant.ai.nlp_old;

import mariusz.ambroziak.kassistant.ai.nlp_old.enums.WordType;

public class WordClassification {
	private String word;
	private WordType type;
	
	
	public WordClassification(String word, WordType type) {
		super();
		this.word = word;
		this.type = type;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public WordType getType() {
		return type;
	}
	public void setType(WordType type) {
		this.type = type;
	}
	
}
