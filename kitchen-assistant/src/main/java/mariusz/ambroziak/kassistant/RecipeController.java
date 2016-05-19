package mariusz.ambroziak.kassistant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mariusz.ambroziak.kassistant.agents.ProduktAgent;
import mariusz.ambroziak.kassistant.agents.ReadingAgent;
import mariusz.ambroziak.kassistant.agents.RecipeAgent;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.exceptions.ShopNotFoundException;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.jsp.InvalidSearchResult;
import mariusz.ambroziak.kassistant.model.jsp.MultiProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.jsp.ProduktWithRecountedPrice;
import mariusz.ambroziak.kassistant.model.jsp.SingleProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.BasicIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithBasicIngredients;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;



//import database.holders.StringHolder;

@Controller
public class RecipeController {


	@RequestMapping(value="/recipeForm")

	public ModelAndView recipeForm(HttpServletRequest request) {

		return new ModelAndView("recipeForm");

	}






	@RequestMapping(value="/chooseProdukts")
	public ModelAndView chooseProdukts(HttpServletRequest request) {
		String url=request.getParameter("recipeurl");

		if(url==null){
			return new ModelAndView("recipeForm");
		}

		int liczbaSkladnikow=
				Integer.parseInt(request.getParameter(JspStringHolder.liczbaSkladnikow));

		Map<MultiProdukt_SearchResult,PreciseQuantity> result = new HashMap<MultiProdukt_SearchResult,PreciseQuantity>();

		for(int i=0;i<liczbaSkladnikow;i++){
			String searchPhrase=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.searchPhrase_name);
			String produktPhrase=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.produktPhrase_name);
			String quantityAmount=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.quantity_amount);
			String quantityType=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.quantity_type);

			PreciseQuantity quantity=extractQuantity(quantityAmount, quantityType);
			List<Produkt> possibleProdukts=null;
			try {
				possibleProdukts=getProduktsWithRecountedPrice(RecipeAgent.parseProdukt(produktPhrase),quantity);
			} catch (AgentSystemNotStartedException e) {
				return new ModelAndView("agentSystemNotStarted");
			}

			result.put(
					new MultiProdukt_SearchResult(searchPhrase, produktPhrase, quantity.toJspString(), possibleProdukts)
					,quantity);
		}

		//		try {
		//			result = RecipeAgent.getQuantitiesAndProduktsFromRecipeUrl(url);
		//		} catch (AgentSystemNotStartedException e) {
		//			return new ModelAndView("agentSystemNotStarted");
		//		}
		if(result==null||result.isEmpty()){

			return new ModelAndView("agentSystemNotStarted");

		}else{
			ModelAndView mav=new ModelAndView("chooseProducts");

			mav.addObject("url",url);
			mav.addObject("results",result);

			return mav;
		}


	}






	private List<Produkt> getProduktsWithRecountedPrice(List<Produkt> parseProdukt,
			PreciseQuantity neededQuantity) {

		List<Produkt> retValue=new ArrayList<Produkt>();
		if(parseProdukt!=null){
			for(Produkt p:parseProdukt){
				ProduktWithRecountedPrice pRec=getProduktWithRecountedPrice(p,neededQuantity);
				retValue.add(pRec);
			}
		}

		return retValue;
	}













	private ProduktWithRecountedPrice getProduktWithRecountedPrice(Produkt p, PreciseQuantity neededQuantity) {
		String recountedPrice="";
		PreciseQuantity produktQuan=PreciseQuantity.parseFromJspString(p.getQuantityPhrase());
		if(produktQuan.getType()!=neededQuantity.getType()){
			ProblemLogger.logProblem("Wielkoœci skladnika w przepisie+"+neededQuantity+" i w sklepie "+produktQuan+"nie s¹ tego samego typu");
		}else{
			if(produktQuan.getAmount()>=neededQuantity.getAmount()){
				recountedPrice=produktQuan+"->"+p.getCena()+" z³";
			}else{
				int multiplier=1;
				while(produktQuan.getAmount()*multiplier<neededQuantity.getAmount()){
					++multiplier;
				}
				recountedPrice=produktQuan+" x "+multiplier+" -> "
						+p.getCena()+" x "+multiplier+" z³="+p.getCena()*multiplier+" z³";
			}
		}


		ProduktWithRecountedPrice retValue=new ProduktWithRecountedPrice(p, recountedPrice);
		return retValue;
	}






	private PreciseQuantity extractQuantity(String quantityAmount, String quantityType) {
		AmountTypes at=AmountTypes.valueOf(quantityType);
		try{
			float f=Float.parseFloat(quantityAmount);

			if(at!=null){
				return new PreciseQuantity(f, at);
			}

		}catch(NumberFormatException nfe){
			nfe.printStackTrace();
		}

		return null;
	}


	@RequestMapping(value="/correctQuantities")
	public ModelAndView correctQuantities(HttpServletRequest request) {
		String url=request.getParameter(JspStringHolder.recipeUrl_name);

		if(url==null){
			return new ModelAndView("recipeForm");
		}

		ArrayList<MultiProdukt_SearchResult> result;
		try {
			result = RecipeAgent.getPhrasesAndQuantitiesFromRecipeUrl(url);
		} catch (AgentSystemNotStartedException e) {
			return new ModelAndView("agentSystemNotStarted");
		}
		if(result==null){

			return new ModelAndView("agentSystemNotStarted");

		}else{
			ModelAndView mav=new ModelAndView("correctQuantities");

			mav.addObject(JspStringHolder.recipeUrl_name,url);
			ArrayList<PreciseQuantity> quantities=extractQuantities(result);
			mav.addObject("quantities",quantities);


			mav.addObject("results",result);

			return mav;
		}


	}



	@RequestMapping(value="/correctProdukts")
	public ModelAndView correctProdukts(HttpServletRequest request) {
		String url=request.getParameter(JspStringHolder.recipeUrl_name);

		ArrayList<SingleProdukt_SearchResult> goodResults= new ArrayList<SingleProdukt_SearchResult>();
		ArrayList<InvalidSearchResult> usersBadChoice= new ArrayList<InvalidSearchResult>();
		ArrayList<String> skippedResults= new ArrayList<String>();




		int liczbaSkladnikow=
				Integer.parseInt(request.getParameter(JspStringHolder.liczbaSkladnikow));

		for(int i=1;i<=liczbaSkladnikow;i++){
			String searchPhrase=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.searchPhrase_name);
			String produktPhrase=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.produktPhrase_name);
			String quantityPhrase=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.quantity_name);


			String wybranyProdukt=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.skladnikRadio_name);

			Produkt produkt=null;
			if(JspStringHolder.innaOpcja_name.equals(wybranyProdukt)){
				String innyUrl=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.innyUrl_name);
				try {
					produkt=ProduktAgent.getOrScrapProdukt(innyUrl);
					if(produkt!=null){
						ArrayList<Produkt> produkts=new ArrayList<Produkt>();
						produkts.add(produkt);
						SingleProdukt_SearchResult sr=new SingleProdukt_SearchResult(searchPhrase,produktPhrase, quantityPhrase, produkt);
						goodResults.add(sr);
					}

					else
					{
						//znowu pobieramy produkty
						List<Produkt> possibleProdukts = ProduktAgent.searchForProdukt(produktPhrase);
						InvalidSearchResult isr=new InvalidSearchResult(searchPhrase,produktPhrase,quantityPhrase,possibleProdukts,
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
					SingleProdukt_SearchResult sr=new SingleProdukt_SearchResult(searchPhrase,produktPhrase, quantityPhrase, produkt);
					goodResults.add(sr);
				}
			}

		}


		if(usersBadChoice.isEmpty()){
			ModelAndView mav=new ModelAndView("displayNutrientValues");

			try {
				Map<SingleProdukt_SearchResult, ProduktWithBasicIngredients> retrievedBasicNutrientValues = 
						ReadingAgent.retrieveOrScrapBasicNutrientValues(goodResults);
				Collection<BasicIngredientQuantity> sumOfNutrients = ReadingAgent.getSumOfNutrients(retrievedBasicNutrientValues);
				mav.addObject("nutrientsMap", retrievedBasicNutrientValues);
				
				mav.addObject("sumOfNutrients", sumOfNutrients);
			} catch (AgentSystemNotStartedException e) {
				return new ModelAndView("agentSystemNotStarted");
			} catch (ShopNotFoundException e) {
				//pozostawione, nie powinno wyst¹piæ
				e.printStackTrace();
			} catch (Page404Exception e) {
				//pozostawione, nie powinno wyst¹piæ
				e.printStackTrace();
			}
			
			return mav; 

		}else{
			ModelAndView mav=new ModelAndView("correctProducts");

			mav.addObject("badResults",usersBadChoice);

			mav.addObject("correctResults",goodResults);

			mav.addObject("skippedResults",skippedResults);

			return mav;

		}


	}

	private ArrayList<PreciseQuantity> extractQuantities(ArrayList<MultiProdukt_SearchResult> results) {
		ArrayList<PreciseQuantity> retValue=new ArrayList<PreciseQuantity>();

		for(int i=0;i<results.size();i++){
			PreciseQuantity pq=new PreciseQuantity(-1, AmountTypes.szt);
			String quanPhrase=results.get(i).getQuantity();
			if(quanPhrase==null||quanPhrase.equals("")){
				ProblemLogger.logProblem("Empty amount from hidden field");
			}else{
				String[] elems=quanPhrase.split(JspStringHolder.QUANTITY_PHRASE_BORDER);
				if(elems.length<2){
					ProblemLogger.logProblem("Empty quantity from hidden field");
				}else{
					float amount=Float.parseFloat(elems[1]);
					AmountTypes at=AmountTypes.retrieveTypeByName(elems[0]);
					pq=new PreciseQuantity(amount,at);
				}
			}

			retValue.add(i,pq);
		}


		return retValue;
	}

	@RequestMapping(value="/quantitiesCorrected")
	public ModelAndView quantitiesCorrected(HttpServletRequest request) {





		return null;
	}


}
