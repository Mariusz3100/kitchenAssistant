package mariusz.ambroziak.kassistant.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.runtime.Inner;

import java.util.Map.Entry;

import mariusz.ambroziak.kassistant.exceptions.IncopatibleAmountTypesException;
import mariusz.ambroziak.kassistant.exceptions.InvalidArgumentException;
import mariusz.ambroziak.kassistant.model.jsp.SingleProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.quantity.NotPreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.BasicIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithBasicIngredients;

public class CompoundMapManipulator <OuterMapKey,InnerMapKey>{

	private Map<OuterMapKey,Map<InnerMapKey,NotPreciseQuantity>> compoundMap;
	
	
	public CompoundMapManipulator(Map<OuterMapKey,Map<InnerMapKey,NotPreciseQuantity>> compoundMap){
		this.compoundMap=compoundMap;
	}
	
	public Set<InnerMapKey> getAllInnerMapsKeys() {
		Set<InnerMapKey> setOfAllNutrients=new HashSet<InnerMapKey>();

		for(Map<InnerMapKey, NotPreciseQuantity> innerMap:compoundMap.values())
			setOfAllNutrients.addAll(innerMap.keySet());

		return setOfAllNutrients;
	}

	
	public Map<InnerMapKey, NotPreciseQuantity> sumUpInnerMaps() {
		Map<InnerMapKey, NotPreciseQuantity> retValue=new HashMap<InnerMapKey, NotPreciseQuantity>();
		for(Entry<OuterMapKey, Map<InnerMapKey, NotPreciseQuantity>> outerMapEntry:compoundMap.entrySet()){

			for(Entry<InnerMapKey, NotPreciseQuantity> amountOfSingleNutrient:outerMapEntry.getValue().entrySet()){
				NotPreciseQuantity sumForNutrient=retValue.get(amountOfSingleNutrient.getKey());

				if(sumForNutrient==null){
					sumForNutrient=amountOfSingleNutrient.getValue().getClone();
					retValue.put(amountOfSingleNutrient.getKey(),sumForNutrient);
				}else{
					addToSumHandleExceptions(amountOfSingleNutrient, sumForNutrient);
				}
			}
		}
		return retValue;
	}
	
	private void addToSumHandleExceptions(Entry<InnerMapKey, NotPreciseQuantity> amountOfSingleNutrient,
			NotPreciseQuantity sumForNutrient) {
		try {
			sumForNutrient.add(amountOfSingleNutrient.getValue());
		} catch (IncopatibleAmountTypesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			ProblemLogger.logProblem("There was a problem with summing up nutrients from: "+amountOfSingleNutrient.getValue()
			+" to "+sumForNutrient);
			ProblemLogger.logStackTrace(e.getStackTrace());
		}
	}
	
	public static Map<String, Map<String, NotPreciseQuantity>> getProduktToNutrientToAmountMap(
			Map<SingleProdukt_SearchResult, ProduktWithBasicIngredients> retrievedBasicNutrientValues) {
		//IngredientName->(NutrientName->amount)
		Map<String, Map<String,NotPreciseQuantity>> amountsMap=new HashMap<String, Map<String,NotPreciseQuantity>>();

		for(Entry<SingleProdukt_SearchResult, ProduktWithBasicIngredients> e:retrievedBasicNutrientValues.entrySet()){
			Map<String, NotPreciseQuantity> produktNutrients = amountsMap.get(e.getKey());
			if(produktNutrients==null){
				produktNutrients=new HashMap<String,NotPreciseQuantity>();
				amountsMap.put(e.getKey().getProduktNameAndSearchPhrase(),produktNutrients );
			}

			for(BasicIngredientQuantity biq:e.getValue().getBasicsFromLabelTable()){
				produktNutrients.put(biq.getName(),biq.getAmount());
			}

		}
		return amountsMap;
	}

	
}
