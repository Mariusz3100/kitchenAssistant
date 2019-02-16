package mariusz.ambroziak.kassistant.dao;

import java.util.List;
import java.util.Map;

import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient_Nutrient_Data_Source;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient_Nutrient_amount;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;

public interface Basic_Ingredient_Nutrient_AmountDAO {
	public void saveNutrientData(Basic_Ingredient basicIngredient, Basic_Ingredient_Nutrient_Data_Source binds, Map<Nutrient, Float> nutrientsAmounts);
	public boolean areNutrientsForBasicIngredient(Long basicIngredientId);
	public boolean areNutrientsForBasicIngredient(String basicIngredientName);
	public Map<Nutrient, Float> getNutrientsForBasicIngredientById(Long basicIngredientId);
	public Map<Nutrient, Float> getNutrientsForBasicIngredientByName(String basicIngredientId);
	public List<Basic_Ingredient_Nutrient_amount> getNutrientsForDataSourceById(Long basicIngredientId);

	
}
