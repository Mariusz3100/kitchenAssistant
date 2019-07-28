package mariusz.ambroziak.kassistant.ai.nlp_old;

import java.io.IOException;
import java.io.InputStream;

import mariusz.ambroziak.kassistant.ai.FilesProvider;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

public class EngConstituencyParser {






	private static ParserModel model;
	private static Parser parser;

	public static Parse parsingSentences(String sentence) throws IOException{  
		//Loading parser model 
		if(model==null)
			initializeModelAndParser(); 


		Parse topParses[] = ParserTool.parseLine(sentence, parser, 1); 

		if(topParses==null||topParses.length==0)
			return null;
		else
			return topParses[0];


	}

	private static void initializeModelAndParser() throws IOException {
		InputStream inputStream =FilesProvider.getInstance().getParserChunkerModelFile().getInputStream(); 


		model = new ParserModel(inputStream);
		parser = ParserFactory.create(model);
	} 
}
