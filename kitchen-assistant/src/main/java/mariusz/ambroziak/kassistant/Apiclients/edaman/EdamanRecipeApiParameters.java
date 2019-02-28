package mariusz.ambroziak.kassistant.Apiclients.edaman;

import java.util.ArrayList;
import java.util.List;

public class EdamanRecipeApiParameters {
	public static String getApp_id() {
		return app_id;
	}
	public static String getApp_key() {
		return app_key;
	}
	private static String app_id="af08be14";
	private static String app_key="2ac175efa4ddfeff85890bed42dff521";
	
	private static String baseUrl="https://api.edamam.com/search";
	
	public static String getBaseUrl() {
		return baseUrl;
	}
	public static void setBaseUrl(String baseUrl) {
		EdamanRecipeApiParameters.baseUrl = baseUrl;
	}
	public void setHealthLabels(List<HealthLabels> healthLabels) {
		this.healthLabels = healthLabels;
	}
	public void setDietLabels(List<DietLabels> dietLabels) {
		this.dietLabels = dietLabels;
	}
	private String phrase;
	private List<HealthLabels> healthLabels=new ArrayList<>();
	private List<DietLabels> dietLabels=new ArrayList<>();
	public String getPhrase() {
		return phrase;
	}
	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}
	public List<HealthLabels> getHealthLabels() {
		if(healthLabels==null)return new ArrayList<>();
		return healthLabels;
	}
	public void addHealthLabels(HealthLabels healthLabel) {
		this.healthLabels.add(healthLabel);
	}
	public List<DietLabels> getDietLabels() {
		if(dietLabels==null)return new ArrayList<>();
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
