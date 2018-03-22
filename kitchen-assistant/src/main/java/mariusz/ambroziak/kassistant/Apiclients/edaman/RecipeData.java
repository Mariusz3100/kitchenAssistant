package mariusz.ambroziak.kassistant.Apiclients.edaman;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import mariusz.ambroziak.kassistant.model.utils.ApiIngredientAmount;
import mariusz.ambroziak.kassistant.model.utils.ProduktIngredientQuantity;
import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.StringHolder;

public class RecipeData {
	String url;
	String label;
	String imageUrl;
	String edamanId;
	String parseUrl;
	
	public String getEdamanId() {
		return edamanId;
	}
	
	public String getParseUrl() {
		try {
			return StringHolder.currentAppName+"/apiRecipeParsed?"+JspStringHolder.recipeApiId+"="+URLEncoder.encode(edamanId,StringHolder.ENCODING);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return StringHolder.currentAppName+"/apiRecipeParsed?recipeId="+URLEncoder.encode(edamanId);

	}
	
	public void setEdamanId(String shopId) {
		this.edamanId = shopId;
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
