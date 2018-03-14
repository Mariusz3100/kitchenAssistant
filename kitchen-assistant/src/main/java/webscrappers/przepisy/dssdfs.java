package webscrappers.przepisy;

import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;

public  class dssdfs{
	private AmountTypes targetAmountType;
	private float multiplier;
	
	
	public dssdfs(AmountTypes targetAmountType, float multiplier) {
		super();
		this.targetAmountType = targetAmountType;
		this.multiplier = multiplier;
	}
	public AmountTypes getTargetAmountType() {
		return targetAmountType;
	}
	public void setTargetAmountType(AmountTypes targetAmountType) {
		this.targetAmountType = targetAmountType;
	}
	public float getMultiplier() {
		return multiplier;
	}
	public void setMultiplier(float multiplier) {
		this.multiplier = multiplier;
	}
}