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
import mariusz.ambroziak.kassistant.exceptions.Page404Exception;
import mariusz.ambroziak.kassistant.exceptions.ShopNotFoundException;
import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithAllIngredients;
import mariusz.ambroziak.kassistant.model.utils.NotPreciseQuantityWithPhrase;
import mariusz.ambroziak.kassistant.model.utils.PreciseQuantityWithPhrase;
import mariusz.ambroziak.kassistant.shops.ShopRecognizer;
import mariusz.ambroziak.kassistant.utils.MessageTypes;
import mariusz.ambroziak.kassistant.utils.ParameterHolder;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;
import mariusz.ambroziak.kassistant.utils.SkladnikiFinder;
import mariusz.ambroziak.kassistant.utils.StringHolder;

import org.json.JSONObject;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;


//import com.codesnippets4all.json.parsers.JsonParserFactory;















import webscrappers.SJPWebScrapper;

import webscrappers.przepisy.PrzepisyPLQExtract;

public class FoodIngredientAgent extends BaseAgent{

	static ArrayList<FoodIngredientAgent> agents;
	public static final String PARSER_NAME = "foodIngredientParser";

	private static final long serialVersionUID = 1L;
	public  static final boolean checkShops = true;




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
		
		setBusy(false);
	}


	@Override
	protected void pause(int milliSeconds) {
		
		super.pause(milliSeconds);
	}

	@Override
	protected void activate() {
		if(!isGroup(AGENT_COMMUNITY,AGENT_GROUP)){
			createGroup(AGENT_COMMUNITY,AGENT_GROUP);
		}
		
		requestRole(AGENT_COMMUNITY, AGENT_GROUP, PARSER_NAME);
		

		if(agents==null)agents=new ArrayList<FoodIngredientAgent>();
		agents.add(this);


		
		setLogLevel(Level.FINEST);
		super.activate();
	}

	public FoodIngredientAgent() {
		super();
		AGENT_COMMUNITY=StringHolder.AGENT_COMMUNITY;
	
	}


	@Override
	protected void end() {
		super.end();
	}


	public static Map<Nutrient, PreciseQuantity> parseFoodIngredient(String name)
			throws AgentSystemNotStartedException{
		FoodIngredientAgent freeOne = getFreeAgent();
		if(freeOne==null){
			return null;
		}else{
			freeOne.setBusy(true);
			Map<Nutrient, PreciseQuantity> result;
			try{
				result= freeOne.retrieveFromDbOrParseNutrientsOf(name);
			}finally{
				freeOne.setBusy(false);
			}
			return result;
		}
	}

	private Map<Nutrient, PreciseQuantity> retrieveFromDbOrParseNutrientsOf(String ingredientPhrase) {
		//TODO update
		return null;
		
		//		String name=SkladnikiFinder.getBaseName(ingredientPhrase);
//		Basic_Ingredient basicIngredientFromDb = SkladnikiFinder.findBasicIngredientInDbByBaseName(name);
//		boolean nutrientsSurelyNotInDb=false;
//		if(basicIngredientFromDb==null){
//			nutrientsSurelyNotInDb=true;
//			basicIngredientFromDb=new Basic_Ingredient();
//			basicIngredientFromDb.setName(name);
//			DaoProvider.getInstance().getBasicIngredientDao().saveBasicIngredient(basicIngredientFromDb);
//		}
//		if(!nutrientsSurelyNotInDb){
//			nutrientsSurelyNotInDb=!DaoProvider.getInstance().getNutrientDao()
//					.areNutrientsForBasicIngredient(basicIngredientFromDb.getBi_id());
//		}
//		
//		Map<Nutrient, PreciseQuantity> retValue=null;
//		if(nutrientsSurelyNotInDb){
//			HashMap<Nutrient, PreciseQuantity> scrapedNutritientData = JedzDobrzeScrapper.scrapSkladnik(basicIngredientFromDb.getName());
//			saveInDb(basicIngredientFromDb,scrapedNutritientData);
//			retValue=scrapedNutritientData;
//		}else{
//			retValue=retrieveFromDbNutrientDataFor(basicIngredientFromDb);
//		}
//		
//		return retValue;
	}

	private Map<Nutrient, PreciseQuantity> retrieveFromDbNutrientDataFor(Basic_Ingredient basicIngredientFromDb) {
		Map<Nutrient, Float> nutrientsOfBasicIngredientFromDb = DaoProvider.getInstance().getNutrientDao().getNutrientsForBasicIngredient(basicIngredientFromDb.getBi_id());
		Map<Nutrient, PreciseQuantity> retValue=new HashMap<Nutrient, PreciseQuantity>();
		
		if(nutrientsOfBasicIngredientFromDb==null||nutrientsOfBasicIngredientFromDb.isEmpty())
			return retValue;
		
		for(Entry<Nutrient, Float>  e:nutrientsOfBasicIngredientFromDb.entrySet())
		{
			Float value = e.getValue();
			PreciseQuantity amountPer100g=new PreciseQuantity(Math.round(value*100*1000), AmountTypes.mg);
			
			
			retValue.put(e.getKey(), amountPer100g);
		}
		
		
		return retValue;
	}



	private HashMap<Nutrient, Float> getRelativeAmountsFromPreciseQuantityFor100g(
			HashMap<Nutrient, PreciseQuantity> scrapedNutritientData) {
		HashMap<Nutrient, Float> retValue=new HashMap<Nutrient, Float>();
		
		if(scrapedNutritientData==null||scrapedNutritientData.isEmpty())
			return retValue;
		
		for(Entry<Nutrient, PreciseQuantity>  e:scrapedNutritientData.entrySet())
		{
			PreciseQuantity value = e.getValue();
			
			if(!AmountTypes.mg.equals(value.getType())&&!AmountTypes.kalorie.equals(value.getType())){
				ProblemLogger.logProblem(
						"Proba zapisywania w bazie innego typu ilosci skladnika odzywczego niz mg.");
			}else{
				
				
				float coefficient=value.getAmount()/(100*1000);
				
				retValue.put(e.getKey(), coefficient);
			}
			
		}
		
		return retValue;
	}

	private static FoodIngredientAgent getFreeAgent() throws AgentSystemNotStartedException {
		FoodIngredientAgent freeOne=null;
		
		if(agents==null){
			System.out.println("Agent system not started");
			throw new AgentSystemNotStartedException();
		}
		else{
			while(freeOne==null){
				for(FoodIngredientAgent ra:agents){
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

	


}
