package mariusz.ambroziak.kassistant.model.utils;

import mariusz.ambroziak.kassistant.utils.AmountTypes;

public class QuantityProdukt {
	private String produktPhrase;
	private AmountTypes amountType;
	private float amount;

	public String getProduktPhrase() {
		return produktPhrase;
	}
	public void setProduktPhrase(String produktName) {
		this.produktPhrase = produktName;
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
}
