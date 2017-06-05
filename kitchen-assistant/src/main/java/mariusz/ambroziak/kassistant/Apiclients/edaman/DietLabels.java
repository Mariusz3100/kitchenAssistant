package mariusz.ambroziak.kassistant.Apiclients.edaman;

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
	
	public  static DietLabels retrieveByName(String name){
		if(name==null)return null;
		for(DietLabels at:DietLabels.values()){
			if(at.getLabel().toLowerCase().equals(name.toLowerCase())||
					at.getParameterName().toLowerCase().equals(name.toLowerCase())){
				return at;
			}
		}
		ProblemLogger.logProblem("unparsable Diet label: "+name);
		return null;
		
	}
}
