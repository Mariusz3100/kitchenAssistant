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
		
		String [] x={"--launchAgents", 
//				"mariusz.ambroziak.kassistant.agents.AuchanAgent,false,1;",
				"mariusz.ambroziak.kassistant.agents.RecipeAgent,false,1;",
				"mariusz.ambroziak.kassistant.agents.ClockAgent,false,1;",
//				"agents.TestAgent,true,1;",
				"mariusz.ambroziak.kassistant.agents.ShopsListAgent,false,1;",
				"mariusz.ambroziak.kassistant.agents.ProduktAgent,false,1;",
				"mariusz.ambroziak.kassistant.agents.FoodIngredientAgent,false,1;",
				"mariusz.ambroziak.kassistant.agents.EdamanRecipeAgent,false,1;",
				"mariusz.ambroziak.kassistant.api.agents.ShopComAgent,false,1;",
				"mariusz.ambroziak.kassistant.api.agents.GoogleAgent,false,1;",
				"mariusz.ambroziak.kassistant.agents.ReadingNutritientsUsdaAgent,false,1"};
		
		new Madkit(x);
		
		
//		response.setCharacterEncoding(StringHolder.SERVLET_RESPONSE_ENCODING);
		//response.getWriter().write("Agent System Started");
		
		return "agent system started";
	}
	
	
	
	

}
