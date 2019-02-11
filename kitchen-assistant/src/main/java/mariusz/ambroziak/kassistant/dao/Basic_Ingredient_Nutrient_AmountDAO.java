package mariusz.ambroziak.kassistant.dao;

import java.util.List;
import java.util.Map;

import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;

public interface Basic_Ingredient_Nutrient_AmountDAO {
	public void saveNutrientData(Basic_Ingredient basicIngredient, Map<Nutrient, Float> nutrientsAmounts);
	public boolean areNutrientsForBasicIngredient(Long basicIngredientId);
	public boolean areNutrientsForBasicIngredient(String basicIngredientName);
	public Map<Nutrient, Float> getNutrientsForBasicIngredient(Long basicIngredientId);
	public Map<Nutrient, Float> getNutrientsForBasicIngredient(String basicIngredientId);
	
}
