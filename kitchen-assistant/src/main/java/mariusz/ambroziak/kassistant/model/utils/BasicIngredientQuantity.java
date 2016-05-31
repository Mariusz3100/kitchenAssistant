package mariusz.ambroziak.kassistant.model.utils;

import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.NotPreciseQuantity;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;

public class BasicIngredientQuantity implements ProduktIngredientQuantity{
	private Basic_Ingredient bi;
	private NotPreciseQuantity quan;
	
	
	public BasicIngredientQuantity(Basic_Ingredient bi, NotPreciseQuantity quan) {
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
	public Basic_Ingredient getBi() {
		return bi;
	}
	public NotPreciseQuantity getAmount() {
		return quan;
	}
	public AmountTypes getAmountType() {
		return quan.type;
	}
	
	@Override
	public String getName() {
		return bi!=null&&bi.getName()!=null?bi.getName():"brak nazwy";
	}
	public void setBi(Basic_Ingredient bi) {
		this.bi = bi;
	}

	
	public String toString(){
		return "{"+getName()+" : "+getAmount()+"}";
	}
	
}
