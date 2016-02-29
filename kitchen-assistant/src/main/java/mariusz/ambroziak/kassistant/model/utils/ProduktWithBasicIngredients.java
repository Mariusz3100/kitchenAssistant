package mariusz.ambroziak.kassistant.model.utils;

import java.util.ArrayList;
import java.util.Map;

import webscrappers.auchan.ProduktDetails;
import mariusz.ambroziak.kassistant.model.Produkt;

public class ProduktWithBasicIngredients {

	private ProduktDetails produkt;
	

	private ArrayList<BasicIngredientQuantity> basicsFromLabel;
	
	


	public ProduktDetails getProdukt() {
		return produkt;
	}




	public ProduktWithBasicIngredients(ProduktDetails produkt,
			ArrayList<BasicIngredientQuantity> basicsFor100g) {
		super();
		this.produkt = produkt;
		this.basicsFromLabel = basicsFor100g;
	}






	public ArrayList<BasicIngredientQuantity> getBasicsFor100g() {
		return basicsFromLabel;
	}


	
}
