package mariusz.ambroziak.kassistant.controllers.bootstrap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mariusz.ambroziak.kassistant.utils.StringHolder;


@Controller
public class HomeController_bootstrap {


	
	@RequestMapping(value="/bindex")
	public ModelAndView b_index() {
		ModelAndView model = new ModelAndView(StringHolder.bootstrapFolder+"boot_EntryPage");

		
		return model;
	}
	
	

}
