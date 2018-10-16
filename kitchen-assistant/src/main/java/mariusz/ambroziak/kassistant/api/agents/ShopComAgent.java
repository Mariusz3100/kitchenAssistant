package mariusz.ambroziak.kassistant.api.agents;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.Apiclients.edaman.RecipeData;
import mariusz.ambroziak.kassistant.Apiclients.shopcom.ShopComApiParameters;
import mariusz.ambroziak.kassistant.agents.BaseAgent;
import mariusz.ambroziak.kassistant.agents.ProduktAgent;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.shopcom.ShopComApiClient;
import mariusz.ambroziak.kassistant.shopcom.ShopComApiClientParticularProduct;
import mariusz.ambroziak.kassistant.utils.MessageTypes;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;
import mariusz.ambroziak.kassistant.utils.StringUtils;
import webscrappers.auchan.GA_ProduktScrapped;
import webscrappers.auchan.ProduktDetails;

public class ShopComAgent extends BaseAgent {
	public static final String SHOP_COM_API_AGENT_NAME = "shopComApiAgent";

	static ArrayList<ShopComAgent> agents;
	int tickets=0;
	int lastCheckedClock=0;

	public static boolean agentOn=true;
	private static ArrayList<GA_ProduktScrapped> produktsToScrap;

	public static String baseUrl="https://api.shop.com/stores/v1/products/";

	public ShopComAgent() {
		super();
		AGENT_ROLE = SHOP_COM_API_AGENT_NAME;	

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
	@RequestMapping("/agents/"+SHOP_COM_API_AGENT_NAME)
	public String showData(){
		return htmlLogs.toString().replace("\n", "\n<br>");
	}

	private static ShopComAgent getFreeAgent() throws AgentSystemNotStartedException {
		ShopComAgent freeOne=null;

		if(agents==null){
			System.out.println("Agent system not started");
			throw new AgentSystemNotStartedException();

		}
		else{
			while(freeOne==null){
				for(ShopComAgent ra:agents){
					if(!ra.isBusy()){
						freeOne=ra;

					}
				}
				if(freeOne==null){
					try {
						System.out.println("Free RecipeParser Not Found");
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return freeOne;
	}

	
	public static List<Produkt> searchForIngredient(String searchPhrase) throws AgentSystemNotStartedException{
		ShopComAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.setBusy(true);
			List<Produkt> result;
			try{
				result= freeOne.searchForCorrect(searchPhrase);
			}finally{
				freeOne.setBusy(false);
			}
			return result;
		}
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
		StringMessage messageToSend=null;
		
		String ids = searchForCorrectHandleNulls(json);
		
		try{
			messageToSend = createResponseMessage(ids);
		}catch(Exception e) {
			ProblemLogger.logProblem(e.toString());
			ProblemLogger.logStackTrace(e.getStackTrace());
			messageToSend=createExceptionResponseMessage(e);
			
			setBusy(false);
		}

		sendReplyWithRoleKA(m, messageToSend,AGENT_ROLE);
	}

	private StringMessage createExceptionResponseMessage(Exception e) {
		JSONObject result=new JSONObject();

		result.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.ExceptionOccured);
		result.put(StringHolder.MESSAGE_CREATOR_NAME, SHOP_COM_API_AGENT_NAME);
		result.put(StringHolder.EXCEPTION_MESSAGE_NAME, e.toString());
		result.put(StringHolder.EXCEPTION_STACKTRACE_NAME, StringUtils.stackTraceToString(e));
		StringMessage messageToSend = new StringMessage(result.toString());

		return messageToSend;
	}

	private StringMessage createResponseMessage(String ids) {
		JSONObject result=new JSONObject();

		result.put(StringHolder.PRODUKT_IDS_NAME, ids);
		result.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.SearchForResponse);
		result.put(StringHolder.MESSAGE_CREATOR_NAME, SHOP_COM_API_AGENT_NAME);

		StringMessage messageToSend = new StringMessage(result.toString());
		return messageToSend;
	}

	private String searchForCorrectHandleNulls(JSONObject json) {
		String searchForPhrase=(String) json.get(StringHolder.SEARCH4_NAME);

		ArrayList<Produkt> produkts = searchForCorrect(searchForPhrase);
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
		
		if(ids.indexOf("null")>0){
			ids=ids.replaceAll("null", "");
			ids.replaceAll("  ", " ");
		}
		return ids;
	}

	private ArrayList<Produkt> getAndSaveDetails(ArrayList<Produkt> produkts) {
		ArrayList<Produkt> retValue=new ArrayList<Produkt>();

		for(Produkt gaProdukt : produkts){
			Produkt produktByURL = DaoProvider.getInstance().getProduktDao().getProduktsByURL(gaProdukt.getUrl());

			if(produktByURL!=null){
				retValue.add(produktByURL);
			}else{

				DaoProvider.getInstance().getProduktDao().saveProdukt(gaProdukt);

				htmlLog("Na podstawie url "+gaProdukt.getUrl()
				+" sparsowano i zapisano w bd produkt o id ["+gaProdukt.getP_id()+"]\n");
				retValue.add(gaProdukt);


			}
		}
		return retValue;

	}

	public static String[] splitSearchPhrase(String searchPhrase) {
		return searchPhrase.split(" ");
	}

	public ArrayList<Produkt> searchForCorrect(String searchForPhrase) {
		ArrayList<Produkt> produktsFound =ShopComApiClient.getProduktsFor(searchForPhrase);
		String[] searchWords=splitSearchPhrase(searchForPhrase);
		ArrayList<Produkt> retValue=new ArrayList<>();
		for(Produkt prodScrapped:produktsFound){

			boolean found=true;
			for(String lookForWord:searchWords){

				if(!prodScrapped.getNazwa().toLowerCase().contains(lookForWord.toLowerCase()))
					found=false;
			}
			if(found){
				retValue.add(prodScrapped);
			}
			//else addProduktsToScrapLater(prodScrapped); TODO put it back?
		}

		return retValue;
	}



	public void processGetProduktByUrlMesage(StringMessage m) {
		Produkt produkt = getOrParseProduktByUrl(m);
		StringMessage messageToSend=null;
		try {
			messageToSend = createProduktByUrlResponse(produkt);
		}catch(Exception e) {
			ProblemLogger.logProblem(e.toString());
			ProblemLogger.logStackTrace(e.getStackTrace());
			messageToSend=createExceptionResponseMessage(e);
			
			setBusy(false);
		}
		
		sendReplyWithRoleKA(m, messageToSend,AGENT_ROLE);
	}

	private StringMessage createProduktByUrlResponse(Produkt produkt) {
		String id="";
		if(produkt!=null){
			id+=produkt.getP_id();

		}

		JSONObject result=new JSONObject();

		result.put(StringHolder.SINGLE_PRODUKT_ID_NAME, id);
		result.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.GetProduktDataResponse);

		StringMessage messageToSend = new StringMessage(result.toString());
		return messageToSend;
	}


	public Produkt  getOrParseProduktByUrl(String produktUrl) {
		if(produktUrl==null||produktUrl.equals("")){
			ProblemLogger.logProblem("Empty url in "+produktUrl);
			return null;
		}else{
			Produkt foundProdukt=
					DaoProvider.getInstance().getProduktDao().getProduktsByURL(produktUrl );

			if(foundProdukt!=null){
				htmlLog("Na podstawie url "+produktUrl
						+" w bazie danych znaleziono produkt o id ["+foundProdukt.getP_id()+"]\n");

			}else{


				//		try {
				if(produktUrl!=null&&!produktUrl.equals(""))
					foundProdukt = ShopComApiClientParticularProduct.getProduktByShopId(produktUrl);
					DaoProvider.getInstance().getProduktDao().saveProdukt(foundProdukt);
				//				} catch (Page404Exception e) {
				//				ProblemLogger.logProblem("404 page at "+produktUrl);
				//				ProblemLogger.logStackTrace(e.getStackTrace());
				//			}

				DaoProvider.getInstance().getProduktDao().saveProdukt(foundProdukt);

				htmlLog("Na podstawie url "+produktUrl
						+" sparsowano i zapisano w bd produkt o id ["+foundProdukt.getP_id()+"]\n");

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


			Produkt foundProdukt=
					DaoProvider.getInstance().getProduktDao().getProduktsByURL(url);

			if(foundProdukt!=null){
				htmlLog("Na podstawie url "+url
						+" w bazie danych znaleziono produkt o id ["+foundProdukt.getP_id()+"]\n");

			}else{

				ProduktDetails produktDetails = null;
		//		try {
					if(url!=null&&!url.equals(""))
						produktDetails = getProduktByUrl(url);//will it work??? AuchanParticular.getProduktDetails(url);

		//		} catch (Page404Exception e) {
			//		ProblemLogger.logProblem("404 page at "+url);
			//		ProblemLogger.logStackTrace(e.getStackTrace());
		//		}

				if(produktDetails!=null){
					foundProdukt=new Produkt();
					foundProdukt.setCena(produktDetails.getCena());
					foundProdukt.setNazwa(produktDetails.getNazwa());
					foundProdukt.setOpis(produktDetails.getOpis());
					foundProdukt.setPrzetworzony(false);
					foundProdukt.setUrl(url);
					foundProdukt.setQuantityPhrase(produktDetails.getAmount()==null?
							"":produktDetails.getAmount().toJspString());

					DaoProvider.getInstance().getProduktDao().saveProdukt(foundProdukt);

					htmlLog("Na podstawie url "+url
							+" sparsowano i zapisano w bd produkt o id ["+foundProdukt.getP_id()+"]\n");

				}
			}
			return foundProdukt;

		}

	}

	private ProduktDetails getProduktByUrl(String url) {
		String fullUrl=ShopComApiParameters.getUrlWithKeys(url);
		Produkt produktByUrl = ShopComApiClientParticularProduct.getProduktByUrl(fullUrl);
		ProduktDetails ret=new ProduktDetails(produktByUrl.getUrl(), produktByUrl.getNazwa(),produktByUrl.getCena() , produktByUrl.getOpis());
		return ret;
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
		if(agents==null)agents=new ArrayList<ShopComAgent>();
		agents.add(this);
		
		requestRole(AGENT_COMMUNITY,AGENT_GROUP, AGENT_ROLE);
		super.activate();
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		super.end();
	}

}
