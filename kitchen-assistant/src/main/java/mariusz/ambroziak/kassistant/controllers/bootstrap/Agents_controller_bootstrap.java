package mariusz.ambroziak.kassistant.controllers.bootstrap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import mariusz.ambroziak.kassistant.Apiclients.edaman.EdamanRecipeApiClient;
import mariusz.ambroziak.kassistant.Apiclients.edaman.ParseableRecipeData;
import mariusz.ambroziak.kassistant.agents.BaseAgent;
import mariusz.ambroziak.kassistant.agents.EdamanRecipeAgent;
import mariusz.ambroziak.kassistant.agents.config.AgentsSystem;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.StringHolder;


@Controller
public class Agents_controller_bootstrap {

	
	@RequestMapping(value="/b_startSystem")
	public ModelAndView b_engRecipeForm() {
		ModelAndView model = new ModelAndView(StringHolder.bootstrapFolder+"boot_startAgentSystem");
		
		String[] agentsCreated = AgentsSystem.bootstrapstartSystem();
		
		ArrayList<String> listOfAgents=new ArrayList<>();

		for(String x:agentsCreated) {
			if(x.indexOf("launchAgents")<0) {
				listOfAgents.add(x);
			}
		}
		
		model.addObject("agents",listOfAgents);
		return model;
	}

	@RequestMapping(value="/b_agentList")
	public ModelAndView b_agentList() {
		ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_agentList");
		HashMap<String, BaseAgent> agents = BaseAgent.getExtent();
		//agents.keySet()
		if(agents==null||agents.isEmpty()) {
			return returnAgentSystemNotStartedPage();
		}
		mav.addObject("agents", agents);
		HashMap<BaseAgent,List<Object>> rolesMap=new  HashMap<>();
		
		for(BaseAgent ba:agents.values()) {
			List<Object> asList = Arrays.asList(ba.getMyRolesKA());
			rolesMap.put(ba, asList);
		}
//		agents.get("RecipeAgent")
		mav.addObject("agentRoles", rolesMap);
//		rolesMap.get(agents.get("ProduktAgent").getMyRolesKA()).get(0)
		return mav;
	}

	@RequestMapping(value="/boot_agentInfo")
	public ModelAndView b_agentInfo(HttpServletRequest request) {
		HashMap<String, BaseAgent> allAgents = BaseAgent.getExtent();
		String agentName=request.getParameter(JspStringHolder.agentName);
		ModelAndView mav;
		if(allAgents==null||allAgents.isEmpty()) {
			return returnAgentSystemNotStartedPage();
		}
		if(agentName==null||agentName.equals("")
				||allAgents==null||!allAgents.containsKey(agentName)){
			mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_emptyAgentInfo");
		}else{
			mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_agentInfo");
			BaseAgent agentFound = allAgents.get(agentName);
			mav.addObject("agent", agentFound);

		}
		return mav;

	}
	
	protected ModelAndView returnAgentSystemNotStartedPage() {
		return new ModelAndView(StringHolder.bootstrapFolder+"boot_agentSystemNotStarted");
	}
	
	/*
	 * 	@RequestMapping(value="/agents/info")
	public ModelAndView getAgent(HttpServletRequest request){
		HashMap<String, BaseAgent> allAgents = BaseAgent.getExtent();
		
		String agentName=request.getParameter("name");
		ModelAndView mav;
		if(agentName==null||agentName.equals("")
				||allAgents==null||!allAgents.containsKey(agentName)){
			mav=new ModelAndView("agentInfoEmpty");
		}else{
			mav=new ModelAndView("agentInfo");
			mav.addObject("agent", allAgents.get(agentName));
			mav.addObject("name", agentName);
			mav.addObject("newLine", "\n");
		}
		
		return mav;
	}
	 */
		

}
