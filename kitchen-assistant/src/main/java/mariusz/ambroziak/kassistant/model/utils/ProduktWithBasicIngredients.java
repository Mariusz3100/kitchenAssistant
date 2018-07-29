package mariusz.ambroziak.kassistant.model.utils;

import java.util.ArrayList;
import java.util.Map;

import webscrappers.auchan.ProduktDetails;
import mariusz.ambroziak.kassistant.model.Produkt;

public class ProduktWithBasicIngredients {
	private ProduktDetails produkt;
	
	//kcal,proteins, fats and so on, from label
	private ArrayList<BasicIngredientQuantity> basicsFromLabelTable;

	public ProduktDetails getProdukt() {
		return produkt;
	}

	public ProduktWithBasicIngredients(ProduktDetails produkt,
			ArrayList<BasicIngredientQuantity> basicsFor100g) {
		super();
		this.produkt = produkt;
		this.basicsFromLabelTable = basicsFor100g;
	}

	public ArrayList<BasicIngredientQuantity> getBasicsFromLabelTable() {
		return basicsFromLabelTable;
	}


	
}
