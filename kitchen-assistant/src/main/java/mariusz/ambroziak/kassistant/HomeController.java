package mariusz.ambroziak.kassistant;

import java.util.ArrayList;
import java.util.List;

import mariusz.ambroziak.kassistant.dao.ProduktDAO;
import mariusz.ambroziak.kassistant.dao.UserDAO;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private ProduktDAO produktDao;
	
	@RequestMapping(value="/users")
	public ModelAndView users() {
		List<User> listUsers = userDao.list();
		ModelAndView model = new ModelAndView("usersList");
		model.addObject("userList", listUsers);
		return model;
	}
	
	@RequestMapping(value="/produkts")
	public ModelAndView produkts() {
		List<Produkt> listProdukts = produktDao.list();
		ModelAndView model = new ModelAndView("produktsList");
		model.addObject("produktList", listProdukts);
		return model;
	}
	
	@RequestMapping(value="/produkts1")
	public ModelAndView produkts1() {
		List<Produkt> listProdukts = new ArrayList<Produkt>();

		Produkt x= produktDao.getProduktByURL("aaa");
		listProdukts.add(x);
		ModelAndView model = new ModelAndView("produktsList");
		model.addObject("produktList", listProdukts);
		return model;
	}
	
}
