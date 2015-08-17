package mariusz.ambroziak.kassistant;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import madkit.kernel.Madkit;
import mariusz.ambroziak.kassistant.agents.BaseAgent;
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

	
//	@RequestMapping(value="/agents")
//	@ResponseBody
//	public String users() {
//		String html="<html>\r\n" + 
//				"    <head>\r\n" + 
//				"        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n" + 
//				"        <title>Home</title>\r\n" + 
//				"    </head>\r\n" + 
//				"    <body>";
//		
//		html+="hello World!";
//		
//		
//		
//		
//		
//		html+="    </body>\r\n" + 
//				"</html>";
//		
//		return html;
//	}
	
	
	@RequestMapping(value="/agents/start")
	@ResponseBody
	public String startAgents() {
		return "<h3>"+AgentsSystem.startSystem()+"</h3>";
	}
	
	
//	@RequestMapping(value="/agents/clock")
//	@ResponseBody
//	public String checkClock() {
//		return "time is :"+ClockAgent.getTimePassed();
//	}
	
	
	@RequestMapping(value="/agents/all")
	public ModelAndView getAllAgents(){
		ModelAndView mav=new ModelAndView("agentList");
		
		mav.addObject("agents", BaseAgent.getExtent());
		
		return mav;
	}
	
	
	@RequestMapping(value="/agents/info")
	@ResponseBody
	public ModelAndView getAgent(HttpServletRequest request){
		HashMap<String, BaseAgent> allAgents = BaseAgent.getExtent();
		
		String agentName=request.getParameter("name");
		ModelAndView mav;
		if(agentName==null||agentName.equals("")
				||!allAgents.containsKey(agentName)){
			mav=new ModelAndView("agentInfoEmpty");
		}else{
			mav=new ModelAndView("agentInfo");
			mav.addObject("agent", allAgents.get(agentName));
			mav.addObject("name", agentName);
//			mav.addObject("newLine", "\n");
		}
		
		return mav;
	}
//		
//		return "time is :"+ClockAgent.getTimePassed();
//	}
	
	
//	@RequestMapping(value="/agents/result")
//	
//	public ModelAndView form(HttpServletRequest request) {
//		System.out.println("xx");
//		
//		return new ModelAndView("form");
//	}
	
	
}
