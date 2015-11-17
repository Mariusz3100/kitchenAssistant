package mariusz.ambroziak.kassistant.agents.config;

import madkit.kernel.Madkit;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

public class AgentsSystem {

	
	public static String startSystem(){
		
//		StringHolder.servletBasePath = request.getServletContext().getRealPath("/");
//		new File("alalalalalalabamba.txt").createNewFile();
//		String[] args2 = { "--launchAgents",
//		"agent.AuchanAgent,false;",
//		"agent.RecipeAgent,false;",
//		"agent.ShopsListAgent,false" };
	//	Madkit.main(args2);
		
		new Madkit("--launchAgents", 
				"mariusz.ambroziak.kassistant.agents.AuchanAgent,false,1;",
				"mariusz.ambroziak.kassistant.agents.RecipeAgent,false,1;",
				"mariusz.ambroziak.kassistant.agents.ClockAgent,false,1;",
//				"agents.TestAgent,true,1;",
				"mariusz.ambroziak.kassistant.agents.ShopsListAgent,false,1;"
				);
		
		
//		response.setCharacterEncoding(StringHolder.SERVLET_RESPONSE_ENCODING);
		//response.getWriter().write("Agent System Started�곟z���");
		
		return "agent system started";
	}
	
	
	
	

}
