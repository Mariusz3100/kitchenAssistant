package mariusz.ambroziak.kassistant.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mariusz.ambroziak.kassistant.Apiclients.edaman.nutrientClients.EdamaneIngredientParsingApiClient;
import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleCalendarApiClient;
import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleDriveApiClient;
import mariusz.ambroziak.kassistant.Apiclients.walmart.ConvertApiClient;
import mariusz.ambroziak.kassistant.Apiclients.wikipedia.WikipediaApiClient;
import mariusz.ambroziak.kassistant.Apiclients.wordsapi.WordNotFoundException;
import mariusz.ambroziak.kassistant.Apiclients.wordsapi.WordsApiClient;
import mariusz.ambroziak.kassistant.Apiclients.wordsapi.WordsApiResult;
import mariusz.ambroziak.kassistant.ai.FilesProvider;
import mariusz.ambroziak.kassistant.ai.categorisation.edaman.IngredientUnparsedApiDetails;
import mariusz.ambroziak.kassistant.ai.categorisation.shops.Categoriser;
import mariusz.ambroziak.kassistant.ai.categorisation.shops.Category;
import mariusz.ambroziak.kassistant.ai.categorisation.shops.CategoryHierarchy;
import mariusz.ambroziak.kassistant.ai.nlp_old.EngTokenizer;
import mariusz.ambroziak.kassistant.ai.nlp_old.NlpTesting;
import mariusz.ambroziak.kassistant.ai.nlp_old.QuantityExtractor;
import mariusz.ambroziak.kassistant.ai.nlp_old.WordClassification;
import mariusz.ambroziak.kassistant.ai.nlp_old.enums.WordType;
import mariusz.ambroziak.kassistant.exceptions.GoogleDriveAccessNotAuthorisedException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.tesco.TescoApiClient;
import mariusz.ambroziak.kassistant.tesco.TescoApiClientParticularProduct_notUsed;
import mariusz.ambroziak.kassistant.utils.StringHolder;


@Controller
public class NlpController {

	


	
	
	@RequestMapping(value="/tokenize_ingredientPhrases")
	public ModelAndView tokenize_ingredientPhrases() {
		ModelAndView model = new ModelAndView("List");
		ArrayList<String> list=new ArrayList<>();
		
		
		String teachingEdamanContents = getTeachingEdamanContents();

		String[] splited=teachingEdamanContents.split("<");

		for(String line:splited) {
			String[] lineSplitted=line.split("->");

			String productName=lineSplitted[0];

			String[] tokenized=EngTokenizer.tokenize(productName);

			String resultLine=productName+"->";

			for(String token:tokenized) {
				resultLine+=":"+token;
			}
			
			list.add(resultLine);

		}

		
		
		model.addObject("list",list);
		
		return model;
	}

	@RequestMapping(value="/check_wikipedia_redirects")
	public static ModelAndView check_wikipedia_redirects() throws Page404Exception {
		ArrayList<String> list=new ArrayList<String>();

		list.add("onions"+"->"+WikipediaApiClient.getRedirectIfAny("onions"));
//		list.add("tablespoon"+"->"+WikipediaApiClient.getRedirectIfAny("tablespoon"));
//		list.add("tbsp."+"->"+WikipediaApiClient.getRedirectIfAny("tbsp."));
		
		ModelAndView model = new ModelAndView("List");
		model.addObject("list",list);

		return model;
		
		
		
	}
	
	
	@RequestMapping(value="/check_convert_example")
	public static ModelAndView check_convert_example() throws WordNotFoundException {
		ArrayList<String> list=new ArrayList<String>();

//		list.add("tbsp"+"->"+ConvertApiClient.checkForTranslation("tbsp"));
//		list.add("tablespoon"+"->"+ConvertApiClient.checkForTranslation("tablespoon"));
		list.add("pinch"+"->"+ConvertApiClient.checkForTranslation("pinch"));
		
		ModelAndView model = new ModelAndView("List");
		model.addObject("list",list);

		return model;
		
		
		
	}
	
	private static String getTeachingEdamanContents() {
		Resource teachingExpectationsFile = FilesProvider.getInstance().getTeachingEdamanFile();
		StringBuilder content=new StringBuilder();

		InputStream inputStream;
		try {
			inputStream = teachingExpectationsFile.getInputStream();

			BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
			String temp=br.readLine();
			while(temp!=null) {
				content.append(temp);
				temp=br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content.toString();
	}
	
	
	@RequestMapping(value="/quantity_mark")
	public ModelAndView bhome() throws IOException {
		InputStream wordsInputStream = FilesProvider.getInstance().getWordsInputFile().getInputStream();
		ArrayList<String> list=new ArrayList<>();
		BufferedReader reader=new BufferedReader(new InputStreamReader(wordsInputStream));

		String line=reader.readLine();

		while(line!=null) {
			ArrayList<WordClassification> classifyWords = QuantityExtractor.classifyWords(line);
			String single="";
			for(WordClassification classificated:classifyWords ) {
				if(classificated.getType()==null) {

					single+="<span style=\"background-color:red\">"+classificated.getWord()+"</span> ";
				}else if(classificated.getType()==WordType.QuantityElement) {

					single+="<span style=\"background-color:blue\">"+classificated.getWord()+"</span>";
				}else if(classificated.getType()==WordType.ProductElement) {

					single+="<span style=\"background-color:green\">"+classificated.getWord()+"</span>";
				}

			}
			list.add(single);

			line=reader.readLine();
		}

		ModelAndView model = new ModelAndView("List");
		model.addObject("list",list);

		return model;
	}

}
