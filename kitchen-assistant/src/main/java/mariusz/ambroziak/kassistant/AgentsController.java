package mariusz.ambroziak.kassistant;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import madkit.kernel.Madkit;
import mariusz.ambroziak.kassistant.agents.ClockAgent;
import mariusz.ambroziak.kassistant.agents.config.AgentsSystem;
import mariusz.ambroziak.kassistant.model.User;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

//import database.holders.StringHolder;

@Controller
public class AgentsController {

	
	@RequestMapping(value="/agents")
	@ResponseBody
	public String users() {
		String html="<html>\r\n" + 
				"    <head>\r\n" + 
				"        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n" + 
				"        <title>Home</title>\r\n" + 
				"    </head>\r\n" + 
				"    <body>";
		
		html+="hello World!";
		
		
		
		
		
		html+="    </body>\r\n" + 
				"</html>";
		
		return html;
	}
	
	
	@RequestMapping(value="/agents/start")
	@ResponseBody
	public String startAgents() {
		return AgentsSystem.startSystem();
	}
	
	
	@RequestMapping(value="/agents/checkClock")
	@ResponseBody
	public String checkClock() {
		return "time is :"+ClockAgent.getTimePassed();
	}
	
	
	@RequestMapping(value="/agents/param")
	@ResponseBody
	public String checkClock(HttpServletRequest request) {
		System.out.println("xx");
		
		return "time is :"+ClockAgent.getTimePassed();
	}
	
	
	@RequestMapping(value="/agents/result")
	
	public ModelAndView form(HttpServletRequest request) {
		System.out.println("xx");
		
		return new ModelAndView("form");
	}
	
	
}
