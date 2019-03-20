package mariusz.ambroziak.kassistant.Apiclients.edaman;

import java.util.ArrayList;
import java.util.List;

import mariusz.ambroziak.kassistant.utils.ProblemLogger;

public enum DietLabels {
	BALANCED("Balanced","balanced"),
	HIGH_FIBER("High-Fiber","high-fiber",false),
	HIGH_PROTEIN("High-Protein","high-protein"),
	LOW_CARB("Low-Carb","low-carb"),
	LOW_FAT("Low-Fat","low-fat"),
	LOW_SODIUM("Low-Sodium","low-sodium",false)
	;



	private boolean freeOfCharge=true;
	private String label;
	private String parameterName;
	
	public boolean isFreeOfCharge() {
		return freeOfCharge;
	}

	public void setFreeOfCharge(boolean freeOfCharge) {
		this.freeOfCharge = freeOfCharge;
	}

	
	private DietLabels(String label, String parameterName) {
		this.label = label;
		this.parameterName = parameterName;
	}
	private DietLabels(String label, String parameterName,boolean freeOfCharge) {
		this.freeOfCharge = freeOfCharge;
		this.label = label;
		this.parameterName = parameterName;
	}	
	public String getLabel() {
		return label;
	}

	public String getParameterName() {
		return parameterName;
	}
	
	public  static DietLabels retrieveByParameterName(String name){
		if(name==null)return null;
		for(DietLabels at:DietLabels.values()){
			if(at.getParameterName().toLowerCase().equals(name.toLowerCase())||
					at.getParameterName().toLowerCase().equals(name.toLowerCase())){
				return at;
			}
		}
		return null;
	}
	
	
	public  static List<DietLabels> tryRetrieving(List<String> list){
		if(list==null)
			return null;

		List<DietLabels> retValue=new ArrayList<>();
		
		for(String label:list) {
			DietLabels hl=tryRetrieving(label);
			if(hl!=null)
				retValue.add(hl);
		}
		
		return retValue;
	}
	
	public  static DietLabels tryRetrieving(String name){
		if(name==null)
			return null;
		
		DietLabels retrieved = retrieveByParameterName(name);
		if(retrieved==null) {
			retrieved=retrieveByLabel(name);
		}
		if(retrieved==null) {
			ProblemLogger.logProblem("unparsable Diet label: "+name);
		}
		return retrieved;
	}
	
	
	public  static List<DietLabels> retrieveByName(List<String> list){
		if(list==null)
			return null;

		List<DietLabels> retValue=new ArrayList<>();
		
		for(String label:list) {
			DietLabels dl=retrieveByParameterName(label);
			if(dl!=null)
				retValue.add(dl);
		}
		
		return retValue;
	}
	
	
	public  static DietLabels retrieveByLabel(String name){
		if(name==null)return null;
		for(DietLabels at:DietLabels.values()){
			if(at.getLabel().toLowerCase().equals(name.toLowerCase())||
					at.getLabel().toLowerCase().equals(name.toLowerCase())){
				return at;
			}
		}
		return null;
	}
	
	public  static List<DietLabels> retrieveByLabel(List<String> list){
		if(list==null)
			return null;

		List<DietLabels> retValue=new ArrayList<>();
		
		for(String label:list) {
			DietLabels dl=retrieveByLabel(label);
			if(dl!=null)
				retValue.add(dl);
		}
		
		return retValue;
	}
	
}
