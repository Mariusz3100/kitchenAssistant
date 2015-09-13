package mariusz.ambroziak.kassistant.dao;

import java.util.Collection;
import java.util.List;

import mariusz.ambroziak.kassistant.model.Amount_Type;
import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.Recipe;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.Variant_Word;

public interface Amount_TypeDAO {
	public List<Amount_Type> list();
	public Amount_Type getTypeByName(String typeName);
//	public void addRecipe(Recipe r);

	

}
