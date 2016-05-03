package mariusz.ambroziak.kassistant.model.quantity;

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
		amount=-1;
		type=AmountTypes.szt;
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
