package mariusz.ambroziak.kassistant.Apiclients.wordsapi;

import java.util.ArrayList;

public class WordsApiResult {
	public String getTypeOf() {
		return typeOf;
	}
	public void setTypeOf(String typeOf) {
		this.typeOf = typeOf;
	}
	private String originalWord;
	private String baseWord;
	private String definition;
	private String typeOf;


	private String partOfSpeech;
	private ArrayList<String> childTypes;
	
	
	public WordsApiResult(String originalWord, String baseWord, String definition, String typeOf, String partOfSpeech,
			ArrayList<String> childTypes) {
		super();
		this.originalWord = originalWord;
		this.baseWord = baseWord;
		this.definition = definition;
		this.typeOf = typeOf;
		this.partOfSpeech = partOfSpeech;
		this.childTypes = childTypes;
	}
	public ArrayList<String> getChildTypes() {
		return childTypes;
	}
	public void setChildTypes(ArrayList<String> childTypes) {
		this.childTypes = childTypes;
	}
	public void addChildType(String childType) {
		if(childTypes==null)
		
		this.childTypes.add(childType);
	}

	public String getOriginalWord() {
		return originalWord;
	}
	public void setOriginalWord(String originalWord) {
		this.originalWord = originalWord;
	}
	public String getBaseWord() {
		return baseWord;
	}
	public void setBaseWord(String baseWord) {
		this.baseWord = baseWord;
	}
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public String getPartOfSpeech() {
		return partOfSpeech;
	}
	public void setPartOfSpeech(String partOfSpeech) {
		this.partOfSpeech = partOfSpeech;
	}
}
