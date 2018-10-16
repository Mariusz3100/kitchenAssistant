package mariusz.ambroziak.kassistant.model.utils;

import java.util.ArrayList;
import java.util.Map;

import webscrappers.auchan.ProduktDetails;
import mariusz.ambroziak.kassistant.model.Produkt;

public class ProduktWithAllIngredients extends ProduktWithBasicIngredients{

	
	private CompoundIngredientQuantity ProduktAsIngredient;

	public ProduktWithAllIngredients(ProduktDetails produkt,
			CompoundIngredientQuantity produktAsIngredient,
			ArrayList<BasicIngredientQuantity> basicsFor100g) {
		super(produkt,basicsFor100g);
		this.ProduktAsIngredient = produktAsIngredient;
		
	}




	public CompoundIngredientQuantity getProduktAsIngredient() {
		return ProduktAsIngredient;
	}









	
}
