package webscrappers.auchan;

import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
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
import mariusz.ambroziak.kassistant.model.utils.QuantityProdukt;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.SkladnikiFinder;

public class AuchanRecipeParser extends AuchanParticular {
	public static final String singleIngredientPattern = "([^()]*?, )|(.*?\\(.*?\\).*?, )";
	public static final String compoundSkladnikPattern = "(.*?\\(.*?\\).*?,)";
	public static final String simpleSkladnikPattern = "([^()]*?,)";

	public static final String percentCheckPatternSimple =".*[\\d,.]*%"; 
	public static final String percentFindPatternSimple =" [\\d,.]*%"; 


//	public static final String percentCheckPatternCompound =".*[\\d,.]*%[ ]?\\(.*\\)"; 
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
										ProblemLogger.logProblem("Nie uda�o si� sparsowa� ilo�ci: "+second.ownText());
									else{
										if(!(extractQuantity.getType().equals(AmountTypes.mg)||extractQuantity.getType().equals(AmountTypes.kalorie))){
											ProblemLogger.logProblem("Ilo�c inna ni� gramy lub kcal: "+second.ownText());
										}else{
											biq=new BasicIngredientQuantity(type_name.getBase(),extractQuantity.getAmount(),extractQuantity.getType());										
										}
									}
								}

							}
							if(biq==null)
								ProblemLogger.logProblem("Nie znana warto�� spo�ywcza: "+first.ownText());
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

					if("WARTO�CI OD�YWCZE".equalsIgnoreCase(first.text())){
						if("W 100 G".equalsIgnoreCase(second.text())
								&&"Na 100 G".equalsIgnoreCase(second.text()))
							ProblemLogger.logProblem("Inna jednostka odniesienia ni� 100 g: "+second.text());
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
					
					parseSklad(sklad,produkt.getAmount());
				}
				
			}
			
			
			
			
			return null;
		}
	}


	public static ArrayList<ProduktIngredientQuantity> parseSklad(String sklad, AbstractQuantity upperQuan) {
		
		
		
//		ArrayList<CompoundIngredientQuantity> compoundIngredients=new ArrayList<CompoundIngredientQuantity>();
//		ArrayList<BasicIngredientQuantity> basicIngredients=new ArrayList<BasicIngredientQuantity>();
		
		ArrayList<ProduktIngredientQuantity> retValue=new ArrayList<ProduktIngredientQuantity>();
		
		
		Pattern p=Pattern.compile(singleIngredientPattern);
		Pattern.matches(singleIngredientPattern, sklad);

		Matcher m=p.matcher(sklad);
		
		
		while(m.find()){
			System.out.println(m.group());
			
			String skladnik=m.group();
			
			if(Pattern.matches(compoundSkladnikPattern, skladnik)){
				getSimpleSkladnik(skladnik,upperQuan);
			}else if(Pattern.matches(compoundSkladnikPattern, skladnik)){
				CompoundIngredientQuantity ciq=getCompoundSkladnik(skladnik, upperQuan);

			}
			
			
			if(skladnik!=null){
				
			}
			
		}
		
		
		
		return retValue;
		
	}


	private static BasicIngredientQuantity getSimpleSkladnik(String skladnik, AbstractQuantity upperLevelquan) {
		AbstractQuantity newQuan=null;

		extractQuantity(skladnik, upperLevelquan);
		
		
		
		Basic_Ingredient znalezionySkladnik = SkladnikiFinder.findIngredient(skladnik);
		
		BasicIngredientQuantity biq=new BasicIngredientQuantity(znalezionySkladnik, amount, amountType);
		return biq;		
	}


	public static void extractQuantity(String skladnik,
			AbstractQuantity upperLevelquan) {
		AbstractQuantity newQuan;
		if(Pattern.matches(percentCheckPatternSimple, skladnik)){
			Pattern p=Pattern.compile(percentFindPatternSimple);
			
			Matcher m=p.matcher(skladnik);

			if(m.find()){
				String procent=m.group().trim();
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
			if(upperLevelquan instanceof PreciseQuantity){
				PreciseQuantity pq=(PreciseQuantity)upperLevelquan;
				
			}else if(upperLevelquan instanceof NotPreciseQuantity){
				NotPreciseQuantity npq=(NotPreciseQuantity)upperLevelquan;
				
			}
			
		}
	}


	public static CompoundIngredientQuantity getCompoundSkladnik(String skladnik,AbstractQuantity upperLevelquan) {

		if(skladnik==null){
			return null;
		}else{
			skladnik=skladnik.trim();
			if(skladnik.charAt(skladnik.length()-1)==','){
				skladnik=skladnik.substring(0,skladnik.length()-1).trim();
			}
			
			String skladnikNameAndPercent=null, skladnikSklad=null;
			
			if(skladnik.indexOf("(")<=0){
				ProblemLogger.logProblem("Nie naleziono dwa nawiasy w sk�adniku z�o�onym?!: "+skladnik);
			}else{
				String tempSkladnik=skladnik.substring(skladnik.indexOf("(")+1);
				if(tempSkladnik.indexOf("(")>0){
					ProblemLogger.logProblem("Znaleziono dwa nawiasy w sk�adniku: "+skladnik);
				}else{
					skladnikNameAndPercent=skladnik.substring(0,skladnik.indexOf("("));
					skladnikSklad=skladnik.substring(skladnik.indexOf("("));
					
					AbstractQuantity newQuan=null;

					if(Pattern.matches(percentCheckPatternSimple, skladnikNameAndPercent)){
						Pattern p=Pattern.compile(percentFindPatternSimple);
						
						Matcher m=p.matcher(skladnikNameAndPercent);
			
						if(m.find()){
							System.out.println(m.group());
							
							String procent=m.group().trim();
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
							skladnikNameAndPercent=skladnikNameAndPercent.replaceAll(procent, "");
							
						}
					}else{
						if(upperLevelquan instanceof PreciseQuantity){
							PreciseQuantity pq=(PreciseQuantity)upperLevelquan;
							
						}else if(upperLevelquan instanceof NotPreciseQuantity){
							NotPreciseQuantity npq=(NotPreciseQuantity)upperLevelquan;
							
						}
						
					}
					
					parseSklad(skladnikSklad, newQuan);
					
				}
				
			
			
			
			
			
			}
			
			
			
			
			

			
			
			
			
			
		}
		
		return null;
	}

	
	
	public static void main(String[] args){
		String sklad="p�atki OWSIANE 20,2%, p�atki PSZENNE 0,20%, p�atki �YTNIE 20%, rodzynki 12%, owoce kandyzowane 12% (papaja, ananas ��ty), p�atki kokosowe 8% (kokos, cukier), chipsy bananowe 8% (banany, t�uszcz ro�linny, cukier, mi�d, aromat),";
		parseSklad(sklad,new PreciseQuantity(1000,AmountTypes.mg));
	
//		NumberFormat nf = new DecimalFormat ("990.0");
//		 Number d = null;
//		try {
//			d = nf.parse ("1,2");
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 
//		 System.out.println(d);
	
	}
}
