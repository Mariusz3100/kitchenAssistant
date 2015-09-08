package mariusz.ambroziak.kassistant.dao;

import java.util.Collection;
import java.util.List;

import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.Variant_Word;

public interface Variant_WordDAO {
	public List<Variant_Word> list();
	public List<Variant_Word> getVariant_Name(String name);
	public Base_Word getBase_Name(String name);
	public List<Base_Word> getBase_Names(Collection<String> names);
	public List<String> getBase_NamesAsStrings(Collection<String> names);
	public void addVariant_word(Variant_Word vw);
	public void addRelation(String variantWord,String baseWord);

	

}
