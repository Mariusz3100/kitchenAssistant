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
	public static final String pattern="http://www.auchandirect.pl/sklep/artykuly/wyszukiwarka/[0-9]+/";

	FileInputInterface input;
	
	private AuchanWebScrapper() {
		super();
		baseUrl="https://api.import.io/store/data/320a8ee3-a82a-4ed7-b349-f9e7f2eac1de/_query";
		detailsBaseUrl="https://api.import.io/store/data/3f572a71-b7ee-48b9-b947-2c0d6f6e43b6/_query";
		apiKey="04ffe01c-7080-4817-8856-dac23896d915%3AOcR"+
				"fx0W9As5LXwlU6bNA%2FkjhmtfdHgzR5or1c4xrry1rs"+
				"VPxU7ByGI46fw04%2BGMpIe5hTXFTu0mKQ74mvy%2BHUA%3D%3D";
		
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
	protected String cutDownProduktUrl(String url) {
		
		
		
		return cutDownProduktUrlAuchan(url);
	}

	public static String cutDownProduktUrlAuchan(String url) {
		
//		boolean z = Pattern.matches(b,a);
	    Pattern p = Pattern.compile(pattern);

	    
	    
		Matcher m=p.matcher(url);
		
		if(m.matches()){
			m.find();
			
			String shortUrl=m.group();
			return shortUrl;
	    }
		return null;
	}
	
}
