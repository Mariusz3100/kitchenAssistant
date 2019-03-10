package mariusz.ambroziak.kassistant.agents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.Apiclients.shopcom.ShopComApiParameters;
import mariusz.ambroziak.kassistant.api.agents.ShopComAgent;
import mariusz.ambroziak.kassistant.dao.Base_WordDAOImpl;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.dao.ProduktDAO;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.ShopNotFoundException;
import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.utils.PreciseQuantityWithPhrase;
import mariusz.ambroziak.kassistant.shopcom.ShopComApiClient;
import mariusz.ambroziak.kassistant.shopcom.ShopComApiClientParticularProduct;
import mariusz.ambroziak.kassistant.shops.ShopRecognizer;
import mariusz.ambroziak.kassistant.utils.MessageTypes;
import mariusz.ambroziak.kassistant.utils.ParameterHolder;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;

import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import webscrappers.SJPWebScrapper;
import webscrappers.przepisy.PrzepisyPLQExtract;

public class ProduktAgent extends BaseAgent{
	public static final String PARSER_NAME = "produktParser";
	private static final long serialVersionUID = 1L;
	
	private static final String productAgentDescription="This agent can be used by users to search for products in the agent system";
	
	static ArrayList<ProduktAgent> agents;
	public  static final boolean checkShops = true;




	@Override
	protected void live() {
		while(true){
			pause(1000000);
		}
	}




	@Override
	protected void pause(int milliSeconds) {
		// TODO Auto-generated method stub
		super.pause(milliSeconds);
	}

	@Override
	protected void activate() {
		if(!isGroup(AGENT_COMMUNITY,AGENT_GROUP)){
			createGroup(AGENT_COMMUNITY,AGENT_GROUP);
		}
		
		requestRole(AGENT_COMMUNITY, AGENT_GROUP, PARSER_NAME);
//		requestRole(AGENT_COMMUNITY, ShopsListAgent.GUIDANCE_GROUP, AGENT_ROLE);
		
		//		interfac=DatabaseInterface.getDBInterface();

		if(agents==null)agents=new ArrayList<ProduktAgent>();
		agents.add(this);
//		AgentAddress x=getAgentWithRole(AGENT_COMMUNITY, AGENT_GROUP, "manager");

//		pause(500);

		
		setLogLevel(Level.FINEST);
		super.activate();
	}

	public ProduktAgent() {
		super();
		AGENT_COMMUNITY=StringHolder.AGENT_COMMUNITY;
		setDescription(productAgentDescription);
	}


	@Override
	protected void end() {
		// TODO Auto-generated method stub
		super.end();
	}


	public static List<Produkt> searchForProdukt(String searchPhrase) throws AgentSystemNotStartedException{
		ProduktAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.setBusy(true);
			List<Produkt> result;
			try{
				result= freeOne.retrieveSkladnik(searchPhrase);
			}finally{
				freeOne.setBusy(false);
			}
			return result;
		}
	}
	
	public static String getProduktFullLink(String shortUrl) throws AgentSystemNotStartedException, ShopNotFoundException{
		ProduktAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.setBusy(true);
			String result;
			try{
				result= freeOne.produceFullLink(shortUrl);
			}finally{
				freeOne.setBusy(false);
			}
			return result;
		}
	}
	
	private String produceFullLink(String shortUrl) throws ShopNotFoundException {
		if(shortUrl==null||shortUrl.equals("")) {
			return "";
		}else if(shortUrl.startsWith(ShopComAgent.baseUrl)) {
			String result=ShopComApiParameters.getUrlWithKeys(shortUrl);
			
			return result;
		}else {
			throw new ShopNotFoundException("Url \""+shortUrl
					+"\" was not matched with any known shop");
		}
		
		
	}




	public static Produkt getOrScrapProdukt(String produktUrl) throws ShopNotFoundException, AgentSystemNotStartedException{
		ProduktAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.setBusy(true);
			Produkt result;
			try{
			result= freeOne.getFromDbOrScrapProdukt(produktUrl);
			}finally{
				freeOne.setBusy(false);
			}
			return result;
		}
	}
	
	
	private Produkt getFromDbOrScrapProdukt(String produktUrl) throws ShopNotFoundException {
		String shortUrl=ShopRecognizer.getShortestWorkingUrl(produktUrl);
		Produkt produkt = getProduktFromDbByUrl(shortUrl);

		if(produkt==null){
			if(ParameterHolder.isCheckShops()){
				StringMessage messageToSend = createGetProduktByUrlMessage(shortUrl);
				
				AgentAddress x=getAgentWithRole(StringHolder.AGENT_COMMUNITY, AGENT_GROUP, ShopsListAgent.SHOP_LIST_NAME);

				StringMessage reply = (StringMessage) sendMessageWithRoleAndWaitForReplyKA(x, messageToSend,PARSER_NAME);
				JSONObject jsonResponse = new JSONObject(reply.getContent());

				if(jsonResponse.has(StringHolder.NO_RESULT_INFO_NAME))
				{
					if(StringHolder.NO_RESULT_UNKNOWN_SHOP.toString().equals(
							jsonResponse.get(StringHolder.NO_RESULT_INFO_NAME))){
						throw new ShopNotFoundException("\""+produktUrl+"\"->\""+shortUrl+"\"");
					}
				}else if(jsonResponse.has(StringHolder.SINGLE_PRODUKT_ID_NAME))
				{
				//TODO co tu powinno byc w ogole?	
					produkt=processSingleIdStringMessage(jsonResponse, produktUrl,shortUrl);
				}else{
					ProblemLogger.logProblem("some weird message: "+jsonResponse+" in produktAgent");
				}
				
			}
		}
		return produkt;
	}

	private StringMessage createGetProduktByUrlMessage(String shortUrl) {
		JSONObject json = new JSONObject();
		json.put(StringHolder.PRODUKT_URL_NAME, shortUrl);
		json.put(StringHolder.MESSAGE_CREATOR_NAME, PARSER_NAME);
		json.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.GetProduktData);
		StringMessage messageToSend = new StringMessage(json.toString());
		return messageToSend;
	}


	private Produkt processSingleIdStringMessage(JSONObject jsonResponse, String produktUrl,String shortUrl) {
		String stringId=(String) jsonResponse.get(StringHolder.SINGLE_PRODUKT_ID_NAME);
		Produkt retValue=null;
		if(stringId==null||stringId.equals(""))
		{
			ProblemLogger.logProblem("For url "+produktUrl+"->"+shortUrl
					+" no produkt was found");
			return null;
		}
		try{
			Long id=Long.parseLong(stringId);
			retValue=DaoProvider.getInstance().getProduktDao().getById(id);

		}catch(NumberFormatException e){
			ProblemLogger.logProblem("For url "+produktUrl+"->"+shortUrl
					+" found id is invalid numbers:'"+stringId+"'");
		}
		return retValue;
	}

	public Produkt getProduktFromDbByUrl(String produktUrl) {
		return DaoProvider.getInstance().getProduktDao().getProduktsByURL(produktUrl);
	}


	private static ProduktAgent getFreeAgent() throws AgentSystemNotStartedException {
		ProduktAgent freeOne=null;
		
		if(agents==null){
			System.out.println("Agent system not started");
			throw new AgentSystemNotStartedException();
		}
		else{
			while(freeOne==null){
				for(ProduktAgent ra:agents){
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

	private String extractQuantity(Element e) {
		
		String quantity=e.parent().select(".quantity").text();
		
		
		if(quantity==null||quantity.equals(""))
			quantity=e.parent().parent().select(".quantity").text();
		return quantity;
	}

	private static PreciseQuantityWithPhrase retrieveQuantity(String quantity) {
		return PrzepisyPLQExtract.extractQuantity(quantity);
		
		
		
	}


	private List<Produkt> retrieveSkladnik(String text) {
		List<Produkt> results;
		ArrayList<String> baseWords = null;
		
		
		
		results=searchInDb(text);

		if(results==null||results.size()<1){
			baseWords=getBaseWords(text);
			
			baseWords=removeNiepoprawne(baseWords);
			
			if(baseWords.size()>0)
				results=searchInDb(baseWords);
		}

		if(results==null||results.size()<1){
			
			if(baseWords==null)
				baseWords=getBaseWords(text);
			
			baseWords=removeNiepoprawne(baseWords);
			
			if(baseWords.size()>0)
				results=checkShops(baseWords);
		}
		
		
		if(results==null||results.size()<1){
			results=checkShops(text);
		}
		return results;

	}


	private ArrayList<String> removeNiepoprawne(ArrayList<String> baseWords) {
		ArrayList<String> retValue=new ArrayList<String>();

		for(String x:baseWords){
			if(!x.equals(Base_WordDAOImpl.niepoprawneSlowoBazowe))
				retValue.add(x);
		}

		return retValue;
	}
	
	private String removeNiepoprawne(String baseWords) {
		return baseWords.replaceAll(Base_WordDAOImpl.niepoprawneSlowoBazowe, 
				"");
	}

	private List<Produkt> checkShops(ArrayList<String> baseWords) {
		
		String baseWordsString="";
		for(String x:baseWords){
			baseWordsString+=x+" ";
		}
		
		
		return checkShops(baseWordsString);
	}


	private ArrayList<String> getBaseWords(String text) {
		String[] words=text.split(" ");
		ArrayList<String> wordsList=new ArrayList<String>();
		
		for(String x : words){
			Base_Word base_Name = DaoProvider.getInstance().getVariantWordDao().getBase_Name(x.toLowerCase());
			if(base_Name!=null){
				if(!Base_WordDAOImpl.niepoprawneSlowoBazowe.equals(base_Name.getB_word()))
					wordsList.add(base_Name.getB_word());
			}
			else
				wordsList.add(SJPWebScrapper.getAndSaveNewRelation(x.toLowerCase()));
		}
		
		return wordsList;
	}





	private ArrayList<Produkt> searchInDb(String text) {
		ArrayList<Produkt> retValue=new ArrayList<Produkt>();

		retValue.addAll(
				DaoProvider.getInstance()
				.getProduktDao()
				.getProduktsBySpacedName(text));



		return retValue;
	}

	private ArrayList<Produkt> searchInDb(Collection<String> texts) {
		ArrayList<Produkt> retValue=new ArrayList<Produkt>();

		retValue.addAll(
				DaoProvider.getInstance()
				.getProduktDao()
				.getProduktsByNames(texts));



		return retValue;
	}



	private ArrayList<Produkt> checkShops(String text) {
		if(checkShops){
			JSONObject json = new JSONObject();
			
			json.put(StringHolder.SEARCH4_NAME, text);
			json.put(StringHolder.MESSAGE_CREATOR_NAME, PARSER_NAME);
			json.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.SearchFor);
			
			
			AgentAddress x=getAgentWithRole(StringHolder.AGENT_COMMUNITY, AGENT_GROUP, ShopsListAgent.SHOP_LIST_NAME);
	
			StringMessage messageToSend = new StringMessage(json.toString());
			StringMessage response=(StringMessage) 
					sendMessageWithRoleAndWaitForReplyKA(x, messageToSend,PARSER_NAME);
			
	
			if(response.getContent().equals(""))
					return null;
			else{
				JSONObject jsonObject = new JSONObject(response.getContent());
				
	//			Map y= factory.newJsonParser().parseJson(response.getContent());
				ProduktDAO produktDao = DaoProvider.getInstance().getProduktDao();
				
				String ids=jsonObject.getString(StringHolder.PRODUKT_IDS_NAME);
				
				ArrayList<Produkt> retValue=new ArrayList<Produkt>();
				
				if(ids!=null&&!ids.equals(""))
					for(String id:ids.split(" ")){
						retValue.add(produktDao.getById(Long.parseLong(id)));
					}
				
				return retValue;
			}
		}else{
			return null;
		}
	}
}
