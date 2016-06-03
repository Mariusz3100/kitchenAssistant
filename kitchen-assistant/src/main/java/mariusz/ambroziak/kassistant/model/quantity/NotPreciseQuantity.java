package mariusz.ambroziak.kassistant.model.quantity;

import java.util.PrimitiveIterator.OfDouble;

import mariusz.ambroziak.kassistant.exceptions.IncopatibleAmountTypesException;
import mariusz.ambroziak.kassistant.exceptions.InvalidArgumentException;
import mariusz.ambroziak.kassistant.model.utils.AbstractQuantity;

public class NotPreciseQuantity extends AbstractQuantity {
	public static float INVALID_AMOUNT=-1;
	private float minimalAmount=INVALID_AMOUNT;
	private float maximalAmount=INVALID_AMOUNT;
	
	
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
	
	public void add(NotPreciseQuantity other) 
			throws IncopatibleAmountTypesException, InvalidArgumentException{
		if(other instanceof PreciseQuantity){
			this.addPrecise((PreciseQuantity) other);
			return;
		}
		
		if(!this.isValid())
			throw new InvalidArgumentException(this+" is not valid Quantity!");
		if(!this.getType().equals(other.getType()))
			throw new IncopatibleAmountTypesException(getType(),other.getType());
		
		float retMinimum=this.getMinimalAmount()+other.getMinimalAmount();
		float retMaximal=this.getMaximalAmount()+other.getMaximalAmount();
		setMaximalAmount(retMaximal);
		setMinimalAmount(retMinimum);
	}
	
	public void addPrecise(PreciseQuantity other) 
			throws IncopatibleAmountTypesException, InvalidArgumentException{
		if(!this.isValid())
			throw new InvalidArgumentException(this+" is not valid Quantity!");
		if(!this.getType().equals(other.getType()))
			throw new IncopatibleAmountTypesException(getType(),other.getType());
		
		float retMinimum=this.getMinimalAmount()+other.getAmount();
		float retMaximal=this.getMaximalAmount()+other.getAmount();
		setMaximalAmount(retMaximal);
		setMinimalAmount(retMinimum);
	}
	
	
	
	public NotPreciseQuantity() {
		super();
	}
	
	public boolean isMaximumValid(){
		return getMaximalAmount()!=INVALID_AMOUNT;
	}
	
	public boolean isMinimumValid(){
		return getMinimalAmount()!=INVALID_AMOUNT;
	}
	
	public boolean isValid(){
		if(this.getType()==null||this.getMaximalAmount()<0||this.getMinimalAmount()<0
				||this.getMinimalAmount()>this.getMaximalAmount())
			return false;
		else
			return true;
	}


	public NotPreciseQuantity(float minimalAmount, float maximalAmount,AmountTypes type) {
		super();
		this.minimalAmount = minimalAmount;
		this.maximalAmount = maximalAmount;
		this.setType(type);
	}

	public String toString(){
		return minimalAmount+" - "+maximalAmount+" "+type;
	}
	
	public void multiplyBy(float multiplier){
		if(isMinimumValid())
			minimalAmount*=multiplier;
		
		if(isMaximumValid())
			maximalAmount*=multiplier;
	}

	public NotPreciseQuantity getClone(){
		return new NotPreciseQuantity(this.getMinimalAmount(), this.getMaximalAmount(),this.getType());
	}
}
