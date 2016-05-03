package mariusz.ambroziak.kassistant.model.utils;

import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;

public interface ProduktIngredientQuantity {
	public String getName();
	public AmountTypes getAmountType();
	public AbstractQuantity getAmount();
}
