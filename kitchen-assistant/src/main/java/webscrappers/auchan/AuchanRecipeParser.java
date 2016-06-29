package webscrappers.auchan;

import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import mariusz.ambroziak.kassistant.QuantityExtractor.AuchanQExtract;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient_Name;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.NotPreciseQuantity;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.AbstractQuantity;
import mariusz.ambroziak.kassistant.model.utils.BasicIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.CompoundIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithBasicIngredients;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithAllIngredients;
import mariusz.ambroziak.kassistant.model.utils.NotPreciseQuantityWithPhrase;
import mariusz.ambroziak.kassistant.model.utils.PreciseQuantityWithPhrase;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.SkladnikiFinder;

public class AuchanRecipeParser extends AuchanParticular {
	private static final float absoluteMinimalFraction = 0.0001f;
	public static final String singleIngredientPattern ="([^()]*?, )|([^(]*?\\(([^()]*?"+
									"|"+
									"[^()]*?\\([^()]*?\\)[^()]*?)\\).*?, )";
	public static final String compoundSkladnikPattern = "([^(]*?\\(([^()]*?|[^()]*?\\([^()]*?\\)[^()]*?)\\).*?)";
	public static final String simpleSkladnikPattern = "([^()]*?)";

	public static final String percentCheckPatternSimple =".*?[\\d,.]*%.*?"; 
	public static final String percentFindPatternSimple =" [\\d,.]*%"; 
	public static final String findInnerSkladPattern =" \\(.*?\\)"; 


	//	public static final String percentCheckPatternCompound =".*?[\\d,.]*%[ ]?\\(.*\\)"; 
	//	public static final String percentFindPatternCompound =" [\\d,.]*%[ ]?\\(.*\\)"; 



	public static ProduktWithBasicIngredients getBasics(String url) throws Page404Exception{
		String page="";


		try {
			page = getPage(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Document doc = Jsoup.parse(page);

		doc.setBaseUri(baseURL);

		if(checkIf404Page(doc))
			throw new Page404Exception( url);

		ProduktDetails produkt = extractDataExceptUrl(doc);


		produkt.setUrl(url);



		ArrayList<BasicIngredientQuantity> basicsFor100g=retrieveBasics(doc);




		return new ProduktWithBasicIngredients(produkt, basicsFor100g);
	}


	private static ArrayList<BasicIngredientQuantity> retrieveBasics(Document doc) {
		ArrayList<BasicIngredientQuantity> retvalue=new ArrayList<BasicIngredientQuantity>();

		if(doc==null)
			return retvalue;


		Elements table = doc.select(".nutritional-values");

		if(table!=null&& table.size()>0)
		{


			Elements thead = table.get(0).select("thead");

			checkifHeadIsOk(thead);


			Elements tbody = table.get(0).select("tbody");

			if(tbody!=null&&tbody.size()>0){
				Elements rows = tbody.select("tr");

				for(int i=0;rows!=null&&i<rows.size();i++){
					Element element = rows.get(i);
					Elements tds = element.select("td");

					if(tds!=null&&tds.size()>1){
						Element first = tds.get(0);
						Element second= tds.get(1);

						List<Basic_Ingredient_Name> types=DaoProvider.getInstance().getBasicIngredientNameDao().list();
						BasicIngredientQuantity biq=null;

						String amount= second.ownText();

						if(amount!=null&&!Pattern.matches("[0-9]+ (kj|kJ|KJ|Kj)", amount)){

							PreciseQuantity extractQuantity =null;
							for(int j=0;j<types.size()&&extractQuantity==null;j++){
								Basic_Ingredient_Name type_name=types.get(j);
								if("-".equals(first.text())&&i>0
										&&rows.get(i-1).select("td")!=null
										&&rows.get(i-1).select("td").get(0)!=null)
									first=rows.get(i-1).select("td").get(0);

								if(first.text()!=null&&first.text().toLowerCase().contains(type_name.getPossible_name().toLowerCase()))
								{

									extractQuantity = AuchanQExtract.extractQuantity(amount);

									if(extractQuantity==null||extractQuantity.getAmount()<0)
										ProblemLogger.logProblem("Nie uda³o siê sparsowaæ iloœci: "+second.ownText());
									else{
										if(!(extractQuantity.getType().equals(AmountTypes.mg)||extractQuantity.getType().equals(AmountTypes.kalorie))){
											ProblemLogger.logProblem("Iloœc inna ni¿ gramy lub kcal: "+second.ownText());
										}else{
											biq=new BasicIngredientQuantity(type_name.getBase(),extractQuantity.getAmount(),extractQuantity.getType());										
										}
									}
								}

							}
							if(biq==null)
								ProblemLogger.logProblem("Nie znana wartoœæ spo¿ywcza: "+first.ownText());
							else
								retvalue.add(biq);
						}	


					}
				}

			}




		}

		return retvalue;
	}


	private static void checkifHeadIsOk(Elements thead) {
		if(thead!=null&&thead.size()>0){
			Elements rows = thead.select("tr");

			if(rows!=null&&rows.size()>0){
				Element element = rows.get(0);

				Elements tds = element.select("th");

				if(tds!=null&&tds.size()>1){
					Element first = tds.get(0);
					Element second= tds.get(1);

					if("WARTOŒCI OD¯YWCZE".equalsIgnoreCase(first.text())){
						if("W 100 G".equalsIgnoreCase(second.text())
								&&"Na 100 G".equalsIgnoreCase(second.text()))
							ProblemLogger.logProblem("Inna jednostka odniesienia ni¿ 100 g: "+second.text());
					}
				}

			}

		}
	}


	public static ProduktWithAllIngredients getAllIngredients(String url) throws Page404Exception{
		String page="";


		try {
			page = getPage(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Document doc = Jsoup.parse(page);

		doc.setBaseUri(baseURL);

		if(checkIf404Page(doc))
			throw new Page404Exception( url);

		ProduktDetails produkt = extractDataExceptUrl(doc);


		produkt.setUrl(url);



		CompoundIngredientQuantity allIng=retrieveAllIngredients(doc,produkt);

		ArrayList<BasicIngredientQuantity> basicsFor100g=retrieveBasics(doc);

		return new ProduktWithAllIngredients(produkt,allIng,basicsFor100g);

	}


	private static CompoundIngredientQuantity retrieveAllIngredients(Document doc, ProduktDetails produkt) {
		if(doc==null){
			return null;
		}else{
			Elements table = doc.select("#product-desc-1");
			if(table!=null&&table.size()>0){
				Elements paragraphs = table.get(0).select("p");
				if(paragraphs!=null&&paragraphs.size()>0){
					String sklad=paragraphs.get(0).ownText();
					ArrayList<ProduktIngredientQuantity> parseSkladniki = parseSkladString(produkt.getAmount(), sklad);

					ArrayList<BasicIngredientQuantity> prosteSkladniki=new ArrayList<BasicIngredientQuantity>();
					ArrayList<CompoundIngredientQuantity> zlozoneSkladniki=new ArrayList<CompoundIngredientQuantity>();

					for(ProduktIngredientQuantity piq:parseSkladniki){
						if(piq instanceof CompoundIngredientQuantity)
							zlozoneSkladniki.add((CompoundIngredientQuantity) piq);

						if(piq instanceof BasicIngredientQuantity)
							prosteSkladniki.add((BasicIngredientQuantity) piq);

					}
					CompoundIngredientQuantity ciq=new CompoundIngredientQuantity(produkt.getNazwa(),
							produkt.getAmount(),zlozoneSkladniki,prosteSkladniki);
					return ciq;
				}
			}
			return null;
		}
	}


	public static ArrayList<ProduktIngredientQuantity> parseSkladString(NotPreciseQuantity upperLevelQuan, String skladString) {
		if(skladString==null)
			return null;
		SkladReader skladParser=new SkladReader(skladString, upperLevelQuan);
		Collection<NotPreciseQuantityWithPhrase> skladnikiPhrasesWithQuanties = skladParser.parseSkladnikiStringsWithProperlyCalulatedQuantities();
		ArrayList<ProduktIngredientQuantity> skladnikiSparsowane;
		if(skladnikiPhrasesWithQuanties.size()==1){
			skladnikiSparsowane=new ArrayList<ProduktIngredientQuantity>();
			skladnikiSparsowane.add(parseSkladnikiFromPartiallyParsedMapWhenThereIsOnlyOneElement(getOneEntryFrom(skladnikiPhrasesWithQuanties)));
		}else{
			skladnikiSparsowane = parseSkladnikiFromPartiallyParsedMap(skladnikiPhrasesWithQuanties,upperLevelQuan);
		}
		return skladnikiSparsowane;
	}


	private static NotPreciseQuantityWithPhrase getOneEntryFrom(
			Collection<NotPreciseQuantityWithPhrase> skladnikiPhrasesWithQuanties) {
		return skladnikiPhrasesWithQuanties.iterator().next();
	}


	private static ProduktIngredientQuantity parseSkladnikiFromPartiallyParsedMapWhenThereIsOnlyOneElement(
			NotPreciseQuantityWithPhrase quantityWithName) {
		if(quantityWithName==null){
			return null;
		}else{
				ProduktIngredientQuantity retValue=null;
				if(Pattern.matches(simpleSkladnikPattern, quantityWithName.getPhrase())){
					retValue= parseSimpleSkladnik(quantityWithName.getQuan(), quantityWithName.getPhrase());
				}else if(Pattern.matches(compoundSkladnikPattern, quantityWithName.getPhrase())){
					retValue= parseCompoundSkladnik(quantityWithName.getPhrase(), quantityWithName);
				}else{
					ProblemLogger.logProblem("Ani prosty, ani skomplikowany sk³adnik??? "+quantityWithName.getPhrase());
				}
			return retValue;
		}
	}


	private static class SkladReader{
		private String sklad;
		private LinkedHashMap<String,NotPreciseQuantityWithPhrase> mapaSkladnikow;
		private ArrayList<NotPreciseQuantityWithPhrase> orderedListOfQuans;

		private NotPreciseQuantity upperLevelquan;

		public SkladReader(String sklad,NotPreciseQuantity upperLevelquan) {
			super();
			this.sklad = sklad;
			eliminateDotAtEndOfSkladIfPresent();
			addCommaAtEndOfSkladIfNotPresent();
			this.upperLevelquan=upperLevelquan;
			mapaSkladnikow=new LinkedHashMap<String, NotPreciseQuantityWithPhrase>();
			orderedListOfQuans=new ArrayList<NotPreciseQuantityWithPhrase>();
		}



		public Collection<NotPreciseQuantityWithPhrase> parseSkladnikiStringsWithProperlyCalulatedQuantities(){
			fillSkladnikMapAndOrderedQuanListWithoutAnyRelationToEachOther();
			adjustQuantitiesOfIngredientWithRespectToOtherIngredients();
			return mapaSkladnikow.values();
		}



		private void adjustQuantitiesOfIngredientWithRespectToOtherIngredients() {
			if(mapaSkladnikow.size()==1){
				updateSingleUnknownMinimalByCopyingUpperLevelMinimal(mapaSkladnikow.entrySet().iterator().next().getValue());
			}else{
			
				ArrayList<NotPreciseQuantityWithPhrase> unknownMinimal = 
						adjustQuantitiesOfIngredientsWithRespectToEachOtherExceptForLastUnknownMinimls();
				updateLastUnknownMinimals(unknownMinimal);
			}

		}



		private void updateLastUnknownMinimals(ArrayList<NotPreciseQuantityWithPhrase> unknownMinimal) {
			if(!unknownMinimal.isEmpty()){
					float rest = calculateUpperMinimalMinusKnownMinimals();
					removeFirstElementAndUpdateItWithRestOverNumberOfUnknowns(unknownMinimal, rest);
					updateCollectionWithMinimalFractionOfUpperQuan(unknownMinimal);

			}
		}



		private void updateSingleUnknownMinimalByCopyingUpperLevelMinimal(NotPreciseQuantityWithPhrase quantityWithName) {
			quantityWithName.setQuan(upperLevelquan.getClone());
		}



		private float calculateUpperMinimalMinusKnownMinimals() {
			float rest=((NotPreciseQuantity)upperLevelquan).getMinimalAmount();
			for(int i=0;i<orderedListOfQuans.size();i++){
				if(((NotPreciseQuantity)orderedListOfQuans.get(i).getQuan()).isMinimumValid())
				rest-=((NotPreciseQuantity)orderedListOfQuans.get(i).getQuan()).getMinimalAmount();
			}
			return rest;
		}



		private void updateCollectionWithMinimalFractionOfUpperQuan(ArrayList<NotPreciseQuantityWithPhrase> unknownMinimal) {
			for(NotPreciseQuantityWithPhrase qpc1:unknownMinimal){
				if(upperLevelquan instanceof NotPreciseQuantity){
					((NotPreciseQuantity)(qpc1.getQuan())).setMinimalAmount((float)Math.floor(absoluteMinimalFraction*((NotPreciseQuantity)upperLevelquan).getMinimalAmount()));
				}
			}
			unknownMinimal.clear();
		}



		private void removeFirstElementAndUpdateItWithRestOverNumberOfUnknowns(ArrayList<NotPreciseQuantityWithPhrase> unknownMinimal,
				float rest) {
			NotPreciseQuantityWithPhrase firstElement = getFirstNotNullElementIfPossible(unknownMinimal);
			((NotPreciseQuantity)firstElement.getQuan()).setMinimalAmount((rest/(float)unknownMinimal.size()));
			unknownMinimal.remove(firstElement);
		}



		private NotPreciseQuantityWithPhrase getFirstNotNullElementIfPossible(ArrayList<NotPreciseQuantityWithPhrase> unknownMinimal) {
			NotPreciseQuantityWithPhrase firstElement = unknownMinimal.get(0);
			for(int i=1;i<unknownMinimal.size()&&firstElement==null;i++){
				firstElement=unknownMinimal.get(i);
			}
			return firstElement;
		}



		private ArrayList<NotPreciseQuantityWithPhrase> adjustQuantitiesOfIngredientsWithRespectToEachOtherExceptForLastUnknownMinimls() {
			float currentMax = calculateFirstElementMaxUsingConstantsAndUpperQuanAndMinimals();
			return adjustQuantitiesOfIngredientsWithRespectToEachOtherExceptForLastUnknownMinimalsStartingWithCurrentMax(
					currentMax);
		}



		private ArrayList<NotPreciseQuantityWithPhrase> adjustQuantitiesOfIngredientsWithRespectToEachOtherExceptForLastUnknownMinimalsStartingWithCurrentMax(
				float currentMax) {
			ArrayList<NotPreciseQuantityWithPhrase> unknownMinimal=new ArrayList<NotPreciseQuantityWithPhrase>();
			for(int i=0;i<orderedListOfQuans.size();i++){
				NotPreciseQuantityWithPhrase qpc=orderedListOfQuans.get(i);
				currentMax = adjustMaxAmountInCaseCalculatingFromPositionIsPreciser(upperLevelquan, currentMax, i);
				NotPreciseQuantity quan = qpc.getQuan();
				if(quan!=null){
					if(quan.isMaximumValid()){
						currentMax=((PreciseQuantity) quan).getMaximalAmount();
						setPreviousMinimalsFromCurrentMax(currentMax, unknownMinimal);
					}
				}else{
					NotPreciseQuantity unknownMinimalQuan = createQuantityWithUnknownMinimal(upperLevelquan, currentMax);
					qpc.setQuan(unknownMinimalQuan);
					unknownMinimal.add(qpc);
				}		
			}
			return unknownMinimal;
		}



		private float calculateFirstElementMaxUsingConstantsAndUpperQuanAndMinimals() {
			float maxAmount = getMaxAmountMinusNecessaryMinimals(upperLevelquan, orderedListOfQuans);
			float currentMax=getMaxMinusAbsoluteMinimalsForOtherSkladniki()*maxAmount;
			return currentMax;
		}



		private float getMaxMinusAbsoluteMinimalsForOtherSkladniki() {
			if(orderedListOfQuans.isEmpty()){
				ProblemLogger.logProblem("Próba obliczenia iloœci maksymalnej dla pierwszego elementu pustej listy");
				return 0;
			}else{
				return 1-(absoluteMinimalFraction*(orderedListOfQuans.size()-1));
			}
		}



		private void setPreviousMinimalsFromCurrentMax(float currentMax, ArrayList<NotPreciseQuantityWithPhrase> unknownMinimal) {
			for(NotPreciseQuantityWithPhrase qpc1:unknownMinimal){
				((NotPreciseQuantity)(qpc1.getQuan())).setMinimalAmount(currentMax);

			}
			unknownMinimal.clear();
		}



		private void fillSkladnikMapAndOrderedQuanListWithoutAnyRelationToEachOther() {
			Pattern p=Pattern.compile(singleIngredientPattern);
			Matcher m=p.matcher(sklad);
			while(m.find()){
				String skladnik=m.group().trim();
				if(skladnik.endsWith(","))
					skladnik=skladnik.substring(0, skladnik.length()-1);
				NotPreciseQuantityWithPhrase quanExtracted = extractQuantityAndPhraseIfPercent(skladnik, upperLevelquan);
				orderedListOfQuans.add(quanExtracted);
				mapaSkladnikow.put(skladnik, quanExtracted);
			}
			
		}


		private void addCommaAtEndOfSkladIfNotPresent() {
			if(!sklad.endsWith(", "))
				sklad=sklad+=", ";
		}


		private void eliminateDotAtEndOfSkladIfPresent() {
			if(sklad.endsWith("."))
				sklad=sklad.substring(0, sklad.length()-1);
		}
	}




	private static NotPreciseQuantity createQuantityWithUnknownMinimal(AbstractQuantity upperLevelquan,
			float currentMax) {
		NotPreciseQuantity newQuan=new NotPreciseQuantity();

		newQuan.setType(upperLevelquan.getType());
		newQuan.setMaximalAmount(currentMax);
		return newQuan;
	}


	private static float adjustMaxAmountInCaseCalculatingFromPositionIsPreciser(AbstractQuantity upperLevelquan,
			float currentMax, int i) {
		float maxAmountCalclatedFromPositionAlone=((NotPreciseQuantity)upperLevelquan).getMaximalAmount()/(float)(i+1);

		if(maxAmountCalclatedFromPositionAlone<currentMax)
			currentMax=maxAmountCalclatedFromPositionAlone;
		return currentMax;
	}


	private static float getMaxAmountMinusNecessaryMinimals(AbstractQuantity upperLevelquan,
			ArrayList<NotPreciseQuantityWithPhrase> orderedListOfQuans) {
		float maxAmount = extractPossibleMaximumAmount(upperLevelquan);

		float currentminimalAmountSkladnik=0f;
		for(int i=orderedListOfQuans.size()-1;i>0;i--){
			NotPreciseQuantityWithPhrase quantityPhraseClone = orderedListOfQuans.get(i);
			AbstractQuantity iterateQuan = quantityPhraseClone.getQuan();
			if(iterateQuan!=null&&((NotPreciseQuantity) iterateQuan).isMinimumValid())
			{
				currentminimalAmountSkladnik=((NotPreciseQuantity) iterateQuan).getMinimalAmount();
			}

			maxAmount-=currentminimalAmountSkladnik;

		}
		return maxAmount;
	}


	private static float extractPossibleMaximumAmount(AbstractQuantity upperLevelquan) {
		float maxAmount=-1;
		if(upperLevelquan instanceof NotPreciseQuantity)
			maxAmount=((NotPreciseQuantity) upperLevelquan).getMaximalAmount();
		return maxAmount;
	}

	public static ArrayList<ProduktIngredientQuantity> parseSkladnikiFromPartiallyParsedMap(
			Collection<NotPreciseQuantityWithPhrase> skladnikiPhrasesWithQuanties, AbstractQuantity upperLevelQuan) {
		if(skladnikiPhrasesWithQuanties==null||skladnikiPhrasesWithQuanties.isEmpty())
			return null;
		ArrayList<ProduktIngredientQuantity> retValue=new ArrayList<ProduktIngredientQuantity>();
		for(NotPreciseQuantityWithPhrase singleSkladnikWithExtractedQuantity:skladnikiPhrasesWithQuanties){
			if(Pattern.matches(simpleSkladnikPattern, singleSkladnikWithExtractedQuantity.getPhrase())){
				BasicIngredientQuantity biq = parseSimpleSkladnik(singleSkladnikWithExtractedQuantity.getQuan(), singleSkladnikWithExtractedQuantity.getPhrase());
				retValue.add(biq);
			}else if(Pattern.matches(compoundSkladnikPattern, singleSkladnikWithExtractedQuantity.getPhrase())){
				CompoundIngredientQuantity compoundSkladnik = parseCompoundSkladnik(singleSkladnikWithExtractedQuantity.getPhrase(), singleSkladnikWithExtractedQuantity);
				retValue.add(compoundSkladnik);
			}else{
				ProblemLogger.logProblem("Ani prosty, ani skomplikowany sk³adnik??? "+singleSkladnikWithExtractedQuantity.getPhrase());
			}
		}
		return retValue;
	}


	private static BasicIngredientQuantity parseSimpleSkladnik(NotPreciseQuantity skladnikQuan, String skladnik) {
		String  skladnikBezProcenta=removeProcentIfAny(skladnik);
		Basic_Ingredient simpleSkladnik = retrieveFromDbOrCreateNewSimpleSkladnik(skladnikBezProcenta);
		BasicIngredientQuantity biq=new BasicIngredientQuantity(simpleSkladnik, skladnikQuan);
		return biq;
	}


	private static String removeProcentIfAny(String skladnik) {
		String skladnikWithoutPercent=null;

		if(Pattern.matches(percentCheckPatternSimple, skladnik)){
			Pattern p=Pattern.compile(percentFindPatternSimple);
			Matcher m=p.matcher(skladnik);

			if(m.find()){
				String procent=m.group().trim();

				skladnikWithoutPercent=skladnik.replaceAll(procent, "");
			}else{
				ProblemLogger.logProblem("Najpier znaleziono procent, potem ju¿ nie?!: "+skladnik);
			}

		}else{
			skladnikWithoutPercent=skladnik;
		}

		return skladnikWithoutPercent;
	}


	private static Basic_Ingredient retrieveFromDbOrCreateNewSimpleSkladnik(String skladnik) {
		//TODO dodaæ element zapisywania do i wyciagania z bazy danych
		if(skladnik==null){
			return null;
		}else{
			skladnik = trimAndRemoveTrailingCommaIfAny(skladnik);
			Basic_Ingredient bi=new Basic_Ingredient();
			bi.setName(skladnik);
			return bi;
		}
	}


	private static NotPreciseQuantityWithPhrase extractQuantityAndPhraseIfPercent(String skladnik,
			AbstractQuantity upperLevelquan) {
		if(skladnik==null||upperLevelquan==null)
			return null;
		skladnik=skladnik.trim();
		if(Pattern.matches(percentCheckPatternSimple, skladnik)){
			Pattern p=Pattern.compile(percentFindPatternSimple);
			Matcher m=p.matcher(skladnik);
			if(m.find()){
				return extractQuantityWithPhraseFromSkladnikWithPercent(skladnik, upperLevelquan, m);
			}else{
				ProblemLogger.logProblem("Prawdopodobnie coœ nie tak z regexami: najpierw stwierdzono obecnoœæ procentu, potem go nie znaleziono dla "+skladnik);
				return new NotPreciseQuantityWithPhrase(skladnik, null);
			}
		}else{
			return new NotPreciseQuantityWithPhrase(skladnik, null);
		}
		
	}


	private static NotPreciseQuantityWithPhrase extractQuantityWithPhraseFromSkladnikWithPercent(String skladnik,
			AbstractQuantity upperLevelquan, Matcher m) {
		//String nazwaSkladnika;
		String procent=m.group().trim();
		//nazwaSkladnika=skladnik.substring(0,skladnik.indexOf(procent)).trim();
		float fraction = calculateFractionFromProcentString(procent);
		NotPreciseQuantity relativeSkladnik = upperLevelquan.getClone();
		relativeSkladnik.multiplyBy(fraction);
		NotPreciseQuantityWithPhrase retValue=new NotPreciseQuantityWithPhrase(skladnik,relativeSkladnik);
		return retValue;
	}


	private static NotPreciseQuantity getRelativeQuantity(AbstractQuantity upperLevelquan, float fraction) {
		NotPreciseQuantity newQuan;
		PreciseQuantity upperLevelQuanCastToPrecise=(PreciseQuantity)upperLevelquan;
		PreciseQuantity newPQ=upperLevelQuanCastToPrecise.getClone();
		newPQ.multiplyBy(fraction);
		newQuan=newPQ;
		return newQuan;
	}


	private static float calculateFractionFromProcentString(String procent) {
		if(procent.contains(",")&&!procent.contains("."))
			procent=procent.replaceAll(",", ".");
		procent=procent.replaceAll("%", "");
		float fraction=Float.parseFloat(procent)/100f;
		return fraction;
	}

	public static CompoundIngredientQuantity parseCompoundSkladnik(NotPreciseQuantityWithPhrase skladnikPhraseWirhQuantity) {
		return parseCompoundSkladnik(skladnikPhraseWirhQuantity.getPhrase(),skladnikPhraseWirhQuantity);
	}

	public static CompoundIngredientQuantity parseCompoundSkladnik(String skladnik,NotPreciseQuantityWithPhrase quantityPhraseClone) {
		if(skladnik==null){
			return null;
		}else{
			skladnik = trimAndRemoveTrailingCommaIfAny(skladnik);
			String skladnikSklad=null;
			if(!checkAndReportLackOfBracket(skladnik)){
				if(!checkForAndReportTripleQuotes(skladnik)){
					skladnikSklad = cutOutSkladString(skladnik);
					ArrayList<ProduktIngredientQuantity> parsedSklad = parseSkladString(quantityPhraseClone.getQuan(), skladnikSklad);
					ArrayList<BasicIngredientQuantity> prosteSkladniki=new ArrayList<BasicIngredientQuantity>();
					ArrayList<CompoundIngredientQuantity> zlozoneSkladniki=new ArrayList<CompoundIngredientQuantity>();

					for(ProduktIngredientQuantity piq:parsedSklad){
						if(piq instanceof CompoundIngredientQuantity)
							zlozoneSkladniki.add((CompoundIngredientQuantity) piq);

						if(piq instanceof BasicIngredientQuantity)
							prosteSkladniki.add((BasicIngredientQuantity) piq);
					}
					CompoundIngredientQuantity ciq = new CompoundIngredientQuantity(
							quantityPhraseClone.getPhrase(), quantityPhraseClone.getQuan(), zlozoneSkladniki, prosteSkladniki);
					return ciq;
				}
			}
		}
		return null;
	}


	private static String cutOutSkladString(String skladnik) {
		String skladnikSklad;
		skladnikSklad=skladnik.substring(skladnik.indexOf("(")+1).trim();
		skladnikSklad=skladnikSklad.substring(0,skladnikSklad.lastIndexOf(")"));
		return skladnikSklad;
	}


	private static String trimAndRemoveTrailingCommaIfAny(String skladnik) {
		skladnik=skladnik.trim();
		if(skladnik.charAt(skladnik.length()-1)==','){
			skladnik=skladnik.substring(0,skladnik.length()-1).trim();
		}
		return skladnik;
	}


	private static boolean checkAndReportLackOfBracket(String skladnik) {
		if(skladnik.indexOf("(")<0){
			ProblemLogger.logProblem("Nie naleziono nawiasów w sk³adniku z³o¿onym?!: "+skladnik);
			return true;
		}
		return false;
	}


	private static boolean checkForAndReportTripleQuotes(String skladnik) {
		String skladnikWithSpace=skladnik+" ";
		String[] splitOverOpeningBrackets = skladnikWithSpace.split("\\(");
		String[] splitOverClosingBrackets = skladnikWithSpace.split("\\)");
		if(splitOverOpeningBrackets.length!=splitOverClosingBrackets.length){
			ProblemLogger.logProblem("Sk³adnik z ró¿n¹ liczb¹ nawiasów otwieraj¹cych i zamykaj¹cych:"+skladnik);
			return true;
		}else{
			if(splitOverOpeningBrackets.length>3){
				ProblemLogger.logProblem("Sk³adnik z iloœci¹ wiêksz¹ iloœci¹ nawiasów ni¿ 2:"+skladnik);
				return true;
			}
		}
		
		return false;
	}





}
