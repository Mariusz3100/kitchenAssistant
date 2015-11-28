package webscrappers.Jsoup;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Pattern;

import mariusz.ambroziak.kassistant.utils.ProblemLogger;

import org.junit.BeforeClass;
import org.junit.Test;

import webscrappers.Jsoup.auchan.AuchanGroup;
import webscrappers.Jsoup.auchan.GA_ProduktScrapped;

public class AuchanGroupTest {
	static ArrayList<GA_ProduktScrapped> searchFor;


	
	@BeforeClass
	public static void setUp() {
		searchFor = AuchanGroup.searchFor("mro¿one owoce");
	}
	
	@Test
	public void test() {
		URLConnection connection;
		InputStream detResponse = null;

		for(GA_ProduktScrapped p:searchFor){
			try {
				connection = new URL(p.getUrl()).openConnection();
				connection.setRequestProperty("Accept-Charset", java.nio.charset.StandardCharsets.UTF_8.toString());
				
				detResponse = connection.getInputStream();
				
			} catch (IOException e) {
				fail("There was a problem with accessing url '"+p.getUrl());
				
				//e.printStackTrace();
			}
		}
		
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(detResponse,java.nio.charset.StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			fail(e1.getMessage());
			e1.printStackTrace();		}
		BufferedReader detBR=new BufferedReader(inputStreamReader);
		
		String respLine=null;
//			detResponse.
		StringBuilder response=new StringBuilder();
		try {
			while((respLine=detBR.readLine())!=null){
				response.append(respLine);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
			e.printStackTrace();
			
		}
		
		
		
	}
}
