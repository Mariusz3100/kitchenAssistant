package webscrappers.auchan;

import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import mariusz.ambroziak.kassistant.QuantityExtractor.AmountTypes;
import mariusz.ambroziak.kassistant.QuantityExtractor.AuchanQExtract;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient_Name;
import mariusz.ambroziak.kassistant.model.utils.AbstractQuantity;
import mariusz.ambroziak.kassistant.model.utils.BasicIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.CompoundIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.NotPreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithBasicIngredients;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithAllIngredients;
import mariusz.ambroziak.kassistant.model.utils.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.QuantityPhraseClone;
import mariusz.ambroziak.kassistant.model.utils.QuantityProdukt;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.SkladnikiFinder;

public class AuchanRecipeParser extends AuchanParticular {
	private static final float absoluteMinimal = 0.0001f;
	public static final String singleIngredientPattern = "([^()]*?, )|(.*?\\(.*?\\).*?, )";
	public static final String compoundSkladnikPattern = "(.*?\\(.*?\\).*?)";
	public static final String simpleSkladnikPattern = "([^()]*?)";

	public static final String percentCheckPatternSimple =".*?[\\d,.]*%.*"; 
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


	public static void checkifHeadIsOk(Elements thead) {
		if(thead!=null&&thead.size()>0){
			Elements rows = thead.select("th");

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
					
					ArrayList<ProduktIngredientQuantity> parseSkladniki = parseSklad(produkt.getAmount(), sklad);
					
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


	public static ArrayList<ProduktIngredientQuantity> parseSklad(AbstractQuantity quan, String sklad) {
		LinkedHashMap<String, QuantityPhraseClone> relativeAmountsFromSklad = getRelativeAmountsFromSklad(sklad, quan);
		
		ArrayList<ProduktIngredientQuantity> skladnikiSparsowane = parseSkladniki(relativeAmountsFromSklad,quan);
		
		return skladnikiSparsowane;
	}

	public static LinkedHashMap<String,QuantityPhraseClone> getRelativeAmountsFromSklad(String sklad,AbstractQuantity upperLevelquan){
		if(sklad==null)
			return null;
		
		sklad=sklad.trim();
		
		if(sklad.endsWith("."))
			sklad=sklad.substring(0, sklad.length()-1);
		
		if(!sklad.endsWith(", "))
			sklad=sklad+=", ";
		ArrayList<QuantityPhraseClone> orderedListOfQuans=new ArrayList<QuantityPhraseClone>();

		LinkedHashMap<String,QuantityPhraseClone> mapaSkladnikow=new LinkedHashMap<String, QuantityPhraseClone>();
		
		
		Pattern p=Pattern.compile(singleIngredientPattern);
		Pattern.matches(singleIngredientPattern, sklad);

		Matcher m=p.matcher(sklad);
		
		
		while(m.find()){
			System.out.println(m.group());
			
			String skladnik=m.group().trim();
			
			if(skladnik.endsWith(","))
				skladnik=skladnik.substring(0, skladnik.length()-1);
			
			
			QuantityPhraseClone quanExtracted = extractQuantityAndPhraseIfPercent(skladnik, upperLevelquan);


			orderedListOfQuans.add(quanExtracted);
			mapaSkladnikow.put(skladnik, quanExtracted);

		}

		float maxAmount=-1;

//		if(upperLevelquan instanceof PreciseQuantity)
//			maxAmount=((PreciseQuantity) upperLevelquan).getAmount();
//		else
			if(upperLevelquan instanceof NotPreciseQuantity)
			maxAmount=((NotPreciseQuantity) upperLevelquan).getMaximalAmount();
		//maxAmount to iloœæ ca³ego produktu/sk³adnika z³o¿onego podana do metody
		//odejmujemy po kolei minimalne iloœci kolejnych sk³adników
		
		float currentminimalAmountSkladnik=0f;
		for(int i=orderedListOfQuans.size()-1;i>0;i--){
			QuantityPhraseClone quantityPhraseClone = orderedListOfQuans.get(i);
			AbstractQuantity iterateQuan = quantityPhraseClone.getQuan();
			if(iterateQuan!=null)
			{
//				if(iterateQuan instanceof PreciseQuantity)
//				{
//					currentminimalAmountSkladnik=((PreciseQuantity) iterateQuan).getAmount();
//				}else
					if(iterateQuan instanceof NotPreciseQuantity)
				{
					currentminimalAmountSkladnik=((NotPreciseQuantity) iterateQuan).getMinimalAmount();
				}
			}
			
			maxAmount-=currentminimalAmountSkladnik;
			
		}
		
		
		
		
		ArrayList<QuantityPhraseClone> unknownMinimal=new ArrayList<QuantityPhraseClone>();

		
//		if(upperLevelquan instanceof PreciseQuantity)
//		{
//			float currentMax=maxAmount;
//			
//			for(QuantityPhraseClone qpc:orderedListOfQuans){
//				AbstractQuantity quan = qpc.getQuan();
//				if(quan==null){
//					NotPreciseQuantity newQuan=new NotPreciseQuantity();
//					qpc.setQuan(newQuan);
//					newQuan.setType(upperLevelquan.getType());
//					
//					newQuan.setMaximalAmount(currentMax);
//	
//					unknownMinimal.add(qpc);
//				}else{
//					if(quan instanceof PreciseQuantity){
//						currentMax=((PreciseQuantity) quan).getAmount();
//						
//						for(QuantityPhraseClone qpc1:unknownMinimal){
//							((NotPreciseQuantity)(qpc1.getQuan())).setMinimalAmount(
//									((PreciseQuantity) quan).getAmount());
//			
//						}
//						unknownMinimal.clear();
//						
//					}
//				}
//				
//				
//			}
//		}else 
			if(upperLevelquan instanceof NotPreciseQuantity){
			
			float currentMax=(1-absoluteMinimal)*maxAmount;
			
			
			
			for(int i=0;i<orderedListOfQuans.size();i++){
				QuantityPhraseClone qpc=orderedListOfQuans.get(i);
				
				float emptyListMax=((NotPreciseQuantity)upperLevelquan).getMaximalAmount()/(float)(i+1);
				
				if(emptyListMax<currentMax)
					currentMax=emptyListMax;
				
				AbstractQuantity quan = qpc.getQuan();
				if(quan==null){
					NotPreciseQuantity newQuan=new NotPreciseQuantity();
					qpc.setQuan(newQuan);
					newQuan.setType(upperLevelquan.getType());
					
					
					newQuan.setMaximalAmount(currentMax);
	
					unknownMinimal.add(qpc);
				}else{
					if(quan instanceof PreciseQuantity){
						currentMax=((PreciseQuantity) quan).getAmount()*((NotPreciseQuantity)upperLevelquan).getMaximalAmount();
						
						for(QuantityPhraseClone qpc1:unknownMinimal){
							((NotPreciseQuantity)(qpc1.getQuan())).setMinimalAmount(
									((PreciseQuantity) quan).getAmount()*((NotPreciseQuantity)upperLevelquan).getMinimalAmount());
			
						}
						unknownMinimal.clear();
						
					}
				}
				
				
			}
			
			
		}
		
		if(unknownMinimal.size()>0){
			float rest=((NotPreciseQuantity)upperLevelquan).getMinimalAmount();
			
			
			for(int i=0;i<orderedListOfQuans.size();i++){
				rest-=((NotPreciseQuantity)orderedListOfQuans.get(i).getQuan()).getMinimalAmount();
				
			}

			QuantityPhraseClone firstElement = unknownMinimal.get(0);

			((NotPreciseQuantity)firstElement.getQuan()).setMinimalAmount((rest/(float)unknownMinimal.size()));
			unknownMinimal.remove(firstElement);
			
			for(QuantityPhraseClone qpc1:unknownMinimal){
				if(upperLevelquan instanceof NotPreciseQuantity){
					((NotPreciseQuantity)(qpc1.getQuan())).setMinimalAmount((float)Math.floor(absoluteMinimal*((NotPreciseQuantity)upperLevelquan).getMinimalAmount()));

				}
			}
			unknownMinimal.clear();
		}
			
			
			
		
		
		
		
		return mapaSkladnikow;
	}
	
	
	public static ArrayList<ProduktIngredientQuantity> parseSkladniki(
			LinkedHashMap<String, QuantityPhraseClone> relativeAmountsFromSklad, AbstractQuantity upperQuan) {
		
		if(relativeAmountsFromSklad==null)
			return null;
		
		

		ArrayList<ProduktIngredientQuantity> retValue=new ArrayList<ProduktIngredientQuantity>();
		
		
		for(Entry<String, QuantityPhraseClone> e:relativeAmountsFromSklad.entrySet()){
			String skladnik=e.getKey();
			
			if(Pattern.matches(simpleSkladnikPattern, skladnik)){
				String  skladnikBezProcenta=parseSimpleSkladnik(skladnik);
				Basic_Ingredient simpleSkladnik = getSimpleSkladnik(skladnikBezProcenta);
				
				BasicIngredientQuantity biq=new BasicIngredientQuantity(simpleSkladnik, e.getValue().getQuan());
				
				retValue.add(biq);
			}else if(Pattern.matches(compoundSkladnikPattern, skladnik)){
				CompoundIngredientQuantity compoundSkladnik = getCompoundSkladnik(skladnik, e.getValue());
				
				retValue.add(compoundSkladnik);
			}else{
				ProblemLogger.logProblem("Ani prosty, ani skomplikowany sk³adnik??? "+skladnik);
			}
			
		}
		
			
		return retValue;

	}


	private static String parseSimpleSkladnik(String skladnik) {
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


	private static Basic_Ingredient getSimpleSkladnik(String skladnik) {
		
		if(skladnik==null){
			return null;
		}else{
			skladnik=skladnik.trim();
			if(skladnik.charAt(skladnik.length()-1)==','){
				skladnik=skladnik.substring(0,skladnik.length()-1).trim();
			}

			Basic_Ingredient bi=new Basic_Ingredient();
			bi.setName(skladnik);
			return bi;
		}
	}


	public static QuantityPhraseClone extractQuantityAndPhraseIfPercent(String skladnik,
			AbstractQuantity upperLevelquan) {
		if(skladnik==null)
			return null;
		
		AbstractQuantity newQuan = null;
		String nazwaSkladnika=null;
		
		if(Pattern.matches(percentCheckPatternSimple, skladnik)){
			Pattern p=Pattern.compile(percentFindPatternSimple);
			
			Matcher m=p.matcher(skladnik);

			if(m.find()){
				String procent=m.group().trim();
				
				nazwaSkladnika=skladnik.substring(0,skladnik.indexOf(procent)).trim();
				
				if(procent.contains(",")&&!procent.contains("."))
					procent=procent.replaceAll(",", ".");
				
				procent=procent.replaceAll("%", "");
				float fraction=Float.parseFloat(procent)/100f;
				if(upperLevelquan instanceof PreciseQuantity){
					PreciseQuantity pq=(PreciseQuantity)upperLevelquan;
					
					PreciseQuantity newPQ=new PreciseQuantity();
					newPQ.setType(pq.getType());
					newPQ.setAmount(fraction*pq.getAmount());
					newQuan=newPQ;
				}else if(upperLevelquan instanceof NotPreciseQuantity){
					NotPreciseQuantity npq=(NotPreciseQuantity)upperLevelquan;
					
					NotPreciseQuantity newNpq=new NotPreciseQuantity();
					newNpq.setType(upperLevelquan.getType());
					newNpq.setMinimalAmount(npq.getMinimalAmount()*fraction);
					newNpq.setMaximalAmount(npq.getMaximalAmount()*fraction);
					newQuan=newNpq;
				}
			}
		}else{
///			if(upperLevelquan instanceof PreciseQuantity){
			String tempSkladnik=skladnik.trim();	
			
			Pattern p=Pattern.compile(findInnerSkladPattern);
			
			Matcher m=p.matcher(tempSkladnik);

			if(m.find()){
				String sklad=m.group();
				nazwaSkladnika=tempSkladnik.replaceAll(Pattern.quote(sklad), "").trim();
				
			}else{
				nazwaSkladnika=tempSkladnik;
			}
			newQuan=null;
//			}else if(upperLevelquan instanceof NotPreciseQuantity){
//				newQuan=null;
//				skladnikWithoutPercent=skladnik;
//			}
			
		}
		
		
		QuantityPhraseClone retValue=new QuantityPhraseClone(nazwaSkladnika,newQuan);
		
		return retValue;
		
	}


	public static CompoundIngredientQuantity getCompoundSkladnik(String skladnik,QuantityPhraseClone quantityPhraseClone) {

		if(skladnik==null){
			return null;
		}else{
			skladnik=skladnik.trim();
			if(skladnik.charAt(skladnik.length()-1)==','){
				skladnik=skladnik.substring(0,skladnik.length()-1).trim();
			}
			
			String skladnikSklad=null;
			
			if(skladnik.indexOf("(")<0){
				ProblemLogger.logProblem("Nie naleziono nawiasów w sk³adniku z³o¿onym?!: "+skladnik);
			}else{
				String tempSkladnik=skladnik.substring(skladnik.indexOf("(")+1);
				if(tempSkladnik.indexOf("(")>0){
					ProblemLogger.logProblem("Znaleziono dwa nawiasy w sk³adniku: "+skladnik);
				}else{
					skladnikSklad=skladnik.substring(skladnik.indexOf("(")+1).trim();
					
					skladnikSklad=skladnikSklad.substring(0,skladnikSklad.lastIndexOf(")"));
					
					ArrayList<ProduktIngredientQuantity> parsedSklad = parseSklad(quantityPhraseClone.getQuan(), skladnikSklad);
					
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

	
	
	public static void main2(String[] args){

		String sklad="rodzynki, owoce kandyzowane 12% (papaja, ananas ¿ó³ty), p³atki kokosowe (kokos, cukier), chipsy bananowe 8% (banany, t³uszcz roœlinny, cukier, miód, aromat),";
		
		LinkedHashMap<String, QuantityPhraseClone> relativeAmountsFromSklad = getRelativeAmountsFromSklad(sklad,new PreciseQuantity(1000,AmountTypes.mg));
		
		System.out.println();
		
		
		ArrayList<ProduktIngredientQuantity> parsedSkladniki = parseSkladniki(relativeAmountsFromSklad,new PreciseQuantity(1000,AmountTypes.mg));
		
		System.out.println();
	
	}
	
	public static void main(String[] args){

		String sklad="m¹ka PSZENNA, sól, dro¿d¿e, margaryna, woda, polepszacz, cukier";
		
		LinkedHashMap<String, QuantityPhraseClone> relativeAmountsFromSklad = getRelativeAmountsFromSklad(sklad,new PreciseQuantity(1000,AmountTypes.mg));
		
		System.out.println();
		
		
		ArrayList<ProduktIngredientQuantity> parsedSkladniki = parseSkladniki(relativeAmountsFromSklad,new PreciseQuantity(1000,AmountTypes.mg));
		
		System.out.println();
	
	}
	
	public static void main1(String[] args){

		String sklad="	masa kakaowa*, cukier kokosowy* 25%, t³uszcz kakaowy*, orzechy laskowe* , kruszone ziarna kakao*,emulgator:lecytyna sojowa*";
		
		LinkedHashMap<String, QuantityPhraseClone> relativeAmountsFromSklad = getRelativeAmountsFromSklad(sklad,new PreciseQuantity(1000,AmountTypes.mg));
		
		System.out.println();
		
		
		ArrayList<ProduktIngredientQuantity> parsedSkladniki = parseSkladniki(relativeAmountsFromSklad,new PreciseQuantity(1000,AmountTypes.mg));
		
		System.out.println();
	
	}
	
	
	
}
