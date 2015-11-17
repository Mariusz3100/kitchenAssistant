package webscrappers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.InterfaceAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;



import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.Document;

import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.utils.Converter;
import mariusz.ambroziak.kassistant.utils.FileInputInterface;
import mariusz.ambroziak.kassistant.utils.SystemEnv;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

//import words.Converter;








import com.codesnippets4all.json.parsers.JsonParserFactory;

//import database.holders.DBOpenshiftInterface;
//import database.holders.DatabaseInterface;
//import database.holders.DbLocalInterface;
//import database.holders.StringHolder;
//import database.holders.SystemEnv;

public class AuchanWebScrapper extends ImportIoWebScrapper {
	private String auchanSearchUrl="http://www.auchandirect.pl/sklep/wyszukiwarka/__search__";
	String filename="\\classes\\auchanEntries.txt";
	public static final String workingUrlPattern="http://www.auchandirect.pl/sklep/artykuly/wyszukiwarka/[0-9]+/[a-zA-Z_0-9-]+";
	public static final String shortestPattern="http://www.auchandirect.pl/sklep/artykuly/wyszukiwarka/[0-9]+/";

	FileInputInterface input;
	
	private AuchanWebScrapper() {
		super();
//		baseUrl="https://api.import.io/store/data/320a8ee3-a82a-4ed7-b349-f9e7f2eac1de/_query";
		baseUrl="https://api.import.io/store/data/929a8342-f326-422b-a31b-53552799b6fa/_query";
		
		detailsBaseUrl="https://api.import.io/store/data/929a8342-f326-422b-a31b-53552799b6fa/_query";
//		apiKey="04ffe01c-7080-4817-8856-dac23896d915%3AOcR"+
//				"fx0W9As5LXwlU6bNA%2FkjhmtfdHgzR5or1c4xrry1rs"+
//				"VPxU7ByGI46fw04%2BGMpIe5hTXFTu0mKQ74mvy%2BHUA%3D%3D";

		
		groupApiKey="04ffe01c708048178856dac23896d91539c45fc745bd02ce"
				+ "4b5f0954e9b340fe48e19ad7dd1e0cd1e68af5738c6ba"
				+ "f2d6bb153f153b072188e3a7f0d38f8632921ee614d71"
				+ "53bb498a43be26bf2f8750";
		
		detailedApiKey="04ffe01c-7080-4817-8856-dac23896d915%3"+
				"AOcRfx0W9As5LXwlU6bNA%2FkjhmtfdHgzR5or1c4xrry1rsVPxU7B"+
				"yGI46fw04%2BGMpIe5hTXFTu0mKQ74mvy%2BHUA%3D%3D";
		if(SystemEnv.isRemoteHost()){
			filename="/entries/auchanEntriesCounted.txt";
		}
		else{
			filename="\\entries\\auchanEntriesCounted.txt";
		}
		
		input=new FileInputInterface(filename);
		
	}


	private static AuchanWebScrapper singleton;
    public static AuchanWebScrapper getAuchanWebScrapper(){
    	if(singleton==null){
    		singleton=new AuchanWebScrapper();
    	}
    	
    	return singleton;
    }
    
//	public String lookForInShop(String lookFor) throws UnsupportedEncodingException,
//	IOException, MalformedURLException {
//		String url=auchanSearchUrl.replaceAll("__search__", Converter.auchanConvertion(lookFor));
//		
//		return lookForInShop(url, lookFor);
//	}
	
	public  ArrayList<Produkt> lookup(String lookFor) throws UnsupportedEncodingException,
	IOException, MalformedURLException {
		String search4=Converter.auchanConvertion(lookFor);
//		String tempSearch4=search4.replaceAll("\\\\$","\\\\\\\\$");
		
	//	Pattern.quote(lookFor);
		String search4withSpaces=search4.replaceAll("\\$", " ");
				
		
		String urlWithSpaces=auchanSearchUrl.replaceAll("__search__",search4withSpaces);
		
		String finalUrl=urlWithSpaces.replaceAll(" ","\\$");

		
		return lookForInShop(finalUrl, lookFor);
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
		if(details.has("opis"))
			return details.getString("opis");
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

	    arrrrgh
	    
		Matcher m=p.matcher(url);
		
		if(m.matches()){
			m.find();
			
			String shortUrl=m.group();
			return shortUrl;
	    }
		return null;
	}
	
}
