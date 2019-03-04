package mariusz.ambroziak.kassistant.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import madkit.kernel.Madkit;
import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaFoodDetails;
import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaNutrientApiClient;
import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaNutrientApiClientParticularFood;
import mariusz.ambroziak.kassistant.agents.ClockAgent;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.agents.config.AgentsSystem;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.Problem;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.jsp.MultiProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;




//import database.holders.StringHolder;

@Controller
public class NutrientsController {

	@RequestMapping(value="/nutrientsForNdbno")
	public ModelAndView nutrientsForNdbno(HttpServletRequest request) {
		String ndbno=request.getParameter(JspStringHolder.ndbno);

		if(ndbno==null||ndbno.equals("")) {
			return new ModelAndView("nutrientsForm");
		}else {
			Map<Nutrient, PreciseQuantity> nutrientDetailsForDbno = UsdaNutrientApiClientParticularFood.getNutrientDetailsForDbno(ndbno);

			return putIntoListMav(ndbno,nutrientDetailsForDbno);

		}
	}
	
	@RequestMapping(value="/nutrientsForFoodName")
	public ModelAndView nutrientsForFoodName(HttpServletRequest request) {
		String ndbno=request.getParameter(JspStringHolder.ndbno);
		if(ndbno==null||ndbno.equals("")) {
			String searchFor=request.getParameter(JspStringHolder.foodName);
			if(searchFor==null||searchFor.equals("")) {
				return new ModelAndView("nutrientsForm");
			}
			UsdaFoodDetails nutrientDetails = UsdaNutrientApiClient.searchForNutritionDetailsOfAProdukt(searchFor);
			return putIntoListMav(nutrientDetails);
		}else {
			Map<Nutrient, PreciseQuantity> nutrientDetailsForDbno = UsdaNutrientApiClientParticularFood.getNutrientDetailsForDbno(ndbno);
			return putIntoListMav(ndbno,nutrientDetailsForDbno);
		}
	}

	private ModelAndView putIntoListMav(String ndbno, Map<Nutrient, PreciseQuantity> nutrientDetailsForDbno) {
		List<String> list =new ArrayList<>();
		list.add("ndbo of found product is "+ndbno);
		list =putNutrientDetailsIntoStringList(nutrientDetailsForDbno,list);

		ModelAndView mav=new ModelAndView("List");
		mav.addObject("list",list);

		return mav;
		
	}

	private ModelAndView putIntoListMav(UsdaFoodDetails nutrientDetails) {
		List<String> list =new ArrayList<>();
		list.add("Ndbo of product is "+nutrientDetails.getId().getNdbno());
		list.add("Name of product is \""+nutrientDetails.getId().getName()+"\"");
		list = putNutrientDetailsIntoStringList(nutrientDetails.getNutrietsMapPer100g(),list);

		ModelAndView mav=new ModelAndView("List");
		mav.addObject("list",list);

		return mav;
	}

	private ArrayList<String> putNutrientDetailsIntoStringList(Map<Nutrient, PreciseQuantity> nutrietsMap) {
		ArrayList<String> list=new ArrayList<>();
		for(Nutrient n:nutrietsMap.keySet()) {
			list.add(n+"->"+nutrietsMap.get(n));
		}
		return list;
	}

	
	private List<String> putNutrientDetailsIntoStringList(Map<Nutrient, PreciseQuantity> nutrietsMap,List<String> list) {
		for(Nutrient n:nutrietsMap.keySet()) {
			list.add(n+"->"+nutrietsMap.get(n));
		}
		return list;
	}

}
