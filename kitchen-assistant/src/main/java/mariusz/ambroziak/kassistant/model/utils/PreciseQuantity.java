package mariusz.ambroziak.kassistant.model.utils;

import mariusz.ambroziak.kassistant.QuantityExtractor.AmountTypes;

public class PreciseQuantity extends AbstractQuantity {
	private float amount;

	public PreciseQuantity(float amount, AmountTypes type) {
		super();
		this.amount = amount;
		this.type = type;
	}
	public PreciseQuantity() {
		super();
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	
	public String toString(){
		return amount+" "+type;
	}
}
