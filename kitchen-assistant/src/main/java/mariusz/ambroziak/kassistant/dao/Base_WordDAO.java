package mariusz.ambroziak.kassistant.dao;

import java.util.Collection;
import java.util.List;

import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.Variant_Word;

public interface Base_WordDAO {
	public List<Base_Word> list();
	public Base_Word getBase_Name(String name);
	public void addBase_Word(Base_Word bw);
	public void initializeVariantWords(Base_Word bw);
	Base_Word getBase_NameInitializedVar(String name);
	

}
