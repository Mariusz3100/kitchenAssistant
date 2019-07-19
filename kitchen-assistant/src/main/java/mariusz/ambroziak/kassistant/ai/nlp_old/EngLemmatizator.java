package mariusz.ambroziak.kassistant.ai.nlp_old;

import java.io.IOException;
import java.io.InputStream;

import mariusz.ambroziak.kassistant.ai.FilesProvider;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class EngLemmatizator {


	private static DictionaryLemmatizer lemmatizer;


	public static String[] lemmatize(String[] tokens,String[] tags) {
		if(lemmatizer==null)
			initializeLemmatizer();

		if(lemmatizer==null) {
			return null;
		}else {
			String[] lemmas = lemmatizer.lemmatize(tokens, tags);
			return lemmas;
		}
	}


	private static void initializeLemmatizer() {
		InputStream dictLemmatizer;
		try {
			dictLemmatizer = FilesProvider.getInstance().getLemmaModelFile().getInputStream();

			lemmatizer = new DictionaryLemmatizer(dictLemmatizer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	
}
