package mariusz.ambroziak.kassistant.model.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;

public class NutrientDetailsOfBasicIngredient {
	private Basic_Ingredient basicIngredient;
	private Map<Nutrient,Float> nutrientsPer100gMap;
	public Basic_Ingredient getBasicIngredient() {
		return basicIngredient;
	}
	public void setBasicIngredient(Basic_Ingredient basicIngredient) {
		this.basicIngredient = basicIngredient;
	}
	public Map<Nutrient, Float> getNutrientsPer100gMap() {
		return nutrientsPer100gMap;
	}
	public void setNutrientsPer100gMap(Map<Nutrient, Float> nutrientsMap) {
		this.nutrientsPer100gMap = nutrientsMap;
	}
	public NutrientDetailsOfBasicIngredient(Basic_Ingredient basicIngredient, Map<Nutrient, Float> nutrientsPer100gMap) {
		super();
		this.basicIngredient = basicIngredient;
		this.nutrientsPer100gMap = nutrientsPer100gMap;
	}



	public NutrientDetailsOfBasicIngredient() {
		super();
	}
	public void setNutrientsMapFromMapWithPreciseQuantityValues(Map<Nutrient, PreciseQuantity> map) {
		this.nutrientsPer100gMap=new HashMap<Nutrient, Float>();
		if(map!=null&&!map.isEmpty())
		{
			for(Entry<Nutrient,PreciseQuantity> e:map.entrySet()) {
				this.nutrientsPer100gMap.put(e.getKey(), e.getValue()==null?0:e.getValue().getAmount());
			}
		}

	}

	public Map<Nutrient, PreciseQuantity> getNutrientsMapOfPreciseQuantityValues() {
		Map<Nutrient, PreciseQuantity> retValue=new HashMap<>();

		if(this.getNutrientsPer100gMap()==null)
			return retValue;

		for(Entry<Nutrient, Float> e:getNutrientsPer100gMap().entrySet()) {
			retValue.put(e.getKey(),new PreciseQuantity(e.getValue(),AmountTypes.mg));
		}
		return retValue;

	}
	public boolean isEmpty() {
		return this.getBasicIngredient()==null&&(this.getNutrientsPer100gMap()==null||this.getNutrientsPer100gMap().size()==0);

	}


}
