package mariusz.ambroziak.kassistant.agents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.message.StringMessage;
import mariusz.ambroziak.kassistant.dao.Base_WordDAOImpl;
import mariusz.ambroziak.kassistant.dao.DaoProvider;
import mariusz.ambroziak.kassistant.dao.ProduktDAO;
import mariusz.ambroziak.kassistant.exceptions.AgentSystemNotStartedException;
import mariusz.ambroziak.kassistant.exceptions.IncopatibleAmountTypesException;
import mariusz.ambroziak.kassistant.exceptions.InvalidArgumentException;
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.exceptions.ShopNotFoundException;
import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.jsp.SingleProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.NotPreciseQuantity;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.AbstractQuantity;
import mariusz.ambroziak.kassistant.model.utils.BasicIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithAllIngredients;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithBasicIngredients;
import mariusz.ambroziak.kassistant.model.utils.PreciseQuantityWithPhrase;
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

public class ReadingNutritientsApiAgent extends BaseAgent{

	//TODO update
//	static ArrayList<ReadingNutritientsApiAgent> agents;
//	public static final String PARSER_NAME = "apiNutritientsParser";
//
//	private static final long serialVersionUID = 1L;
//	public  static final boolean checkShops = true;
//
//
//	@Override
//	protected void live() {
//
//		while(true){
//			pause(1000000);
//		}
//	}
//
//
//
//	@Override
//	protected void pause(int milliSeconds) {
//		// TODO Auto-generated method stub
//		super.pause(milliSeconds);
//	}
//
//	@Override
//	protected void activate() {
//		if(!isGroup(AGENT_COMMUNITY,AGENT_GROUP)){
//			createGroup(AGENT_COMMUNITY,AGENT_GROUP);
//		}
//
//		requestRole(AGENT_COMMUNITY, AGENT_GROUP, PARSER_NAME);
//
//
//		if(agents==null)agents=new ArrayList<ReadingNutritientsApiAgent>();
//		agents.add(this);
//
//		setLogLevel(Level.FINEST);
//		super.activate();
//	}
//
//	public ReadingNutritientsApiAgent() {
//		super();
//		AGENT_COMMUNITY=StringHolder.AGENT_COMMUNITY;
//
//	}
//
//
//	@Override
//	protected void end() {
//		super.end();
//	}
//
//
//	public static ProduktWithAllIngredients parseFullSklad(String shortUrl)
//			throws AgentSystemNotStartedException, ShopNotFoundException, Page404Exception{
//		ReadingNutritientsApiAgent freeOne = getFreeAgent();
//		if(freeOne==null){
//			return null;
//		}else{
//			freeOne.setBusy(true);
//			ProduktWithAllIngredients result;
//			try{
//				result= freeOne.getAllFoodIngredients(shortUrl);
//			}finally{
//				freeOne.setBusy(false);
//			}
//			return result;
//		}
//	}
//
//	private ProduktWithAllIngredients getAllFoodIngredients(String shortUrl)
//			throws ShopNotFoundException, AgentSystemNotStartedException, Page404Exception {
//
//		ProduktWithAllIngredients produktWithIngredinets = AuchanRecipeParser.getAllIngredients(shortUrl);
//
//		return produktWithIngredinets;
//	}
//
//
//	private static ReadingNutritientsApiAgent getFreeAgent() throws AgentSystemNotStartedException {
//		ReadingNutritientsApiAgent freeOne=null;
//
//		if(agents==null){
//			System.out.println("Agent system not started");
//			throw new AgentSystemNotStartedException();
//		}
//		else{
//			while(freeOne==null){
//				for(ReadingNutritientsApiAgent ra:agents){
//					if(!ra.isBusy()){
//						freeOne=ra;
//					}
//				}
//				if(freeOne==null){
//					try {
//						System.out.println("Free RecipeParser Not Found");
//						Thread.sleep(2000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//		return freeOne;
//	}
//
//	public static Collection<BasicIngredientQuantity> getSumOfNutrients(Map<SingleProdukt_SearchResult, ProduktWithBasicIngredients> nutrients) {
//		Map<Basic_Ingredient,NotPreciseQuantity> retValue=new HashMap<Basic_Ingredient,NotPreciseQuantity>();
//		for( Entry<SingleProdukt_SearchResult, ProduktWithBasicIngredients> e:nutrients.entrySet()){
//			ProduktWithBasicIngredients value = e.getValue();
//			addBasicNutrientsToMap(value,retValue);
//		}
//		return getBasicIngredientQuantityCollection(retValue);
//	}
//
//	public static Collection<BasicIngredientQuantity> getSumOfAllBasicIngredients(Map<SingleProdukt_SearchResult, ProduktWithAllIngredients> nutrients) {
//		Map<Basic_Ingredient,NotPreciseQuantity> retValue=new HashMap<Basic_Ingredient,NotPreciseQuantity>();
//		for( Entry<SingleProdukt_SearchResult, ProduktWithAllIngredients> e:nutrients.entrySet()){
//			ProduktWithAllIngredients value = e.getValue();
//			ArrayList<BasicIngredientQuantity> allBasicIngredients = value.getProduktAsIngredient().getAllBasicIngredients();
//			addBasicNutrientsToMap(allBasicIngredients,retValue);
//		}
//		return getBasicIngredientQuantityCollection(retValue);
//	}
//	
//	private static void addBasicNutrientsToMap(ArrayList<BasicIngredientQuantity> allBasicIngredients,
//			Map<Basic_Ingredient, NotPreciseQuantity> mapWithSums) {
//		for(BasicIngredientQuantity biq:allBasicIngredients){
//			NotPreciseQuantity ingredientsQuantity = mapWithSums.get(biq.getBi());
//			if(ingredientsQuantity==null){
//				mapWithSums.put(biq.getBi(),biq.getAmount().getClone());
//			}else{
//				try {
//					ingredientsQuantity.add(biq.getAmount());
//				} catch (IncopatibleAmountTypesException e1) {
//					ProblemLogger.logProblem("There was a problem with summing up nutrients from: "+biq
//							+" to "+ingredientsQuantity);
//				} catch (InvalidArgumentException e1) {
//					e1.printStackTrace();
//				}
//			}
//			
//		}		
//	}
//
//	private static Collection<BasicIngredientQuantity> getBasicIngredientQuantityCollection(Map<Basic_Ingredient, NotPreciseQuantity> ingredientQuantitiesMap) {
//		Collection<BasicIngredientQuantity> retValue=new ArrayList<BasicIngredientQuantity>();
//		for(Entry<Basic_Ingredient, NotPreciseQuantity> e:ingredientQuantitiesMap.entrySet()){
//			BasicIngredientQuantity biq=new BasicIngredientQuantity(e.getKey(), e.getValue());
//			retValue.add(biq);
//		}
//		return retValue;
//	}
//
//	private static void addBasicNutrientsToMap(ProduktWithBasicIngredients valueToAdd,
//			Map<Basic_Ingredient, NotPreciseQuantity> mapWithSums) {
//		for(BasicIngredientQuantity biq:valueToAdd.getBasicsFromLabelTable()){
//			NotPreciseQuantity ingredientsQuantity = mapWithSums.get(biq.getBi());
//			if(ingredientsQuantity==null){
//				mapWithSums.put(biq.getBi(),biq.getAmount().getClone());
//			}else{
//				try {
//					ingredientsQuantity.add(biq.getAmount());
//				} catch (IncopatibleAmountTypesException e1) {
//					ProblemLogger.logProblem("There was a problem with summing up nutrients from: "+valueToAdd.getProdukt().getUrl()
//							+" to "+ingredientsQuantity);
//				} catch (InvalidArgumentException e1) {
//					e1.printStackTrace();
//				}
//			}
//			
//		}
//	}
//
//	public static Map<SingleProdukt_SearchResult, ProduktWithBasicIngredients> 
//		retrieveOrScrapBasicNutrientValues(ArrayList<SingleProdukt_SearchResult> goodResults) 
//			throws AgentSystemNotStartedException, ShopNotFoundException, Page404Exception{
//		Map<SingleProdukt_SearchResult,ProduktWithBasicIngredients> retValue=
//				new HashMap<SingleProdukt_SearchResult,ProduktWithBasicIngredients>();
//		for(SingleProdukt_SearchResult sr:goodResults){
//			ProduktWithBasicIngredients basics = ReadingNutritientsApiAgent.parseBasicSklad(sr.getProdukt().getUrl());
//			retValue.put(sr, basics);
//		}
//		return retValue;
//	}
//	
//	public static Map<SingleProdukt_SearchResult, ProduktWithAllIngredients> 
//	retrieveOrScrapAllNutrientValues(ArrayList<SingleProdukt_SearchResult> goodResults) 
//		throws AgentSystemNotStartedException, ShopNotFoundException, Page404Exception{
//	Map<SingleProdukt_SearchResult,ProduktWithAllIngredients> retValue=
//			new HashMap<SingleProdukt_SearchResult,ProduktWithAllIngredients>();
//	for(SingleProdukt_SearchResult sr:goodResults){
//		ProduktWithAllIngredients basics = ReadingNutritientsApiAgent.parseFullSklad(sr.getProdukt().getUrl());
//		retValue.put(sr, basics);
//	}
//	return retValue;
//}
//	
//	
//	public static ProduktWithBasicIngredients parseBasicSklad(String shortUrl)
//			throws AgentSystemNotStartedException, ShopNotFoundException, Page404Exception {
//		ReadingNutritientsApiAgent freeOne = getFreeAgent();
//		if(freeOne==null){
//			return null;
//		}else{
//			freeOne.setBusy(true);
//			ProduktWithBasicIngredients result;
//			try{
//				result= freeOne.getOrParseBasicFoodIngredients(shortUrl);
//			}finally{
//				freeOne.setBusy(false);
//			}
//			return result;
//		}
//	}
//
//	private ProduktWithBasicIngredients getOrParseBasicFoodIngredients(String shortUrl) throws Page404Exception {
//
//		return  AuchanRecipeParser.getBasics(shortUrl);
//	}
//



}
