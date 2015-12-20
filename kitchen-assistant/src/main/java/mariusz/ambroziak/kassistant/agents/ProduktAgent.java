package mariusz.ambroziak.kassistant.agents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.QuantityExtractor.AuchanQExtract;
import mariusz.ambroziak.kassistant.dao.Base_WordDAOImpl;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.dao.ProduktDAO;
import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.jsp.QuantityProdukt;
import mariusz.ambroziak.kassistant.utils.MessageTypes;
import mariusz.ambroziak.kassistant.utils.ParameterHolder;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.StringHolder;

import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;

//import com.codesnippets4all.json.parsers.JsonParserFactory;




import webscrappers.SJPWebScrapper;
import webscrappers.auchan.AuchanAbstractScrapper;
import webscrappers.przepisy.PrzepisyPLQExtract;

public class ProduktAgent extends BaseAgent{

	static ArrayList<ProduktAgent> agents;
	private boolean busy=false;


//	@Autowired
//	private ProduktDAO produktDao;
	
	
	public static final String PARSER_NAME = "produktParser";

	private static final long serialVersionUID = 1L;
	public  static final boolean checkShops = true;


	public void setBusy(boolean busy) {
		this.busy = busy;
	}
	
	public boolean isBusy() {
		return busy;
	}


	@Override
	protected void live() {
		
		StringMessage m=(StringMessage) waitNextMessageKA();
		
		
		processMessage(m);
		
		while(true){
			pause(1000);
		}
	}

	private void processMessage(StringMessage m) {
		setBusy(true);
				
		
		
		
		
//		Message messageSentEarlier = getMessageSentEarlier(m.getConversationID());
//		
//		if(messageSentEarlier==null){
//			//TODO message from recipe parser
//
//		}else{
//			//message from shoplist with some results
//			JSONObject message=new JSONObject(m.getContent());
//			
//			
//			
//		}
		
		setBusy(false);
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
	
	}


	@Override
	protected void end() {
		// TODO Auto-generated method stub
		super.end();
	}


	public static List<Produkt> searchForProdukt(String searchPhrase){
		ProduktAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.busy=true;
			List<Produkt> result= freeOne.retrieveSkladnik(searchPhrase);
			freeOne.busy=false;
			return result;
		}
	}
	
	
	public static Produkt getOrScrapProdukt(String produktUrl){
		ProduktAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.busy=true;
			Produkt result= freeOne.getFromDbOrScrapProdukt(produktUrl);
			freeOne.busy=false;
			return result;
		}
	}
	
	
	private Produkt getFromDbOrScrapProdukt(String produktUrl) {
		
		
		String shortUrl=
				AuchanAbstractScrapper.getAuchanShortestWorkingUrl(produktUrl);
		
		Produkt produkt = getProduktFromDbByUrl(shortUrl);

		if(produkt==null){
			if(ParameterHolder.isCheckShops()){
				JSONObject json = new JSONObject();
				
				json.put(StringHolder.PRODUKT_URL_NAME, shortUrl);
				json.put(StringHolder.MESSAGE_CREATOR_NAME, PARSER_NAME);
				json.put(StringHolder.MESSAGE_TYPE_NAME, MessageTypes.GetProduktData);
//				json.put(StringHolder.MESSAGE_ID_NAME, MessageCounter.getCount());
				
				AgentAddress x=getAgentWithRole(StringHolder.AGENT_COMMUNITY, AGENT_GROUP, ShopsListAgent.SHOP_LIST_NAME);
		
				StringMessage messageToSend = new StringMessage(json.toString());
				
				
				
				
				StringMessage reply = (StringMessage) sendMessageWithRoleAndWaitForReplyKA(x, messageToSend,PARSER_NAME);
				
				
				JSONObject jsonResponse = new JSONObject(reply.getContent());
				
				String stringId=(String) jsonResponse.get("id");
				
				if(stringId==null||stringId.equals(""))
				{
					ProblemLogger.logProblem("For url "+produktUrl+"->"+shortUrl
							+" no produkt was found");
					return null;
				}
				try{
					Long id=Long.parseLong(stringId);
					produkt=DaoProvider.getInstance().getProduktDao().getById(id);

				}catch(NumberFormatException e){
					ProblemLogger.logProblem("For url "+produktUrl+"->"+shortUrl
							+" found ids are '"+stringId+"'");
				}
				
				
				
			}
		}
		return produkt;
	}


	public Produkt getProduktFromDbByUrl(String produktUrl) {
		return DaoProvider.getInstance().getProduktDao().getProduktsByURL(produktUrl);
	}


	private static ProduktAgent getFreeAgent() {
		ProduktAgent freeOne=null;
		
		if(agents==null){
			System.out.println("Agent system not started");
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

	
//	private QuantityProdukt retrieveProduktAmountData(Element e) {
//		// TODO Auto-generated method stub
//		String ingredient = e.text();
//		QuantityProdukt retValue =null;
//		
//
//		
//		retValue=AuchanQExtract.retrieveProduktAmountData(e);
//		return retValue;
//	}


	private String extractQuantity(Element e) {
		
		String quantity=e.parent().select(".quantity").text();
		
		
		if(quantity==null||quantity.equals(""))
			quantity=e.parent().parent().select(".quantity").text();
		return quantity;
	}

	private static QuantityProdukt retrieveQuantity(String quantity) {
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
			if(!Base_WordDAOImpl.niepoprawneSlowoBazowe.equals(baseWordsString))
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
				
				String ids=jsonObject.getString("ids");
				
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
