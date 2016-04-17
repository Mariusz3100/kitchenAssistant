package mariusz.ambroziak.kassistant.dao;

import java.util.Collection;
import java.util.List;

import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;

public interface ProduktDAO {
	public List<Produkt> list();
	public Produkt getById(Long id);
	public Produkt getProduktsByURL(String url);
	public List<Produkt> getProduktsBySpacedName(String name);
	public void saveProdukt(Produkt produkt);
	public List<Produkt> getProduktsBySpacedVariantName(String name);
	public List<Produkt> getProduktsByNazwa(String nazwa);
	public List<Produkt> getProduktsByVariantNames(Collection<String> name);
	public List<Produkt> getProduktsByNames(Collection<String> parts);
	public List<Produkt> getProduktsByUrlILike(String url);
	

}
