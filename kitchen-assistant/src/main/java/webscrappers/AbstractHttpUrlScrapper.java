package webscrappers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.utils.StringHolder;
public class AbstractHttpUrlScrapper {

	public static void main(String[] args) {
		System.out.println(new AbstractHttpUrlScrapper().getPage("https://przepisy.pl/przepis/crespelle-czyli-zapiekane-nalesniki-z-gorgonzola-i-cukinia"));
	}
	
	
	public String getPage(String url) {
		String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
		//String param1 = "value1";
		//String param2 = "value2";
		StringBuilder page=new StringBuilder();

		boolean successfull=false;
		URLConnection connection;
		InputStream response = null;

		try {

			connection = new URL(url).openConnection();
			connection.setRequestProperty("Accept-Charset", charset);
			response = connection.getInputStream();

			BufferedReader detBR=new BufferedReader(new InputStreamReader(response, charset));
				
			String respLine=null;
//			detResponse.
		
			while((respLine=detBR.readLine())!=null){
				page.append(respLine);
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally{
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		
		return page.toString();

	}
}
