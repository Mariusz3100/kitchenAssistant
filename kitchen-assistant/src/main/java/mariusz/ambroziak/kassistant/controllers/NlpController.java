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
import mariusz.ambroziak.kassistant.exceptions.GoogleDriveAccessNotAuthorisedException;
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
	
}
