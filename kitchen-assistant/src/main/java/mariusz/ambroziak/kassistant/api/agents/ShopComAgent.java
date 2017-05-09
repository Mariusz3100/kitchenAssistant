package mariusz.ambroziak.kassistant.api.agents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
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
import mariusz.ambroziak.kassistant.agents.BaseAgent;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.utils.MessageTypes;
import mariusz.ambroziak.kassistant.utils.ParameterHolder;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;
import mariusz.ambroziak.kassistant.utils.SystemEnv;

public class ShopComAgent extends BaseAgent {
	public static final String SHOP_COM_API_CLIENT_NAME = "auchanWebScrapper";


	int tickets=0;
	int lastCheckedClock=0;
	
	public static boolean agentOn=true;
	private static ArrayList<GA_ProduktScrapped> produktsToScrap;

	public ShopComAgent() {
		super();
		AGENT_ROLE = SHOP_COM_API_CLIENT_NAME;	

		AGENT_COMMUNITY=StringHolder.AGENT_COMMUNITY;
		//		AGENT_GROUP = StringHolder.SCRAPPERS_GROUP;
	}

	@Override
	protected void live() {
//		super.live();
		StringMessage m;

		while(true){

			m = nextMessageKA();
			if(m!=null)
				processMessage(m);
			else
				enjoyYourOwn();
		}
	}

	public void enjoyYourOwn() {
//				if(ParameterHolder.isCheckShops())
//					if(produktsToScrap!=null&&!produktsToScrap.isEmpty()){
//						try {
//							Thread.sleep(20000);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						Iterator<GA_ProduktScrapped> it=produktsToScrap.iterator();
//						GA_ProduktScrapped produkt = it.next();
//						it.remove();
//						Produkt pToSave=getOrParseProduktByUrl(produkt.getUrl());
//						DaoProvider.getInstance().getProduktDao().saveProdukt(pToSave);
//					}
					
					
					
	}

	@ResponseBody
	@RequestMapping("/agents/"+SHOP_COM_API_CLIENT_NAME)
	public String showData(){
		return htmlLogs.toString().replace("\n", "\n<br>");
	}


	private void processMessage(StringMessage m) {
		if(m!=null){
			String content=((StringMessage)m).getContent();
			JSONObject json=new JSONObject(content);

			if(json.get(StringHolder.MESSAGE_TYPE_NAME)==null
					||json.get(StringHolder.MESSAGE_TYPE_NAME).equals("")){
				ProblemLogger.logProblem("Message has no type (in Shop.com agent): "+content);
			}else if(json.get(StringHolder.MESSAGE_TYPE_NAME).equals(MessageTypes.SearchFor.toString())){
				processSearchForMessage(m);
			}else if(json.get(StringHolder.MESSAGE_TYPE_NAME).equals(MessageTypes.GetProduktData.toString())){
				processGetProduktByUrlMesage(m);

			}
		}
	}

	private void processSearchForMessage(StringMessage m) {
		JSONObject json=new JSONObject(m.getContent());

		String searchForPhrase=(String) json.get(StringHolder.SEARCH4_NAME);

		ArrayList<GA_ProduktScrapped> produkts = searchForCorrectRememberOthers(searchForPhrase);
		String ids="";
		if(produkts.isEmpty()){
			htmlLog("Not able to find produkt for:"+searchForPhrase+" in shop.com.\n");
		}else{
			ArrayList<Produkt> savedDetails = getAndSaveDetails(produkts);

			for(Produkt p:savedDetails){
				ids+=p.getP_id()+" ";
			}
			ids=ids.trim();
		}
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
						produkt.setQuantityPhrase(details.getAmount()==null?
								"":details.getAmount().toJspString());
						
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

	
	public Produkt  getOrParseProduktByUrl(String produktUrl) {
		if(produktUrl==null||produktUrl.equals("")){
			ProblemLogger.logProblem("Empty url in "+produktUrl);
			return null;
		}else{
			String shorturl=AuchanAbstractScrapper.getAuchanShortestWorkingUrl(produktUrl);

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
					ProblemLogger.logProblem("404 page at "+produktUrl);
					ProblemLogger.logStackTrace(e.getStackTrace());
				}

				if(produktDetails!=null){
					foundProdukt=new Produkt();
					foundProdukt.setCena(produktDetails.getCena());
					foundProdukt.setNazwa(produktDetails.getNazwa());
					foundProdukt.setOpis(produktDetails.getOpis());
					foundProdukt.setPrzetworzony(false);
					foundProdukt.setUrl(shorturl);
					foundProdukt.setQuantityPhrase(produktDetails.getAmount()==null?
							"":produktDetails.getAmount().toJspString());
					
					DaoProvider.getInstance().getProduktDao().saveProdukt(foundProdukt);

					htmlLog("Na podstawie url "+shorturl
							+" sparsowano i zapisano w bd produkt o id ["+foundProdukt.getP_id()+"]\n");

				}
			}
			return foundProdukt;

		}
	
		
		
		
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
					foundProdukt.setQuantityPhrase(produktDetails.getAmount()==null?
							"":produktDetails.getAmount().toJspString());
					
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
		super.activate();
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		super.end();
	}

}
