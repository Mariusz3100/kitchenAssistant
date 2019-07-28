package mariusz.ambroziak.kassistant.ai.nlp_old;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;

import java.util.regex.Pattern;

import api.extractors.AbstractQuantityEngExtractor.QuantityTranslation;
import mariusz.ambroziak.kassistant.Apiclients.walmart.ConvertApiClient;
import mariusz.ambroziak.kassistant.Apiclients.wikipedia.WikipediaApiClient;
import mariusz.ambroziak.kassistant.Apiclients.wordsapi.WordNotFoundException;
import mariusz.ambroziak.kassistant.Apiclients.wordsapi.WordsApiClient;
import mariusz.ambroziak.kassistant.Apiclients.wordsapi.WordsApiResult;
import mariusz.ambroziak.kassistant.ai.nlp_old.enums.WordType;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;

public class QuantityExtractor {
	public static final String numberRegex="\\d+(\\/\\d+)*(\\.\\d+)*(x\\d+)*";
	public static final String punctationsRegex="[^a-zA-Z\\d\\s]*";
	public static ArrayList<String> productTypeKeywords;
	public static ArrayList<String> irrelevanceKeywords;
	public static ArrayList<String> quantityTypeKeywords;
	


	static {
		productTypeKeywords=new ArrayList<String>();

		productTypeKeywords.add("vegetable");


		productTypeKeywords.add("flavouring");
		productTypeKeywords.add("seasoning");
		productTypeKeywords.add("dairy");
		productTypeKeywords.add("meat");
		productTypeKeywords.add("food");





		irrelevanceKeywords=new ArrayList<String>();
		irrelevanceKeywords.add("activity");
		irrelevanceKeywords.add("love");


		quantityTypeKeywords=new ArrayList<String>();
		quantityTypeKeywords.add("containerful");
		quantityTypeKeywords.add("small indefinite quantity");
		
		

	}

	public static ArrayList<WordClassification> classifyWords(String phrase) {
		phrase=correctErrors(phrase);

		ArrayList<WordClassification> retValue=new ArrayList<WordClassification>();

		String[] tokenized = EngTokenizer.tokenize(phrase);
		
		String[] tags=EngPosTagger.tagWithPos(tokenized);
		String[] lemmatized=EngLemmatizator.lemmatize(tokenized, tags);

		for(int i=0;i<tokenized.length;i++) {
			WordType classifiedWord=null;
			if("CD".equals(tags[i])) {
				classifiedWord=WordType.QuantityElement;
			}else {
					classifiedWord = classifyWord(tokenized[i],lemmatized[i]);
				
			}
			retValue.add(new WordClassification(tokenized[i],classifiedWord));
		}

		return retValue;
	}



	private static String correctErrors(String phrase) {

		phrase=phrase.replaceFirst("½", "1/2");
		phrase=phrase.replaceFirst("¼", "1/4");
		if(phrase.substring(0, phrase.length()<10?phrase.length():10).indexOf(" c ")>0) {
			phrase=phrase.replaceFirst(" c ", " cup ");
		}

		//		if(phrase.substring(0, 10).indexOf("x")>0) {
		//			phrase=phrase.replaceFirst(" c ", " cup ");
		//		}

		return phrase;
	}



	public static WordType classifyWord(String token,String lemma) {
		if(java.util.regex.Pattern.matches(numberRegex, token)) {
			return WordType.QuantityElement;
		}
		if(java.util.regex.Pattern.matches(punctationsRegex, token)) {
			return null;
		}

		WordType retValue=null;

		try {
	//		WordsApiClient.searchFor("baking soda");
			ArrayList<WordsApiResult> wordResults = WordsApiClient.searchFor(token);

			if(wordResults==null||wordResults.isEmpty()) {

				if(lemma!=null&&!lemma.isEmpty()&&!lemma.equals("O"))
				{
					wordResults = WordsApiClient.searchFor(lemma);
					
				}
			}
			if(wordResults==null||wordResults.isEmpty()) {

				if(lemma!=null&&!lemma.isEmpty())
				{
					QuantityTranslation checkForTranslation = ConvertApiClient.checkForTranslation(token);
					if(checkForTranslation!=null) {
						return WordType.QuantityElement;
					}
				}
			}
			if(wordResults==null||wordResults.isEmpty()) {

				String baseWord = WikipediaApiClient.getRedirectIfAny(token);

				if(baseWord==null||baseWord.isEmpty())
				{
					QuantityTranslation checkForTranslation = ConvertApiClient.checkForTranslation(token);
					if(checkForTranslation!=null) {
						return WordType.QuantityElement;
					}
				}
				if(baseWord!=null&&!baseWord.isEmpty())
				{
					wordResults = WordsApiClient.searchFor(baseWord);
				}
			}

			if(wordResults!=null&&!wordResults.isEmpty()) {
				if(checkQuantityTypesForWordObject(wordResults)) {
					return WordType.QuantityElement;
				}else if(checkProductTypesForWordObject(wordResults)) {
					return WordType.ProductElement;
				}else {
					return null;
				}
			}


		} catch (WordNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();//ignore
		} catch (Page404Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();//ignore
		}

		return retValue;
	}



	private static boolean checkProductTypesForWordObject(ArrayList<WordsApiResult> wordResults) {
		for(WordsApiResult war:wordResults) {
			if(checkIfTypesContainKeywords(war.getOriginalWord(),war.getTypeOf(),productTypeKeywords)) {
				return true;
			}
		}
		return false;		
	}


	private static boolean checkQuantityTypesForWordObject(ArrayList<WordsApiResult> wordResults) {
		for(WordsApiResult war:wordResults) {
			if(checkIfTypesContainKeywords(war.getOriginalWord(),war.getTypeOf(),quantityTypeKeywords)) {
				return true;
			}
		}
		return false;		
	}
	private static boolean checkTypesRecursively(ArrayList<String> typeResults) {
		if(typeResults!=null&&!typeResults.isEmpty()) {
			for(String typeToBeChecked:typeResults) {
				for(String typeConsidered:productTypeKeywords) {
					if(typeToBeChecked.indexOf(typeConsidered)>=0) {
						System.err.println(typeToBeChecked+" : "+typeConsidered);
						return true;
					}else {
						try {
							ArrayList<String> typesOf = WordsApiClient.getTypesOf(typeToBeChecked);
							return checkTypesRecursively(typesOf);
						} catch (WordNotFoundException e) {
							
								//sometimes /typeOf endpoint does not rerturn properly
								ArrayList<WordsApiResult> x=WordsApiClient.searchFor(typeToBeChecked);
								ArrayList<String > retValue=new ArrayList<String>();
								for(WordsApiResult a:x) {
									retValue.addAll(a.getTypeOf());
								}
								return  checkTypesRecursively(retValue);
							
						}
					}
				}			
			}
		}
		return false;
	}



	private static boolean checkIfTypesContainKeywords(String productName, ArrayList<String> typeResults,ArrayList<String> keywords) {
		for(String typeToBeChecked:typeResults) {
			for(String typeConsidered:keywords) {
				if(typeToBeChecked.indexOf(typeConsidered)>=0) {
					System.err.println(productName+" -> "+typeToBeChecked+" : "+typeConsidered);

					return true;
				}
			}
		}
		return false;
	}





}
