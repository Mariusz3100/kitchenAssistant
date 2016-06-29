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
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.AbstractQuantity;
import mariusz.ambroziak.kassistant.model.utils.PreciseQuantityWithPhrase;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

public class AuchanQExtract extends AbstractQuantityExtracter{




	@SuppressWarnings("unused")
	public static PreciseQuantity extractQuantity(String text){
		PreciseQuantity retValue=null;
		text=correctText(text);
		if(text==null||text.equals("")){
			return createInvalidPreciseQuantity();
		}

		text=text.trim();
		if(text.contains("x")){
			return extractRecursively(text);
		}
		
		String[] elems=text.split(" ");
		if(elems.length<2){
			elems = extractAmountArrayFromSpacelessText(text);
		}

		retValue=tryGettingAmountFromTwoElements(elems);

		if(retValue.getType()==null){
			QuantityTranslation quantityTranslation = tryGettingTranslationToActualQuantityType(elems);
			if(quantityTranslation==null)
			{
				ProblemLogger.logProblem("Nieznana miara "+elems[1]);
				return createInvalidPreciseQuantity();
			}else{
				retValue.setType(quantityTranslation.getTargetAmountType());
				retValue.setAmount(retValue.getAmount()*quantityTranslation.getMultiplier());
			}
		}

		return retValue;


	}











	private static String correctText(String text) {
		///TODO wykasowaæ, powinno byæ zast¹pione rêcznym sprawdzaniem w bazie danych albo nawet niczym.
		if(text==null||text.equals(""))
			return "";
		
		if(text.startsWith("/"))
			text=text.substring(1);
		
		return text;
		
	}











	private static PreciseQuantity createInvalidPreciseQuantity() {
		return new PreciseQuantity(-1,AmountTypes.szt);
	}











	private static QuantityTranslation tryGettingTranslationToActualQuantityType(String[] elems) {
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
		return quantityTranslation;
	}











	private static PreciseQuantity tryGettingAmountFromTwoElements(String[] elems) {
		if(elems==null||elems.length<2)
			return null;

		PreciseQuantity retValue=new PreciseQuantity(-1,null);
		String probablyFloat=elems[0].replaceAll(",", ".").trim();
		String probablyQuantityTypePhrase=elems[1];
		float parsedFloat = -1;

		try{
			parsedFloat=Float.parseFloat(probablyFloat);
		}catch(NumberFormatException e){
			ProblemLogger.logProblem("Unparsable quantity found: "+elems[0]+" "+elems[1]);
		}
		
		retValue.setAmount(parsedFloat);
		for(AmountTypes at:AmountTypes.values()){
			if(at.getType().equals(probablyQuantityTypePhrase)){
				retValue.setType(at);
			}
		}
		//TODO poprawiæ
		
		
		
		return retValue;
	}

	
	
	private static String[] extractAmountArrayFromSpacelessText(String text) {
		String[] elems;
		elems=new String[2];

		Pattern p = Pattern.compile("[0-9.,]+");

		Matcher m=p.matcher(text);

		if(m.find()){
			elems[0]=m.group();
			elems[1]=text.replaceFirst(elems[0], "");
		}
		return elems;
	}











	private static PreciseQuantity extractRecursively(String text) {
		if(text==null||text.isEmpty()||text.indexOf("x")<0)
			return createInvalidPreciseQuantity();
		
		String[] elems=text.split("x");
		String attemptedMultiplier=elems[0];
		String attemptedBase=elems[1];
		
		
		Float multiplier=null;
		
		try{
			multiplier=Float.parseFloat(attemptedMultiplier);
		}catch(NumberFormatException e){
			attemptedMultiplier=elems[1];
			attemptedBase=elems[0];
			multiplier=Float.parseFloat(attemptedMultiplier);
		}
		
		
		PreciseQuantity extractedQuantity = extractQuantity(attemptedBase);
		extractedQuantity.setAmount(extractedQuantity.getAmount()*multiplier);
		return extractedQuantity;
	}





//	public static float getFloat(String[] elems) {
//		String argument=elems[0].replaceAll(",", ".").trim();
//
//		try{
//
//			return Float.parseFloat(argument); 
//
//		}catch(NullPointerException e){
//			ProblemLogger.logProblem("Empty quantity found: "+elems[0]+" : "+elems[1]);
//		}catch(NumberFormatException e){
//			ProblemLogger.logProblem("Unparsable quantity found: "+elems[0]+" : "+elems[1]);
//		}
//
//		return 1f;
//
//	}





	public static AbstractQuantity retrieveProduktAmountData(Element e) {
		String ingredient = e.text();
		AbstractQuantity retValue =null;

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
