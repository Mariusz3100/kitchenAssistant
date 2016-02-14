package webscrappers.auchan;

import java.net.MalformedURLException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import mariusz.ambroziak.kassistant.QuantityExtractor.AuchanQExtract;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.model.utils.ProduktIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.NutritionalValueQuantity;
import mariusz.ambroziak.kassistant.model.utils.NutritionalValueType;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithIngredients;
import mariusz.ambroziak.kassistant.model.utils.QuantityProdukt;
import mariusz.ambroziak.kassistant.utils.AmountTypes;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

public class AuchanRecipeParser extends AuchanParticular {

	public static ProduktWithIngredients getBasics(String url) throws Page404Exception{
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



		ArrayList<NutritionalValueQuantity> basicsFor100g=retrieveBasics(doc);




		return new ProduktWithIngredients(produkt, basicsFor100g);
	}


	private static ArrayList<NutritionalValueQuantity> retrieveBasics(Document doc) {
		ArrayList<NutritionalValueQuantity> retvalue=new ArrayList<NutritionalValueQuantity>();

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

						NutritionalValueType[] types=NutritionalValueType.values();
						NutritionalValueQuantity nvq=null;

						String amount= second.ownText();

						if(amount!=null){
							if(amount.toLowerCase().contains("kcal")||amount.toLowerCase().contains("kalori"))
							{
								String number=amount.split(" ")[0];

								try{
									float kalorie=Float.parseFloat(number);

									nvq=new NutritionalValueQuantity(NutritionalValueType.Energia,kalorie);
								}catch(NumberFormatException e){
									ProblemLogger.logProblem("Nie uda³o siê sparsowaæ iloœci kalorii: "+amount);
								}
							}else{

								for(NutritionalValueType type:types){
									if(first.text()!=null&&first.text().toLowerCase().contains(type.getName().toLowerCase()))
									{
										QuantityProdukt extractQuantity = AuchanQExtract.extractQuantity(amount);

										if(extractQuantity==null||extractQuantity.getAmount()<0)
											ProblemLogger.logProblem("Nie uda³o siê sparsowaæ iloœci: "+second.ownText());
										else{
											if(!extractQuantity.getAmountType().equals(AmountTypes.mg)){
												ProblemLogger.logProblem("Iloœc inna ni¿ gramy lub kcal: "+second.ownText());
											}else{
												nvq=new NutritionalValueQuantity(type,extractQuantity.getAmount());										
											}
										}
									}

								}

							}	

							if(nvq==null||nvq.getAmount()<0)
								ProblemLogger.logProblem("Nie znana wartoœæ spo¿ywcza: "+first.ownText());
							else
								retvalue.add(nvq);
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


	public static ProduktWithIngredients getAllIngredients(String url) throws Page404Exception{
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



		ArrayList<ProduktIngredientQuantity> allIng=retrieveAllIngredients(doc);

		ArrayList<NutritionalValueQuantity> basicsFor100g=retrieveBasics(doc);



		return new ProduktWithIngredients(produkt, basicsFor100g,);
	}


	private static ArrayList<ProduktIngredientQuantity> retrieveAllIngredients(
			Document doc) {
		// TODO Auto-generated method stub
		return null;
	}

}
