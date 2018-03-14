package webscrappers.przepisy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;

import api.extractors.AbstractQuantityEngExtractor.QuantityTranslation;
import webscrappers.SJPWebScrapper;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Recipe_Ingredient;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.utils.PreciseQuantityWithPhrase;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

public class PrzepisyPLQExtract extends AbstractQuantityExtracter{

	

	
	public static PreciseQuantityWithPhrase extractQuantity(String text){
		PreciseQuantityWithPhrase retValue=new PreciseQuantityWithPhrase(text, null);
		
		if(text==null||text.equals(""))
		{
			retValue.setAmount(-1);
			retValue.setAmountType(AmountTypes.szt);
			return retValue;
		}
		
		for(String x:ommissions)
			text=text.replaceAll(x, "");
			
		text=text.trim();
		
		String[] elems=text.split(" ");
		
		retValue.setAmount(Float.parseFloat(elems[0].replaceAll(",", ".")));
		for(AmountTypes at:AmountTypes.values()){
			if(at.getType().equals(elems[1])){
				retValue.setAmountType(at);
			}
		}
		
		if(retValue.getAmountType()==null){
			QuantityTranslation quantityTranslation = translations.get(elems[1]);
			
			if(quantityTranslation==null){
				Base_Word base_Name = 
					DaoProvider.getInstance().getVariantWordDao().getBase_Name(elems[1]);
				
				String new_word=null;
				
				
				
				if(base_Name!=null)
					new_word = 	base_Name.getB_word();		
				
				if(new_word==null)
					new_word=SJPWebScrapper.getAndSaveNewRelation(elems[1]);
					
				if(new_word!=null)
					quantityTranslation = translations.get(new_word);
				
			}
			
			

			
			if(quantityTranslation==null)
			{
				retValue.setAmount(-1);
				retValue.setAmountType(AmountTypes.szt);
				
				ProblemLogger.logProblem("Nieznana miara "+elems[1]);
				return retValue;
			}else{
				retValue.setAmountType(quantityTranslation.getTargetAmountType());
				retValue.setAmount(retValue.getAmount()*quantityTranslation.getMultiplier());
			}
		}
		
		return retValue;
		
		
	}
	
	

	
	
	public static PreciseQuantityWithPhrase retrieveProduktAmountData(Element e) {
		// TODO Auto-generated method stub
		String ingredient = e.text();
		PreciseQuantityWithPhrase retValue =null;
		
		if(ingredient.indexOf('(')>0&&ingredient.indexOf(')')>0){
			String attemptedQ=
					ingredient.substring(ingredient.indexOf('(')+1,ingredient.indexOf(')'));
				
			try{
				retValue=extractQuantity(attemptedQ);
			}catch(IllegalArgumentException ex){
				retValue=null;
			}
			
			ingredient=ingredient.replaceAll(Pattern.quote(attemptedQ), "")
					.replaceAll("\\(", "")
					.replaceAll("\\)", "").trim();
			
		}
		
		if(retValue==null){
			String quantity=extractQuantityString(e);
			retValue = extractQuantity(quantity);
		}
		
		
		return retValue;
	}

	
	private static String extractQuantityString(Element e) {
		
		String quantity=e.parent().select(".quantity").text();
		
		
		if(quantity==null||quantity.equals(""))
			quantity=e.parent().parent().select(".quantity").text();
		return quantity;
	}
	
}
