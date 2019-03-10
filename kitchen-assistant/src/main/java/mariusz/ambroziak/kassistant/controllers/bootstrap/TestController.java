package mariusz.ambroziak.kassistant.controllers.bootstrap;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mariusz.ambroziak.kassistant.Apiclients.googleAuth.GoogleAuthApiClient;
import mariusz.ambroziak.kassistant.exceptions.GoogleDriveAccessNotAuthorisedException;
import mariusz.ambroziak.kassistant.utils.StringHolder;


@Controller
public class TestController {

	
	@RequestMapping(value="/btest")
	public ModelAndView bhome() {
		ModelAndView model = new ModelAndView("List");
		ArrayList<String> list=new ArrayList<>();
		list.add("hello world");
		model.addObject("list",list);
		
		return model;
	}

	@RequestMapping(value="/bradio")
	public ModelAndView bradio() {
		ModelAndView model = new ModelAndView(StringHolder.bootstrapFolder+"radioTest");
		
		
		return model;
	}
	
	@RequestMapping(value="/google_test")
	public ModelAndView b_main() throws IOException, GoogleDriveAccessNotAuthorisedException {
		GoogleAuthApiClient.main(null);
		
		return null;
	}

}
