package mariusz.ambroziak.kassistant.model.utils;

import mariusz.ambroziak.kassistant.QuantityExtractor.AmountTypes;

public class PreciseQuantity extends NotPreciseQuantity {
	private float amount;

	@Override
	public float getMinimalAmount() {
		return getAmount();
	}
	@Override
	public float getMaximalAmount() {
		 return getAmount();
	}
	public PreciseQuantity(float amount, AmountTypes type) {
		super();
		this.amount = amount;
		this.type = type;
		this.setMaximalAmount(amount);
		this.setMinimalAmount(amount);
		
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
