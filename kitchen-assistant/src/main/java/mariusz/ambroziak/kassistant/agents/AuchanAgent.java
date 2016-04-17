package mariusz.ambroziak.kassistant.agents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;























import webscrappers.auchan.AuchanAbstractScrapper;
import webscrappers.auchan.AuchanGroup;
import webscrappers.auchan.AuchanParticular;
import webscrappers.auchan.GA_ProduktScrapped;
import webscrappers.auchan.ProduktDetails;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.utils.MessageTypes;
import mariusz.ambroziak.kassistant.utils.ParameterHolder;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;
import mariusz.ambroziak.kassistant.utils.SystemEnv;

public class AuchanAgent extends BaseAgent {
	public static final String AUCHAN_WEB_SCRAPPER_NAME = "auchanWebScrapper";

	public static final String acceptedURL="(http://)?www.auchandirect.pl/sklep/.+";
	public static final String baseUrl="www.auchandirect.pl/";


	public static boolean agentOn=true;
	//	AuchanWebScrapper webScrapper;

	private static ArrayList<GA_ProduktScrapped> produktsToScrap;

	public AuchanAgent() {
		super();
		AGENT_ROLE = AUCHAN_WEB_SCRAPPER_NAME;	

		AGENT_COMMUNITY=StringHolder.AGENT_COMMUNITY;
		//		AGENT_GROUP = StringHolder.SCRAPPERS_GROUP;
	}

	@Override
	protected void live() {
		super.live();


		StringMessage m;

		while(true){


			m = nextMessageKA();
			if(m!=null)
				processMessage(m);
			else
				enjoyYourOwn();
		}




		//	BufferedReader br=null;


	}

	public void enjoyYourOwn() {
		//		if(ParameterHolder.isCheckShops())
		//			webScrapper.enjoyYourOwn();
	}

	@ResponseBody
	@RequestMapping("/agents/"+AUCHAN_WEB_SCRAPPER_NAME)
	public String showData(){
		return htmlLogs.toString().replace("\n", "\n<br>");
	}


	private void processMessage(StringMessage m) {
		if(m!=null){
			String content=((StringMessage)m).getContent();
			JSONObject json=new JSONObject(content);

			if(json.get(StringHolder.MESSAGE_TYPE_NAME)==null
					||json.get(StringHolder.MESSAGE_TYPE_NAME).equals("")){
				ProblemLogger.logProblem("Message has no type (in AuchanAgent): "+content);
			}else if(json.get(StringHolder.MESSAGE_TYPE_NAME).equals(MessageTypes.SearchFor.toString())){
				processSearchForMessage(m);
			}else if(json.get(StringHolder.MESSAGE_TYPE_NAME).equals(MessageTypes.GetProduktData.toString())){
				processGetProduktByUrlMesage(m);

			}
		}
		//		try {
		//			ArrayList<Produkt> x=null;//webScrapper.lookup(message.getString(StringHolder.SEARCH4_NAME));
		//			AgentAddress other=getAgentWithRole(AGENT_COMMUNITY, AGENT_GROUP, ShopsListAgent.SHOP_LIST_NAME);
		//			
		//			
		//			JSONObject result=new JSONObject();
		//			
		//			if(x==null||x.size()==0){
		//				result.put("ids", "");
		//				htmlLog("Nie uda³o siê znaleŸæ ¿adnego produktu dla "+message.getString(StringHolder.SEARCH4_NAME)+" w sklepie auchan.\n");
		//			}else{
		//				
		//				String foundProdukts="";
		//				
		//				for(Produkt p:x){
		//					foundProdukts+=p.getP_id()+" ";
		//				}
		//				result.put("ids", foundProdukts);
		//				
		//				
		//				htmlLog("W sklepie auchan znaleziono produkt(y) o id ["+foundProdukts+"]\n");
		//				
		//			}
		//			
		//			result.put(StringHolder.MESSAGE_CREATOR_NAME, AUCHAN_WEB_SCRAPPER_NAME);
		//			result.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.SearchFor);
		//			result.put(StringHolder.SEARCH4_NAME, message.getString(StringHolder.SEARCH4_NAME));
		//			StringMessage messageToSend = new StringMessage(result.toString());
		//
		//			sendMessageWithRoleKA(other, messageToSend,AGENT_ROLE);
		//			
		//			
		////		} catch (UnsupportedEncodingException e) {
		////			// TODO Auto-generated catch block
		////			e.printStackTrace();
		////		} catch (MalformedURLException e) {
		////			// TODO Auto-generated catch block
		////			e.printStackTrace();
		////		} catch (JSONException e) {
		////			// TODO Auto-generated catch block
		////			e.printStackTrace();
		////		} catch (IOException e) {
		////			// TODO Auto-generated catch block
		////			e.printStackTrace();
		////		}
		//		
	}

	private void processSearchForMessage(StringMessage m) {
		JSONObject json=new JSONObject(m.getContent());

		String searchForPhrase=(String) json.get(StringHolder.SEARCH4_NAME);

		ArrayList<GA_ProduktScrapped> produkts = searchForCorrectRememberOthers(searchForPhrase);

		ArrayList<Produkt> savedDetails = getAndSaveDetails(produkts);

		String ids="";
		
		for(Produkt p:savedDetails){
			ids+=p.getP_id()+" ";
		}
		ids=ids.trim();

		JSONObject result=new JSONObject();

		result.put(StringHolder.PRODUKT_IDS_NAME, ids);
		result.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.SearchForResponse);

		StringMessage messageToSend = new StringMessage(result.toString());

		sendReplyWithRoleKA(m, messageToSend,AGENT_ROLE);
	}

	private ArrayList<Produkt> getAndSaveDetails(ArrayList<GA_ProduktScrapped> produkts) {
		ArrayList<Produkt> retValue=new ArrayList<Produkt>();
		
		for(GA_ProduktScrapped gaProdukt : produkts){
			String shortUrl=AuchanAbstractScrapper
					.getAuchanShortestWorkingUrl(gaProdukt.getUrl());
			
			Produkt produktByURL = DaoProvider.getInstance().getProduktDao().getProduktsByURL(shortUrl);
			
			if(produktByURL!=null){
				retValue.add(produktByURL);
			}else{
				try {
					ProduktDetails details = AuchanParticular.getProduktDetails(gaProdukt.getUrl());
					Produkt produkt;
					if(details!=null){
						produkt=new Produkt();
						produkt.setCena(details.getCena());
						produkt.setNazwa(details.getNazwa());
						produkt.setOpis(details.getOpis());
						produkt.setPrzetworzony(false);
						produkt.setUrl(shortUrl);
	
						DaoProvider.getInstance().getProduktDao().saveProdukt(produkt);
	
						htmlLog("Na podstawie url "+shortUrl
								+" sparsowano i zapisano w bd produkt o id ["+produkt.getP_id()+"]\n");
						retValue.add(produkt);

					}
				
				} catch (Page404Exception e) {
					ProblemLogger.logProblem("404 page at "+gaProdukt.getUrl());
					ProblemLogger.logStackTrace(e.getStackTrace());
				}
			}
		}
		return retValue;
		
	}

	public static String[] splitSearchPhrase(String searchPhrase) {
		return searchPhrase.split(" ");
	}

	public static ArrayList<GA_ProduktScrapped> searchForCorrectRememberOthers(String searchForPhrase) {
		ArrayList<GA_ProduktScrapped> produktsFound = AuchanGroup.searchFor(searchForPhrase);
		String[] searchWords=splitSearchPhrase(searchForPhrase);

		ArrayList<GA_ProduktScrapped> retValue=new ArrayList<GA_ProduktScrapped>();

		for(GA_ProduktScrapped prodScrapped:produktsFound){

			boolean found=true;
			for(String lookForWord:searchWords){

				if(!prodScrapped.getNazwa().toLowerCase().contains(lookForWord.toLowerCase()))
					found=false;
			}


			if(found){

				retValue.add(prodScrapped);

			}else
				addProduktsToScrapLater(prodScrapped);
		}
		
		return retValue;
	}

	private static void addProduktsToScrapLater(GA_ProduktScrapped produkt){
		if(produktsToScrap==null)
			produktsToScrap=new ArrayList<GA_ProduktScrapped>();

			produktsToScrap.add(produkt);

	}

	public void processGetProduktByUrlMesage(StringMessage m) {
		Produkt produkt = getOrParseProduktByUrl(m);

		String id="";
		if(produkt!=null){
			id+=produkt.getP_id();

		}

		JSONObject result=new JSONObject();

		result.put(StringHolder.SINGLE_PRODUKT_ID_NAME, id);
		result.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.GetProduktDataResponse);

		StringMessage messageToSend = new StringMessage(result.toString());

		sendReplyWithRoleKA(m, messageToSend,AGENT_ROLE);
	}

	public Produkt  getOrParseProduktByUrl(StringMessage m) {
		JSONObject json=new JSONObject(m.getContent());

		String url=(String) json.get(StringHolder.PRODUKT_URL_NAME);

		if(url==null||url.equals("")){
			ProblemLogger.logProblem("Empty url in "+m);
			return null;
		}
		else{

			String shorturl=AuchanAbstractScrapper.getAuchanShortestWorkingUrl(url);

			Produkt foundProdukt=
					DaoProvider.getInstance().getProduktDao().getProduktsByURL(shorturl);

			if(foundProdukt!=null){
				htmlLog("Na podstawie url "+shorturl
						+" w bazie danych znaleziono produkt o id ["+foundProdukt.getP_id()+"]\n");

			}else{
				
				ProduktDetails produktDetails = null;
				try {
					if(shorturl!=null&&!shorturl.equals(""))
						produktDetails = AuchanParticular.getProduktDetails(shorturl);

				} catch (Page404Exception e) {
					ProblemLogger.logProblem("404 page at "+url);
					ProblemLogger.logStackTrace(e.getStackTrace());
				}

				if(produktDetails!=null){
					foundProdukt=new Produkt();
					foundProdukt.setCena(produktDetails.getCena());
					foundProdukt.setNazwa(produktDetails.getNazwa());
					foundProdukt.setOpis(produktDetails.getOpis());
					foundProdukt.setPrzetworzony(false);
					foundProdukt.setUrl(shorturl);

					DaoProvider.getInstance().getProduktDao().saveProdukt(foundProdukt);

					htmlLog("Na podstawie url "+shorturl
							+" sparsowano i zapisano w bd produkt o id ["+foundProdukt.getP_id()+"]\n");

				}
			}
			return foundProdukt;

		}

	}

	@Override
	protected void pause(int milliSeconds) {
		// TODO Auto-generated method stub
		super.pause(milliSeconds);
	}

	@Override
	protected void activate() {

		setLogLevel(Level.FINEST);
		//		setWarningLogLevel(Level.FINEST);

		if(!isGroup(AGENT_COMMUNITY,AGENT_GROUP)){
			createGroup(AGENT_COMMUNITY,AGENT_GROUP);
		}

		requestRole(AGENT_COMMUNITY,AGENT_GROUP, AGENT_ROLE);
		//		requestRole(AGENT_COMMUNITY, ShopsListAgent.GUIDANCE_GROUP, AGENT_ROLE);


		//		webScrapper=AuchanWebScrapper.getAuchanWebScrapper();
		super.activate();
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		super.end();
	}

}
