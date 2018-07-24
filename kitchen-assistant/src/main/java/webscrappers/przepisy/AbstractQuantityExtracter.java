package webscrappers.przepisy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import api.extractors.AbstractQuantityEngExtractor.QuantityTranslation;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;

public class AbstractQuantityExtracter {
	
	protected static Map<String,QuantityTranslation> translations;
	protected static ArrayList<String> ommissions;
	
	static{
		
		
		translations=new HashMap<String, QuantityTranslation>();
		
		translations.put("g",new QuantityTranslation(AmountTypes.mg, 1000) );
		translations.put("kg",new QuantityTranslation(AmountTypes.mg, 1000000) );
		translations.put("szczypta",new QuantityTranslation(AmountTypes.mg, 500) );
		translations.put("gram",new QuantityTranslation(AmountTypes.mg, 1000) );
		translations.put("kilogram",new QuantityTranslation(AmountTypes.mg, 1000000) );
		 
		
		
		translations.put("l",new QuantityTranslation(AmountTypes.ml, 1000) );
		translations.put("---y---eczka",new QuantityTranslation(AmountTypes.ml, 5) );
		translations.put("kropla",new QuantityTranslation(AmountTypes.ml, 0.1f) );
		translations.put("l",new QuantityTranslation(AmountTypes.ml, 1000) );
		translations.put("---y---ka",new QuantityTranslation(AmountTypes.ml, 15) );
		translations.put("---y---ka sto---owa",new QuantityTranslation(AmountTypes.ml, 15) );
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
		ommissions.add("oko---o");
		
		
		
	}
	
	

}
