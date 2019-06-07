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

import org.mortbay.util.UrlEncoded;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mariusz.ambroziak.kassistant.Apiclients.edaman.ParseableRecipeData;
import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleCalendarApiClient;
import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleDriveApiClient;
import mariusz.ambroziak.kassistant.ai.FilesProvider;
import mariusz.ambroziak.kassistant.ai.categorisation.CategorisationTeacher;
import mariusz.ambroziak.kassistant.ai.categorisation.Categoriser;
import mariusz.ambroziak.kassistant.ai.categorisation.Category;
import mariusz.ambroziak.kassistant.ai.categorisation.CategoryHierarchy;
import mariusz.ambroziak.kassistant.ai.categorisation.IngredientCategoriser;
import mariusz.ambroziak.kassistant.exceptions.GoogleDriveAccessNotAuthorisedException;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.utils.ApiIngredientAmount;
import mariusz.ambroziak.kassistant.tesco.TescoApiClient;
import mariusz.ambroziak.kassistant.tesco.TescoApiClientParticularProduct_notUsed;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.StringHolder;


@Controller
public class RecipeIngredientsCategorisation_bootstrap {

	
	@RequestMapping(value="/edaman_ingredient_categorisation_teaching")
	public ModelAndView categorisation_teaching() throws IOException, GoogleDriveAccessNotAuthorisedException {
//		ArrayList<String> list=new ArrayList<String>();
//
//		list.add("tomato:");
		List<String> correctnessChecked = CategorisationTeacher.TescoCheckCorectness();
//
//		
////		for(Entry<Produkt, Category> e:testedCategoriesForTomato.entrySet()) {
////			list.add(e.getKey().getNazwa()+" ("+e.getKey().getUrl()+")->"+e.getValue());
////		}
//		
		
		ModelAndView mav=new ModelAndView("List");
		mav.addObject("list",correctnessChecked);
		return mav;
	}

	@RequestMapping(value="/edaman_relevant_ingredients")
	public ModelAndView edaman_relevant_ingredients() throws IOException, GoogleDriveAccessNotAuthorisedException {
		String phrase="tomato";
		
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
		String teachingEdamanContents = getTeachingEdamanContents();
		
		String[] split = teachingEdamanContents.split("\n");
		
		for(String line:split) {
			String[] lineSplitted = line.split("->");
			
			String ingredientPhrase=lineSplitted[0];
			String catPhrase=lineSplitted[1];
			
			
			
			
		}
		
		
		return null;
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
