package webscrappers.auchan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mariusz.ambroziak.kassistant.dao.Base_WordDAOImpl;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.utils.Converter;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.SystemEnv;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.dao.DataIntegrityViolationException;


public class AuchanGroup extends AuchanAbstractScrapper{
	//private static final String auchanSearchUrl="http://www.auchandirect.pl/sklep/wyszukiwarka/__search__";
	
	private static final String auchanSearchUrl="http://www.auchandirect.pl/auchan-warszawa/pl/search?text=__search__%3Arelevance%3Aadult%3Afalse";
	String filename="\\classes\\auchanEntries.txt";
	public static final String noResults="Przykro nam ale nie znaleziono wyników dla podanego zapytania"; 
	public static final String validLinkPart="/auchan-warszawa/pl";
	
	
	
	
	public static ArrayList<GA_ProduktScrapped> searchFor(String searchPhrase){
		ArrayList<GA_ProduktScrapped> retValue=new ArrayList<GA_ProduktScrapped>();
		
//		String search4=searchPhrase.trim().replaceAll(" ", "+");
		
//		String search4withSpaces=search4.replaceAll("\\$", " ");
				
		
//		String urlWithSpaces=auchanSearchUrl.replaceAll("__search__",search4withSpaces);
		
		if(searchPhrase==null||searchPhrase.equals(""))
			return retValue;
		
		
		String searchForConverted = Converter.auchanConvertion(searchPhrase.trim());
		String finalUrl=auchanSearchUrl.replace("__search__", 
				searchForConverted);
				
				
//				urlWithSpaces.replaceAll(" ","\\$");
		
		try {
			String response = getPage(finalUrl);
			
			if(response.indexOf(noResults)>-1)
			{
				//no results, return empty list?
			}else{
				Document doc = Jsoup.parse(response);
				
				
				
				doc.setBaseUri(baseURL);
				
				Elements produkts=doc.select(".product");
						
				for(Element produkt: produkts){
					Elements links = produkt.select(".about").get(0).getElementsByTag("a");
					
					for(Element link: links){
						if(link.attr("href").indexOf(validLinkPart)>-1){
							String l=link.absUrl("href");
							String nazwa=link.text();
							GA_ProduktScrapped gap=new GA_ProduktScrapped(nazwa, l);

							retValue.add(gap);
							
								
						}
						
					}
					
				}
				
				
				
				
				

			}
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return retValue;

	}

	
	
//	private static void addProduktsToScrap(GA_ProduktScrapped produkt){
//		if(produktsToScrap==null)
//			produktsToScrap=new ArrayList<GA_ProduktScrapped>();
//			
//			produktsToScrap.add(produkt);
//			
//	}
//	private static Produkt getAndSaveDetailsIfNew(String detailsUrl) {
//		Produkt existingDetails = checkForExistingDetails(detailsUrl);
//		if(existingDetails!=null)
//			return existingDetails;
//		else
//			return getProduktDetails(detailsUrl);
//		
//	}
	


//	protected static Produkt checkForExistingDetails(String detailsURL) {
//		
//		String shortUrl=AuchanAbstractScrapper.getAuchanShortestWorkingUrl(detailsURL);
//		
//		
//		Produkt produktByURL = DaoProvider.getInstance().getProduktDao().getProduktsByURL(shortUrl);
//
//
//		return produktByURL;
//		
//	}
	
	
//	protected static Produkt getpossibleProdukt(String detailsURL)
//	{
//		String shortUrl=getAuchanShortestWorkingUrl(detailsURL);
//		Produkt retValue=null;
//		
//		ProduktDetails produktDetails = null;
//		try {
//			produktDetails = AuchanParticular.getProduktDetails(shortUrl);
//		} catch (Page404Exception e) {
//			
//			ProblemLogger.logProblem("For url "+detailsURL+"->"+shortUrl
//					+" no produkt was found");		
//			}
//		
//		if(produktDetails!=null){
//		
//		retValue=new Produkt();
//		
//		if(produktDetails!=null){
//			retValue.setCena(produktDetails.getCena());
//			retValue.setNazwa(produktDetails.getNazwa());
//			retValue.setOpis(produktDetails.getOpis());
//			retValue.setPrzetworzony(false);
//			retValue.setUrl(shortUrl);
//
//			//DaoProvider.getInstance().getProduktDao().addProdukt(retValue);
//
//		}
//		
//	
//	}
//	return retValue;
//
//}
//	
//	
//	protected static Produkt getOrParseProduktDetails(String detailsURL)
//	{
//		String shortUrl=getAuchanShortestWorkingUrl(detailsURL);
//		Produkt retValue=null;
//		
//		ProduktDetails produktDetails = null;
//		try {
//			produktDetails = AuchanParticular.getProduktDetails(shortUrl);
//		} catch (Page404Exception e) {
//			
//			ProblemLogger.logProblem("For url "+detailsURL+"->"+shortUrl
//					+" no produkt was found");		
//			}
//		
//		if(produktDetails!=null){
//		
//		retValue=new Produkt();
//		
//		if(produktDetails!=null){
//			retValue.setCena(produktDetails.getCena());
//			retValue.setNazwa(produktDetails.getNazwa());
//			retValue.setOpis(produktDetails.getOpis());
//			retValue.setPrzetworzony(false);
//			retValue.setUrl(shortUrl);
//
//			//DaoProvider.getInstance().getProduktDao().addProdukt(retValue);
//
//		}
//		
//	
//	}
//	return retValue;
//
//}
/*	
	public static ArrayList<String> searchForProdukts(String searchForPhrase) 	{
			String search4=Converter.auchanConvertion(searchForPhrase);
//			String tempSearch4=search4.replaceAll("\\\\$","\\\\\\\\$");
			
		//	Pattern.quote(lookFor);
			String search4withSpaces=search4.replaceAll("\\$", " ");
					
			
			String urlWithSpaces=auchanSearchUrl.replaceAll("__search__",search4withSpaces);
			
			String finalUrl=urlWithSpaces.replaceAll(" ","\\$");

			
			return lookForInShop(finalUrl, searchForPhrase);
		}
	
	
	
	
//	
//	public static  String getAndSaveNewRelation(String x) {
//		String retValue=SJPWebScrapper.scrapWord(Converter.getOnlyLetters(x));
//		retValue=Converter.getOnlyLetters(retValue);
//		if(retValue==null)retValue="";
//		
//		DaoProvider.getInstance().getVariantWordDao().addRelation(x.toLowerCase(),retValue.toLowerCase());
//		
//		return retValue;
//	}
//	

	
	
	public static void main(String[] args){
		ArrayList<GA_ProduktScrapped> searchFor = searchFor("mro¿one owoce");
		
		for(GA_ProduktScrapped p:searchFor){
			String a=getAuchanShortestWorkingUrl(p.getUrl());
			
			String b=getAuchanShortestWorkingUrl(p.getUrl());
			
			System.out.println(a+" "+b);
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public String getProductsURL() {
		
		return input.getLine();
	}

	public float extractCena(JSONObject details) {
		
		if(details.has("cena_mala")&&details.has("cena_duza")){
			int cenaMala=details.getInt("cena_mala");
			int cenaDuza=details.getInt("cena_duza");
		
			
			float cena = cenaDuza+cenaMala/100f;
			return cena;
		}
		else return -1l;
		
	}

	public String extractOpis(JSONObject details) {
		if(details.has("opis")){
			String opis = details.getString("opis");
			opis=opis.replaceFirst("Opis produktu", "");

			
			return opis;
		}
		else return "";
	}

	public String extractSklad(JSONObject details) {
		if(details.has("sklad"))
			return details.getString("sklad");
		else return "";
	}

	public String extractNazwa(JSONObject details) {
		if(details.has("nazwa"))
			return details.getString("nazwa");
		else return "";
	}
	
	public String extractWartosciOdzywcze(JSONObject details) {
		if(details.has("wartosci_odzywcze"))
			return details.getString("wartosci_odzywcze");
		else return "";
	}

	@Override
	protected String getUrlPattern(String url) {
		
		
		
		return getAuchanUrlPattern(url);
	}

	@Override
	protected String getShortestWorkingUrl(String url) {
		
		
		
		return getAuchanShortestWorkingUrl(url);
	}

		
	
	public static String getAuchanUrlPattern(String url) {
		
//		boolean z = Pattern.matches(b,a);
		
		String tempUrl=url.replaceAll("_", "");
	    Pattern p = Pattern.compile(shortestPattern);

	    
	    
		Matcher m=p.matcher(url);
		
		if(m.matches()){
			m.find();
			
			String shortUrl=m.group();
			return shortUrl+"%";
	    }
		return null;
	}
	
	public static String getAuchanShortestWorkingUrl(String url) {
		
//		boolean z = Pattern.matches(b,a);
	    Pattern p = Pattern.compile(workingUrlPattern);

//	    arrrrgh????
	    
		Matcher m=p.matcher(url);
		
		if(m.matches()){
			m.find();
			
			String shortUrl=m.group();
			return shortUrl;
	    }
		return null;
	}
	
	public static URLConnection getSearchForConnection(String originalUrl)
			throws UnsupportedEncodingException, 
			MalformedURLException {
		URLConnection connection = null;
		String query=baseUrl+"?"+urlParamName+"="+URLEncoder.encode(originalUrl,java.nio.charset.StandardCharsets.UTF_8.toString())+
				"&"+userParamName+"="+auchanUser+"&"+apiKeyParamName+"="+groupApiKey;
		

			connection = waitTillConnOpen( query);

		return connection;
	}
	
	
	private Produkt getAndSaveDetailsIfNew(String detailsUrl) {
		Produkt existingDetails = checkForExistingDetails(detailsUrl);
		if(existingDetails!=null)
			return existingDetails;
		else
			return getAndSaveDetails(detailsUrl);
		
	}

	private static String processNutrValuesQuery(String nutrVals) {
		if(nutrVals==null||nutrVals.equals(""))
			return null;
			
		String htmlTalbe="<table>"+nutrVals+"</table>";
		
		Document doc = Jsoup.parse(htmlTalbe);
		
		for (Element table : doc.select("table")) {
            for (Element row : table.select("tr")) {
                Elements tds = row.select("td");
                System.out.println(tds.get(0).text()); 
                 
                
                
                
            }
        }
		
		
		
		return null;
	}

	public static ArrayList<String> lookForInShop(String url,String lookFor) throws UnsupportedEncodingException,
	IOException, MalformedURLException {

		ArrayList<Produkt> retValue=new ArrayList<Produkt>();

		
		String page=getPage(url);
		
		Document doc = Jsoup.parse(page);
		
		
		
		
		
		URLConnection connection = getSearchForConnection(url);
		String jsonRespons = getResponseText(connection);
		
		JSONObject jsonResponseObject = new JSONObject(jsonRespons);
		
		if(!jsonResponseObject.has("results"))
		{
			System.out.println("Problem");
		}
		
		JSONArray jsonResults=jsonResponseObject.getJSONArray("results");
		
		
		for(int i=0;i<jsonResults.length()&&SystemEnv.AgentsOn;i++){
			JSONObject res=jsonResults.getJSONObject(i);
//			String productDesc=res.getString("prodboxtext_link/_text");
			

			String detailsUrl=getShortestWorkingUrl(res.getString("link"));
			String nazwa=res.getString("nazwa");
			
	//		if(!checkForExistingDetails(detailsUrl))	
				rememberDetailsToBeSaved(detailsUrl);
			
			boolean found=true;
			for(String lookForWord:lookFor.split(" ")){

				if(!nazwa.toLowerCase().contains(lookForWord.toLowerCase()))
					found=false;
			}
			
			if(found){
				retValue.add(getAndSaveDetailsIfNew(detailsUrl));
				
			}

	
		}
		
		return retValue;
		
	}
	protected static String getResponseText(URLConnection connection)
			throws IOException, UnsupportedEncodingException {
		InputStream response = connection.getInputStream();
		BufferedReader innerBR=new BufferedReader(new InputStreamReader(response,java.nio.charset.StandardCharsets.UTF_8.toString()));
		String innerLine=null;
		
		String jsonRespons="";
		while((innerLine=innerBR.readLine())!=null){
			jsonRespons+=innerLine;
		}
		return jsonRespons;
	}
	*/
}
