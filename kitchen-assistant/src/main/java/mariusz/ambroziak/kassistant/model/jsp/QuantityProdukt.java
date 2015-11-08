package mariusz.ambroziak.kassistant.model.jsp;

import mariusz.ambroziak.kassistant.utils.AmountTypes;

public class QuantityProdukt {
	private String produktName;
	private AmountTypes amountType;
	public String getProduktName() {
		return produktName;
	}
	public void setProduktName(String produktName) {
		this.produktName = produktName;
	}
	public AmountTypes getAmountType() {
		return amountType;
	}
	public void setAmountType(AmountTypes amountType) {
		this.amountType = amountType;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	private float amount;
}
