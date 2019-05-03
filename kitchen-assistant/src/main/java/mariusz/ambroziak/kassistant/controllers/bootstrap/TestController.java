package mariusz.ambroziak.kassistant.controllers.bootstrap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleCalendarApiClient;
import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleDriveApiClient;
import mariusz.ambroziak.kassistant.ai.FilesProvider;
import mariusz.ambroziak.kassistant.ai.categorisation.Categoriser;
import mariusz.ambroziak.kassistant.ai.categorisation.Category;
import mariusz.ambroziak.kassistant.ai.categorisation.CategoryHierarchy;
import mariusz.ambroziak.kassistant.exceptions.GoogleDriveAccessNotAuthorisedException;
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

	@RequestMapping(value="/xml_read_test")
	public ModelAndView xml_read_test() throws IOException, GoogleDriveAccessNotAuthorisedException {
		Resource categoriesFile = FilesProvider.getInstance().getCategoriesFile();
		
		InputStream inputStream = categoriesFile.getInputStream();
		BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
		List<String> content=new ArrayList<String>();
		String temp=br.readLine();
		while(temp!=null) {
			content.add(temp);
			temp=br.readLine();
		}
		
		
		ModelAndView mav=new ModelAndView("List");
		mav.addObject("list",content);
		return null;
		
	}
	
	
	@RequestMapping(value="/categories_read_test")
	public ModelAndView categories_read_test() {
		Category singletonRoot = CategoryHierarchy.getSingletonCategoryRoot();
		
		
		ModelAndView mav=new ModelAndView("List");
		mav.addObject("list",singletonRoot.getName());
		return null;
		
	}

	
	@RequestMapping(value="/tesco_test")
	public ModelAndView tesco_test() {
		TescoApiClient.main(null);
		return new ModelAndView();
	}
	
	
	@RequestMapping(value="/categorisation_test")
	public ModelAndView categorisation_test() {
		Categoriser.testCategoriesFor("cucumber", 10);
		
		
		return new ModelAndView();
	}
	
	@RequestMapping(value="/tesco_particular_test")
	public ModelAndView tesco_particular_test() {
		TescoApiClientParticularProduct_notUsed.main(null);
		return new ModelAndView();
	}
}
