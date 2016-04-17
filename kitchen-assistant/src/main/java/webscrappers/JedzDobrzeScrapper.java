package webscrappers;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import mariusz.ambroziak.kassistant.QuantityExtractor.JedzDobrzeExtractor;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.dao.Health_Relevant_IngredientDAOImpl;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.utils.PreciseQuantity;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;

public class JedzDobrzeScrapper extends AbstractScrapper {
	private static final String BASE_URI = "http://www.jedzdobrze.pl";
	public static final String urlTemplate="http://www.jedzdobrze.pl/tabele/szukaj/?primary_id=0&secondary_id=0&producer_id=0&seller_id=0&phrase=__search_phrase__";
	
	public static HashMap<Nutrient,PreciseQuantity> scrapSkladnik(String fraza){
		HashMap<Nutrient,PreciseQuantity> retValue=new HashMap<Nutrient,PreciseQuantity>();
		
		List<Nutrient> relevantIngredientsList = DaoProvider.getInstance().getHealthRelevantIngredientsDao().list();
		String url=getFoodIngredientUrl(fraza);
		
		if(url!=null&&url.length()>0){
		try {
			String page=getPage(url);
			
			Document doc = Jsoup.parse(page);
			
			
			Elements table = doc.select("#content > table > tbody");
			
			
			if(table!=null&&table.size()>0){
				Elements rows = table.select("tr");
				
				if(rows!=null){
					for(Element row:rows){
						Elements tds = row.select("td");
						if(tds!=null&&tds.size()>1){
							for(Nutrient hri:relevantIngredientsList){
								if(hri.getName().toLowerCase().equals(tds.get(0).text().toLowerCase())){
									String quantity=tds.get(1).text();
									
									PreciseQuantity quan=
											JedzDobrzeExtractor.extractQuantity(quantity);
									
									retValue.put(hri, quan);
								}
							}
							
							
						}
						
					}
				}
				
			}
			
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		}
		
		return  retValue;
	}

	private static String getFoodIngredientUrl(String fraza) {
		String convertedFraza = null;
		try {
			convertedFraza = URLEncoder.encode(fraza.trim(),StringHolder.ENCODING);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String compareFraza=fraza.replaceAll(",","");
		String finalUrl=urlTemplate.replaceAll("__search_phrase__", convertedFraza);
		String page=null;
		String retUrl=null;
		try {
			page=getPage(finalUrl);
			
			Document doc = Jsoup.parse(page);
			doc.setBaseUri(BASE_URI);
			
			Elements tbody = doc.select("#content > table > tbody");
			Elements thead = doc.select("#content > table > thead");
			
			
			if(checkifHeadIsOk(thead)&&tbody!=null&&tbody.size()>0){
				Elements rows = tbody.select("tr");
				
				for(Element e:rows){
					Elements tds = e.select("td");
					
					if(tds!=null&&tds.size()>4){
//						if(tds.get(2).text()==null||tds.get(2).text().equals(" ")trim().equals("")||tds.get(2).text().trim().equals("&nbsp;"))
						{
							Element firstElement = tds.get(0);
							
							String tableText=firstElement.text();
							boolean found=true;
							for(String phraseElement:compareFraza.split(" ")){
								if(!tableText.contains(phraseElement)){
									found=false;
								}
							}
							
							if(found){
								Elements links = tds.get(0).select("a");
								
								if(links!=null&&links.size()>0){
									String link=links.get(0).absUrl("href");
									if(retUrl==null||link.length()<retUrl.length()){
										retUrl=link;
									}
								}
								
							}
							
							
						}
					}else{
						ProblemLogger.logProblem("Za ma³o kolumn wewn¹trz tabeli na jedzdobrze?!");
					}
					
				}
				
			}
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			ProblemLogger.logProblem("Malformed url: "+finalUrl);
			e.printStackTrace();
		}
		
		return retUrl;
	}
	
	public static boolean checkifHeadIsOk(Elements thead) {
		if(thead!=null&&thead.size()>0){
			Elements rows = thead.select("tr");

			if(rows!=null&&rows.size()>0){
				Element element = rows.get(0);

				Elements tds = element.select("td");

				if(tds!=null&&tds.size()>1){
					if(tds.size()<5){
						ProblemLogger.logProblem("za ma³o kolumn na jedzdobrze");
						return false;
					}else{
						if("Nazwa".equals(tds.text())
							||"Info".equals(tds.text())
							||"Producent / Dystrubutor".equals(tds.text())
							||"Energia w 100g.".equals(tds.text())
							||"Indeks Dietmana".equals(tds.text())){
								ProblemLogger.logProblem("Kolumny maj¹ nieprawid³owe podpisy");
								return false;
						}
							
					}

				}

			}else{
				ProblemLogger.logProblem("za ma³o wierszy nag³ówka...? na jedzdobrze");
				return false;
			}

		}
		return true;
		
	}

	public static void main(String[] args){
		scrapSkladnik("banan");
	}
	
}
