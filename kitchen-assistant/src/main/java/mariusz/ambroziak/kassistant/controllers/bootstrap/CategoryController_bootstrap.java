package mariusz.ambroziak.kassistant.controllers.bootstrap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.tesco.TescoApiClient;
import mariusz.ambroziak.kassistant.tesco.TescoApiClientParticularProduct_notUsed;
import mariusz.ambroziak.kassistant.utils.StringHolder;


@Controller
public class CategoryController_bootstrap {

	

	
	@RequestMapping(value="/xml_categorize_test")
	public ModelAndView xml_categorize_test() throws IOException, GoogleDriveAccessNotAuthorisedException {
		CategoryHierarchy.initializeCategoriesFromXml();
		
		Category singletonRoot = CategoryHierarchy.getSingletonCategoryRoot();

		ModelAndView mav=new ModelAndView("List");
		mav.addObject("list",singletonRoot==null?"nothing":singletonRoot.getName());
		return mav;
	}
	
	@RequestMapping(value="/get_xml_category")
	public ModelAndView get_xml_category() throws IOException, GoogleDriveAccessNotAuthorisedException {
		Category createCategoriesFromXmlFile = CategoryHierarchy.createCategoriesFromXmlFile();
		String results=createCategoriesFromXmlFile.toJsonRepresentation().toString();
		List<String> list=new ArrayList<String>();
		list.add(results);
		ModelAndView mav=new ModelAndView("List");
		mav.addObject("list",list);
		return mav;
	}
	
	@RequestMapping(value="/get_xml_test_category")
	public ModelAndView get_xml_test_category() throws IOException, GoogleDriveAccessNotAuthorisedException {
		Category createCategoriesFromXmlFile = CategoryHierarchy.createCategoryFromTestXmlFile();
		String results=createCategoriesFromXmlFile.toJsonRepresentation().toString();
		List<String> list=new ArrayList<String>();
		list.add(results);
		ModelAndView mav=new ModelAndView("List");
		mav.addObject("list",list);
		return mav;
	}
	
	
	
	@RequestMapping(value="/get_static_category")
	public ModelAndView get_static_category() throws IOException, GoogleDriveAccessNotAuthorisedException {
		Category createCategoriesFromXmlFile = CategoryHierarchy.initializeCategoriesStaticlly();
		String results=createCategoriesFromXmlFile.toJsonRepresentation().toString();
		List<String> list=new ArrayList<String>();
		list.add(results);
		ModelAndView mav=new ModelAndView("List");
		mav.addObject("list",list);
		return mav;
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

	
	@RequestMapping(value="/categorisation_test_with_static")
	public ModelAndView categorisation_test_with_static() {
		ArrayList<String> list=new ArrayList<String>();
		list.add("cucumber:");
		
		Map<Produkt, Category> testedCategoriesFor = Categoriser.testStaticCategoriesFor("cucumber", 10);
		for(Entry<Produkt, Category> e:testedCategoriesFor.entrySet()) {
			list.add(e.getKey().getNazwa()+" ("+e.getKey().getUrl()+")->"+e.getValue());
		}
		

		ModelAndView modelAndView = new ModelAndView("List");
		modelAndView.addObject("list",list);
		return modelAndView;
	}
//	
	@RequestMapping(value="/categorisation_test")
	public ModelAndView categorisation_test() {
		ArrayList<String> list=new ArrayList<String>();
//		list.add("cucumber:");
//		
//		Map<Produkt, Category> testedCategoriesFor = Categoriser.testCategoriesFor("cucumber", 10);
//		for(Entry<Produkt, Category> e:testedCategoriesFor.entrySet()) {
//			list.add(e.getKey().getNazwa()+" ("+e.getKey().getUrl()+")->"+e.getValue().getName());
//		}
		
		list.add("tomato:");
		Map<Produkt, Category> testedCategoriesForTomato = Categoriser.testCategoriesFor("tomato", 10);
		for(Entry<Produkt, Category> e:testedCategoriesForTomato.entrySet()) {
			list.add(e.getKey().getNazwa()+" ("+e.getKey().getUrl()+")->"+e.getValue());
		}
//		
		ModelAndView modelAndView = new ModelAndView("List");
		modelAndView.addObject("list",list);
		return modelAndView;
	}
	
	@RequestMapping(value="/categorisation_test_single")
	public ModelAndView categorisation_test_single() {
		ArrayList<String> list=new ArrayList<String>();

//		int id=83286592;
		
		int id=56774766;
		Map<Produkt, Category> testCategoryProduct = Categoriser.testCategoryProduct(id);
		Produkt product = testCategoryProduct.keySet().iterator().next();
		list.add(""+id);
		list.add(""+product.getNazwa());
		list.add(""+product.getUrl());
		list.add(testCategoryProduct.get(product).getName());
		
		
		ModelAndView modelAndView = new ModelAndView("List");
		modelAndView.addObject("list",list);
		return modelAndView;
	}

}
