package mariusz.ambroziak.kassistant.dao;

import java.util.Collection;
import java.util.List;

import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.Recipe;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.Variant_Word;

public interface RecipeDAO {
	public List<Recipe> list();
	public Recipe getRecipeByURL(String url);
	public void addRecipe(Recipe r);

	

}
