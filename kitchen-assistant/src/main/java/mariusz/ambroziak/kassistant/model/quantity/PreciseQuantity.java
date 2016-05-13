package mariusz.ambroziak.kassistant.model.quantity;

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
}
