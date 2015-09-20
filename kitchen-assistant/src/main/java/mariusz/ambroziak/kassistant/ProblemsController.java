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
public class ProblemsController {
	public static String problems="";
	

	

	@RequestMapping(value="/problems")
	public ModelAndView recipeParsed(HttpServletRequest request) {
		List<Problem> problems=DaoProvider.getInstance().getProblemDao().list();

		ModelAndView mav=new ModelAndView("List");
		ArrayList<String> list=new ArrayList<String>();
		
		list.add("Problems to solve:");
		
		for(Problem p:problems)
		{
			String message = p.getMessage();
			if(p.getNext_p_id()>0)
				message+=" -->"+p.getNext_p_id();
				
			list.add(message);
		}
		
		mav.addObject("list",list);
		
		return mav;


	}
	
	

	
}
