package mariusz.ambroziak.kassistant.Apiclients.edaman;

import java.util.List;

public class HealthAndDietLimitations {
	public HealthAndDietLimitations(List<HealthLabels> healthLabels, List<DietLabels> dietLabels) {
		super();
		this.healthLabels = healthLabels;
		this.dietLabels = dietLabels;
	}
	public HealthAndDietLimitations() {
	}
	private List<HealthLabels> healthLabels;
	private List<DietLabels> dietLabels;
	
	
	public List<HealthLabels> getHealthLabels() {
		return healthLabels;
	}
	public void setHealthLabels(List<HealthLabels> healthLabels) {
		this.healthLabels = healthLabels;
	}
	public List<DietLabels> getDietLabels() {
		return dietLabels;
	}
	public void setDietLabels(List<DietLabels> dietLabels) {
		this.dietLabels = dietLabels;
	}
	
	
	
}
