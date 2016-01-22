package testing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.Recipe;
import mariusz.ambroziak.kassistant.model.Recipe_Ingredient;
import mariusz.ambroziak.kassistant.model.jsp.SearchResult;
import mariusz.ambroziak.kassistant.model.utils.QuantityProdukt;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import webscrappers.przepisy.PrzepisyPLQExtract;
import webscrappers.przepisy.SkladnikiExtractor;

public class QuantityTesting {
	
	public static List<String> testQuantity(String url){
			
			
			
			ArrayList<String> retValue=new ArrayList<String>();
//			StringBuilder outPage=new StringBuilder();
			try{
				URLConnection connection = new URL(url).openConnection();//connection.getRequestProperties()
				connection.setRequestProperty("Accept-Charset", java.nio.charset.StandardCharsets.UTF_8.toString());
				InputStream detResponse = connection.getInputStream();
				InputStreamReader inputStreamReader = new InputStreamReader(detResponse,java.nio.charset.StandardCharsets.UTF_8.toString());
				BufferedReader detBR=new BufferedReader(inputStreamReader);
				String respLine=null;
				String html="";
				while((respLine=detBR.readLine())!=null){
					html+=respLine;
				}

				Document doc = Jsoup.parse(html);


				Elements ings=doc.select("[itemprop=\"ingredients\"]");


				for(Element e:ings){
//					ArrayList<Produkt> znalezioneProdukty=new ArrayList<Produkt>()
					
					String ingredient = e.text();
					String quantity= extractQuantity(e);
					
					QuantityProdukt results = SkladnikiExtractor.extract(ingredient, quantity);
//					if(ingredient.indexOf('(')>0&&ingredient.indexOf(')')>0){
//						String attemptedQ=
//								ingredient.substring(ingredient.indexOf('(')+1,ingredient.indexOf(')'));
//							
//						try{
//							quantityRetrieved=retrieveQuantity(attemptedQ);
//						}catch(IllegalArgumentException ex){
//							quantityRetrieved=null;
//						}
//						
//						ingredient=ingredient.replaceAll(attemptedQ, "")
//								.replaceAll("\\(", "")
//								.replaceAll("\\)", "").trim();
//						
//					}
//					
//					if(quantityRetrieved==null){
//						String quantity=extractQuantity(e);
//						quantityRetrieved = retrieveQuantity(quantity);
//					}
//						
					
					 
					 
					retValue.add(ingredient+":"+quantity+" -> "+
							results.getProduktPhrase()+":"+results.getAmount()+":"+results.getAmountType()+"<BR><BR>");
					


				}



			}catch( IOException e){
				e.printStackTrace();
			}



			return retValue;
		}
	private static QuantityProdukt retrieveQuantity(String quantity) {
		return PrzepisyPLQExtract.extractQuantity(quantity);
		
		
		
	}
	
	
	private static String extractQuantity(Element e) {
		
		String quantity=e.parent().select(".quantity").text();
		
		
		if(quantity==null||quantity.equals(""))
			quantity=e.parent().parent().select(".quantity").text();
		return quantity;
	}
	
}
