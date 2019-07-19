package mariusz.ambroziak.kassistant.ai.nlp_old;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import mariusz.ambroziak.kassistant.ai.FilesProvider;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class NlpTesting {

//	public static String paragraph="1 (15-ounce) can diced tomatoes, with juice";
	public static String paragraph="600ml milk";
	
	
	public static void main(String[] args) throws Exception {
		givenEnglishModel_whenDetect_thenSentencesAreDetected();
		givenEnglishModel_whenTokenize_thenTokensAreDetected();
	}

	public static ArrayList<String> givenEnglishModel_whenDetect_thenSentencesAreDetected() 
	{
		ArrayList<String> lines=new ArrayList<String>();

		InputStream is;
		try {
			is = FilesProvider.getInstance().getSentModelFile().getInputStream();

			SentenceModel model = new SentenceModel(is);

			SentenceDetectorME sdetector = new SentenceDetectorME(model);

			String sentences[] = sdetector.sentDetect(paragraph);
			lines.add("Source: "+paragraph);
			for(String s:sentences) {
				lines.add("Sentence:"+s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;
	}

	public static ArrayList<String> givenEnglishModel_whenTokenize_thenTokensAreDetected() 
	{
		ArrayList<String> lines=new ArrayList<String>();

		InputStream is;
		try {
			is = FilesProvider.getInstance().getTokenModelFile().getInputStream();

			TokenizerModel model = new TokenizerModel(is);
			TokenizerME tokenizer = new TokenizerME(model);
			String[] tokens = tokenizer.tokenize(paragraph);

			lines.add("Source: "+paragraph);

			for(String s:tokens) {
				lines.add("Token:"+s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;

	}


	public static ArrayList<String> givenPOSModel_whenPOSTagging_thenPOSAreDetected() 
			throws Exception {

		InputStream is = FilesProvider.getInstance().getPosModelFile().getInputStream();
		ArrayList<String> lines=new ArrayList<String>();


		SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
		String[] tokens = tokenizer.tokenize(paragraph);

		InputStream inputStreamPOSTagger =is;
		POSModel posModel = new POSModel(inputStreamPOSTagger);
		POSTaggerME posTagger = new POSTaggerME(posModel);
		String tags[] = posTagger.tag(tokens);
		System.out.println("Token: | Tag:");

		for(int i=0;i<tokens.length;i++) {
			lines.add("Token:"+tokens[i]+" | Pos:"+tags[i]);
		}
		return lines;

	}


}