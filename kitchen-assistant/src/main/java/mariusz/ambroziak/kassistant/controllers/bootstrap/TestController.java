package mariusz.ambroziak.kassistant.controllers.bootstrap;

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
import mariusz.ambroziak.kassistant.Apiclients.wordsapi.WordNotFoundException;
import mariusz.ambroziak.kassistant.Apiclients.wordsapi.WordsApiClient;
import mariusz.ambroziak.kassistant.Apiclients.wordsapi.WordsApiResult;
import mariusz.ambroziak.kassistant.ai.FilesProvider;
import mariusz.ambroziak.kassistant.ai.categorisation.shops.Categoriser;
import mariusz.ambroziak.kassistant.ai.categorisation.shops.Category;
import mariusz.ambroziak.kassistant.ai.categorisation.shops.CategoryHierarchy;
import mariusz.ambroziak.kassistant.ai.nlp_old.NlpTesting;
import mariusz.ambroziak.kassistant.exceptions.GoogleDriveAccessNotAuthorisedException;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.tesco.TescoApiClient;
import mariusz.ambroziak.kassistant.tesco.TescoApiClientParticularProduct_notUsed;
import mariusz.ambroziak.kassistant.utils.StringHolder;


@Controller
public class TestController {

	
	@RequestMapping(value="/btest")
	public ModelAndView bhome() {
		ModelAndView model = new ModelAndView("List");
		ArrayList<String> list=new ArrayList<>();
		list.add("hello world");
		model.addObject("list",list);
		
		return model;
	}

	@RequestMapping(value="/bradio")
	public ModelAndView bradio() {
		ModelAndView model = new ModelAndView(StringHolder.bootstrapFolder+"radioTest");
		
		
		return model;
	}
	
	@RequestMapping(value="/google_test")
	public ModelAndView b_main() throws IOException, GoogleDriveAccessNotAuthorisedException {
		GoogleDriveApiClient.main(null);
		
		return null;
	}
	
	@RequestMapping(value="/google_calendar_test")
	public ModelAndView google_calendar_test() throws IOException, GoogleDriveAccessNotAuthorisedException {
		GoogleCalendarApiClient.main(null);
		
		return null;
	}

	
	
	
	@RequestMapping(value="/edamanParsingTest")
	public ModelAndView edamanParsingTest() {
		EdamaneIngredientParsingApiClient.main(null);
		return new ModelAndView();
	}
	

	
	@RequestMapping(value="/tesco_test")
	public ModelAndView tesco_test() {
		TescoApiClient.main(null);
		return new ModelAndView();
	}
	

	@RequestMapping(value="/tesco_particular_test")
	public ModelAndView tesco_particular_test() {
		TescoApiClientParticularProduct_notUsed.main(null);
		return new ModelAndView();
	}
	

	@RequestMapping(value="/words_test")
	public ModelAndView words_test() {
		WordsApiClient.main(null);
		return new ModelAndView();
	}
	
	@RequestMapping(value="/sentences_test")
	public ModelAndView detect_sentences_test() throws Exception {
		ArrayList<String> lines = NlpTesting.givenEnglishModel_whenDetect_thenSentencesAreDetected();
		ArrayList<String> lines2 = NlpTesting.givenEnglishModel_whenTokenize_thenTokensAreDetected();
		lines.addAll(lines2);
		
		ArrayList<String> lines3=NlpTesting.givenPOSModel_whenPOSTagging_thenPOSAreDetected();
		lines.addAll(lines3);
		
		ModelAndView model = new ModelAndView("List");
		model.addObject("list",lines);

		return model;
	}
	
	@RequestMapping(value="/tokenize_test")
	public ModelAndView tokenize_test() {
		NlpTesting.givenEnglishModel_whenTokenize_thenTokensAreDetected();
		return new ModelAndView();
	}
	
	@RequestMapping(value="/word_definition_test")
	public ModelAndView word_definition_test(HttpServletRequest request) {
		String phrase=request.getParameter("phrase");
		ArrayList<WordsApiResult> searchFor;
		ModelAndView model = new ModelAndView("List");
		ArrayList<String> list=new ArrayList<>();

		try {
			searchFor = WordsApiClient.searchFor(phrase);
			for(WordsApiResult war:searchFor) {
				list.add("<br>"+war.getOriginalWord()+" -> "+war.getBaseWord()+" -> "+war.getDefinition());
			}
			model.addObject("list",list);

		} catch (WordNotFoundException e) {
			list.add("<br>Words not found for "+phrase);
		}

		return model;
	}
	
	
}
