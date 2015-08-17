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



import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import mariusz.ambroziak.kassistant.agents.ClockAgent;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.utils.SystemEnv;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.codesnippets4all.json.parsers.JSONParser;
import com.codesnippets4all.json.parsers.JsonParserFactory;



public abstract class ImportIoWebScrapper {
	 private static final int ENJOY_YOUR_OWN_WAIT_TIME = 500;
	private static final float ticketsFactor = 0.001f;
//	DatabaseInterface interfac;
	 ArrayList<String> detailsToBeSavedList;
		private float tickets=1;
		private float lastTime=0;

	 
	public ImportIoWebScrapper() {
//		interfac=DatabaseInterface.getDBInterface();
		detailsToBeSavedList=new ArrayList<String>();
		lastTime=ClockAgent.getTimePassed();

	}
	final String insertQuery="insert into product (product_id,  url,nazwa,sklad,opis,brieflyProcessed)values(__id__,'__url__','__nazwa__','__sklad__','__opis__',__briefly_Processed__);";
	final String countQuery="select count(p_id) from product where url='__url__';";

	
	String charset = java.nio.charset.StandardCharsets.UTF_8.name();  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
	String baseUrl="https://api.import.io/store/data/320a8ee3-a82a-4ed7-b349-f9e7f2eac1de/_query";
	String detailsBaseUrl="";

	String urlParamName="input/webpage/url";
//	String auchanURLToProcess=	"http%3A%2F%2Fwww.auchandirect.pl%2Fsklep%2Fartykuly%2F1160_1181_1292%2FProdukty-Swieze%2FCukiernia%2FCiasta-Ciastka";
	
	String userParamName="_user";
	String auchanUser="04ffe01c-7080-4817-8856-dac23896d915";
	
	String apiKeyParamName="_apikey";
	String apiKey="";
//			"04ffe01c-7080-4817-8856-dac23896d915%3AOcR"+
	//		"fx0W9As5LXwlU6bNA%2FkjhmtfdHgzR5or1c4xrry1rs"+
	//		"VPxU7ByGI46fw04%2BGMpIe5hTXFTu0mKQ74mvy%2BHUA%3D%3D";	
	String detailedApiKey="";
	 JsonParserFactory factory=JsonParserFactory.getInstance();


	public void scrap(String pageUrls){
		BufferedReader br=null;
		try {
			br=new BufferedReader(new StringReader(pageUrls));
			
			String url=null;
			
			while((url=br.readLine())!=null){
				
				while(!SystemEnv.AgentsOn)
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
				
				
				getNewUrlsFromFile(url);
				
				
			}
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public abstract String getProductsURL();
	
	public void enjoyYourOwn(){
		updateTickets();
		if(getTickets()>0){
			try {
	
				if(detailsToBeSavedList.isEmpty()){
					getNewUrlsFromFile(getProductsURL());
	
				}else{
					String detailsUrl=detailsToBeSavedList.get(0);
					detailsToBeSavedList.remove(0);
					getAndSaveDetailsIfNew(detailsUrl);
	
				}
	
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			try {
				Thread.sleep(ENJOY_YOUR_OWN_WAIT_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void getNewUrlsFromFile(String url)
			throws UnsupportedEncodingException,
			IOException, MalformedURLException {
		URLConnection connection = null;
		
		updateTickets();
		substractTickets();
		String query=baseUrl+"?"+urlParamName+"="+URLEncoder.encode(url,java.nio.charset.StandardCharsets.UTF_8.toString())+
				"&"+userParamName+"="+auchanUser+"&"+apiKeyParamName+"="+apiKey;
		
		connection = new URL(query).openConnection();
		connection.setRequestProperty("Accept-Charset", charset);
		String jsonRespons = getResponseText(connection);

		JSONArray jsonResults=new JSONObject(jsonRespons).getJSONArray("results");
		
		for(int i=0;i<jsonResults.length()&&SystemEnv.AgentsOn;i++){
			JSONObject res=jsonResults.getJSONObject(i);
	
			String detailsURL=res.getString("prodphoto_link");

			//if(!checkForExistingDetails(detailsURL))	
				rememberDetailsToBeSaved(detailsURL);
			

			
		}
	}

	private void updateTickets() {
		tickets+=(ClockAgent.getTimePassed()-lastTime)*ticketsFactor;
		lastTime=ClockAgent.getTimePassed();
	}
	
	public void substractTickets() {
		--tickets;
		
	}
	
	
	
	public float getTickets() {
		return tickets;
	}

	private void rememberDetailsToBeSaved(String detailsURL) {
		detailsToBeSavedList.add(detailsURL);
		
	}


	protected String getResponseText(URLConnection connection)
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
	
	
	protected boolean checkForExistingDetails(String detailsURL) {
		return 
				DaoProvider.getInstance().getProduktDao().getProduktsByURL(detailsURL).size()
				>0;

	}
	
	
//	protected boolean checkForExistingDetails(String detailsURL) {
//		String query="select count(product_id) from product where url='"+detailsURL+"';";
////		query=query.replaceAll("__url__", detailsURL);
//		
//		ResultSet rs=interfac.runQuery(query);
//		
//		if(rs==null)return false;
//		else
//			{
//			try {
//				rs.next();
//				return rs.getInt("count")>0?true:false;
//
//
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			}
//	
//		return false;
//	}
	
	
	protected void getAndSaveDetails(String detailsURL)
			{

		while(!SystemEnv.AgentsOn)
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		updateTickets();
		substractTickets();
		
		try {
		URLConnection connection;
		String detailsQuery=detailsBaseUrl+"?"+urlParamName+"="+URLEncoder.encode(detailsURL,java.nio.charset.StandardCharsets.UTF_8.toString())+
				"&"+userParamName+"="+auchanUser+"&"+apiKeyParamName+"="+detailedApiKey;
//		System.out.println("new File(\"xxx\")."+new File("xxx").getAbsolutePath());
		
		
			connection = new URL(detailsQuery).openConnection();
		
		connection.setRequestProperty("Accept-Charset", java.nio.charset.StandardCharsets.UTF_8.toString());
		InputStream detResponse = connection.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(detResponse,java.nio.charset.StandardCharsets.UTF_8.toString());
		BufferedReader detBR=new BufferedReader(inputStreamReader);
		String respLine=null;
//		detResponse.
		String jsonDetRespons="";
		while((respLine=detBR.readLine())!=null){
			jsonDetRespons+=respLine;
		}

		
			JSONObject jsonObjecttemp = new JSONObject(jsonDetRespons);
			if(jsonObjecttemp.has("results"))
				{
				JSONArray jsonResults=jsonObjecttemp.getJSONArray("results");
	
				JSONObject details=jsonResults.getJSONObject(0);
				
				
				saveJSONDetails(detailsURL,details);
				
				
				
				
				
//				String query=insertQuery;
//				
//				//String idQuery="SELECT nextval('product_id_seq');";
//	//			int id=0;
//				//ResultSet rs=interfac.runQuery(idQuery);
//	//			if(rs!=null){
//	//				try {
//	//					rs.next();
//	//
//	//					id=rs.getInt("nextval");
//	//					System.out.println("id=rs.getInt(\"nextval\");: "+id);
//	//				} catch (SQLException e) {
//	//					// TODO Auto-generated catch block
//	//					e.printStackTrace();
//	//				}
//	//			}
//	//			System.out.println("idQuery: "+idQuery+" rs "+rs);
//	
//				
//				
//				query=query.replace("__id__", " nextval('product_product_id_seq') ");
//				
//				if(details.has("wartosci_odzywcze")){
//					
//					query=query.replace("__odzywcze__", details.getString("wartosci_odzywcze"));
//					
//				}
//				
//				query=query.replace("__nazwa__", details.getString("nazwa"));
//				query=query.replace("__url__", detailsURL);
//				
//				if(details.has("sklad"))
//					query=query.replace("__sklad__", details.getString("sklad"));
//				else
//					query=query.replace("__sklad__", "");
//				
//				query=query.replace("__opis__", details.getString("opis"));
//				System.out.println("Insert query: "+query);
//				
//				String nutrValsInsertQuery=null;
//				String nutrVals=null;
//				
//				if(details.has("wartosci_odzywcze")){
//				nutrVals=details.getString("wartosci_odzywcze");
//				}
//	//				nutrValsInsertQuery=processNutrValuesQuery(nutrVals);
//	//				if(nutrValsInsertQuery!=null&&!nutrValsInsertQuery.equals("")){
//	//					query=query.replace("__briefly_Processed__", "true");
//	//	
//	//				}else{
//						query=query.replace("__briefly_Processed__", "false");
//	
//	//				}
//				
//				
////				interfac.runNoResultQuery(query);
			}else{
				System.out.println("no results found");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}
	
	
	private void saveJSONDetails(String detailsURL, JSONObject details) {
		
		
//		String url=details.getString("details");
		String nazwa=extractNazwa(details);
		String sklad=extractSklad(details);
		String opis=extractOpis(details);
		long cena = extractCena(details);
		
		Produkt p=new Produkt(detailsURL, nazwa, sklad, opis, cena, false, false);
		
		DaoProvider.getInstance().getProduktDao().addProdukt(p);
		
//		if(details.has("wartosci_odzywcze")){
//			
//			query=query.replace("__odzywcze__", details.getString("wartosci_odzywcze"));
//			
//		}
//		
//		query=query.replace("__nazwa__", details.getString("nazwa"));
//		query=query.replace("__url__", detailsURL);
//		
//		if(details.has("sklad"))
//			query=query.replace("__sklad__", details.getString("sklad"));
//		else
//			query=query.replace("__sklad__", "");
//		
//		query=query.replace("__opis__", details.getString("opis"));
//		System.out.println("Insert query: "+query);
//		
//		String nutrValsInsertQuery=null;
//		String nutrVals=null;
//		
//		if(details.has("wartosci_odzywcze")){
//		nutrVals=details.getString("wartosci_odzywcze");
	}

	public abstract String extractNazwa(JSONObject details);
	public abstract String extractSklad(JSONObject details);
	public abstract String extractOpis(JSONObject details);
	public abstract long extractCena(JSONObject details);

	
	public ArrayList<Produkt> lookForInShop(String url,String lookFor) throws UnsupportedEncodingException,
	IOException, MalformedURLException {
		updateTickets();
		substractTickets();
		
		URLConnection connection = null;
		ArrayList<Produkt> retValue=new ArrayList<Produkt>();
		String query=baseUrl+"?"+urlParamName+"="+URLEncoder.encode(url,java.nio.charset.StandardCharsets.UTF_8.toString())+
				"&"+userParamName+"="+auchanUser+"&"+apiKeyParamName+"="+apiKey;
		
		connection = new URL(query).openConnection();
		connection.setRequestProperty("Accept-Charset", charset);
		String jsonRespons = getResponseText(connection);
		
		JSONArray jsonResults=new JSONObject(jsonRespons).getJSONArray("results");
		
		
		for(int i=0;i<jsonResults.length()&&SystemEnv.AgentsOn;i++){
			JSONObject res=jsonResults.getJSONObject(i);
//			String productDesc=res.getString("prodboxtext_link/_text");
			

			String detailsUrl=res.getString("prodphoto_link");
			String nazwa=res.getString("prodboxtext_link/_text");
			
	//		if(!checkForExistingDetails(detailsUrl))	
				rememberDetailsToBeSaved(detailsUrl);
			
			boolean found=true;
			for(String lookForWord:lookFor.split(" ")){

				if(!nazwa.toLowerCase().contains(lookForWord.toLowerCase()))
					found=false;
			}
			
			if(found){
				retValue.add(new Produkt(nazwa,detailsUrl));
				getAndSaveDetailsIfNew(detailsUrl);
			}

	
		}
		
		return retValue;
		
	}
	
	
	private void getAndSaveDetailsIfNew(String detailsUrl) {
		if(!checkForExistingDetails(detailsUrl))
			getAndSaveDetails(detailsUrl);
		
	}

	private String processNutrValuesQuery(String nutrVals) {
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
}
