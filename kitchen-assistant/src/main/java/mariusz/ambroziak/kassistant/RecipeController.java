package mariusz.ambroziak.kassistant;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import madkit.kernel.Madkit;
import mariusz.ambroziak.kassistant.agents.ClockAgent;
import mariusz.ambroziak.kassistant.agents.ProduktAgent;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.agents.config.AgentsSystem;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.ShopNotFoundException;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.jsp.InvalidSearchResult;
import mariusz.ambroziak.kassistant.model.jsp.SearchResult;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import testing.QuantityTesting;



//import database.holders.StringHolder;

@Controller
public class RecipeController {




	//	@RequestMapping(value="/recipe/result")
	//	@ResponseBody
	//	public ModelAndView checkClock(HttpServletRequest request) {
	//		System.out.println("xx");
	//		
	//		String url=request.getParameter("recipeurl");
	//		
	//		Map<String,ArrayList<Produkt>> result=RecipeAgent.parse(url);
	//
	//		ModelAndView mav=new ModelAndView("recipeParsed");
	//		
	//		mav.addObject("ingredients",result);
	//		
	//		return mav;
	//	}


	@RequestMapping(value="/recipeForm")

	public ModelAndView recipeFrom(HttpServletRequest request) {

		return new ModelAndView("form");

	}






	@RequestMapping(value="/chooseProdukts")
	public ModelAndView chooseProdukts(HttpServletRequest request) {
		String url=request.getParameter("recipeurl");

		if(!url.startsWith("-")){
			ArrayList<SearchResult> result=RecipeAgent.parse(url);
			if(result==null){

				return new ModelAndView("agentSystemNotStarted");

			}else{
				ModelAndView mav=new ModelAndView("chooseProducts");

				mav.addObject("url",url);
				mav.addObject("results",result);

				return mav;
			}
		}else{
			List<String> result=QuantityTesting.testQuantity(url.substring(1));

			if(result==null){

				return new ModelAndView("agentSystemNotStarted");

			}else{
				ModelAndView mav=new ModelAndView("quantitiesTested");

				mav.addObject("url",url);
				mav.addObject("results",result);

				return mav;
			}
		}


	}


	@RequestMapping(value="/confirmProdukts")
	public ModelAndView chooseQuantities(HttpServletRequest request) {
		try {
			request.setCharacterEncoding(java.nio.charset.StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ModelAndView mav=new ModelAndView("correctProducts");

//		Map<String,String> invalidShopUrls= new HashMap<String,String>();
//		Map<String,String> invalidProduktUrls= new HashMap<String,String>();
		ArrayList<SearchResult> goodResults= new ArrayList<SearchResult>();
		ArrayList<InvalidSearchResult> usersBadChoice= new ArrayList<InvalidSearchResult>();
		ArrayList<String> skippedResults= new ArrayList<String>();



		
		int liczbaSkladnikow=
				Integer.parseInt(request.getParameter(JspStringHolder.liczbaSkladnikow));
		
		for(int i=1;i<=liczbaSkladnikow;i++){
			String searchPhrase=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.searchPhrase_name);
			String produktPhrase=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.produktPhrase_name);
			String quantityPhrase=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.quantity_name);
			
			
			String wybranyProdukt=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.skladnikRadio_name);
			//		String innaOpcjaRadio=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.innyUrl_name);
			Produkt produkt=null;
			if(JspStringHolder.innaOpcja_name.equals(wybranyProdukt)){
				String innyUrl=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.innyUrl_name);
				try {
					produkt=ProduktAgent.getOrScrapProdukt(innyUrl);
					if(produkt!=null){
						ArrayList<Produkt> produkts=new ArrayList<Produkt>();
						produkts.add(produkt);
						SearchResult sr=new SearchResult(searchPhrase,produktPhrase, quantityPhrase, produkts);
						goodResults.add(sr);
					}
						
					else
					{
						//znowu pobieramy produkty
						List<Produkt> searchResults = ProduktAgent.searchForProdukt(produktPhrase);

						
						InvalidSearchResult isr=new InvalidSearchResult(searchPhrase,produktPhrase,quantityPhrase,searchResults,
								"Wygl¹da na to, ¿e strona internetowa pod url \""+innyUrl+"\" nie istnieje, lub nie opisuje ¿adnego produktu");
						
						usersBadChoice.add(isr);
					}
				} catch (ShopNotFoundException e) {
//					invalidShopUrls.put(produktPhrase,innyUrl);
					List<Produkt> searchResults;
					try {
						searchResults = ProduktAgent.searchForProdukt(produktPhrase);
					} catch (AgentSystemNotStartedException e1) {
						return new ModelAndView("agentSystemNotStarted");
					}

					
					InvalidSearchResult isr=new InvalidSearchResult(searchPhrase,produktPhrase,quantityPhrase,searchResults,
							"Wygl¹da na to, ¿e url \""+innyUrl
							+"\" nie zosta³ rozpoznany jako pasuj¹cy do ¿adnego ze wspieranych sklepów.");
					
					usersBadChoice.add(isr);
					
				} catch (AgentSystemNotStartedException e) {
					return new ModelAndView("agentSystemNotStarted");
				}

			}else if(JspStringHolder.pominOpcja_name.equals(wybranyProdukt)){
				skippedResults.add(searchPhrase);
			
			}else if(wybranyProdukt!=null&&wybranyProdukt.startsWith(JspStringHolder.radioValuePrefix)){
				if(wybranyProdukt!=null&&wybranyProdukt.length()>0)
					wybranyProdukt=wybranyProdukt.replaceFirst(Pattern.quote(JspStringHolder.radioValuePrefix), "");
				
				produkt=DaoProvider.getInstance().getProduktDao().getProduktsByURL(wybranyProdukt);
				
				if(produkt==null){
					ProblemLogger.logProblem(
							"Wygl¹da na to, ¿e produkt wybrany przez przycisk radio nie istnieje w systemie!: "
									+wybranyProdukt);
					List<Produkt> searchResults;
					try {
						searchResults = ProduktAgent.searchForProdukt(produktPhrase);
					} catch (AgentSystemNotStartedException e1) {
						return new ModelAndView("agentSystemNotStarted");
					}
					InvalidSearchResult isr=new InvalidSearchResult(searchPhrase,produktPhrase,quantityPhrase,searchResults,
							"UPS! Wydaje siê, ¿e zaproponowany przez nas produkt nie wystêpuje w naszym systemie!! Powiadom o tym administratora albo coœ...");
					
					usersBadChoice.add(isr);
					
				}else{
					ArrayList<Produkt> produkts=new ArrayList<Produkt>();
					produkts.add(produkt);
					SearchResult sr=new SearchResult(searchPhrase,produktPhrase, quantityPhrase, produkts);
					goodResults.add(sr);
				}
			}

		}

		 
		
		
		mav.addObject("badResults",usersBadChoice);

		mav.addObject("correctResults",goodResults);

		mav.addObject("skippedResults",skippedResults);

		
//		if(invalidShopUrls.size()>0||invalidProduktUrls.size()>0){
//			for(Entry<String,String> e:invalidShopUrls.entrySet()){
//				List<Produkt> searchResults = ProduktAgent.searchForProdukt(e.getKey());
//				
//				
//				SearchResult sr=new SearchResult(e.getKey(),);
//				
//				usersBadChoice.add(arg0)
//				
//			}
//				
//							
//		}else{
//			
//		}
		
		
		return mav;




	}

	//	@RequestMapping(value="/recipe2")
	//	
	//	public ModelAndView form2(HttpServletRequest request) {
	//		String url=request.getParameter("recipeurl");
	//		
	//		
	//		
	//		if(url==null||url.equals("")){
	//		
	//			return new ModelAndView("form");
	//		}else{
	//			
	//			if(url.startsWith("-")){
	//				ArrayList<SearchResult> result=RecipeAgent.parse(url);
	//
	//				ModelAndView mav=new ModelAndView("recipeParsed");
	//				
	//				mav.addObject("results",result);
	//				
	//				return mav;				
	//				
	//			}else{
	//				ArrayList<SearchResult> result=RecipeAgent.parse(url);
	//	
	//				ModelAndView mav=new ModelAndView("quantitiesTested");
	//				
	//				mav.addObject("results",result);
	//				
	//				return mav;
	//			}
	//		}
	//		
	//			
	//	}

}
