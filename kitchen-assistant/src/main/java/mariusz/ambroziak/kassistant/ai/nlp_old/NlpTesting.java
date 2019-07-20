package mariusz.ambroziak.kassistant.ai.nlp_old;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import mariusz.ambroziak.kassistant.ai.FilesProvider;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class NlpTesting {

	//	public static String paragraph="1 (15-ounce) can diced tomatoes, with juice";
	//	public static String paragraph="600ml milk";
	//	public static String paragraph="1/4 cup water";
	public static String paragraph="2tbsps olive oil ( or reserved oil from the sun-dried tomatoes)";


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

	public static ArrayList<String> givenEnglishDictionary_whenLemmatize_thenLemmasAreDetected() 
			throws Exception {

		SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
		String[] tokens = tokenizer.tokenize("4 onions");
		ArrayList<String> lines=new ArrayList<String>();

		InputStream inputStreamPOSTagger = FilesProvider.getInstance().getPosModelFile().getInputStream();
		POSModel posModel = new POSModel(inputStreamPOSTagger);
		POSTaggerME posTagger = new POSTaggerME(posModel);
		String tags[] = posTagger.tag(tokens);
		InputStream dictLemmatizer = FilesProvider.getInstance().getLemmaModelFile().getInputStream();
		DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(dictLemmatizer);
		String[] lemmas = lemmatizer.lemmatize(tokens, tags);

		String line = tokens[0]+" : "+tags[0]+" : "+lemmas[0];

		lines.add(line);
		line = tokens[1]+" : "+tags[1]+" : "+lemmas[1];

		lines.add(line);
		return lines;

	}


	public static ArrayList<String> 
	givenChunkerModel_whenChunk_thenChunksAreDetected() 
			throws Exception {
		ArrayList<String> lines=new ArrayList<String>();

		SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
//		String phrase = "He reckons the current account deficit will narrow to only 8 billion.";
		String phrase = " 2tbsps olive oil ( or reserved oil from the sun-dried tomatoes) ";
		
		String[] tokens = tokenizer.tokenize(phrase);

		InputStream inputStreamPOSTagger = FilesProvider.getInstance().getPosModelFile().getInputStream();
		POSModel posModel = new POSModel(inputStreamPOSTagger);
		POSTaggerME posTagger = new POSTaggerME(posModel);
		String tags[] = posTagger.tag(tokens);

		InputStream inputStreamChunker =FilesProvider.getInstance().getChunkerModelFile().getInputStream();
		ChunkerModel chunkerModel = new ChunkerModel(inputStreamChunker);
		ChunkerME chunker = new ChunkerME(chunkerModel);
		String[] chunks = chunker.chunk(tokens, tags);

		for(int i=0;i<chunks.length;i++) {
			String line = tokens[i]+" : "+tags[i]+" : "+chunks[i];

			lines.add(line);
		}
		return lines;
	}

	
	public static void parsingSentences() throws Exception{  
	      //Loading parser model 
	      InputStream inputStream =FilesProvider.getInstance().getParserChunkerModelFile().getInputStream(); 
	      ParserModel model = new ParserModel(inputStream); 
	       
	      //Creating a parser 
	      Parser parser = ParserFactory.create(model); 
	      
	      //Parsing the sentence 
	//      String sentence = "Tutorialspoint is the largest tutorial library.";
			String sentence = " 2tbsps olive oil ( or reserved oil from the sun-dried tomatoes) ";

	      
	      Parse topParses[] = ParserTool.parseLine(sentence, parser, 2); 
	    
	      for (Parse p : topParses) 
	         p.show();   
	      
	      
	   } 
}
