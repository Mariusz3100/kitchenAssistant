package webscrappers.Jsoup.importio;

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
import java.util.List;
import java.util.Map;

import mariusz.ambroziak.kassistant.agents.ClockAgent;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.SystemEnv;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.dao.DataIntegrityViolationException;

import com.codesnippets4all.json.parsers.JSONParser;
import com.codesnippets4all.json.parsers.JsonParserFactory;

public abstract class ImportIoWebScrapper {
	 private static final int ENJOY_YOUR_OWN_WAIT_TIME = 500;
	private static final float ticketsFactor = 1f;
//	DatabaseInterface interfac;
	 ArrayList<String> detailsToBeSavedList;
		private float tickets=0;
		private float lastTime=0;

	 private StringBuilder scrapperLog;
	public ImportIoWebScrapper() {
//		interfac=DatabaseInterface.getDBInterface();
		detailsToBeSavedList=new ArrayList<String>();
		lastTime=ClockAgent.getTimePassed();
		scrapperLog=new StringBuilder();
	}
	final String insertQuery="insert into product (product_id,  url,nazwa,sklad,opis,brieflyProcessed)values(__id__,'__url__','__nazwa__','__sklad__','__opis__',__briefly_Processed__);";
	final String countQuery="select count(p_id) from product where url='__url__';";

	
	String charset = java.nio.charset.StandardCharsets.UTF_8.name();  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
	String baseUrl="";//="https://api.import.io/store/data/320a8ee3-a82a-4ed7-b349-f9e7f2eac1de/_query";
//	String baseUrlNew="https://api.import.io/store/data/929a8342-f326-422b-a31b-53552799b6fa/_query";

	String detailsBaseUrl="";

	String urlParamName="input=webpage/url";
//	String auchanURLToProcess=	"http%3A%2F%2Fwww.auchandirect.pl%2Fsklep%2Fartykuly%2F1160_1181_1292%2FProdukty-Swieze%2FCukiernia%2FCiasta-Ciastka";
	
	String userParamName="_user";
	String auchanUser="04ffe01c-7080-4817-8856-dac23896d915";
	
	String apiKeyParamName="_apikey";
	String groupApiKey="";
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
					 scrapperLog.append("New urls retrieved from file, current state: ");
	
					 for(String x:detailsToBeSavedList){
						 scrapperLog.append(x+"; ");
					}
					 
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
		URLConnection connection = getConnection(url);
		String jsonRespons = getResponseText(connection);

		JSONObject jsonObjecttemp = new JSONObject(jsonRespons);
		if(jsonObjecttemp.has("results"))
			{
			JSONArray jsonResults=new JSONObject(jsonRespons).getJSONArray("results");
			
			for(int i=0;i<jsonResults.length()&&SystemEnv.AgentsOn;i++){
				JSONObject res=jsonResults.getJSONObject(i);
		
				String detailsURL=res.getString("link");

				//if(!checkForExistingDetails(detailsURL))	
					rememberDetailsToBeSaved(detailsURL);
				

				
			}
			
			
		}else{
			System.out.println("no results found on "+url);
			
		}
		

	}

	public URLConnection getConnection(String originalUrl)
			throws UnsupportedEncodingException, 
			MalformedURLException {
		

		updateTickets();
		substractTickets();
		String query=baseUrl+"?"+urlParamName+":"+URLEncoder.encode(originalUrl,java.nio.charset.StandardCharsets.UTF_8.toString())+
				"&"+userParamName+"="+auchanUser+"&"+apiKeyParamName+"="+groupApiKey;
		URLConnection connection = waitTillConnOpen(query);
		return connection;
	}

	public URLConnection waitTillConnOpen(String query) {
		
		URLConnection connection = null;
		boolean successful=false;
		int timeOut=100000;	
		while(!successful){		
			try {
				connection = new URL(query).openConnection();
				connection.setConnectTimeout(timeOut);
				connection.setRequestProperty("Accept-Charset", charset);
				connection.getInputStream();

				successful=true;
			} catch (IOException e) {
				ProblemLogger.logProblem(
						"There was a problem with accessing url '"+query+"' Waiting "+timeOut+" ms");
				
//				try {
//					Thread.sleep(timeOut);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
				timeOut*=2;
			//	e.printStackTrace();
			}
		}
		return connection;
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
	
	
	protected Produkt checkForExistingDetails(String detailsURL) {
		
		String shortUrl=getUrlPattern(detailsURL);
		
		
		List<Produkt> produktsByURL = DaoProvider.getInstance().getProduktDao().getProduktsByUrlILike(shortUrl);

		if(produktsByURL.size()>1)
			throw new  DataIntegrityViolationException(
					"In the table recipe there is more than one recipe for url: "+detailsURL);
		else
			if(produktsByURL.size()==1)
				return produktsByURL.get(0);
			else 
				return null;
		
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
	
	
	protected Produkt getAndSaveDetails(String detailsURL)
			{

//		while(!SystemEnv.AgentsOn)
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
		updateTickets();
		substractTickets();
		
		Produkt produktDetails = getProduktDetails(detailsURL);

		if(produktDetails==null){
			scrapperLog.append("No data found at: "+produktDetails);
		}else{
			scrapperLog.append("Saving data from \""+detailsURL+"\": "+produktDetails);
			DaoProvider.getInstance().getProduktDao().addProdukt(produktDetails);
		}
		return produktDetails;
		
	}

	public  Produkt getProduktDetails(String detailsURL) {
		try {
		URLConnection connection;
		String detailsQuery=detailsBaseUrl+"?"+urlParamName+":"+URLEncoder.encode(detailsURL,java.nio.charset.StandardCharsets.UTF_8.toString())+
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
				
				
				return getDetailsFromJson(detailsURL,details);
				
				
			}else{
				System.out.println("no results found on "+detailsURL);
				return null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	private Produkt getDetailsFromJson(String detailsURL, JSONObject details) {
		
		
//		String url=details.getString("details");
		String nazwa=extractNazwa(details);
		String sklad=extractSklad(details);
		String opis=extractOpis(details);
		float cena = extractCena(details);
		if(detailsURL!=null)
			detailsURL=detailsURL.split(";")[0];
		Produkt p=new Produkt(detailsURL, nazwa, sklad, opis, cena, false, false);
		
//		scrapperLog.append("Scrapped data from \""+detailsURL+"\": "+p);
		
		return p;
	}

	public abstract String extractNazwa(JSONObject details);
	public abstract String extractSklad(JSONObject details);
	public abstract String extractOpis(JSONObject details);
	public abstract float extractCena(JSONObject details);

	
	public ArrayList<Produkt> lookForInShop(String url,String lookFor) throws UnsupportedEncodingException,
	IOException, MalformedURLException {
		updateTickets();
		substractTickets();
		ArrayList<Produkt> retValue=new ArrayList<Produkt>();

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

	public URLConnection getSearchForConnection(String originalUrl)
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

	protected abstract String getUrlPattern(String url);

	protected abstract String getShortestWorkingUrl(String url);
}
