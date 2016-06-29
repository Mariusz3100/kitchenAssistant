package webscrappers.przepisy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;

public class AbstractQuantityExtracter {
	
	protected static Map<String,QuantityTranslation> translations;
	protected static ArrayList<String> ommissions;
	
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
		translations.put("",new QuantityTranslation(AmountTypes.szt, 1) );
		
		ommissions=new ArrayList<String>();
		
		ommissions.add("ok.");
		ommissions.add("oko這");
		
		
		
	}
	
	
	public static class QuantityTranslation{
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
}
