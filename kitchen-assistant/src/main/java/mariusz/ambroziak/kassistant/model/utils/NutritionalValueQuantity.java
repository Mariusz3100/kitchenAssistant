package mariusz.ambroziak.kassistant.model.utils;

public class NutritionalValueQuantity {
	private NutritionalValueType type;
	private float amount;
	
	
	public NutritionalValueType getType() {
		return type;
	}

	public void setType(NutritionalValueType type) {
		this.type = type;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	public NutritionalValueQuantity(NutritionalValueType type, float amount) {
		super();
		this.type = type;
		this.amount = amount;
	}
	
}
