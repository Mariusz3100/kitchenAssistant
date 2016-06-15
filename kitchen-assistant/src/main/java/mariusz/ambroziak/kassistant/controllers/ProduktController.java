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
import mariusz.ambroziak.kassistant.agents.ReadingAgent;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.agents.config.AgentsSystem;
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
import webscrappers.auchan.AuchanAbstractScrapper;
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
				produkts = RecipeAgent.searchForProdukt(url,quantity);
			} catch (AgentSystemNotStartedException e) {
				return new ModelAndView("agentSystemNotStarted");
			}
			
			ModelAndView model = new ModelAndView("produktsList");
			model.addObject("produktList", produkts);
			
			return model;
		}
	}
	
	
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
	public ModelAndView produktValues(HttpServletRequest request) {
		try {
			request.setCharacterEncoding(StringHolder.ENCODING);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String url=request.getParameter("url");
		String type=request.getParameter("type");
		
		if(url==null||url.equals("")||type==null||type.equals(""))
			return new ModelAndView("produktParseForm");
		else
		{
			String shortUrl=AuchanAbstractScrapper.getAuchanShortestWorkingUrl(url);

			if(type.equals("NV")){
				return getBasicNutritionalDataForProduktHandleErrors(url, shortUrl);
			}else if(type.equals("Full")){
				return parseAllIngredientsOfProduktHandleExceptions(url, shortUrl);
			}
			
			return null;
		}
		
	}


	private ModelAndView parseAllIngredientsOfProduktHandleExceptions(String url, String shortUrl) {
		try {
			ProduktWithAllIngredients ingredients = ReadingAgent.parseFullSklad(shortUrl);
			Produkt produkt = retrieveFromDbOrParseProdukt(shortUrl, ingredients);
			
			
			//IngredientName->(NutrientName->amount)
			Map<String, Map<String, NotPreciseQuantity>> amountsMap = getMapOfIngredients(ingredients);
			
			CompoundMapManipulator<String, String> cmm=new CompoundMapManipulator<String, String>(amountsMap);
			Map<String,NotPreciseQuantity> sums=cmm.sumUpInnerMaps();
			List<String> nutrientsList=new ArrayList<String>(cmm.getAllInnerMapsKeys());
			ModelAndView mav=new ModelAndView("produktDetails");
			mav.addObject("produkt", produkt);
			mav.addObject("compundIngredient", ingredients.getProduktAsIngredient()==null?"BRAK INFORMACJI":ingredients.getProduktAsIngredient().toString());
			mav.addObject("amountsSum", sums);
			mav.addObject("amountsMap", amountsMap);
			mav.addObject("allNutrients",nutrientsList );
			
			return mav;
			
		} catch (Page404Exception e) {
			return createPageEmptyMav(url, shortUrl);
		} catch (AgentSystemNotStartedException e) {
			return new ModelAndView("agentSystemNotStarted");
		} catch (ShopNotFoundException e) {
			return createShopUnknownMav(url, shortUrl);
		}
	}


	private Map<String, Map<String, NotPreciseQuantity>> getMapOfIngredients(ProduktWithAllIngredients ingredients)
			throws AgentSystemNotStartedException {
		Map<String, Map<String,NotPreciseQuantity>> amountsMap=new HashMap<String, Map<String,NotPreciseQuantity>>();

		ArrayList<BasicIngredientQuantity> allBasicIngredients = ingredients.getProduktAsIngredient().getAllBasicIngredients();
		for(int i=0; i<allBasicIngredients.size();i++){
			BasicIngredientQuantity biq=allBasicIngredients.get(i);
			Map<Nutrient, PreciseQuantity> parsedFoodIngredientNutrients = FoodIngredientAgent.parseFoodIngredient(biq.getName());
			Map<String,NotPreciseQuantity> currentIngredientAmounts=new HashMap<String, NotPreciseQuantity>();
			
			for(Nutrient nutrient:parsedFoodIngredientNutrients.keySet()){
				NotPreciseQuantity quantity = biq.getAmount();
				NotPreciseQuantity relativeAmount=multiplyQuantities(quantity,parsedFoodIngredientNutrients.get(nutrient));
				currentIngredientAmounts.put(nutrient.getName(), relativeAmount);
			}
			amountsMap.put(biq.getName()+" ["+biq.getAmount()+"]", currentIngredientAmounts);
			
		}
		return amountsMap;
	}


	private Produkt retrieveFromDbOrParseProdukt(String shortUrl, ProduktWithAllIngredients ingredients) {
		Produkt produkt =DaoProvider.getInstance().getProduktDao().getProduktsByURL(shortUrl);
		if(produkt==null)
		{
			produkt=ingredients.getProdukt().extractProduktWithoutUrl();
			produkt.setUrl(shortUrl);

			DaoProvider.getInstance().getProduktDao().saveProdukt(produkt);
		}
		return produkt;
	}


	private ModelAndView createShopUnknownMav(String url, String shortUrl) {
		ModelAndView mav=new ModelAndView("List");
		ArrayList<String> list=new ArrayList<String>();
		
		list.add("Url "+url+"->"+shortUrl+" nie zosta³ dopasowany do ¿adnego ze sklepów");
		mav.addObject("list", list);
		
		return mav;
	}


	private ModelAndView createPageEmptyMav(String url, String shortUrl) {
		ModelAndView mav=new ModelAndView("List");
		ArrayList<String> list=new ArrayList<String>();
		
		list.add("Url "+url+"->"+shortUrl+
				" jest podobny do stron jakiegoœ sklepu, ale nie wskazuje na ¿adn¹ stronê z produktem.");
		mav.addObject("list", list);
		
		return mav;
	}


	private ModelAndView getBasicNutritionalDataForProduktHandleErrors(String url, String shortUrl) {
		try {
			return scrapBasicNutritionalDataFromLabel(shortUrl);
			
		} catch (Page404Exception e) {
			return createPageEmptyMav(url, shortUrl);
		} catch (AgentSystemNotStartedException e) {
			return new ModelAndView("agentSystemNotStarted");
		} catch (ShopNotFoundException e) {
			return createShopUnknownMav(url, shortUrl);
		}
	}


	private ModelAndView scrapBasicNutritionalDataFromLabel(String shortUrl)
			throws AgentSystemNotStartedException, ShopNotFoundException, Page404Exception {
		ProduktWithBasicIngredients basics = ReadingAgent.parseBasicSklad(shortUrl);
		
		
		
		ArrayList<String> list=new ArrayList<String>();
		list.add(basics.getProdukt().getNazwa()+" - "+basics.getProdukt().getUrl());
		
		for(BasicIngredientQuantity bpi:basics.getBasicsFromLabelTable())
		{
			String opis=bpi.getName()+": "+bpi.getAmount();
			list.add(opis);
		}
		
		ModelAndView mav=new ModelAndView("List");
		
		mav.addObject("list", list);
		
		return mav;
	}


	private NotPreciseQuantity multiplyQuantities(AbstractQuantity ingredientQuantity,PreciseQuantity quanPer100g) {
		
		Float coeff=quanPer100g.getAmount()/(1000*100);
		
		NotPreciseQuantity aq=null;
		if(ingredientQuantity instanceof PreciseQuantity){
			PreciseQuantity pq=new PreciseQuantity();
			pq.setAmount(coeff*((PreciseQuantity)ingredientQuantity).getAmount());
			aq=pq;
		}else if(ingredientQuantity instanceof NotPreciseQuantity){
			NotPreciseQuantity npq=new NotPreciseQuantity();
			npq.setMaximalAmount(coeff*((NotPreciseQuantity)ingredientQuantity).getMaximalAmount());
			npq.setMinimalAmount(coeff*((NotPreciseQuantity)ingredientQuantity).getMinimalAmount());
			aq=npq;
		} 
		
		aq.setType(ingredientQuantity.getType());
		
		return aq;
	}


		
		
		
	
	
}
