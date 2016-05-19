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
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;

public class JedzDobrzeScrapper extends AbstractScrapper {
	private static final String BASE_URI = "http://www.jedzdobrze.pl";
	public static final String urlTemplate="http://www.jedzdobrze.pl/tabele/szukaj/?primary_id=0&secondary_id=0&producer_id=0&seller_id=0&phrase=__search_phrase__";

	public static HashMap<Nutrient,PreciseQuantity> scrapSkladnik(String fraza){
		HashMap<Nutrient,PreciseQuantity> retValue=new HashMap<Nutrient,PreciseQuantity>();

		List<Nutrient> relevantIngredientsList = DaoProvider.getInstance().getNutrientDao().list();
		String url=getFoodIngredientUrl(fraza);

		if(url!=null&&url.length()>0){
			try {
				String page=getPage(url);

				Document doc = Jsoup.parse(page);

				Elements header= doc.select("#content > table > thead");
				if(checkIf_ParticularProduktPage_TableHeadIsOk(header)){

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
											
											if(AmountTypes.szt.equals(quan.getType())&&quan.getAmount()==-1){
												ProblemLogger.logProblem(
														"Nie uda³o siê sparsowaæ iloœci sk³adnika odzywczego "+hri.getName());				
											}else if(!AmountTypes.mg.equals(quan.getType())&&
													!AmountTypes.kalorie.equals(quan.getType()))
											{
												ProblemLogger.logProblem(
													"Próbowano zapisaæ typ iloœci inny ni¿ mg lub kcal dla sk³adnika "+url);
											}else{
												retValue.put(hri, quan);
											}
										}
									}
								}
							}
						}
					}
				}
			} catch (MalformedURLException e) {
				ProblemLogger.logProblem("Malformed url: "+url);
				ProblemLogger.logStackTrace(e.getStackTrace());
			} catch (Page404Exception e) {
				ProblemLogger.logProblem("PageNotFound url: "+url);
				ProblemLogger.logStackTrace(e.getStackTrace());
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


			if(checkif_GroupPage_TableHeadIsOk(thead)&&tbody!=null&&tbody.size()>0){
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
			ProblemLogger.logProblem("Malformed url: "+finalUrl);
			ProblemLogger.logStackTrace(e.getStackTrace());
		} catch (Page404Exception e) {
			ProblemLogger.logProblem("PageNotFound url: "+finalUrl);
			ProblemLogger.logStackTrace(e.getStackTrace());
		}

		return retUrl;
	}

	public static boolean checkIf_ParticularProduktPage_TableHeadIsOk(Elements thead) {
		if(thead!=null&&thead.size()>0){
			Elements rows = thead.select("tr");

			if(rows!=null&&rows.size()>0){
				Element element = rows.get(0);

				Elements tds = element.select("td");

				if(tds!=null&&tds.size()>1){
					if(tds.size()<2){
						ProblemLogger.logProblem("za ma³o kolumn na jedzdobrze, strona konkretnego sk³adnika");
						return false;
					}else{
						if(!"Nazwa".equals(tds.get(0).text()))
						{
							ProblemLogger.logProblem("Pierwsza kolumna ma nieprawid³owy podpis na jedzdobrze, strona konkretnego sk³adnika");
							return false;
						}else{
							if(!"W 100 g.".equals(tds.get(1).text()))
							{
								ProblemLogger.logProblem("Druga kolumna ma nieprawid³owy podpis na jedzdobrze, strona konkretnego sk³adnika");
								return false;
							}else{
								return true;
							}
						}
					}
				}else{
					ProblemLogger.logProblem("Brak komórek nag³ówka...? na jedzdobrze, strona konkretnego sk³adnika");
					return false;
				}
			}else{
				ProblemLogger.logProblem("za ma³o wierszy nag³ówka...? na jedzdobrze, strona konkretnego sk³adnika");
				return false;
			}
		}else{
			ProblemLogger.logProblem("Brak nag³ówka...? na jedzdobrze, strona konkretnego sk³adnika");
			return false;
		}
	}

	public static boolean checkif_GroupPage_TableHeadIsOk(Elements thead) {
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
						if("Nazwa".equals(tds.get(0).text())
								||"Info".equals(tds.get(1).text())
								||"Producent / Dystrubutor".equals(tds.get(2).text())
								||"Energia w 100g.".equals(tds.get(3).text())
								||"Indeks Dietmana".equals(tds.get(4).text())){
							
							return true;
						}else{
							ProblemLogger.logProblem("Kolumny maj¹ nieprawid³owe podpisy");
							return false;
							
						}
					}
				}else{
					ProblemLogger.logProblem("Brak komórek nag³ówka...? na jedzdobrze");
					return false;
				}
			}else{
				ProblemLogger.logProblem("za ma³o wierszy nag³ówka...? na jedzdobrze");
				return false;
			}
		}else{
			ProblemLogger.logProblem("Brak nag³ówka...? na jedzdobrze");
			return false;
		}
	}

	public static void main(String[] args){
		scrapSkladnik("banan");
	}

}
