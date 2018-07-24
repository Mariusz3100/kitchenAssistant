package webscrappers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import mariusz.ambroziak.kassistant.dao.Base_WordDAOImpl;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.dao.Variant_WordDAOImpl;
import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Variant_Word;
import mariusz.ambroziak.kassistant.utils.Converter;
import mariusz.ambroziak.kassistant.utils.StringHolder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class SJPWebScrapper {
	static String baseUrl="http://sjp.pwn.pl/szukaj/_word_.html";
	
	public static String scrapWord(String word){
		String url = null;
		try {
			url = baseUrl.replaceAll("_word_", URLEncoder.encode(word, java.nio.charset.StandardCharsets.UTF_8.toString()));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String retValue=null;
		URLConnection connection;
		try {
			connection = new URL(url).openConnection();
			connection.setRequestProperty("Accept-Charset", java.nio.charset.StandardCharsets.UTF_8.toString());
			InputStream detResponse = connection.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(detResponse,java.nio.charset.StandardCharsets.UTF_8.toString());
			BufferedReader detBR=new BufferedReader(inputStreamReader);
			
			String respLine=null;
//			detResponse.
			String response="";
			while((respLine=detBR.readLine())!=null){
				response+=respLine;
			}
			
			if(response.indexOf("Nie znaleziono ---adnych wynik---w wyszukiwania")>-1)
			{
				retValue="";
			}else{
				Document doc = Jsoup.parse(response);
				
				Element e=doc.select(".anchor-title")
						.select("[href*=http://sjp.pwn.pl/so/]")
						.first();
				if(e==null)
					retValue=Base_WordDAOImpl.niepoprawneSlowoBazowe;
				else
					retValue=e.attr("title");
			}
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//connection.getRequestProperties()
		
		
		return retValue;

	}
	
	
	public static  String getAndSaveNewRelation(String x) {
		String retValue=SJPWebScrapper.scrapWord(Converter.getOnlyLetters(x));
		retValue=Converter.getOnlyLetters(retValue);
		if(retValue==null)retValue="";
		
		DaoProvider.getInstance().getVariantWordDao().addRelation(x.toLowerCase(),retValue.toLowerCase());
		
		return retValue;
	}
	
	public static String retrieveFromDbOrScrapAndSaveInDb(String variantWord){
		if(variantWord==null||variantWord.equals("")){
			return "";
		}else{
			return retrieveFromDbOrScrapAndSaveNotEmptyArgument(variantWord);	
		}
		
	}


	private static String retrieveFromDbOrScrapAndSaveNotEmptyArgument(String variantWord) {
		String base_word = tryGettingFromDb(variantWord);
		if(base_word!=null){
			return base_word;
		}
		else
			return getAndSaveNewRelation(variantWord.toLowerCase());
	}


	private static String tryGettingFromDb(String variantWord) {
		Base_Word base_Name = DaoProvider.getInstance().getVariantWordDao().getBase_Name(variantWord.toLowerCase());

		if(base_Name==null)
			return null;
		
		return processNotNullPossiblyInvalidBaseWord(base_Name);
	}


	private static String processNotNullPossiblyInvalidBaseWord(Base_Word base_Name) {
		if(!base_Name.getB_word().equals("")&&
				!Base_WordDAOImpl.niepoprawneSlowoBazowe.equals(base_Name.getB_word()))
			return base_Name.getB_word();
		else
			return "";
	}
	
	
	
	public static void main(String[] args){
		scrapWord("---y---eczki");
	}
}
