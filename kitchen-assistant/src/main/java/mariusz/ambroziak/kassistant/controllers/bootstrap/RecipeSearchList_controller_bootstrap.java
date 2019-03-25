package mariusz.ambroziak.kassistant.controllers.bootstrap;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mariusz.ambroziak.kassistant.Apiclients.edaman.EdamanRecipeApiClient;
import mariusz.ambroziak.kassistant.Apiclients.edaman.ParseableRecipeData;
import mariusz.ambroziak.kassistant.agents.EdamanRecipeAgent;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.StringHolder;


@Controller
public class RecipeSearchList_controller_bootstrap {

	
	@RequestMapping(value="/b_engRecipeForm")
	public ModelAndView b_engRecipeForm() {
		ModelAndView model = new ModelAndView(StringHolder.bootstrapFolder+"boot_recipeEngForm");
		
		
		
		return model;
	}

	private ModelAndView createGoogleAccessNotGrantedMav() {
		ModelAndView mav=new ModelAndView(StringHolder.bootstrapFolder+"boot_GoogleDataAccessDeleted");
		return mav;
	}

	private Cookie getKitchenAssistantCookie(HttpServletRequest req) {
		Cookie[] cookies = req.getCookies();
		  
		  if(cookies!=null) {
			  for(Cookie c:cookies) {
				  if(StringHolder.CREDENTIAL_COOKIE_NAME.equals(c.getName())){
					  return c;
				  }
			  }
		  }
		  return null;
	}

	@RequestMapping(value="/b_apirecipes")
	public ModelAndView b_apirecipes(HttpServletRequest request) {
		String searchPhrase=request.getParameter(JspStringHolder.recipeSearchPhrase_name);
		
		List<ParseableRecipeData> results = null;
		try {
			Cookie c=getKitchenAssistantCookie(request);
			if(c==null||c.getValue()==null||c.getValue().equals("")) {
				results = EdamanRecipeAgent.searchForRecipes("",searchPhrase);
			}else {
				results = EdamanRecipeAgent.searchForRecipes(c.getValue(),searchPhrase);
			}
		} catch (AgentSystemNotStartedException e) {
			return returnAgentSystemNotStartedPage();
		} 
		
		ModelAndView modelAndView = new ModelAndView(StringHolder.bootstrapFolder+"boot_RecipeList");
		modelAndView.addObject("recipeList", results);
		return modelAndView;
	}

	

	@RequestMapping(value="/b_engRecipeUrlForm")
	public ModelAndView b_engRecipeUrlForm(HttpServletRequest request) {
		ModelAndView model = new ModelAndView(StringHolder.bootstrapFolder+"boot_recipeEngUrlForm");

		
		
		return model;
	}



	protected ModelAndView returnAgentSystemNotStartedPage() {
		return new ModelAndView(StringHolder.bootstrapFolder+"boot_agentSystemNotStarted");
	}
}
