package mariusz.ambroziak.kassistant.model.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;

public class NutrientDetailsOfBasicIngredient {
	Basic_Ingredient basicIngredient;
	Map<Nutrient,Float> nutrientsMap;
	public Basic_Ingredient getBasicIngredient() {
		return basicIngredient;
	}
	public void setBasicIngredient(Basic_Ingredient basicIngredient) {
		this.basicIngredient = basicIngredient;
	}
	public Map<Nutrient, Float> getNutrientsMap() {
		return nutrientsMap;
	}
	public void setNutrientsMap(Map<Nutrient, Float> nutrientsMap) {
		this.nutrientsMap = nutrientsMap;
	}
	public NutrientDetailsOfBasicIngredient(Basic_Ingredient basicIngredient, Map<Nutrient, Float> nutrientsMap) {
		super();
		this.basicIngredient = basicIngredient;
		this.nutrientsMap = nutrientsMap;
	}


	
	public NutrientDetailsOfBasicIngredient() {
		super();
	}
	public void setNutrientsMapFromMapWithPreciseQuantityValues(Map<Nutrient, PreciseQuantity> map) {
		this.nutrientsMap=new HashMap<Nutrient, Float>();
		if(map!=null&&!map.isEmpty())
		{
			for(Entry<Nutrient,PreciseQuantity> e:map.entrySet()) {
				this.nutrientsMap.put(e.getKey(), e.getValue()==null?0:e.getValue().getAmount());
			}
		}
		
	}
	public boolean isEmpty() {
		return this.getBasicIngredient()==null&&(this.getNutrientsMap()==null||this.getNutrientsMap().size()==0);
				
	}
	
	
}
