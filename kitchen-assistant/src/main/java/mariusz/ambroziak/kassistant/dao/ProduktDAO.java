package mariusz.ambroziak.kassistant.dao;

import java.util.List;

import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;

public interface ProduktDAO {
	public List<Produkt> list();
	public List<Produkt> getProduktsByURL(String url);
	public List<Produkt> getProduktsBySpacedName(String name);
	public void addProdukt(Produkt produkt);
	

}
