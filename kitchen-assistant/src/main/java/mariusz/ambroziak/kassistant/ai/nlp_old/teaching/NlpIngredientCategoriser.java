package mariusz.ambroziak.kassistant.ai.nlp_old.teaching;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mariusz.ambroziak.kassistant.ai.FilesProvider;
import mariusz.ambroziak.kassistant.ai.nlp_old.EngConstituencyParser;
import mariusz.ambroziak.kassistant.ai.nlp_old.QuantityExtractor;
import mariusz.ambroziak.kassistant.ai.nlp_old.WordClassification;
import mariusz.ambroziak.kassistant.ai.nlp_old.enums.WordType;
import opennlp.tools.parser.Parse;

public class NlpIngredientCategoriser {

	public static ModelAndView categoriseFromFiles() throws IOException {
		InputStream wordsInputStream = FilesProvider.getInstance().getWordsInputFile().getInputStream();

		BufferedReader reader=new BufferedReader(new InputStreamReader(wordsInputStream));

		String line=reader.readLine();

		ArrayList<String> phrases=new ArrayList<String>();
		Map<String,String> fullConstituencies=new HashMap<String, String>();
		Map<String,String> nonQuantityConstituencies=new HashMap<String, String>();
		Map<String,String> markedPhrases=new HashMap<String, String>();
		Map<String,String> quantityPhrases=new HashMap<String, String>();
		Map<String,String> productPhrases=new HashMap<String, String>();


		while(line!=null) {
			//			parseSingleLine(line, phrases, constituencies,markedPhrases);

			phrases.add(line);


			ArrayList<WordClassification> classifyWords = QuantityExtractor.classifyWords(line);
			String colorResults="";
			String nonQuantityPhrase="",quantityPhrase="";

			for(WordClassification classificated:classifyWords ) {
				//				colorResults += colorSingleWord(classificated);

				if(classificated.getType()==null) {
					nonQuantityPhrase+=classificated.getWord()+" ";
					colorResults+="<span style=\"background-color:red\">"+classificated.getWord()+"</span> ";
				}else if(classificated.getType()==WordType.QuantityElement) {
					quantityPhrase+=classificated.getWord()+" ";
					colorResults+="<span style=\"background-color:blue\">"+classificated.getWord()+"</span>";
				}else {
					nonQuantityPhrase+=classificated.getWord()+" ";
					if(classificated.getType()==WordType.ProductElement) {
						colorResults+="<span style=\"background-color:green\">"+classificated.getWord()+"</span>";

					}
				}

			}

			markedPhrases.put(line, colorResults);
			quantityPhrases.put(line, quantityPhrase);
			productPhrases.put(line, nonQuantityPhrase);


			Parse constituency = EngConstituencyParser.parsingSentences(line);
			if(constituency!=null) {
				StringBuffer outToString=new StringBuffer();
				constituency.show(outToString);
				fullConstituencies.put(line, outToString.toString());
			}

			constituency = EngConstituencyParser.parsingSentences(nonQuantityPhrase);
			if(constituency!=null) {
				StringBuffer outToString=new StringBuffer();
				constituency.show(outToString);
				nonQuantityConstituencies.put(line, outToString.toString());
			}

			line=reader.readLine();	
		}




		ModelAndView model = new ModelAndView("ingredientCategorisationWorkspace");
		model.addObject("phrases",phrases);
		model.addObject("constituency",fullConstituencies);
		model.addObject("markedPhrases",markedPhrases);
		model.addObject("productPhrases",productPhrases);
		model.addObject("quantityPhrases",quantityPhrases);
		model.addObject("nonQuantityConstituencies",nonQuantityConstituencies);



		return model;
	}






}
