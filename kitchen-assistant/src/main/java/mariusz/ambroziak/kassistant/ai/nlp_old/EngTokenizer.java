package mariusz.ambroziak.kassistant.ai.nlp_old;

import java.io.IOException;
import java.io.InputStream;

import mariusz.ambroziak.kassistant.ai.FilesProvider;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class EngTokenizer {
	private static TokenizerModel model;
	
	
	public static String[] tokenize(String phrase) {
		if(model==null)
			initializeModel();
		
		TokenizerME tokenizer=new TokenizerME(model);
		String[] tokenized = tokenizer.tokenize(phrase);
		
		return tokenized;
	}


	private static void initializeModel() {
		InputStream is;
		try {
			is = FilesProvider.getInstance().getTokenModelFile().getInputStream();

			model = new TokenizerModel(is);
		} catch (IOException e) {
			ProblemLogger.logProblem("Problem during initialization tokenizer: "+e.getMessage());
			ProblemLogger.logStackTrace(e.getStackTrace());
		}
		
	}
}
