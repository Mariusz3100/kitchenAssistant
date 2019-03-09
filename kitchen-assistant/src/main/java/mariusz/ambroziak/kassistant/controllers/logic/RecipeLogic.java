package mariusz.ambroziak.kassistant.controllers.logic;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import api.extractors.EdamanQExtract;
import mariusz.ambroziak.kassistant.agents.ProduktAgent;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.ShopNotFoundException;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.jsp.InvalidSearchResult;
import mariusz.ambroziak.kassistant.model.jsp.ProduktWithRecountedPrice;
import mariusz.ambroziak.kassistant.model.jsp.SearchResult;
import mariusz.ambroziak.kassistant.model.jsp.SingleProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.jsp.SkippedSearchResult;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.GoodBadSkippedResults;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;

public class RecipeLogic {


	protected void setEncoding(HttpServletRequest request) {

		try {
			request.setCharacterEncoding(StringHolder.ENCODING);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	protected ModelAndView returnProduktsCorrectingPage(GoodBadSkippedResults resultsHolder) {
		ModelAndView mav=new ModelAndView("correctProducts");
		mav.addObject("badResults",resultsHolder.getUsersBadChoice());
		mav.addObject("correctResults",resultsHolder.getGoodResults());
		mav.addObject("skippedResults",resultsHolder.getSkippedResults());
		return mav;
	}


	protected InvalidSearchResult convertGoodResultToBadOne(SingleProdukt_SearchResult produktToBeConverted) throws AgentSystemNotStartedException {
		List<Produkt> searchResults= ProduktAgent.searchForProdukt(produktToBeConverted.getProduktPhrase());
		InvalidSearchResult retValue=new InvalidSearchResult(produktToBeConverted.getSearchPhraseAnswered().getSearchPhrase(),
				produktToBeConverted.getProduktPhrase(), 
				produktToBeConverted.getQuantity(), 
				searchResults, 
				generateSorryProduktNotFoundInvalidityReason(produktToBeConverted));

		return retValue;
	}



	protected String generateSorryProduktNotFoundInvalidityReason(SingleProdukt_SearchResult produktToBeChecked) {
		return "We are really sorry, it seems url \""+produktToBeChecked.getProdukt().getUrl()+"\" points to a produkt in our database, that is no longer avaible at the shop. Please, choose something else.";
	}



	protected GoodBadSkippedResults extractGoodBadSkippedResults(HttpServletRequest request) throws AgentSystemNotStartedException {
		GoodBadSkippedResults resultsHolder=new GoodBadSkippedResults();
		String liczbaSkladnikowParameter = request.getParameter(JspStringHolder.liczbaSkladnikow);
		
		int liczbaSkladnikow=0;
		try {
			liczbaSkladnikow=Integer.parseInt(liczbaSkladnikowParameter);
		}catch (Exception e) {
				liczbaSkladnikow=0;//the only more or less safe solution
			}
				
			
		for(int i=1;i<=liczbaSkladnikow;i++){
			String searchPhrase=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.searchPhrase_name);
			String produktPhrase=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.produktPhrase_name);
			String quantityPhrase=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.quantity_name);

			String wybranyProdukt=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.skladnikRadio_name);
			Produkt produkt=null;
			if(JspStringHolder.innaOpcja_name.equals(wybranyProdukt)){
				String innyUrl=request.getParameter(JspStringHolder.skladnik_name+i+"_"+JspStringHolder.innyUrl_name);
				try {
					produkt=retieveProdukts(innyUrl,PreciseQuantity.parseFromJspString(quantityPhrase),"$");
					if(produkt!=null){
						SingleProdukt_SearchResult sr=new SingleProdukt_SearchResult(searchPhrase,produktPhrase, quantityPhrase, produkt);
						resultsHolder.addGoodResult(sr);
					}else{
						//znowu pobieramy produkty
						List<Produkt> possibleProdukts = ProduktAgent.searchForProdukt(produktPhrase);
						InvalidSearchResult isr=new InvalidSearchResult(searchPhrase,produktPhrase,quantityPhrase,possibleProdukts,
								"Wygl---da na to, ---e strona internetowa pod url \""+innyUrl+"\" nie istnieje, lub nie opisuje ---adnego produktu");
						resultsHolder.addUsersBadChoice(isr);
					}
				} catch (ShopNotFoundException e) {
					//invalidShopUrls.put(produktPhrase,innyUrl);
					List<Produkt> searchResults;
					searchResults = ProduktAgent.searchForProdukt(produktPhrase);
					searchResults =recountPrices(searchResults,PreciseQuantity.parseFromJspString(quantityPhrase),"$");
					String invalidityReason = //PL:"Wygl---da na to, ---e url \""+innyUrl
							//+"\" nie zosta--- rozpoznany jako pasuj---cy do ---adnego ze wspieranych sklep---w.";
							"It seems url \""+innyUrl+"\" did not match any supported shops.";
					
					InvalidSearchResult isr=new InvalidSearchResult(searchPhrase,produktPhrase,quantityPhrase,searchResults,
							invalidityReason);
					resultsHolder.addUsersBadChoice(isr);

				} 

			}else if(JspStringHolder.pominOpcja_name.equals(wybranyProdukt)){
				resultsHolder.addSkippedResults(new SkippedSearchResult(searchPhrase));

			}else if(wybranyProdukt!=null&&wybranyProdukt.startsWith(JspStringHolder.radioValuePrefix)){
				if(wybranyProdukt!=null&&wybranyProdukt.length()>0)
					wybranyProdukt=wybranyProdukt.replaceFirst(Pattern.quote(JspStringHolder.radioValuePrefix), "");

				produkt=DaoProvider.getInstance().getProduktDao().getProduktsByURL(wybranyProdukt);

				if(produkt==null){
					ProblemLogger.logProblem(
							"Wygl---da na to, ---e produkt wybrany przez przycisk radio nie istnieje w systemie!: "
									+wybranyProdukt);
					List<Produkt> searchResults;

					searchResults = ProduktAgent.searchForProdukt(produktPhrase);

					InvalidSearchResult isr=new InvalidSearchResult(searchPhrase,produktPhrase,quantityPhrase,searchResults,
							"UPS! Wydaje si---, ---e zaproponowany przez nas produkt nie wyst---puje w naszym systemie!! Powiadom o tym administratora albo co---...");
					resultsHolder.addUsersBadChoice(isr);

				}else{
					produkt=retrieveProdukt(PreciseQuantity.parseFromJspString(quantityPhrase),"$",produkt);

					SingleProdukt_SearchResult sr=new SingleProdukt_SearchResult(searchPhrase,produktPhrase, quantityPhrase, produkt);
					resultsHolder.addGoodResult(sr);
				}
			}

		}
		return resultsHolder;
	}
	protected List<Produkt> recountPrices(List<Produkt> searchResults, PreciseQuantity preciseQuantity, String string) {
		return searchResults;
	}
	
	protected Produkt retieveProdukts(String innyUrl, PreciseQuantity neededQuantity, String curency) throws ShopNotFoundException, AgentSystemNotStartedException {
		return ProduktAgent.getOrScrapProdukt(innyUrl);
	}



	protected ArrayList<PreciseQuantity> extractQuantities(ArrayList<SearchResult> result) {
		ArrayList<PreciseQuantity> retValue=new ArrayList<PreciseQuantity>();

		for(int i=0;i<result.size();i++){
			PreciseQuantity pq=new PreciseQuantity(-1, AmountTypes.szt);
			String quanPhrase=result.get(i).getQuantity();
			if(quanPhrase==null||quanPhrase.equals("")){
				ProblemLogger.logProblem("Empty amount from hidden field");
			}else{
				String[] elems=quanPhrase.split(JspStringHolder.QUANTITY_PHRASE_BORDER);
				if(elems.length<2){
					ProblemLogger.logProblem("Empty or too short quantity from hidden field: "+quanPhrase);
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
	
	
	protected List<Produkt> getProduktsWithRecountedPrice(List<Produkt> parseProdukt,
			PreciseQuantity neededQuantity, String curency) {

		List<Produkt> retValue=new ArrayList<Produkt>();
		if(parseProdukt!=null){
			for(Produkt p:parseProdukt){
				ProduktWithRecountedPrice pRec=getProduktWithRecountedPrice(p,neededQuantity,curency);
				retValue.add(pRec);
			}
		}

		return retValue;
	}
	
	protected void moveNotFoundProduktFromGoodToBadChoices(String produktUrl, GoodBadSkippedResults resultsToBeUpdated) throws AgentSystemNotStartedException {
		//TODO correct
		
		//		String shortUrl=AuchanAbstractScrapper.getAuchanShortestWorkingUrl(produktUrl);
//		Iterator<SingleProdukt_SearchResult> goodProduktsIterator = resultsToBeUpdated.getGoodResults().iterator();
//		while(goodProduktsIterator.hasNext()){
//			SingleProdukt_SearchResult produktToBeChecked = goodProduktsIterator.next();
//
//			if(shortUrl.equals(produktToBeChecked.getProdukt().getUrl())){
//				goodProduktsIterator.remove();
//
//				InvalidSearchResult goodResultConverted=convertGoodResultToBadOne(produktToBeChecked);
//
//				resultsToBeUpdated.getUsersBadChoice().add(goodResultConverted);
//			}
//		}

	}
	
	protected ProduktWithRecountedPrice getProduktWithRecountedPrice(Produkt p, PreciseQuantity neededQuantity, String curency) {
		String recountedPrice="";
		PreciseQuantity produktQuan=PreciseQuantity.parseFromJspString(p.getQuantityPhrase());
		if(produktQuan.getType()!=neededQuantity.getType()){
			ProblemLogger.logProblem("Wielkości skladnika w przepisie+"+neededQuantity+" i w sklepie "+produktQuan+"nie są tego samego typu");
		}else{
			if(produktQuan.getAmount()>=neededQuantity.getAmount()){
				recountedPrice=produktQuan+"->"+p.getCena()+" "+curency;
			}else{
				int multiplier = neededQuantity.getMultiplierOfProduktQuantityForNeededQuantity( produktQuan);
				recountedPrice=produktQuan+" x "+multiplier+" -> "
						+p.getCena()+" x "+multiplier+" "+curency+"="+p.getCena()*multiplier+" "+curency;
			}
		}


		ProduktWithRecountedPrice retValue=new ProduktWithRecountedPrice(p, recountedPrice);
		return retValue;
	}

	
	protected PreciseQuantity extractQuantity(String quantityAmount, String quantityType) {
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


	protected ModelAndView returnAgentSystemNotStartedPage() {
		return new ModelAndView("agentSystemNotStarted");
	}
	protected Produkt retrieveProdukt(PreciseQuantity neededQuantity, String curency, Produkt orScrapProdukt) {
		return orScrapProdukt;
	}

}
