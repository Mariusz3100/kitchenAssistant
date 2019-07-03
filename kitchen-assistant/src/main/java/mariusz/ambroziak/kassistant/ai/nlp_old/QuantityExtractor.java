package mariusz.ambroziak.kassistant.ai.nlp_old;

import java.util.ArrayList;

import mariusz.ambroziak.kassistant.Apiclients.wordsapi.WordNotFoundException;
import mariusz.ambroziak.kassistant.Apiclients.wordsapi.WordsApiClient;
import mariusz.ambroziak.kassistant.Apiclients.wordsapi.WordsApiResult;
import mariusz.ambroziak.kassistant.ai.nlp_old.enums.WordType;

public class QuantityExtractor {

	
	public static ArrayList<WordClassification> classifyWords(String phrase) {
		ArrayList<WordClassification> retValue=new ArrayList<WordClassification>();
		
		String[] tokenized = EngTokenizer.tokenize(phrase);
		
		String[] tags=EngPosTagger.tagWithPos(tokenized);
		for(int i=0;i<tokenized.length;i++) {
			WordType classifiedWord=null;
			if("CD".equals(tags[i])) {
				classifiedWord=WordType.QuantityElement;
			}else {
				classifiedWord = classifyWord(tokenized[i]);
			}
			retValue.add(new WordClassification(tokenized[i],classifiedWord));
		}
		
		return retValue;
	}
	
	
	
	public static WordType classifyWord(String token) {
		WordType retValue=null;
		
//		try {
//			ArrayList<WordsApiResult> wordsResults = WordsApiClient.searchFor(token);
//			
//			if(wordsResults!=null&&!wordsResults.isEmpty()) {
//
//			}
//				
//				
//		} catch (WordNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return retValue;
	}	
	
}
