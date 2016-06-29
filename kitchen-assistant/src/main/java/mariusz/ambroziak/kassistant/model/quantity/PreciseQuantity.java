package mariusz.ambroziak.kassistant.model.quantity;

import mariusz.ambroziak.kassistant.exceptions.IncopatibleAmountTypesException;
import mariusz.ambroziak.kassistant.exceptions.InvalidArgumentException;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

public class PreciseQuantity extends NotPreciseQuantity {
	private float amount;

	@Override
	public float getMinimalAmount() {
		return getAmount();
	}
	@Override
	public float getMaximalAmount() {
		 return getAmount();
	}
	public PreciseQuantity(float amount, AmountTypes type) {
		super();
		this.amount = amount;
		this.type = type;
		this.setMaximalAmount(amount);
		this.setMinimalAmount(amount);
		
	}
	
	public void addPrecise(PreciseQuantity other) 
			throws IncopatibleAmountTypesException, InvalidArgumentException{
		if(!this.isValid())
			throw new InvalidArgumentException(this+" is not valid Quantity!");
		if(!this.getType().equals(other.getType()))
			throw new IncopatibleAmountTypesException(getType(),other.getType());
		
		float value=this.getAmount()+other.getAmount();
		setAmount(value);
	}
	
	
	public PreciseQuantity() {
		super();
		amount=-1;
		type=AmountTypes.szt;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	
	public String toString(){
		return amount+" "+type;
	}
	
	public String toJspString(){
		return type+JspStringHolder.QUANTITY_PHRASE_BORDER+amount;
	}
	
	public PreciseQuantity getClone(){
		return new PreciseQuantity(this.getAmount(), this.getType());
	}
	
	public static  PreciseQuantity parseFromJspString(String quanPhrase){
		PreciseQuantity pq = null;
		String[] elems=quanPhrase.split(JspStringHolder.QUANTITY_PHRASE_BORDER);
		if(elems.length<2){
			ProblemLogger.logProblem("Empty quantity from hidden field");
		}else{
			float amount=Float.parseFloat(elems[1]);
			AmountTypes at=AmountTypes.retrieveTypeByName(elems[0]);
			pq=new PreciseQuantity(amount,at);
		}
		
		return pq;
	}
	
	public void multiplyBy(float multiplier){
		if(isValid()){
			float amountToRoundUp = this.getAmount()*multiplier;
			amountToRoundUp=Math.round(amountToRoundUp*100)/100;
			this.setAmount(amountToRoundUp);
		}
	}
	
	
	public boolean isValid(){
		if(this.getType()==null||this.getAmount()<0)
			return false;
		else
			return true;
	}
	
	
	public float getFractionOf100g() {
		if(getType()==AmountTypes.mg){
			return (getAmount()/1000)/100;
		}else{
			ProblemLogger.logProblem("Trying to extract data from something else than mg");
			return 0;
		}
		
		
	}

}


