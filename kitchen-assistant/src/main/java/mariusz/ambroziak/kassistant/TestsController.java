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
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.model.Problem;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.jsp.SearchResult;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;



//import database.holders.StringHolder;

@Controller
public class TestsController {
	public static String problems="";
	

	

	@RequestMapping(value="/test1")
	public ModelAndView test1(HttpServletRequest request) {

		ModelAndView mav=new ModelAndView("test/test");
		
		
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
			List<Produkt> parseProdukt = RecipeAgent.parseProdukt(name);
			
			ArrayList<String> result=new ArrayList<String>();
			
			for(Produkt p:parseProdukt)
				result.add(p.getNazwa()+" : "+p.getUrl());
			
			mav=new ModelAndView("List");
			mav.addObject("list", result);

		}
		
		
		return mav;


	}	

	
}