package mariusz.ambroziak.kassistant.model.utils;


public class NotPreciseQuantity extends AbstractQuantity {

	private float minimalAmount;
	private float maximalAmount;
	
	
	public float getMinimalAmount() {
		return minimalAmount;
	}
	public void setMinimalAmount(float minimalAmount) {
		this.minimalAmount = minimalAmount;
	}
	public float getMaximalAmount() {
		return maximalAmount;
	}
	public void setMaximalAmount(float maximalAmount) {
		this.maximalAmount = maximalAmount;
	}
	
}
