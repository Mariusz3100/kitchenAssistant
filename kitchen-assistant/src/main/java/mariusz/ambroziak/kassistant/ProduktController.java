package mariusz.ambroziak.kassistant;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import madkit.kernel.Madkit;
import mariusz.ambroziak.kassistant.agents.ClockAgent;
import mariusz.ambroziak.kassistant.agents.ProduktAgent;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.agents.config.AgentsSystem;
import mariusz.ambroziak.kassistant.dao.ProduktDAO;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.ShopNotFoundException;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.jsp.SearchResult;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import testing.QuantityTesting;



//import database.holders.StringHolder;

@Controller
public class ProduktController {
	@Autowired
	private ProduktDAO produktDao;
	
	@RequestMapping(value="/produkts")
	public ModelAndView produkts() {
		List<Produkt> listProdukts = produktDao.list();
		ModelAndView model = new ModelAndView("produktsList");
		model.addObject("produktList", listProdukts);
		return model;
	}
	
	@RequestMapping(value="/produktUrl")
	public ModelAndView produktUrl(HttpServletRequest request) {
		
		

		String url=request.getParameter("produktUrl");
		
		if(url==null||url.equals(""))
			return new ModelAndView("produktUrlForm");
		else
		{
			Produkt produkt=null;
			try {
				produkt = ProduktAgent.getOrScrapProdukt(url);
			} catch (ShopNotFoundException e) {
				ArrayList<String> a=new ArrayList<String>();
				a.add("URL "+e.getUrl()+" nie nale¿y do ¿adnego ze znanych sklepów");
				
				ModelAndView mav=new ModelAndView("List");
				mav.addObject("list", a);
				return mav;
			} catch (AgentSystemNotStartedException e) {
				return new ModelAndView("agentSystemNotStarted");
			}
			
			List<Produkt> listProdukts =new ArrayList<Produkt>();
			listProdukts.add(produkt);
			
			ModelAndView model = new ModelAndView("produktsList");
			model.addObject("produktList", listProdukts);
			
			return model;
		}
		
	}
	
	@RequestMapping(value="/searchForProdukt")
	public ModelAndView searchForProdukt(HttpServletRequest request) {
		
		

		String url=request.getParameter("searchFor");
		
		if(url==null||url.equals(""))
			return new ModelAndView("produktSearchForForm");
		else
		{
			List<Produkt> produkts = null;
			try {
				produkts = ProduktAgent.searchForProdukt(url);
			} catch (AgentSystemNotStartedException e) {
				return new ModelAndView("agentSystemNotStarted");
			}
			
			ModelAndView model = new ModelAndView("produktsList");
			model.addObject("produktList", produkts);
			
			return model;
		}
		
	}
	
	
}
