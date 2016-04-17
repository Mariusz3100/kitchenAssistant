package mariusz.ambroziak.kassistant.dao;

import java.util.List;
import java.util.Map;

import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.utils.PreciseQuantity;

public interface NutrientDAO {
	public List<Nutrient> list();
	public Nutrient getNutrientByName(String name);
	public Map<Nutrient, PreciseQuantity> getNutrientsOfBasicIngredient(Long basicIngredientId);
	public void saveNutrientData(Long basicIngredientId,Map<Nutrient, PreciseQuantity> nutrientsAmounts);
	public Nutrient getNutrientById(Long nutrientId);
	
	
}
