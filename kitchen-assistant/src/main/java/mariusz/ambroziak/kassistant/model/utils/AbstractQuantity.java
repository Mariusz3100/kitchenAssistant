package mariusz.ambroziak.kassistant.model.utils;

import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.NotPreciseQuantity;

public abstract class AbstractQuantity {

	public AmountTypes type;

	public AmountTypes getType() {
		return type;
	}

	public void setType(AmountTypes type) {
		this.type = type;
	}
	
	public abstract NotPreciseQuantity getClone();
	public abstract boolean isValid();


}
