package mariusz.ambroziak.kassistant.Apiclients.usda;

import java.util.HashMap;
import java.util.Map;

import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;

public class UsdaFoodDetails {
	public UsdaFoodDetails(UsdaFoodId id, Map<Nutrient, PreciseQuantity> nutrietsMap) {
		super();
		this.id = id;
		this.nutrietsMap = nutrietsMap;
	}
	private UsdaFoodId id;
	private Map<Nutrient, PreciseQuantity> nutrietsMap;

	public UsdaFoodId getId() {
		return id;
	}
	public void setId(UsdaFoodId id) {
		this.id = id;
	}
	public Map<Nutrient, PreciseQuantity> getNutrietsMap() {
		return nutrietsMap;
	}
	public void setNutrietsMap(Map<Nutrient, PreciseQuantity> nutrietsMap) {
		this.nutrietsMap = nutrietsMap;
	}

	public static UsdaFoodDetails getEmptyOne(){
		return new UsdaFoodDetails(null, new HashMap<>());
	}

}
