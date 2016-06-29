package mariusz.ambroziak.kassistant.controllers;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import mariusz.ambroziak.kassistant.QuantityExtractor.AuchanQExtract;
import mariusz.ambroziak.kassistant.agents.FoodIngredientAgent;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.PreciseQuantityWithPhrase;
import mariusz.ambroziak.kassistant.utils.CompoundMapManipulator;
import mariusz.ambroziak.kassistant.utils.StringHolder;
import webscrappers.IleWazyScrapper;
import webscrappers.JedzDobrzeScrapper;
import webscrappers.SJPWebScrapper;



//import database.holders.StringHolder;

@Controller
public class FoodIngredientController {

	@RequestMapping(value="/get_food_ingredient_data")
	public ModelAndView produkts(HttpServletRequest request) {
		setEncoding(request);

		String phrase=request.getParameter("ingredient_phrase");

		if(phrase==null||phrase.equals(""))
		{
			return new ModelAndView("foodIngredientForm");
		}else{

			ModelAndView mav=new ModelAndView("List");
			ArrayList<String> lista=new ArrayList<String>();


			Map<Nutrient, PreciseQuantity> scrapSkladnik = null;
			try {
				scrapSkladnik = FoodIngredientAgent.parseFoodIngredient(phrase);
			} catch (AgentSystemNotStartedException e) {
				return createAgentSystemNotStartedMav();
			}





			if(scrapSkladnik!=null&&scrapSkladnik.size()>0)
			{
				lista.add("Dla skladnika zywnoœciowego: "+phrase+" znaleziono nastêpuj¹ce wartosci od¿ywcze:<br>");

				for(Nutrient key:scrapSkladnik.keySet()){
					lista.add(key.getName()+" - "+scrapSkladnik.get(key));
				}				
			}else{
				lista.add("Skladnika zywnoœciowego: "+phrase+" nie odnaleziono.<br>");
			}


			mav.addObject("list",lista);


			return mav;
		}
	}


	@RequestMapping(value="/logFood")
	public ModelAndView simpleFoodLogging(HttpServletRequest request) {
		setEncoding(request);

		String phrase=request.getParameter("foodPhrase");

		if(phrase==null||phrase.equals(""))
		{
			return new ModelAndView("foodLogForm");
		}else{
			try {
				return createMavIfSystemStarted(phrase);
			} catch (AgentSystemNotStartedException e) {
				return createAgentSystemNotStartedMav();
			}
		}
	}


	private ModelAndView createMavIfSystemStarted(String phrase) throws AgentSystemNotStartedException {
		StringBuffer errors=new StringBuffer();
		LinkedList<PreciseQuantityWithPhrase> quantitiesAndProduktPhrases = extractQuantitiesAndProduktPhrases(phrase,errors);
		//entry->[Nutrient->Amount]
		Map<String, Map<Nutrient, PreciseQuantity>> resultsMap
		= getNutrientsDataAdjustedForAmounts(quantitiesAndProduktPhrases,errors);
		ModelAndView mav=new ModelAndView("foodLogResults");
		mav.addObject("originalPhrase", phrase);
		mav.addObject("invalidEntriesInfo", errors);
		mav.addObject("amountsMap", resultsMap);

		CompoundMapManipulator<String,Nutrient> mapManipulator=new CompoundMapManipulator<String, Nutrient>();
		mapManipulator.setFromDifferentCompoundMap(resultsMap);

		mav.addObject("allNutrients",mapManipulator.getAllInnerMapsKeys());
		mav.addObject("amountsSum",mapManipulator.sumUpInnerMaps());


		return mav;
	}


	private Map<String, Map<Nutrient, PreciseQuantity>> getNutrientsDataAdjustedForAmounts(
			LinkedList<PreciseQuantityWithPhrase> quantitiesAndProduktPhrases,StringBuffer errors) throws AgentSystemNotStartedException {
		Map<String, Map<Nutrient, PreciseQuantity>> resultsMap=new HashMap<String, Map<Nutrient,PreciseQuantity>>();

		for(PreciseQuantityWithPhrase pqwp:quantitiesAndProduktPhrases){
			Map<Nutrient, PreciseQuantity> nutrients = FoodIngredientAgent.parseFoodIngredient(pqwp.getProduktPhrase());
			if(pqwp.getQuan().isValid())
			if(AmountTypes.szt.equals(pqwp.getAmountType())){
				String produktBaseName=SJPWebScrapper.retrieveFromDbOrScrapAndSaveInDb(pqwp.getProduktPhrase());
				PreciseQuantity usualAmountOfSztuka = IleWazyScrapper.getUsualAmountOfSztuka(produktBaseName);
				if(usualAmountOfSztuka!=null&&usualAmountOfSztuka.isValid())
				{
					usualAmountOfSztuka.multiplyBy(pqwp.getAmount());;
					pqwp.setQuan(usualAmountOfSztuka);
				}else{
					errors.append("nie uda³o siê oceniæ, jakiej wielkoœci mo¿e byæ œrednia porcja produktu \""+pqwp.getProduktPhrase()
					+"\", pozostawiono 0 gram. Spróbuj jeszcze raz, ale podaj wielkoœæ w gramach.");
					pqwp.setAmount(0);
					pqwp.setAmountType(AmountTypes.mg);
				}
			}

			adjustNutrientsForActualAmount(pqwp.getQuantity(),nutrients);

			resultsMap.put(pqwp.toString(), nutrients);
		}
		return resultsMap;
	}


	private LinkedList<PreciseQuantityWithPhrase> extractQuantitiesAndProduktPhrases(String phrase,StringBuffer errorList) {
		String[] entries=phrase.split(",");
		LinkedList<PreciseQuantityWithPhrase> phraseDividedQuantitiesParsed=new LinkedList<PreciseQuantityWithPhrase>();

		for(String entry:entries){
			String[] dividedEntry=entry.trim().split(":");
			if(dividedEntry.length<2){
				errorList.append("entry "+entry+" doesn't have proper distinction between quantity and produkt<br>");
			}
			String amount = dividedEntry[0];
			String produktPhrase= dividedEntry[1];
			PreciseQuantity extractedQuantity = AuchanQExtract.extractQuantity(amount);

			if(extractedQuantity.isValid()){
				phraseDividedQuantitiesParsed.add(new PreciseQuantityWithPhrase(produktPhrase, extractedQuantity));
			}else{
				errorList.append("Nie uda³o siê sparsowaæ wielkoœci z "+amount+"<br>");
			}
			
		}
		return phraseDividedQuantitiesParsed;
	}




	private void setEncoding(HttpServletRequest request) {
		try {
			request.setCharacterEncoding(StringHolder.ENCODING);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}


	private void adjustNutrientsForActualAmount(PreciseQuantity extractedQuantity, Map<Nutrient, PreciseQuantity> nutrients) {
		float fractionOf100g = 0;
		if(extractedQuantity.isValid())
			fractionOf100g=extractedQuantity.getFractionOf100g();

		for(Entry<Nutrient, PreciseQuantity> e:nutrients.entrySet()){
			e.getValue().multiplyBy(fractionOf100g);
		}
	}


	private ModelAndView createAgentSystemNotStartedMav() {
		return new ModelAndView("agentSystemNotStarted");
	}




	@RequestMapping(value="/get_food_data_banan")
	public ModelAndView produkts11() {

		ModelAndView mav=new ModelAndView("List");

		HashMap<Nutrient, PreciseQuantity> scrapSkladnik = JedzDobrzeScrapper.scrapSkladnik("banan");

		ArrayList<String> lista=new ArrayList<String>();

		lista.add("Dla skladnika zywnoœciowego: banan znaleziono nastêpuj¹ce wartosci od¿ywcze:");

		for(Nutrient key:scrapSkladnik.keySet()){
			lista.add(key.getName()+" - "+scrapSkladnik.get(key));

		}

		mav.addObject("list",lista);


		return mav;
	}



}
