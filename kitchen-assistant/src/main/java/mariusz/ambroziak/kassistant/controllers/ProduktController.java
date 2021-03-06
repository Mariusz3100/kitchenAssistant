package mariusz.ambroziak.kassistant.controllers;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Basic;
import javax.servlet.http.HttpServletRequest;

import madkit.kernel.Madkit;
import mariusz.ambroziak.kassistant.agents.ClockAgent;
import mariusz.ambroziak.kassistant.agents.FoodIngredientAgent;
import mariusz.ambroziak.kassistant.agents.ProduktAgent;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.agents.config.AgentsSystem;
import mariusz.ambroziak.kassistant.api.agents.ShopComAgent;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.dao.ProduktDAO;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.exceptions.ShopNotFoundException;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.jsp.MultiProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.NotPreciseQuantity;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.AbstractQuantity;
import mariusz.ambroziak.kassistant.model.utils.BasicIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.CompoundIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithAllIngredients;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithBasicIngredients;
import mariusz.ambroziak.kassistant.utils.CompoundMapManipulator;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.StringHolder;

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
	

	@RequestMapping(value="/searchForProdukt")
	public ModelAndView searchForProdukt(HttpServletRequest request) throws UnsupportedEncodingException {
		
		request.setCharacterEncoding(StringHolder.ENCODING);
		String url=request.getParameter("searchFor");
		String quantity=request.getParameter("quantity");
		

		if(url==null||url.equals("")||quantity==null||quantity.equals(""))
			return new ModelAndView("produktSearchForForm");
		else
		{
			List<Produkt> produkts = null;
			try {
//				produkts = ShopComAgent.searchForIngredient(url);
				produkts = ProduktAgent.searchForProdukt(url);

			} catch (AgentSystemNotStartedException e) {
				return new ModelAndView("agentSystemNotStarted");
			}
			
			ModelAndView model = new ModelAndView("produktsList");
			model.addObject("produktList", produkts);
			
			return model;
		}
	}
	
//	@RequestMapping(value="/searchApiForProdukt")
//	public ModelAndView searchApiForProdukt(HttpServletRequest request) throws UnsupportedEncodingException {
//		
//		request.setCharacterEncoding(StringHolder.ENCODING);
//		String searchFor=request.getParameter("searchFor");
//		String quantity=request.getParameter("quantity");
//		
//
//		if(searchFor==null||searchFor.equals("")||quantity==null||quantity.equals(""))
//			return new ModelAndView("produktSearchForForm");
//		else
//		{
//			List<Produkt> produkts = null;
//			try {
//				produkts = ShopComAgent.searchForIngredient(searchFor);
//			} catch (AgentSystemNotStartedException e) {
//				return new ModelAndView("agentSystemNotStarted");
//			}
//			
//			ModelAndView model = new ModelAndView("produktsList");
//			model.addObject("produktList", produkts);
//			
//			return model;
//		}
//	}
	
	
	
	@RequestMapping(value="/produktUrl")
	public ModelAndView produktUrl(HttpServletRequest request) {
		try {
			request.setCharacterEncoding(StringHolder.ENCODING);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		

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
				a.add("URL "+e.getUrl()+" nie nale---y do ---adnego ze znanych sklep---w");
				
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
	
	@RequestMapping(value="/produktValues")
	public ModelAndView produktValues(HttpServletRequest request) {
		//TODO update
		return null;
		
		//		try {
//			request.setCharacterEncoding(StringHolder.ENCODING);
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		String url=request.getParameter("url");
//		String type=request.getParameter("type");
//		
//		if(url==null||url.equals("")||type==null||type.equals(""))
//			return new ModelAndView("produktParseForm");
//		else
//		{
//			String shortUrl=AuchanAbstractScrapper.getAuchanShortestWorkingUrl(url);
//
//			if(type.equals("NV")){
//				return getBasicNutritionalDataForProduktHandleErrors(url, shortUrl);
//			}else if(type.equals("Full")){
//				return parseAllIngredientsOfProduktHandleExceptions(url, shortUrl);
//			}
//			
//			return null;
//		}
		
	}



		
	
	
}
