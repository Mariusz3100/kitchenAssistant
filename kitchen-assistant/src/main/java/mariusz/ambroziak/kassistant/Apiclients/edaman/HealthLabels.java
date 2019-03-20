package mariusz.ambroziak.kassistant.Apiclients.edaman;

import java.util.ArrayList;
import java.util.List;

import mariusz.ambroziak.kassistant.model.quantity.AmountTypes;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

public enum HealthLabels {
	Alcohol_free("Alcohol-free","alcohol-free"),
	Celery_free("Celery-free","celery-free",false),
	Crustcean_free("Crustcean-free","crustacean-free",false),
	Dairy("Dairy","dairy-free",false),
	Eggs("Eggs","egg-free",false),
	Fish("Fish","fish-free",false),
	Gluten("Gluten","gluten-free",false),
	Kidney_friendly("Kidney friendly","kidney-friendly",false),
	Kosher("Kosher","kosher",false),
	Low_potassium("Low potassium","low-potassium",false),
	Lupine_free("Lupine-free","lupine-free",false),
	Mustard_free("Mustard-free","mustard-free",false),
	NFA("n/a","low-fat-abs",false),
	No_oil_added("No oil added","No-oil-added",false),
	No_sugar("No-sugar","low-sugar",false),
	Paleo("Paleo","paleo",false),
	Peanuts("Peanuts","peanut-free"),
	Pescatarian("Pescatarian","pecatarian",false),
	Pork_free("Pork-free","pork-free",false),
	Red_meat_free("Red meat-free","red-meat-free",false),
	Sesame_free("Sesame-free","sesame-free",false),
	Shellfish("Shellfish","shellfish-free",false),
	Soy("Soy","soy-free",false),
	Sugar_conscious("Sugar-conscious","sugar-conscious"),
	Tree_Nuts("Tree Nuts","tree-nut-free"),
	Vegan("Vegan","vegan"),
	Vegetarian("Vegetarian","vegetarian"),
	Wheat_free("Wheat-free","wheat-free");
	
	
	
	private boolean freeOfCharge=true;
	private HealthLabels(String label, String parameterName,boolean freeOfCharge) {
		this.freeOfCharge = freeOfCharge;
		this.label = label;
		this.parameterName = parameterName;
	}


	public boolean isFreeOfCharge() {
		return freeOfCharge;
	}


	public void setFreeOfCharge(boolean freeOfCharge) {
		this.freeOfCharge = freeOfCharge;
	}

	private String label;
	private String parameterName;
	
	private HealthLabels(String label, String parameterName) {
		this.label = label;
		this.parameterName = parameterName;
	}
	
	
	public String getLabel() {
		return label;
	}

	public String getParameterName() {
		return parameterName;
	}
	
	public  static HealthLabels retrieveByParameterName(String name){
		if(name==null)return null;
		for(HealthLabels at:HealthLabels.values()){
			if(at.getParameterName().toLowerCase().equals(name.toLowerCase())||
					at.getParameterName().toLowerCase().equals(name.toLowerCase())){
				return at;
			}
		}
		return null;
		
	}
	
	public  static List<HealthLabels> retrieveByParameterName(List<String> list){
		if(list==null)
			return null;

		List<HealthLabels> retValue=new ArrayList<>();
		
		for(String label:list) {
			HealthLabels hl=retrieveByParameterName(label);
			if(hl!=null)
				retValue.add(hl);
		}
		
		return retValue;
	}
	
	public  static HealthLabels tryRetrieving(String name){
		if(name==null)
			return null;
		
		HealthLabels retrieveByLabel = retrieveByParameterName(name);
		if(retrieveByLabel==null) {
			retrieveByLabel=retrieveByLabel(name);
		}
		if(retrieveByLabel==null) {
			ProblemLogger.logProblem("unparsable health label: "+name);
		}
		

		return retrieveByLabel;
	}
	
	public  static List<HealthLabels> tryRetrieving(List<String> list){
		if(list==null)
			return null;

		List<HealthLabels> retValue=new ArrayList<>();
		
		for(String label:list) {
			HealthLabels hl=tryRetrieving(label);
			if(hl!=null)
				retValue.add(hl);
		}
		
		return retValue;
	}
	
	
	public  static HealthLabels retrieveByLabel(String name){
		if(name==null)return null;
		for(HealthLabels at:HealthLabels.values()){
			if(at.getLabel().toLowerCase().equals(name.toLowerCase())||
					at.getLabel().toLowerCase().equals(name.toLowerCase())){
				return at;
			}
		}
		return null;
		
	}
	
	public  static List<HealthLabels> retrieveByLabel(List<String> list){
		if(list==null)
			return null;

		List<HealthLabels> retValue=new ArrayList<>();
		
		for(String label:list) {
			HealthLabels hl=retrieveByLabel(label);
			if(hl!=null)
				retValue.add(hl);
		}
		
		return retValue;
	}
}
