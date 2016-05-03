package mariusz.ambroziak.kassistant.model.quantity;

import java.util.ArrayList;

import mariusz.ambroziak.kassistant.utils.ProblemLogger;

public enum AmountTypes {
	ml("ml"),
	mg("mg"),
	szt("szt"),
	kalorie("kcal");
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	String type;

	private AmountTypes(String type){
		this.type=type;
		
	}
	
	public  static AmountTypes retrieveTypeByName(String name){
		for(AmountTypes at:AmountTypes.values()){
			if(at.getType().equals(name)){
				return at;
			}
		}
		ProblemLogger.logProblem("unparsable amount type: "+name);
		return AmountTypes.szt;
		
	}
	
	public  static AmountTypes[] valuesWithoutCalories(){
		ArrayList<AmountTypes> retValue=new ArrayList<AmountTypes>();
		
		for(AmountTypes at:values()){
			if(!kalorie.equals(at))
				retValue.add(at);
		}
		
		return retValue.toArray(new AmountTypes[]{});
	}
	
	
}
