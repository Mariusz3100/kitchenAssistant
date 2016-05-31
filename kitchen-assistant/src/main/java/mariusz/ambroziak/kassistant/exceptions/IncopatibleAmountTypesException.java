package mariusz.ambroziak.kassistant.exceptions;

import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;

public class IncopatibleAmountTypesException extends Exception {
	private AmountTypes firstType;
	private AmountTypes secondType;
	
	@Override
	public String toString() {
		return "Incopatible AmountTypes: " + firstType + " and " + secondType;
	}
	public IncopatibleAmountTypesException(AmountTypes firstType, AmountTypes secondType) {
		super();
		this.firstType = firstType;
		this.secondType = secondType;
	}
	public AmountTypes getFirstType() {
		return firstType;
	}
	public AmountTypes getSecondType() {
		return secondType;
	}
	
	
}
