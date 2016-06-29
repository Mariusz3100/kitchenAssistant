package mariusz.ambroziak.kassistant.model.utils;

import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.StringHolder;

public class PreciseQuantityWithPhrase {
	private String produktPhrase;
	private PreciseQuantity quan;

	public PreciseQuantity getQuan() {
		return quan;
	}
	public void setQuan(PreciseQuantity quan) {
		this.quan = quan;
	}
	public String getProduktPhrase() {
		return produktPhrase;
	}
	public PreciseQuantityWithPhrase(String produktPhrase, PreciseQuantity quan) {
		super();
		this.produktPhrase = produktPhrase;
		this.quan = quan;
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
	
	public PreciseQuantity getQuantity() {
		return quan;
	}
	public String getQuantityJspPhrase() {
		return getAmountType()+JspStringHolder.QUANTITY_PHRASE_BORDER+getAmount();
	}
	
	public String toString(){
		return getProduktPhrase()+"["+getQuantity()+"]";
	}
}
