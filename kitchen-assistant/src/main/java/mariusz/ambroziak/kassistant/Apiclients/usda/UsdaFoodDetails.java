package mariusz.ambroziak.kassistant.Apiclients.usda;

import java.util.HashMap;
import java.util.Map;

import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;

public class UsdaFoodDetails {
	public UsdaFoodDetails(UsdaFoodId id, Map<Nutrient, PreciseQuantity> nutrietsMapPer100g) {
		super();
		this.id = id;
		this.nutrietsMapPer100g = nutrietsMapPer100g;
	}
	private UsdaFoodId id;
	private Map<Nutrient, PreciseQuantity> nutrietsMapPer100g;

	public UsdaFoodId getId() {
		return id;
	}
	public void setId(UsdaFoodId id) {
		this.id = id;
	}
	public Map<Nutrient, PreciseQuantity> getNutrietsMapPer100g() {
		return nutrietsMapPer100g;
	}
	public void setNutrietsMapPer100g(Map<Nutrient, PreciseQuantity> nutrietsMap) {
		this.nutrietsMapPer100g = nutrietsMap;
	}

	public static UsdaFoodDetails getEmptyOne(){
		return new UsdaFoodDetails(null, new HashMap<>());
	}

	public boolean isEmpty() {
		return this.getId()==null&&(this.getNutrietsMapPer100g()==null||this.getNutrietsMapPer100g().size()==0);
				
	}
}
