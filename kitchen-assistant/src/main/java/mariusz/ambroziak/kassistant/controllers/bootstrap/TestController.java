package mariusz.ambroziak.kassistant.controllers.bootstrap;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


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
	
	@RequestMapping(value="/bindex")
	public ModelAndView b_index() {
		ModelAndView model = new ModelAndView("bootstrap/boot_EntryPage");

		
		return model;
	}
	
	

}
