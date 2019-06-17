package mariusz.ambroziak.kassistant.ai.categorisation.edaman;

import java.util.regex.Pattern;

import api.extractors.AbstractQuantityEngExtractor;
import api.extractors.EdamanQExtract;
import mariusz.ambroziak.kassistant.Apiclients.edaman.nutrientClients.EdamaneIngredientParsingApiClient;
import mariusz.ambroziak.kassistant.ai.categorisation.MetadataConstants;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
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

	public IngredientParsed parseIngredient(String text,String originalSearchPhrase) {
		IngredientUnparsedApiDetails unparsedDetails=EdamaneIngredientParsingApiClient.parseIngredient(text);
		IngredientCategoryHierarchy.getSingletonCategoryRoot();//initialize hierarchy
		IngredientCategory foundCategory = checkSpecialCaseCategories(originalSearchPhrase, unparsedDetails);
		
		if(foundCategory==null||foundCategory.checkIfEmpty()) {
			foundCategory=calculateCategory(unparsedDetails);

		}

		IngredientParsed retValue=new IngredientParsed(unparsedDetails);
		PreciseQuantity quantity = EdamanQExtract.getTranslationToBaseType(unparsedDetails.getAmountTypePhrase()).getQuantity((float)unparsedDetails.getAmount());

		retValue.setQuantity(quantity);
		retValue.setCategory(foundCategory);

		return retValue;	
	}





	private IngredientCategory checkSpecialCaseCategories(String originalSearchPhrase,
			IngredientUnparsedApiDetails unparsedDetails) {
		IngredientCategory foundCategory=null;

		if(unparsedDetails.getProductPhrase().equals(originalSearchPhrase)
				||unparsedDetails.getProductPhrase().equals(originalSearchPhrase+"s")
				||unparsedDetails.getProductPhrase().equals(originalSearchPhrase+"es")) {
			if(unparsedDetails.getOriginalPhrase().contains("(click recipe)"))
				return null;
			
			foundCategory= IngredientCategory.categories.get(MetadataConstants.rawCategoryName);
		}
		return foundCategory;
	}


	//	public IngredientParsed parseIngredient(String text,String originalSearchPhrase) {
	//		IngredientParsed retValue=new IngredientParsed();
	//		IngredientCategoriationData categorisationArguments=new IngredientCategoriationData(text);
	//		retValue.setOriginalPhrase(text);
	//		//		retValue.setProductPhrase(text);
	//		NotPreciseQuantity retQuantity=null;
	//		IngredientCategory retCategory=null;//CategoryHierarchy.getSingletonCategoryRoot();
	//		String productPhrase=null;
	//		if(text==null||text.equals("")){
	//			return retValue;
	//		}
	//
	//		text=text.trim();
	//
	//		String ommissionLess=text;
	//		for(String ommision:ommissions) {
	//			ommissionLess=ommissionLess.replaceAll(" "+ommision, "");
	//			ommissionLess=ommissionLess.replaceAll(ommision+" ", "");
	//			ommissionLess=ommissionLess.replaceAll(ommision, "");
	//
	//		}
	//
	//
	//		Pattern pattern = Pattern.compile(bracketsRegex);
	//		Matcher matcher = pattern.matcher(ommissionLess);
	//		boolean matches = matcher.matches();
	//
	//		if(matches) {
	//
	//			String brackets=matcher.group(2);
	//
	//			System.out.println(brackets);
	//
	//			String inBrackets = brackets.substring(1,brackets.length()-1);
	//
	//			IngredientParsed extractedQuantity = extractQuantity(inBrackets,originalSearchPhrase);
	//
	//			if(extractedQuantity!=null&&extractedQuantity.getQuantity()!=null&&extractedQuantity.getQuantity().isValid()) {
	//				retQuantity= extractedQuantity.getQuantity();
	//			}
	//			productPhrase=extractedQuantity.getProductPhrase();
	//
	//		}else {
	//			IngredientParsed extractedQuantity = extractQuantity(text,originalSearchPhrase);
	//
	//			if(extractedQuantity!=null&&extractedQuantity.getQuantity()!=null&&extractedQuantity.getQuantity().isValid()) {
	//				retQuantity= extractedQuantity.getQuantity();
	//			}
	//
	//			if(extractedQuantity!=null&&extractedQuantity.getCategory()!=null&&(retCategory==null||retCategory.checkIfEmpty())) {
	//				retCategory=extractedQuantity.getCategory();
	//			}
	//			
	//			productPhrase=extractedQuantity.getProductPhrase();
	//		}
	//
	//		if(retCategory==null||retCategory.checkIfEmpty()) {
	//			retCategory=calculateCategory(categorisationArguments);
	//		}
	//		
	//		retValue.setCategory(retCategory);
	//		retValue.setQuantity(retQuantity);
	//		return retValue ;
	//	}
	//
	//






	private IngredientCategory calculateCategory(IngredientUnparsedApiDetails data) {
		if(data==null)return null;

		IngredientCategory resultCategory = IngredientCategoryHierarchy.getSingletonCategoryRoot().assignCategoryFromTree(data);
		return resultCategory;
	}





	public static IngredientParsed extractQuantity(String text,String phrase){
		PreciseQuantity retValue=null;
		//		text=correctText(text);
		if(text==null||text.equals("")){
			IngredientParsed err= new IngredientParsed();
			return err;
		}

		text=text.trim();

		String ommissionLess=text;
		for(String ommision:ommissions) {
			ommissionLess=ommissionLess.replaceAll(" "+ommision, "");
			ommissionLess=ommissionLess.replaceAll(ommision+" ", "");
			ommissionLess=ommissionLess.replaceAll(ommision, "");

		}

		String[] elems = splitOverHyphenOrMax(ommissionLess);

		IngredientParsed resultsOfQExtracting = tryGettingAmountFromTwoElements(elems,phrase);
		//retValue=resultsOfQExtracting.getQuantity();


		return resultsOfQExtracting;


	}











	private static String[] splitOverHyphenOrMax(String bracketless) {
		String[] elems=null;
		String minRangeRemoved=getMaxFromRangeIfPossible(bracketless);

		if(Pattern.matches("\\d*-\\w*", minRangeRemoved)) {
			elems=minRangeRemoved.split("-");
		}else {
			elems=minRangeRemoved.split(" ");
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











	private static IngredientParsed tryGettingAmountFromTwoElements(String[] elems, String phrase) {
		if(elems==null||elems.length<2)
			return null;

		IngredientParsed retValue=new IngredientParsed();

		String rest="";

		for(int i=2;i<elems.length;i++) {
			rest+=elems[i]+" ";
		}
		String probablyQuantityTypePhrase=elems[1];
		float parsedFloat = attemptParsingFloat(elems);
		PreciseQuantity pq=new PreciseQuantity(-1,null);
		pq.setAmount(parsedFloat);
		for(AmountTypes at:AmountTypes.values()){
			if(at.getType().equals(probablyQuantityTypePhrase)){
				pq.setType(at);
			}
		}

		if(pq.getType()==null){
			QuantityTranslation quantityTranslation = tryGettingTranslationToActualQuantityType(elems[1]);
			if(quantityTranslation==null)
			{
				if(elems[1].equals(phrase)||elems[1].equals(phrase+"s")||elems[1].equals(phrase+"es")
						||(elems[1]+"s").equals(phrase)||(elems[1]+"es").equals(phrase+"es")) {
					pq.setType(AmountTypes.szt);
					pq.setAmount(parsedFloat);
					retValue.setCategory(new IngredientCategory("raw"));
				}else {

					ProblemLogger.logProblem("Nieznana miara "+elems[1]);
					pq= createInvalidPreciseQuantity();
				}
			}else{
				pq.setType(quantityTranslation.getTargetAmountType());
				pq.setAmount(pq.getAmount()*quantityTranslation.getMultiplier());
			}
		}
		//TODO poprawic

		retValue.setProductPhrase(rest);
		retValue.setQuantity(pq);


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
		}else if(probablyFloat.contains("/")) {
			String[] split = probablyFloat.split("/");
			if(split.length>=2) {
				retValue=Float.parseFloat(split[0])/Float.parseFloat(split[1]);
			}

		}else if(!probablyFloat.isEmpty()) {
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






	private static QuantityTranslation tryGettingTranslationToActualQuantityType(String probablyType) {
		QuantityTranslation quantityTranslation = translations.get(probablyType.toLowerCase());
		if(quantityTranslation==null) {
			String quant=probablyType.toLowerCase();
			if(quant.endsWith(".")) {
				quant=quant.substring(0,quant.length()-1);
			}

			quantityTranslation = translations.get(quant);

		}

		return quantityTranslation;
	}










	//	private static PreciseQuantity extractRecursively(String text) {
	//		if(text==null||text.isEmpty()||text.indexOf("x")<0)
	//			return createInvalidPreciseQuantity();
	//
	//		PreciseQuantity extractedQuantity = extractQuantity(text);
	//		extractedQuantity.setAmount(extractedQuantity.getAmount());
	//		return extractedQuantity;
	//	}








}
