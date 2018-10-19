package mariusz.ambroziak.kassistant.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.runtime.Inner;

import java.util.Map.Entry;

import mariusz.ambroziak.kassistant.Apiclients.usda.UsdaNutrientApiClient;
import mariusz.ambroziak.kassistant.exceptions.IncopatibleAmountTypesException;
import mariusz.ambroziak.kassistant.exceptions.InvalidArgumentException;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.jsp.SingleProdukt_SearchResult;
import mariusz.ambroziak.kassistant.model.quantity.NotPreciseQuantity;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.model.utils.BasicIngredientQuantity;
import mariusz.ambroziak.kassistant.model.utils.GoodBadSkippedResults;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithAllNutrients;
import mariusz.ambroziak.kassistant.model.utils.ProduktWithBasicIngredients;

public class CompoundMapManipulator <OuterMapKey,InnerMapKey>{

	private Map<OuterMapKey,Map<InnerMapKey,NotPreciseQuantity>> compoundMap;
	
	
	
	public CompoundMapManipulator(Map<OuterMapKey,Map<InnerMapKey,NotPreciseQuantity>> compoundMap){
		this.compoundMap=compoundMap;
	}
	
	public CompoundMapManipulator(){
		
	}
	
	public Map<OuterMapKey, Map<InnerMapKey, NotPreciseQuantity>> getCompoundMap() {
		return compoundMap;
	}

	public void setCompoundMap(Map<OuterMapKey, Map<InnerMapKey, NotPreciseQuantity>> compoundMap) {
		this.compoundMap = compoundMap;
	}

	public void setFromDifferentCompoundMap(Map<OuterMapKey, Map<InnerMapKey, PreciseQuantity>> compoundMap) {
		this.compoundMap = preciseToNotPreciseQuantity(compoundMap);
		
	}

	public Map<OuterMapKey,Map<InnerMapKey,NotPreciseQuantity>> preciseToNotPreciseQuantity(Map<OuterMapKey,Map<InnerMapKey,PreciseQuantity>> map){
		Map<OuterMapKey,Map<InnerMapKey,NotPreciseQuantity>> retValue=new HashMap<OuterMapKey, Map<InnerMapKey,NotPreciseQuantity>>(map.size());
		for(OuterMapKey omKey:map.keySet()){
			Map<InnerMapKey, PreciseQuantity> outerMapValue = map.get(omKey);
			Map<InnerMapKey,NotPreciseQuantity> innerRetMap=new HashMap<InnerMapKey, NotPreciseQuantity>();
			for(InnerMapKey innerMapKey:outerMapValue.keySet()){
				innerRetMap.put(innerMapKey, outerMapValue.get(innerMapKey));
			}
			retValue.put(omKey, innerRetMap);
		}
		return retValue;
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

	public static Map<String, Map<String, NotPreciseQuantity>> stringifyKeys(
			Map<SingleProdukt_SearchResult, Map<Nutrient, PreciseQuantity>> retrievedNutrientDataForProdukts) {
		Map<String, Map<String, NotPreciseQuantity>>  retValue=new HashMap<String, Map<String,NotPreciseQuantity>>();
		
		for(SingleProdukt_SearchResult sp_sr:retrievedNutrientDataForProdukts.keySet()) {
			Map<Nutrient, PreciseQuantity> map = retrievedNutrientDataForProdukts.get(sp_sr);
			Map<String, NotPreciseQuantity> transformedMap=new HashMap<String, NotPreciseQuantity>();
			
			for(Nutrient nutrient:map.keySet()) {
				transformedMap.put(nutrient.getName(), map.get(nutrient));
			}
			retValue.put(sp_sr.getProdukt().getNazwa(), transformedMap);
			
			
		}
		
		return retValue;
	}
	
	public Map<String, Map<String, NotPreciseQuantity>> getSumOfNutrientsForWholeProdukt(
			Map<SingleProdukt_SearchResult, ProduktWithAllNutrients> originalMap) {
		//IngredientName->(FoodIngredientName->amount)
		Map<String, Map<String, NotPreciseQuantity>> amountsMap=new HashMap<String, Map<String,NotPreciseQuantity>>();

		Set<Entry<SingleProdukt_SearchResult, ProduktWithAllNutrients>> entrySet = originalMap.entrySet();
		Iterator<Entry<SingleProdukt_SearchResult, ProduktWithAllNutrients>> iterator = entrySet.iterator();
		while(iterator.hasNext()){
			Entry<SingleProdukt_SearchResult, ProduktWithAllNutrients> entry = iterator.next();
			Map<String, NotPreciseQuantity> nutrientsSum = amountsMap.get(entry.getKey().getProdukt().getNazwa());
			nutrientsSum=initializeMapIfEmpty(nutrientsSum,amountsMap, entry.getKey());
			
			for(Entry<Nutrient,PreciseQuantity>nutrientAmount:originalMap.get(entry.getKey()).getBasicsFromLabelTable().entrySet()){
				try {
					nutrientsSum.get(nutrientAmount.getKey().getName()).add(nutrientAmount.getValue());
				} catch (IncopatibleAmountTypesException | InvalidArgumentException e) {
					e.printStackTrace();
				}
			}
			
			
		}
		return amountsMap;
	}

	private Map<String, NotPreciseQuantity> initializeMapIfEmpty(Map<String, NotPreciseQuantity> produktNutrients,
			Map<String, Map<String, NotPreciseQuantity>> amountsMap,
			SingleProdukt_SearchResult key1) {
		if(produktNutrients==null){
			Map<String, NotPreciseQuantity> mapOutOfList = new HashMap<String, NotPreciseQuantity>();
			amountsMap.put(key1.getProdukt().getNazwa(),mapOutOfList);
			produktNutrients=mapOutOfList;
		}
		return produktNutrients;

	}

	
	
}
