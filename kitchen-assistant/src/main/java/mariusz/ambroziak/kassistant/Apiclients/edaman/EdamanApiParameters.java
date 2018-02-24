package mariusz.ambroziak.kassistant.Apiclients.edaman;

import java.util.ArrayList;

public class EdamanApiParameters {
	public static String getApp_id() {
		return app_id;
	}
	public static String getApp_key() {
		return app_key;
	}
	private static String app_id="af08be14";
	private static String app_key="2ac175efa4ddfeff85890bed42dff521";
	
	
	public void setHealthLabels(ArrayList<HealthLabels> healthLabels) {
		this.healthLabels = healthLabels;
	}
	public void setDietLabels(ArrayList<DietLabels> dietLabels) {
		this.dietLabels = dietLabels;
	}
	private String phrase;
	private ArrayList<HealthLabels> healthLabels=new ArrayList<>();
	private ArrayList<DietLabels> dietLabels=new ArrayList<>();
	public String getPhrase() {
		return phrase;
	}
	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}
	public ArrayList<HealthLabels> getHealthLabels() {
		return healthLabels;
	}
	public void addHealthLabels(HealthLabels healthLabel) {
		this.healthLabels.add(healthLabel);
	}
	public ArrayList<DietLabels> getDietLabels() {
		return dietLabels;
	}
	public void addDietLabel(DietLabels dietLabel) {
		this.dietLabels.add(dietLabel);
	}
	
	
//	public boolean isRecipeAcceptable(RecipeData rd){
//		for(HealthLabels hl:getHealthLabels()){
//			if(rd.getHealthLabels().contains(hl.getParameterName().compareToIgnoreCase(str)))
//		}
//	}
	
}
