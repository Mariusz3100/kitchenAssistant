package mariusz.ambroziak.kassistant.controllers.bootstrap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mariusz.ambroziak.kassistant.Apiclients.edaman.ParseableRecipeData;
import mariusz.ambroziak.kassistant.ai.FilesProvider;
import mariusz.ambroziak.kassistant.ai.categorisation.edaman.EdamanCategorisationTeacher;
import mariusz.ambroziak.kassistant.ai.categorisation.edaman.IngredientParsed;
import mariusz.ambroziak.kassistant.ai.categorisation.shops.IngredientCategoriser;
import mariusz.ambroziak.kassistant.exceptions.GoogleDriveAccessNotAuthorisedException;
import mariusz.ambroziak.kassistant.model.utils.ApiIngredientAmount;


@Controller
public class RecipeIngredientsCategorisation_bootstrap {

	
	@RequestMapping(value="/edaman_relevant_ingredients")
	public ModelAndView edaman_relevant_ingredients() throws IOException, GoogleDriveAccessNotAuthorisedException {
		String phrase="cucumber";
		
		ArrayList<ParseableRecipeData> relevantIngredientsFor = IngredientCategoriser.getRelevantIngredientsFor(phrase);
		ArrayList<ApiIngredientAmount> relevantIngredientsfiltered=new ArrayList<ApiIngredientAmount>();
		
		List<String> returnList = new ArrayList<String>();
		
		for(ParseableRecipeData entry:relevantIngredientsFor) {
			String edamanApi=entry.getEdamanId().replaceAll("#","%23");;
			String detailsUrl="https://api.edamam.com/search?app_id=af08be14&app_key=2ac175efa4ddfeff85890bed42dff521&r="+edamanApi;
			String lines="<br><h3>"+entry.getLabel()+"</h3>";
			lines+="<br><a href=\""+entry.getUrl()+"\">"+entry.getUrl()+"</a>";
			lines+="<br><a href=\""+detailsUrl+"\">"+entry.getEdamanId()+"</a>";
			
			for(int i=0;i<entry.getIngredients().size();i++) {
				ApiIngredientAmount apiIngredientAmount = entry.getIngredients().get(i);
				if(apiIngredientAmount.getName().indexOf(phrase)>=0) {
					lines+="<br><b>"+apiIngredientAmount.getName()+"</b>";
					relevantIngredientsfiltered.add(apiIngredientAmount);
				}else {
					lines+="<br>"+apiIngredientAmount.getName()+"";
				}
			}
			
			returnList.add(lines);
			
		}

		
		String lines="<br><br><h2>Results:</h2>";

		for(int i=0;i<relevantIngredientsfiltered.size();i++) {
			ApiIngredientAmount apiIngredientAmount = relevantIngredientsfiltered.get(i);
			lines+="<br>"+apiIngredientAmount.getName()+"";
			
		}
		returnList.add(lines);

		ModelAndView mav=new ModelAndView("List");
		mav.addObject("list",returnList);
		return mav;
	}
	
	@RequestMapping(value="/edaman_ingredient_categorisation_teaching")
	public ModelAndView edaman_ingredient_categorisation_teaching() throws IOException, GoogleDriveAccessNotAuthorisedException {
		
		Map<String, String> targetValues = EdamanCategorisationTeacher.getTeachingExcercise();

		Map<String, IngredientParsed> checkedCorectness = EdamanCategorisationTeacher.checkCorectness_inEdaman();
		
		
		ArrayList<String> phrases=new ArrayList<String>();

		int greens=0,reds=0,yellows=0;
		Map<String,String> categorisationResults=new HashMap<String, String>();
		Map<String,String> productPhrases=new HashMap<String, String>();
		Map<String,String> quantityPhrases=new HashMap<String, String>();
		ArrayList<String> retList=new ArrayList<String>();

		
		for(String ingredientPhrase:targetValues.keySet()) {
		IngredientParsed ingredientParsed = checkedCorectness.get(ingredientPhrase);
		String catPhrase=targetValues.get(ingredientPhrase);
		phrases.add(ingredientPhrase);
		
		
		String quantityPhrase = ingredientParsed==null||ingredientParsed.getQuantity()==null?"brak":ingredientParsed.getQuantity().toString();
		String categoryPhrase = ingredientParsed==null||ingredientParsed.getCategory()==null?"brak":ingredientParsed.getCategory().toString();
		String productPhrase = ingredientParsed==null||ingredientParsed.getProductPhrase()==null?"brak":ingredientParsed.getProductPhrase().toString();

		
		quantityPhrases.put(ingredientPhrase, quantityPhrase);
		productPhrases.put(ingredientPhrase,productPhrase);
		
		String htmlLine=ingredientPhrase;
		
		if(catPhrase.startsWith(categoryPhrase)) {
			greens++;
			categorisationResults.put(ingredientPhrase,"<span style=\"background-color:green\">"+categoryPhrase+"(("+catPhrase+"))</span>");
		}else if(catPhrase.endsWith("??")) {
			yellows++;
			categorisationResults.put(ingredientPhrase,"<span style=\"background-color:yellow\">"+categoryPhrase+"(("+catPhrase+"))</span>");
		}else {
			reds++;
			categorisationResults.put(ingredientPhrase,"<span style=\"background-color:red\">"+categoryPhrase+"(("+catPhrase+"))</span>");
		}
		retList.add(htmlLine);
		System.out.println(htmlLine);
	}
		
		ModelAndView mav=new ModelAndView("ingredientCategoriesList");

		mav.addObject("results",phrases);
		mav.addObject("categorisationResults",categorisationResults);
		mav.addObject("quantityPhrases",quantityPhrases);
		mav.addObject("productPhrases",productPhrases);
		String sumLine="<b>Reds:"+reds+"   yellows:"+yellows+"    greens:"+greens+"    All:"+(reds+yellows+greens)+"</b>";

		mav.addObject("sumLine",sumLine);
		return mav;
	}
//	
//	@RequestMapping(value="/edaman_ingredient_categorisation_teaching")
//	public ModelAndView edaman_ingredient_categorisation_teaching() throws IOException, GoogleDriveAccessNotAuthorisedException {
//		String teachingEdamanContents = getTeachingEdamanContents();
//		ArrayList<String> retList=new ArrayList<String>();
//		ArrayList<String> phrases=new ArrayList<String>();
//
//		String[] split = teachingEdamanContents.split("<");
//		int greens=0,reds=0,yellows=0;
//		Map<String,String> categorisationResults=new HashMap<String, String>();
//		Map<String,String> productPhrases=new HashMap<String, String>();
//		Map<String,String> quantityPhrases=new HashMap<String, String>();
//		
//		
//		for(String line:split) {
//			String[] lineSplitted = line.split("->");
//			
//			String ingredientPhrase=lineSplitted[0];
//			String catPhrase=lineSplitted[1].trim();
//			phrases.add(ingredientPhrase);
//			
//			FakeProduct fp=new FakeProduct(ingredientPhrase);
//			PreciseQuantity extractQuantity = EdamanQExtract.extractQuantity(ingredientPhrase);
//			
//			IngredientCategoriationData dataToBeCategorised=new IngredientCategoriationData(ingredientPhrase, extractQuantity);
//			
//			Category assignedCategory = EdamanCategoriser.getSingleton().assignCategory(dataToBeCategorised);
//			
//			quantityPhrases.put(ingredientPhrase, extractQuantity.toString());
//			productPhrases.put(ingredientPhrase, EdamanQExtract.correctText(ingredientPhrase));
//			
//			String htmlLine=ingredientPhrase;
//			
//			if(catPhrase.startsWith(assignedCategory.getName())) {
//				greens++;
//				categorisationResults.put(ingredientPhrase,"<span style=\"background-color:green\">"+assignedCategory+"(("+catPhrase+"))</span>");
//			}else if(catPhrase.endsWith("??")) {
//				yellows++;
//				categorisationResults.put(ingredientPhrase,"<span style=\"background-color:yellow\">"+assignedCategory+"(("+catPhrase+"))</span>");
//			}else {
//				reds++;
//				categorisationResults.put(ingredientPhrase,"<span style=\"background-color:red\">"+assignedCategory+"(("+catPhrase+"))</span>");
//			}
//			retList.add(htmlLine);
//			System.out.println(htmlLine);
//		}
//		
//		String sumLine="<b>Reds:"+reds+"   yellows:"+yellows+"    greens:"+greens+"    All:"+(reds+yellows+greens)+"</b>";
//
//		ModelAndView mav=new ModelAndView("ingredientCategoriesList");
//		mav.addObject("results",phrases);
//		mav.addObject("categorisationResults",categorisationResults);
//		mav.addObject("quantityPhrases",quantityPhrases);
//		mav.addObject("productPhrases",productPhrases);
//
//		mav.addObject("sumLine",sumLine);
//		
//		return mav;
//		
//	}
//	
	
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
