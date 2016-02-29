package mariusz.ambroziak.kassistant;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import madkit.kernel.Madkit;
import mariusz.ambroziak.kassistant.agents.ClockAgent;
import mariusz.ambroziak.kassistant.agents.ProduktAgent;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.agents.config.AgentsSystem;
import mariusz.ambroziak.kassistant.dao.ProduktDAO;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.exceptions.ShopNotFoundException;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.jsp.SearchResult;
import mariusz.ambroziak.kassistant.model.utils.BasicIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.CompoundIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithAllIngredients;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithBasicIngredients;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import testing.QuantityTesting;
import webscrappers.auchan.AuchanRecipeParser;



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
	
	@RequestMapping(value="/produktValues")
	public ModelAndView parseProdukt(HttpServletRequest request) {
		String url=request.getParameter("url");
		String type=request.getParameter("type");

		if(url==null||url.equals("")||type==null||type.equals(""))
			return new ModelAndView("produktParseForm");
		else
		{
			if(type.equals("NV")){
				try {
					ProduktWithBasicIngredients basics = AuchanRecipeParser.getBasics(url);
					
					ArrayList<String> list=new ArrayList<String>();
					list.add(basics.getProdukt().getNazwa()+" - "+basics.getProdukt().getUrl());
					
					for(BasicIngredientQuantity bpi:basics.getBasicsFor100g())
					{
						String opis=bpi.getName()+": "+bpi.getAmount()+" "+bpi.getAmountType();
						

							
						list.add(opis);
					}
					
					ModelAndView mav=new ModelAndView("List");
					
					mav.addObject("list", list);
					
					return mav;
					
				} catch (Page404Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(type.equals("Full")){
				try {
					ProduktWithAllIngredients ingredients = AuchanRecipeParser.getAllIngredients(url);
					
					ArrayList<String> list=new ArrayList<String>();
					list.add(ingredients.getProdukt().getNazwa()+" - "+ingredients.getProdukt().getUrl());
					
					for(BasicIngredientQuantity piq:ingredients.getBasicsFor100g())
					{
						String opis=piq.getAmountType().getType()+": "+piq.getAmount();
						list.add(opis);
					}

					
					
					String allIngredients=ingredients.getProduktAsIngredient()==null?"BRAK INFORMACJI":ingredients.getProduktAsIngredient().toString();
					
					list.add("All ingredients:<br>"+allIngredients);
					
					ModelAndView mav=new ModelAndView("List");
					
					mav.addObject("list", list);
					
					return mav;
					
				} catch (Page404Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return null;
		}
		
	}


		
		
		
	
	
}
