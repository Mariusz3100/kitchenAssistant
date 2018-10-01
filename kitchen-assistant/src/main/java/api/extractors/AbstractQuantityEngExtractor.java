package api.extractors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import api.extractors.AbstractQuantityEngExtractor.QuantityTranslation;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;

public class AbstractQuantityEngExtractor {
	
	protected static Map<String,QuantityTranslation> translations;
	protected static ArrayList<String> ommissions;
	
	static{
		
		
		translations=new HashMap<String, QuantityTranslation>();
		
		translations.put("mg",new QuantityTranslation(AmountTypes.mg, 1) );
		translations.put("ml",new QuantityTranslation(AmountTypes.ml, 1) );
		translations.put("szt",new QuantityTranslation(AmountTypes.szt, 1) );
		translations.put("kcal",new QuantityTranslation(AmountTypes.kalorie, 1) );

		
		translations.put("µg",new QuantityTranslation(AmountTypes.mg, 0.000001f) );
		translations.put("g",new QuantityTranslation(AmountTypes.mg, 1000) );
		translations.put("kg",new QuantityTranslation(AmountTypes.mg, 1000000) );
		translations.put("pinch",new QuantityTranslation(AmountTypes.mg, 500) );
		translations.put("gram",new QuantityTranslation(AmountTypes.mg, 1000) );
		translations.put("kilogram",new QuantityTranslation(AmountTypes.mg, 1000000) );
		translations.put("lbs",new QuantityTranslation(AmountTypes.mg, 453000) );
		translations.put("pound",new QuantityTranslation(AmountTypes.mg, 453000) );
		translations.put("lbs.",new QuantityTranslation(AmountTypes.mg, 453000) );
		translations.put("lb",new QuantityTranslation(AmountTypes.mg, 453000) );
		translations.put("pounds",new QuantityTranslation(AmountTypes.mg, 453000) );
		 
		
		
		translations.put("l",new QuantityTranslation(AmountTypes.ml, 1000) );
		translations.put("teaspoon",new QuantityTranslation(AmountTypes.ml, 5) );
		translations.put("teaspoons",new QuantityTranslation(AmountTypes.ml, 5) );
		translations.put("drop",new QuantityTranslation(AmountTypes.ml, 0.1f) );
//		translations.put("l",new QuantityTranslation(AmountTypes.ml, 1000) );
		translations.put("tablespoon",new QuantityTranslation(AmountTypes.ml, 15) );
		translations.put("tsp",new QuantityTranslation(AmountTypes.ml, 15) );
		translations.put("tablespoons",new QuantityTranslation(AmountTypes.ml, 15) );
		translations.put("spoon",new QuantityTranslation(AmountTypes.ml, 15) );
		translations.put("spoons",new QuantityTranslation(AmountTypes.ml, 15) );
		translations.put("tbsp",new QuantityTranslation(AmountTypes.ml, 15) );
		translations.put("tbs",new QuantityTranslation(AmountTypes.ml, 15) );
		translations.put("cup",new QuantityTranslation(AmountTypes.ml, 250) );
		translations.put("cups",new QuantityTranslation(AmountTypes.ml, 250) );
		translations.put("glass",new QuantityTranslation(AmountTypes.ml, 250) );
		translations.put("glasses",new QuantityTranslation(AmountTypes.ml, 250) );
		translations.put("milliliter",new QuantityTranslation(AmountTypes.ml, 1) );
		translations.put("liter",new QuantityTranslation(AmountTypes.ml, 1000) );

		translations.put("piece",new QuantityTranslation(AmountTypes.szt, 1) );
		translations.put("pieces",new QuantityTranslation(AmountTypes.szt, 1) );
		translations.put("pcs",new QuantityTranslation(AmountTypes.szt, 1) );
		translations.put("clove",new QuantityTranslation(AmountTypes.szt, 1) );
		translations.put("cloves",new QuantityTranslation(AmountTypes.szt, 1) );
		translations.put("pack",new QuantityTranslation(AmountTypes.szt, 1) );
		translations.put("bunch",new QuantityTranslation(AmountTypes.szt, 1) );
		
		
//		translations.put("sztuka",new QuantityTranslation(AmountTypes.szt, 1) );
//		translations.put("",new QuantityTranslation(AmountTypes.szt, 1) );
		
		ommissions=new ArrayList<String>();
		
		ommissions.add("circa");
		ommissions.add("round");
		
		
		
	}
	
	
	public static class QuantityTranslation{
		private AmountTypes targetAmountType=null;
		private float multiplier=0;
		
		
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
		
		
		public PreciseQuantity getQuantity() {
			return getQuantity(1);
		}		
		
		public PreciseQuantity getQuantity(float multiplier) {
			if(getMultiplier()>0.0&&getTargetAmountType()!=null)
				return new PreciseQuantity(getMultiplier()*multiplier, getTargetAmountType());
			else
				return null;
		}
	}
}
