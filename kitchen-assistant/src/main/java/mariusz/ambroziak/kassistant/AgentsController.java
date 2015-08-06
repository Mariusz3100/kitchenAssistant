package mariusz.ambroziak.kassistant;

import java.io.File;
import java.util.List;

import madkit.kernel.Madkit;
import mariusz.ambroziak.kassistant.agents.ClockAgent;
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
//		StringHolder.servletBasePath = request.getServletContext().getRealPath("/");
//		new File("alalalalalalabamba.txt").createNewFile();
		String[] args2 = { "--launchAgents",
		"agent.AuchanAgent,false;",
		"agent.RecipeAgent,false;",
		"agent.ShopsListAgent,false" };
	//	Madkit.main(args2);
		
		new Madkit("--launchAgents", 
//				"agents.AuchanAgent,true,1;",
//				"agents.RecipeAgent,true,1;",
				"mariusz.ambroziak.kassistant.agents.ClockAgent,false,1;"
//				"agents.TestAgent,true,1;",
//				"agents.ShopsListAgent,true,1;"
				);
		
		
//		response.setCharacterEncoding(StringHolder.SERVLET_RESPONSE_ENCODING);
		//response.getWriter().write("Agent System Started¹ê³Ÿz¿óæ");
		
		
		return "agent system started";
	}
	
	
	@RequestMapping(value="/agents/checkClock")
	@ResponseBody
	public String checkClock() {
		return "time is :"+ClockAgent.getTimePassed();
	}
	
}
