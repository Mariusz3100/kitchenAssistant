package webscrappers.przepisy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.model.utils.AbstractQuantity;
import webscrappers.AbstractScrapper;


public class PrzepisyPlWebscrapper extends AbstractScrapper {
	private String url;
	private Element doc;


	public static PrzepisyPlWebscrapper parse(String url) throws MalformedURLException, Page404Exception{
		PrzepisyPlWebscrapper retValue=new PrzepisyPlWebscrapper(url);
		retValue.parsePageContent();
		return retValue;
	}
	
	private PrzepisyPlWebscrapper(String url){
		this.url=url;
	}

	public void parsePageContent() throws MalformedURLException, Page404Exception{
		String content=getPage(url);
		doc = Jsoup.parse(content);
	}

	public Elements extractIngredients(){
		Elements ings=doc.select(".row-ingredient");
						

		return ings;
	}
	
	public Element getMainElement(){
		return doc;
	}

}
