package mariusz.ambroziak.kassistant.controllers;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import madkit.kernel.Madkit;
import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaFoodId;
import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaNutrientApiClient;
import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaNutrientApiClientParticularFood;
import mariusz.ambroziak.kassistant.agents.ClockAgent;
import mariusz.ambroziak.kassistant.agents.ProduktAgent;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.agents.config.AgentsSystem;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.dao.Nutrient_NameDAO;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient_Nutrient_Data_Source;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.Nutrient_Name;
import mariusz.ambroziak.kassistant.model.Problem;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.jsp.MultiProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import testing.QuantityTesting;
import webscrappers.przepisy.SkladnikiExtractor;



//import database.holders.StringHolder;

@Controller
public class TestsController {
	public static String problems="";




	@RequestMapping(value="/java8test")
	@ResponseBody
	public String test8(HttpServletRequest request) {
		String retValue = "";

		Optional<Integer> a = Optional.ofNullable(null);

		//Optional.of - throws NullPointerException if passed parameter is null
		Optional<Integer> b = Optional.ofNullable(null);

		retValue+="First parameter is present: " + a.isPresent()+"\n";
		retValue+="Second parameter is present: " + b.isPresent()+"\n";

		//Optional.orElse - returns the value if present otherwise returns
		//the default value passed.
		Integer value1 = a.orElse(new Integer(0));

		//Optional.get - gets the value, value should be present
		Integer value2 = b.orElse(new Integer(1));
		retValue+=(value1 + value2);
		return retValue;



	}

	@RequestMapping(value="/test/produktsTest")
	public ModelAndView chooseProdukts(HttpServletRequest request) {
		String url=request.getParameter("recipeurl");

		List<String> result=QuantityTesting.testQuantity(url.substring(1));

		if(result==null){

			return new ModelAndView("agentSystemNotStarted");

		}else{
			ModelAndView mav=new ModelAndView("quantitiesTested");

			mav.addObject("url",url);
			mav.addObject("results",result);

			return mav;
		}



	}



	@RequestMapping(value="/test1")
	public ModelAndView test1(HttpServletRequest request) {

		ModelAndView mav=new ModelAndView("test/test");


		return mav;


	}

	@RequestMapping(value="/test3")
	public ModelAndView test3
	(HttpServletRequest request) {

		ModelAndView mav=new ModelAndView("test/test");
		SkladnikiExtractor.main(null);

		return mav;


	}



	@RequestMapping(value="/nutrientsTestLazy")
	public ModelAndView nutrientsTest12
	(HttpServletRequest request) {
		Nutrient_NameDAO nutrientNameDao = DaoProvider.getInstance().getNutrientNameDao();
		Nutrient_Name nutrient_Name = nutrientNameDao.list().get(0);
		Nutrient base = nutrient_Name.getBase();
		System.out.println(base.getName());
		return null;
	}


	@RequestMapping(value="/nutrientsTest")
	public ModelAndView nutrientsTest
	(HttpServletRequest request) {


		Nutrient_NameDAO nutrientNameDao = DaoProvider.getInstance().getNutrientNameDao();

		//		List<Nutrient_Name> nutList = nutrientNameDao.list();
		Map<Nutrient, PreciseQuantity> nutrientDetailsForDbno = UsdaNutrientApiClientParticularFood.getNutrientDetailsForDbno("04542");

		System.out.println("Details:");

		for(Nutrient n:nutrientDetailsForDbno.keySet()) {
			System.out.println(n+"->"+nutrientDetailsForDbno.get(n));
		}


		ModelAndView mav=new ModelAndView("homePage");

		return mav;


	}


	@RequestMapping(value="/testDb")
	public ModelAndView testdb(HttpServletRequest request) {
		Map<Nutrient, Float> areNutrientsForBasicIngredient = DaoProvider.getInstance().getBasicIngredientNutrientAmountDao().getNutrientsForBasicIngredientById(1l);
		ModelAndView mav = new ModelAndView("List");
		mav.addObject("list", areNutrientsForBasicIngredient);
		return mav;
	}

	@RequestMapping(value="/testDb2")
	public ModelAndView testdb2(HttpServletRequest request) {


		Map<Nutrient, Float> areNutrientsForBasicIngredient = DaoProvider.getInstance().getBasicIngredientNutrientAmountDao().getNutrientsForBasicIngredientById(1l);
		ModelAndView mav = new ModelAndView("List");
		mav.addObject("list", areNutrientsForBasicIngredient);
		return mav;
	}


	@RequestMapping(value="/testDb3")
	public ModelAndView testdb3(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("List");

		List<Basic_Ingredient> ingredientBySpacedName = DaoProvider.getInstance().getBasicIngredientDao().getIngredientBySpacedName("pork");
		Basic_Ingredient bi= DaoProvider.getInstance().getBasicIngredientDao().getIngredientById(2l);

		ArrayList<UsdaFoodId> searchForProduktsInUsdaDb = UsdaNutrientApiClient.searchForProduktsInUsdaDb("pork");




		return mav;
	}

	@RequestMapping(value="/test2")
	public ModelAndView test2(HttpServletRequest request) {
		try {
			request.setCharacterEncoding(java.nio.charset.StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ModelAndView mav=new ModelAndView("test/test");
		request.getParameterMap();
		request.getParameter("_charset_");
		return mav;


	}

	@RequestMapping(value="/test/getSkladnik")
	public ModelAndView getSkladknik(HttpServletRequest request) {
		try {
			request.setCharacterEncoding(java.nio.charset.StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ModelAndView mav;
		String name=request.getParameter("skladnik");

		if(name==null)
		{
			mav=new ModelAndView("test/form");
		}else{
			List<Produkt> parseProdukt = null;
			try {
				parseProdukt = ProduktAgent.searchForProdukt(name);
			} catch (AgentSystemNotStartedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ArrayList<String> result=new ArrayList<String>();

			for(Produkt p:parseProdukt)
				result.add(p.getNazwa()+" : "+p.getUrl());

			mav=new ModelAndView("List");
			mav.addObject("list", result);

		}


		return mav;


	}	

	@RequestMapping(value="/quantitiesTest")
	public ModelAndView testQuantities(HttpServletRequest request) {
		String url=request.getParameter("recipeurl");

		if(!url.startsWith("-")){
			ArrayList<MultiProdukt_SearchResult> result = null;
			try {
				result = RecipeAgent.getQuantitiesAndProduktsFromRecipeUrl(url);
			} catch (AgentSystemNotStartedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Page404Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ModelAndView mav=new ModelAndView("chooseProducts");

			mav.addObject("url",url);
			mav.addObject("results",result);

			return mav;
		}else{
			List<String> result=QuantityTesting.testQuantity(url.substring(1));


			ModelAndView mav=new ModelAndView("quantitiesTested");

			mav.addObject("results",result);

			return mav;
		}


	}
	
	@RequestMapping(value="/dbRetrieval")
	public ModelAndView testDifferentDbRetrieval(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("List");
		Nutrient nutrientByName = DaoProvider.getInstance().getNutrientDao().getNutrientByName("energy");
		List<Basic_Ingredient_Nutrient_Data_Source> dataSourceBy_ApiId = DaoProvider.getInstance().getBasicIngredientNutrientDataSourceDao().getDataSourceBy_ApiId("07010");
		
		return mav;
		
	}
}
