package mariusz.ambroziak.kassistant.model.utils;

import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;

public class AbstractQuantity {

	public AmountTypes type;

	public AmountTypes getType() {
		return type;
	}

	public void setType(AmountTypes type) {
		this.type = type;
	}

}
