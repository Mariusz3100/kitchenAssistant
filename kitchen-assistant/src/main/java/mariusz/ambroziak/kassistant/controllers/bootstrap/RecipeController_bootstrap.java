package mariusz.ambroziak.kassistant.controllers.bootstrap;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mariusz.ambroziak.kassistant.utils.StringHolder;


@Controller
public class RecipeController_bootstrap {

	
	@RequestMapping(value="/b_engRecipeForm")
	public ModelAndView bForm() {
		ModelAndView model = new ModelAndView(StringHolder.bootstrapFolder+"boot_recipeEngForm");

		
		
		return model;
	}

	@RequestMapping(value="/b_engRecipeUrlForm")
	public ModelAndView bUrlForm() {
		ModelAndView model = new ModelAndView(StringHolder.bootstrapFolder+"boot_recipeEngUrlForm");

		
		
		return model;
	}

}
