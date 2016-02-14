package mariusz.ambroziak.kassistant.model.utils;

import mariusz.ambroziak.kassistant.model.Produkt_Ingredient;
import mariusz.ambroziak.kassistant.utils.AmountTypes;

public class ProduktIngredientQuantity {
	private Produkt_Ingredient pi;
	private float amount;
	private AmountTypes at;

	
	public ProduktIngredientQuantity(Produkt_Ingredient fi, float amount,
			AmountTypes at) {
		super();
		this.pi = fi;
		this.amount = amount;
		this.at = at;
	}
	
	public ProduktIngredientQuantity(Produkt_Ingredient fi, float amount) {
		super();
		this.pi = fi;
		this.amount = amount;
		this.at = AmountTypes.mg;
	}
	
	public Produkt_Ingredient getFi() {
		return pi;
	}
	public void setFi(Produkt_Ingredient fi) {
		this.pi = fi;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public AmountTypes getAt() {
		return at;
	}
	public void setAt(AmountTypes at) {
		this.at = at;
	}
	
	
	
	
}
