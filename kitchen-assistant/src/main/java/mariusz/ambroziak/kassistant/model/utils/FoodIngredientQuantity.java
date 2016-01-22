package mariusz.ambroziak.kassistant.model.utils;

import mariusz.ambroziak.kassistant.model.Food_Ingredient;
import mariusz.ambroziak.kassistant.utils.AmountTypes;

public class FoodIngredientQuantity {
	private Food_Ingredient fi;
	private float amount;
	private AmountTypes at;

	
	public FoodIngredientQuantity(Food_Ingredient fi, float amount,
			AmountTypes at) {
		super();
		this.fi = fi;
		this.amount = amount;
		this.at = at;
	}
	
	public FoodIngredientQuantity(Food_Ingredient fi, float amount) {
		super();
		this.fi = fi;
		this.amount = amount;
		this.at = AmountTypes.mg;
	}
	
	public Food_Ingredient getFi() {
		return fi;
	}
	public void setFi(Food_Ingredient fi) {
		this.fi = fi;
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
