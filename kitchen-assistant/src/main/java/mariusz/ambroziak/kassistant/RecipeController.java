package mariusz.ambroziak.kassistant;

import java.io.File;
import java.io.UnsupportedEncodingException;
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
import mariusz.ambroziak.kassistant.model.jsp.SearchResult;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import testing.QuantityTesting;



//import database.holders.StringHolder;

@Controller
public class RecipeController {

	

	
//	@RequestMapping(value="/recipe/result")
//	@ResponseBody
//	public ModelAndView checkClock(HttpServletRequest request) {
//		System.out.println("xx");
//		
//		String url=request.getParameter("recipeurl");
//		
//		Map<String,ArrayList<Produkt>> result=RecipeAgent.parse(url);
//
//		ModelAndView mav=new ModelAndView("recipeParsed");
//		
//		mav.addObject("ingredients",result);
//		
//		return mav;
//	}
	
	
	@RequestMapping(value="/recipeForm")
	
	public ModelAndView recipeFrom(HttpServletRequest request) {
		
			return new ModelAndView("form");

	}
	
	

	
	
	
	@RequestMapping(value="/chooseProdukts")
	public ModelAndView chooseProdukts(HttpServletRequest request) {
		String url=request.getParameter("recipeurl");

		if(!url.startsWith("-")){
			ArrayList<SearchResult> result=RecipeAgent.parse(url);
	
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
	
	
	@RequestMapping(value="/chooseQuantities")
	public ModelAndView chooseQuantities(HttpServletRequest request) {
			try {
				request.setCharacterEncoding(java.nio.charset.StandardCharsets.UTF_8.toString());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		String url=request.getParameter("recipeurl");
			
			request.getParameterMap();
			
			
			
			
			int i=1;
			while(true){
				String searchPhrase=request.getParameter(JspStringHolder.skladnik_name+i+JspStringHolder.searchPhrase_name);
				String wybranyProdukt=request.getParameter(JspStringHolder.skladnik_name+i+JspStringHolder.skladnikRadio_name);
				String innaOpcjaRadio=JspStringHolder.innaOpcja_name;
				
				
				if(JspStringHolder.innaOpcja_name.equals(wybranyProdukt)){
					String innyUrl=request.getParameter(JspStringHolder.skladnik_name+i+JspStringHolder.innyUrl_name);
					
				
				}
				
				
			}
			
			
			
	//		ModelAndView mav=new ModelAndView("chooseProducts");
	
			
//			return mav;
		
		

	}
	
//	@RequestMapping(value="/recipe2")
//	
//	public ModelAndView form2(HttpServletRequest request) {
//		String url=request.getParameter("recipeurl");
//		
//		
//		
//		if(url==null||url.equals("")){
//		
//			return new ModelAndView("form");
//		}else{
//			
//			if(url.startsWith("-")){
//				ArrayList<SearchResult> result=RecipeAgent.parse(url);
//
//				ModelAndView mav=new ModelAndView("recipeParsed");
//				
//				mav.addObject("results",result);
//				
//				return mav;				
//				
//			}else{
//				ArrayList<SearchResult> result=RecipeAgent.parse(url);
//	
//				ModelAndView mav=new ModelAndView("quantitiesTested");
//				
//				mav.addObject("results",result);
//				
//				return mav;
//			}
//		}
//		
//			
//	}
	
}
