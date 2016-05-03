package mariusz.ambroziak.kassistant.dao;

import java.util.List;
import java.util.Map;

import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;

public interface NutrientDAO {
	public List<Nutrient> list();
	public Nutrient getNutrientByName(String name);
	public Map<Nutrient, Float> getNutrientsOfBasicIngredient(Long basicIngredientId);
	public Nutrient getNutrientById(Long nutrientId);
	public void saveNutrientData(Basic_Ingredient basicIngredient, Map<Nutrient, Float> nutrientsAmounts);
	public boolean areNutrientsForBasicIngredient(Long basicIngredientId);
	
}
