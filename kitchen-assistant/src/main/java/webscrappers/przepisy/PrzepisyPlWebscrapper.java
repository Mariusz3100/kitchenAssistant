package webscrappers.przepisy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class PrzepisyPlWebscrapper {

	private Element doc;


	public PrzepisyPlWebscrapper(String url) throws IOException{
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

		doc = Jsoup.parse(html);


	}


	public Elements extractIngredients(){
		Elements ings=doc.select("[itemprop=\"recipeIngredient\"]");

		return ings;
	}
	
	public Element getMainElement(){
		return doc;
	}

}
