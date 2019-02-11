package mariusz.ambroziak.kassistant.dao;

import java.util.List;
import java.util.Map;

import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient_Nutrient_Data_Source;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;

public interface Basic_Ingredient_Nutrient_Data_SourceDAO {
	public void saveDataSource(Basic_Ingredient_Nutrient_Data_Source dataSource);
	public Basic_Ingredient_Nutrient_Data_Source getDataSource(Long Basic_Ingredient_Nutrient_Data_Source_id);
	public Basic_Ingredient_Nutrient_Data_Source getDataSourceByBasicIngredientId(Basic_Ingredient Basic_Ingredient_id);
	
}
