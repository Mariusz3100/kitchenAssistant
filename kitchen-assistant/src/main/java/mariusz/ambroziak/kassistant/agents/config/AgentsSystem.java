package mariusz.ambroziak.kassistant.agents.config;

import madkit.kernel.Madkit;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

public class AgentsSystem {

	
	private static final String [] xs = {"--launchAgents", 
//				"mariusz.ambroziak.kassistant.agents.AuchanAgent,false,1;",
//			"mariusz.ambroziak.kassistant.agents.RecipeAgent,false,1;",
			"mariusz.ambroziak.kassistant.agents.ClockAgent,false,1;",
//				"agents.TestAgent,true,1;",
			"mariusz.ambroziak.kassistant.agents.ShopsListAgent,false,1;",
			"mariusz.ambroziak.kassistant.agents.ProduktAgent,false,1;",
			"mariusz.ambroziak.kassistant.agents.FoodIngredientAgent,false,1;",
			"mariusz.ambroziak.kassistant.agents.EdamanRecipeAgent,false,1;",
			"mariusz.ambroziak.kassistant.api.agents.ShopComAgent,false,1;",
			"mariusz.ambroziak.kassistant.api.agents.GoogleAgent,false,1;",
			"mariusz.ambroziak.kassistant.agents.ReadingNutritientsUsdaAgent,false,1"};

	public static String startSystem(){
		new Madkit(xs);
		return "agent system started";
	}
	
	public static String[] bootstrapstartSystem(){
		new Madkit(xs);
		return xs;
	}	
	
	

}
