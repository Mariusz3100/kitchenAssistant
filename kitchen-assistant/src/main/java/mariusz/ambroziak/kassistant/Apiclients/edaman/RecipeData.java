package mariusz.ambroziak.kassistant.Apiclients.edaman;

import java.util.ArrayList;

import mariusz.ambroziak.kassistant.model.utils.ApiIngredientAmount;
import mariusz.ambroziak.kassistant.model.utils.ProduktIngredientQuantity;

public class RecipeData {
	String url;
	String label;
	String imageUrl;
	String shopId;
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	private ArrayList<HealthLabels> healthLabels=new ArrayList<>();
	private ArrayList<DietLabels> dietLabels=new ArrayList<>();
	ArrayList<ApiIngredientAmount> ingredients;
	
	
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

	
	public void setIngredients(ArrayList<ApiIngredientAmount> ingredients) {
		this.ingredients = ingredients;
	}
	public ArrayList<ApiIngredientAmount> getIngredients() {
		return ingredients;
	}
	public void addIngredient(ApiIngredientAmount ingredient) {
		if(this.ingredients==null)
			this.ingredients=new ArrayList<>();
		
		this.ingredients.add(ingredient);
		
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}
