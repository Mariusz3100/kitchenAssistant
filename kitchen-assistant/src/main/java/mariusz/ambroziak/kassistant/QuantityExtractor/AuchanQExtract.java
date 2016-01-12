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
import mariusz.ambroziak.kassistant.model.jsp.QuantityProdukt;
import mariusz.ambroziak.kassistant.utils.AmountTypes;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

public class AuchanQExtract extends AbstractQuantityExtracter{

	

	
	public static QuantityProdukt extractQuantity(String text){
		QuantityProdukt retValue=new QuantityProdukt();
		
		if(text==null||text.equals(""))
		{
			retValue.setAmount(-1);
			retValue.setAmountType(AmountTypes.szt);
			return retValue;
		}
		

			
		text=text.trim();
		
		if(text.contains("x")){
			String[] elems=text.split("x");
			
			Float multiplier=getFloat(elems);
			QuantityProdukt extractedQuantity = extractQuantity(elems[1]);
			extractedQuantity.setAmount(extractedQuantity.getAmount()*multiplier);
			
			return extractedQuantity;
		}else{
			String[] elems=text.split(" ");
			
			if(elems.length<2){
				elems=new String[2];
				
				Pattern p = Pattern.compile("[0-9.,]+");
				
				Matcher m=p.matcher(text);
				
				if(m.find()){
					
					
					elems[0]=m.group();
					elems[1]=text.replaceFirst(elems[0], "");
					
					
			    }
			}
			
			float parsedFloat = getFloat(elems);
			
			retValue.setAmount(parsedFloat);
			for(AmountTypes at:AmountTypes.values()){
				if(at.getType().equals(elems[1])){
					retValue.setAmountType(at);
				}
			}
			
			if(retValue.getAmountType()==null){
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
					retValue.setAmountType(AmountTypes.szt);
					
					ProblemLogger.logProblem("Nieznana miara "+elems[1]);
					return retValue;
				}else{
					retValue.setAmountType(quantityTranslation.getTargetAmountType());
					retValue.setAmount(retValue.getAmount()*quantityTranslation.getMultiplier());
				}
			}
		}
		return retValue;
		
		
	}





	public static float getFloat(String[] elems) {
		String argument=elems[0].replaceAll(",", ".").trim();
		
		try{
			
			return Float.parseFloat(argument); 
			
		}catch(NullPointerException e){
			ProblemLogger.logProblem("Empty quantity found: "+elems[0]+" : "+elems[1]);
		}catch(NumberFormatException e){
			ProblemLogger.logProblem("Unpareable quantity found: "+elems[0]+" : "+elems[1]);
		}
		
		return 1f;

	}
	
	

	
	
	public static QuantityProdukt retrieveProduktAmountData(Element e) {
		// TODO Auto-generated method stub
		String ingredient = e.text();
		QuantityProdukt retValue =null;
		
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
