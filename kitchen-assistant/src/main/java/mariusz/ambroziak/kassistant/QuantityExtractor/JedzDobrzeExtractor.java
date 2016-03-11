package mariusz.ambroziak.kassistant.QuantityExtractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;

import webscrappers.SJPWebScrapper;
import webscrappers.przepisy.AbstractQuantityExtracter;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Recipe_Ingredient;
import mariusz.ambroziak.kassistant.model.utils.AbstractQuantity;
import mariusz.ambroziak.kassistant.model.utils.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.QuantityProdukt;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

public class JedzDobrzeExtractor extends AbstractQuantityExtracter{

	

	
	public static PreciseQuantity extractQuantity(String text){
		PreciseQuantity retValue=new PreciseQuantity();
		
		if(text==null||text.equals(""))
		{
			retValue.setAmount(-1);
			retValue.setType(AmountTypes.szt);
			return retValue;
		}
		

			
		text=text.trim();
		
		
			String[] elems=text.split(" ");
			
			
			float parsedFloat = getFloat(elems);
			
			retValue.setAmount(parsedFloat);
			for(AmountTypes at:AmountTypes.values()){
				if(at.getType().equals(elems[1])){
					retValue.setType(at);
				}
			}
			
			if(retValue.getType()==null){
				QuantityTranslation quantityTranslation = translations.get(elems[1].toLowerCase());
				
				if(quantityTranslation==null){
					Base_Word base_Name = 
						DaoProvider.getInstance().getVariantWordDao().getBase_Name(elems[1]==null?"":elems[1].toLowerCase());
					
					String new_word=null;
					
					
					
					if(base_Name!=null)
						new_word = 	base_Name.getB_word();		
					
					if(new_word==null)
						new_word=SJPWebScrapper.getAndSaveNewRelation(elems[1].toLowerCase());
						
					if(new_word!=null)
						quantityTranslation = translations.get(new_word);
					
				}
				
				
	
				
				if(quantityTranslation==null)
				{
					retValue.setAmount(-1);
					retValue.setType(AmountTypes.szt);
					
					ProblemLogger.logProblem("Nieznana miara "+elems[1]);
					return retValue;
				}else{
					retValue.setType(quantityTranslation.getTargetAmountType());
					retValue.setAmount(retValue.getAmount()*quantityTranslation.getMultiplier());
				}
			}
		
		return retValue;
		
		
	}





	public static float getFloat(String[] elems) {
		String argument=elems[0].replaceAll(",", ".").trim();
		
		try{
			
			return Float.parseFloat(argument); 
			
		}catch(NullPointerException e){
			ProblemLogger.logProblem("Empty quantity found: "+elems[0]+" : "+elems[1]+" on jedzdobrze");
		}catch(NumberFormatException e){
			ProblemLogger.logProblem("Unpareable quantity found: "+elems[0]+" : "+elems[1]+" on jedzdobrze");
		}
		
		return 1f;

	}
	
	
	
}
