package mariusz.ambroziak.kassistant.model.utils;

import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;

public class BasicIngredientQuantity implements ProduktIngredientQuantity{
	private Basic_Ingredient bi;
	private AbstractQuantity quan;
	
	
	public BasicIngredientQuantity(Basic_Ingredient bi, AbstractQuantity quan) {
		super();
		this.bi = bi;
		this.quan = quan;
	}
	public BasicIngredientQuantity(Basic_Ingredient bi, float amount,
			AmountTypes amountType) {
		super();
		this.bi = bi;
		quan=new PreciseQuantity();
		((PreciseQuantity) this.quan).setAmount(amount);
		this.quan.type = amountType;
	}
	public Basic_Ingredient getPi() {
		return bi;
	}
	public AbstractQuantity getAmount() {
		return quan;
	}
	public AmountTypes getAmountType() {
		return quan.type;
	}
	
	@Override
	public String getName() {
		return bi!=null&&bi.getName()!=null?bi.getName():"brak nazwy";
	}

	
	
	
}
