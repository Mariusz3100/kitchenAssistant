package mariusz.ambroziak.kassistant.model.utils;

import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;

public class ApiIngredientAmount implements ProduktIngredientQuantity{
	public ApiIngredientAmount(String name, float miligramAmount) {
		super();
		this.name = name;
		this.miligramAmount = miligramAmount;
	}

	String name;
	float miligramAmount;
	
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public AmountTypes getAmountType() {
		// TODO Auto-generated method stub
		return AmountTypes.mg;
	}

	@Override
	public PreciseQuantity getAmount() {
		// TODO Auto-generated method stub
		return new PreciseQuantity(miligramAmount,getAmountType());
	}

}