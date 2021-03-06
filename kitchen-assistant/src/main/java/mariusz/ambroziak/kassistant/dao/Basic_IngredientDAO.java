package mariusz.ambroziak.kassistant.dao;

import java.util.Collection;
import java.util.List;

import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.Variant_Word;

public interface Basic_IngredientDAO {
	public List<Basic_Ingredient> list();
	public Basic_Ingredient getIngredientByName(String name);
	public Basic_Ingredient getIngredientByAnyName(String name);
	public Basic_Ingredient getIngredientById(Long id);
	public List<Basic_Ingredient> getIngredientByNames(Collection<String> name);
	public List<Basic_Ingredient> getIngredientBySpacedName(String phrase);
	public boolean checkIfRecordExistsUpdateArgumentIfNeedBe(Basic_Ingredient bi);
	public void saveBasicIngredient(Basic_Ingredient bi);
	
}
