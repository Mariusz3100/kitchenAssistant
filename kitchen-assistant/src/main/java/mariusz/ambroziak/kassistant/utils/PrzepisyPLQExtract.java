package mariusz.ambroziak.kassistant.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;

import webscrappers.Jsoup.SJPWebScrapper;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Recipe_Ingredient;
import mariusz.ambroziak.kassistant.model.jsp.QuantityProdukt;

public class PrzepisyPLQExtract {
	private static Map<String,QuantityTranslation> translations;
	private static ArrayList<String> ommissions;
	
	static{
		
		
		translations=new HashMap<String, PrzepisyPLQExtract.QuantityTranslation>();
		
		translations.put("g",new QuantityTranslation(AmountTypes.mg, 1000) );
		translations.put("kg",new QuantityTranslation(AmountTypes.mg, 1000000) );
		translations.put("szczypta",new QuantityTranslation(AmountTypes.mg, 500) );
		translations.put("gram",new QuantityTranslation(AmountTypes.mg, 1000) );
		translations.put("kilogram",new QuantityTranslation(AmountTypes.mg, 1000000) );
		 
		
		
		translations.put("l",new QuantityTranslation(AmountTypes.ml, 1000) );
		translations.put("造瞠czka",new QuantityTranslation(AmountTypes.ml, 5) );
		translations.put("kropla",new QuantityTranslation(AmountTypes.ml, 0.1f) );
		translations.put("l",new QuantityTranslation(AmountTypes.ml, 1000) );
		translations.put("造磬a",new QuantityTranslation(AmountTypes.ml, 15) );
		translations.put("造磬a sto這wa",new QuantityTranslation(AmountTypes.ml, 15) );
		translations.put("szklanka",new QuantityTranslation(AmountTypes.ml, 250) );
		translations.put("mililitr",new QuantityTranslation(AmountTypes.ml, 1) );
		translations.put("litr",new QuantityTranslation(AmountTypes.ml, 1000) );

		translations.put("sztuka",new QuantityTranslation(AmountTypes.szt, 1) );
		translations.put("opakowanie",new QuantityTranslation(AmountTypes.szt, 1) );
		translations.put("opak",new QuantityTranslation(AmountTypes.szt, 1) );
		translations.put("opak.",new QuantityTranslation(AmountTypes.szt, 1) );

		translations.put("sztuka",new QuantityTranslation(AmountTypes.szt, 1) );
		
		ommissions=new ArrayList<String>();
		
		ommissions.add("ok.");
		ommissions.add("oko這");
		
	}
	
	public static QuantityProdukt extractQuantity(String text){
		QuantityProdukt retValue=new QuantityProdukt();
		
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
	
	
	static class QuantityTranslation{
		private AmountTypes targetAmountType;
		private float multiplier;
		
		
		public QuantityTranslation(AmountTypes targetAmountType, float multiplier) {
			super();
			this.targetAmountType = targetAmountType;
			this.multiplier = multiplier;
		}
		public AmountTypes getTargetAmountType() {
			return targetAmountType;
		}
		public void setTargetAmountType(AmountTypes targetAmountType) {
			this.targetAmountType = targetAmountType;
		}
		public float getMultiplier() {
			return multiplier;
		}
		public void setMultiplier(float multiplier) {
			this.multiplier = multiplier;
		}
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
