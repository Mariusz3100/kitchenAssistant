package mariusz.ambroziak.kassistant.model.utils;

import java.util.ArrayList;
import java.util.Map;

import webscrappers.auchan.ProduktDetails;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.Produkt_Ingredient;

public class CompoundIngredient {

	private String name;
	private Map<QuantityProdukt,ArrayList<CompoundIngredient>> compoundIngredients;
	
	private Map<QuantityProdukt,ArrayList<Produkt_Ingredient>> baseIngredients;

	public CompoundIngredient(
			String name,
			Map<QuantityProdukt, ArrayList<CompoundIngredient>> compoundIngredients,
			Map<QuantityProdukt, ArrayList<Produkt_Ingredient>> baseIngredients) {
		super();
		this.name = name;
		this.compoundIngredients = compoundIngredients;
		this.baseIngredients = baseIngredients;
	}

	public String getName() {
		return name;
	}

	public Map<QuantityProdukt, ArrayList<CompoundIngredient>> getCompoundIngredients() {
		return compoundIngredients;
	}

	public Map<QuantityProdukt, ArrayList<Produkt_Ingredient>> getBaseIngredients() {
		return baseIngredients;
	}
	
	


	 
	
}
