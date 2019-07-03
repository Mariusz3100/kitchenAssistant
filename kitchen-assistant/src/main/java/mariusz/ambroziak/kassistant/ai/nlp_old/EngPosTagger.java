package mariusz.ambroziak.kassistant.ai.nlp_old;

import java.io.IOException;
import java.io.InputStream;


import mariusz.ambroziak.kassistant.ai.FilesProvider;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class EngPosTagger {
	private static POSModel posModel;
	
	
	public static String[] tagWithPos(String[] phrases) {
		if(posModel==null)
			initializeModel();
		
		
		POSTaggerME posTagger=new POSTaggerME(posModel);
		String[] results = posTagger.tag(phrases);
		
		return results;
	}

	public static String tagWithPos(String phrase) {
		if(posModel==null)
			initializeModel();
		
		
		
		String[] results = tagWithPos(new String[] {phrase});
		
		return results[0];
	}


	private static void initializeModel() {
		InputStream is;
		try {
			is = FilesProvider.getInstance().getPosModelFile().getInputStream();

			posModel = new POSModel(is);
		} catch (IOException e) {
			ProblemLogger.logProblem("Problem during initialization PosTager: "+e.getMessage());
			ProblemLogger.logStackTrace(e.getStackTrace());
		}
		
	}
}
