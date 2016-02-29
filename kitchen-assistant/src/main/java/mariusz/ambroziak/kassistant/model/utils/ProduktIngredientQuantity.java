package mariusz.ambroziak.kassistant.model.utils;

import mariusz.ambroziak.kassistant.QuantityExtractor.AmountTypes;

public interface ProduktIngredientQuantity {
	public String getName();
	public AmountTypes getAmountType();
	public AbstractQuantity getAmount();
}
