package mariusz.ambroziak.kassistant.model.utils;

import java.util.ArrayList;
import java.util.Map;

import webscrappers.auchan.ProduktDetails;
import mariusz.ambroziak.kassistant.model.Produkt;

public class ProduktWithIngredients {

	private ProduktDetails produkt;
	
	private ArrayList<ProduktIngredientQuantity> parsedBasicIngredients;
	
	
	private ArrayList<NutritionalValueQuantity> basicsfor100g;


	
	public Map<ProduktIngredientQuantity, ArrayList<ProduktIngredientQuantity>> getParsedIngredients() {
		return parsedIngredients;
	}




	public ArrayList<ProduktIngredientQuantity> getParsedBasicIngredients() {
		return parsedBasicIngredients;
	}




	public ProduktDetails getProdukt() {
		return produkt;
	}




	public ArrayList<NutritionalValueQuantity> getBasicsFor100g() {
		return basicsFor100g;
	}

	public ProduktWithIngredients(
			ProduktDetails produkt,
			Map<ProduktIngredientQuantity, ArrayList<ProduktIngredientQuantity>> ingredients,
			ArrayList<ProduktIngredientQuantity> basicIngredients,
			ArrayList<NutritionalValueQuantity> basicsFor100g) {
		super();
		this.produkt = produkt;
		this.parsedIngredients = ingredients;
		this.parsedBasicIngredients = basicIngredients;
		this.basicsFor100g = basicsFor100g;
	}
	
	public ProduktWithIngredients(ProduktDetails produkt,ArrayList<NutritionalValueQuantity> basicsFor100g2) {
		super();
		this.produkt = produkt;

		this.basicsFor100g = basicsFor100g2;
	}
	 
	
}
