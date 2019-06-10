package mariusz.ambroziak.kassistant.ai.categorisation.edaman;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;

import api.extractors.AbstractQuantityEngExtractor;
import api.extractors.EdamanCategorisationQExtractor;
import api.extractors.AbstractQuantityEngExtractor.QuantityTranslation;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.NotPreciseQuantity;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.AbstractQuantity;
import mariusz.ambroziak.kassistant.model.utils.ApiIngredientAmount;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

public class EdamanCategoriser extends AbstractQuantityEngExtractor {
	public static final String zamiastNawiasu = " ";
	public static String bracketsRegex="(.*) (\\(.*\\)) (.*)";

	
	
	private static EdamanCategoriser singleton;

	public static void main(String [] args){


		System.out.println();
	}





	public static EdamanCategoriser getSingleton() {
		if(singleton==null) {
			singleton=new EdamanCategoriser();

		}
		return singleton;

	}



	private EdamanCategoriser() {
		super();

	}

	
	public IngredientParsed parseIngredient(String text) {
		IngredientParsed retValue=new IngredientParsed();
		retValue.setOriginalPhrase(text);
		retValue.setProductPhrase(text);
		NotPreciseQuantity retQuantity;
		IngredientCategory retCategory;
		
		if(text==null||text.equals("")){
			return retValue;
		}

		text=text.trim();

		String ommissionLess=text;
		for(String ommision:ommissions) {
			ommissionLess=ommissionLess.replaceAll(" "+ommision, "");
			ommissionLess=ommissionLess.replaceAll(ommision+" ", "");
			ommissionLess=ommissionLess.replaceAll(ommision, "");

		}

		String bracketless=ommissionLess.replaceAll(" \\(.*\\) ", zamiastNawiasu);

		
		Pattern pattern = Pattern.compile(bracketsRegex);
		Matcher matcher = pattern.matcher(ommissionLess);
		boolean matches = matcher.matches();

		
		return null;
	}
	
	
	
		
	



	public static PreciseQuantity extractQuantity(String text){
		PreciseQuantity retValue=null;
		//		text=correctText(text);
		if(text==null||text.equals("")){
			return createInvalidPreciseQuantity();
		}

		text=text.trim();

		String ommissionLess=text;
		for(String ommision:ommissions) {
			ommissionLess=ommissionLess.replaceAll(" "+ommision, "");
			ommissionLess=ommissionLess.replaceAll(ommision+" ", "");
			ommissionLess=ommissionLess.replaceAll(ommision, "");

		}

		String bracketless=ommissionLess.replaceAll(" \\(.*\\) ", zamiastNawiasu);

		
		Pattern pattern = Pattern.compile(bracketsRegex);
		Matcher matcher = pattern.matcher(ommissionLess);
		boolean matches = matcher.matches();

		if(matches) {

			String brackets=matcher.group(2);

			System.out.println(brackets);
			
			String inBrackets = brackets.substring(1,brackets.length()-1);
			
			PreciseQuantity extractedQuantity = EdamanCategorisationQExtractor.extractQuantity(inBrackets);

			if(extractedQuantity!=null&&extractedQuantity.isValid()) {
				return extractedQuantity;
			}
					
		}

		bracketless = getMaxFromRangeIfPossible(bracketless);
		
		
		
		String[] elems = splitOverHyphenOrMax(bracketless);
		



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











	private static String[] splitOverHyphenOrMax(String bracketless) {
		String[] elems=null;

		if(bracketless.contains("-")){
			elems=bracketless.split("-");
		}else if(bracketless.contains(" ")){
			elems=bracketless.split(" ");
		}
		return elems;
	}











	private static String getMaxFromRangeIfPossible(String bracketless) {
		if(Pattern.matches("\\d*-\\d*", bracketless)) {
			bracketless=bracketless.substring(bracketless.indexOf("-"));
		}
		return bracketless;
	}











	public static String correctText(String text) {
		///TODO wykasowac, powinno byc zastąpione recznym sprawdzaniem w bazie danych albo nawet niczym.
		if(text==null||text.equals(""))
			return "";

		String retText=text;


		for(int i=0;i<10;i++) {
			retText=retText.replace(i+"⁄","");
			retText=retText.replace(i+"/","");
			retText=retText.replace(i+".","");

			retText=retText.replace(i+"","");
		}

		for(String portion:translations.keySet()) {
			retText=retText.replace("rounded "+portion+" ", "");
			retText=retText.replace("rounded "+portion+".", "");
			retText=retText.replace(" "+portion+" ", "");
			retText=retText.replace(" "+portion+".", "");


			//		retText=retText.replaceAll("\\d*"+portion+" ", "");
		}

		retText=retText.replace("½", "");
		retText=retText.replace("¼", "");

		retText=retText.trim();

		if(retText.startsWith("of"))
			retText=retText.replace("of","");

		retText=retText.trim();
		return retText;

	}











	private static PreciseQuantity tryGettingAmountFromTwoElements(String[] elems) {
		if(elems==null||elems.length<2)
			return null;

		PreciseQuantity retValue=new PreciseQuantity(-1,null);


		String probablyQuantityTypePhrase=elems[1];
		float parsedFloat = attemptParsingFloat(elems);

		retValue.setAmount(parsedFloat);
		for(AmountTypes at:AmountTypes.values()){
			if(at.getType().equals(probablyQuantityTypePhrase)){
				retValue.setType(at);
			}
		}
		//TODO poprawic



		return retValue;
	}






	private static float attemptParsingFloat(String[] elems) {
		String probablyFloat=elems[0].replaceAll(",", ".").trim();
		float retValue=0;
		if(probablyFloat.endsWith("½")) {
			retValue=0.5f;
			probablyFloat=probablyFloat.substring(0,probablyFloat.length()-1);
		}else if(probablyFloat.endsWith("¼")) {
			retValue=0.5f;
			probablyFloat=probablyFloat.substring(0,probablyFloat.length()-1);
		}
		if(!probablyFloat.isEmpty()) {
			try{
				retValue+=Float.parseFloat(probablyFloat);
			}catch(NumberFormatException e){
				ProblemLogger.logProblem("Unparsable quantity found: "+elems[0]+" "+elems[1]);
			}
		}
		return retValue;
	}





	private static PreciseQuantity createInvalidPreciseQuantity() {
		return new PreciseQuantity(-1,AmountTypes.szt);
	}




	public static QuantityTranslation getTranslationToBaseType(String amount) {
		String temp=(amount==null?"":amount).toLowerCase().trim();
		QuantityTranslation retValue=translations.get(temp);

		if(retValue==null) {
			ProblemLogger.logProblem(amount+ " wasn't parsed properly");
			retValue=new QuantityTranslation(AmountTypes.szt, 0);
		}


		return retValue;


	}






	private static QuantityTranslation tryGettingTranslationToActualQuantityType(String[] elems) {
		QuantityTranslation quantityTranslation = translations.get(elems[1].toLowerCase());
		if(quantityTranslation==null) {
			String quant=elems[1].toLowerCase();
			if(quant.endsWith(".")) {
				quant=quant.substring(0,quant.length()-1);
			}

			quantityTranslation = translations.get(quant);

		}

		return quantityTranslation;
	}










	private static PreciseQuantity extractRecursively(String text) {
		if(text==null||text.isEmpty()||text.indexOf("x")<0)
			return createInvalidPreciseQuantity();

		PreciseQuantity extractedQuantity = extractQuantity(text);
		extractedQuantity.setAmount(extractedQuantity.getAmount());
		return extractedQuantity;
	}






	
	
}
