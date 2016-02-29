package mariusz.ambroziak.kassistant.model.utils;

import mariusz.ambroziak.kassistant.QuantityExtractor.AmountTypes;

public class QuantityProdukt {
	private String produktPhrase;
	private PreciseQuantity quan;

	public String getProduktPhrase() {
		return produktPhrase;
	}
	public void setProduktPhrase(String produktName) {
		this.produktPhrase = produktName;
	}
	public AmountTypes getAmountType() {
		return quan.type;
	}
	public void setAmountType(AmountTypes amountType) {
		if(quan==null)
			quan=new PreciseQuantity();
		this.quan.type = amountType;
	}
	public float getAmount() {
		return quan.getAmount();
	}
	public void setAmount(float amount) {
		if(quan==null)
			quan=new PreciseQuantity();
		this.quan.setAmount(amount);
	}
	
	public String getQuantityPhrase() {
		return getAmountType()+"_"+getAmount();
	}
	
}
