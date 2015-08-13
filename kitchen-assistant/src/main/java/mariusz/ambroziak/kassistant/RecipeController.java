package mariusz.ambroziak.kassistant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import madkit.kernel.Madkit;
import mariusz.ambroziak.kassistant.agents.ClockAgent;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.agents.config.AgentsSystem;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;



//import database.holders.StringHolder;

@Controller
public class RecipeController {

	

	
	@RequestMapping(value="/recipe/result")
	@ResponseBody
	public ModelAndView checkClock(HttpServletRequest request) {
		System.out.println("xx");
		
		String url=request.getParameter("recipeurl");
		
		Map<String,ArrayList<Produkt>> result=RecipeAgent.parse(url);

		ModelAndView mav=new ModelAndView("recipeParsed");
		
		mav.addObject("ingredients",result);
		
		return mav;
	}
	
	
	@RequestMapping(value="/recipe")
	
	public ModelAndView form(HttpServletRequest request) {
		System.out.println("xx");
		
		return new ModelAndView("form");
	}
	
	
}
