package mariusz.ambroziak.kassistant.dao;

import java.util.Collection;
import java.util.List;

import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.Recipe_Ingredient;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.Variant_Word;

public interface Recipe_IngredientDAO {
	public List<Recipe_Ingredient> list();
	public void addRecipe_Ingredient(Recipe_Ingredient vw);
//	public void addIngredientAndAmount(Recipe_Ingredient ri,String amountType);
	public List<Recipe_Ingredient> getByRecID(Long recID);

	

}
