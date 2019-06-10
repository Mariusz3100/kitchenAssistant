package mariusz.ambroziak.kassistant.ai.categorisation.edaman;

import mariusz.ambroziak.kassistant.model.quantity.NotPreciseQuantity;

public class IngredientParsed {
	private String originalPhrase;
	private String productPhrase;
	private NotPreciseQuantity quantity;
	private IngredientCategory category;
	
	
	
	
	public IngredientParsed() {
		super();
	}
	public String getOriginalPhrase() {
		return originalPhrase;
	}
	public void setOriginalPhrase(String originalPhrase) {
		this.originalPhrase = originalPhrase;
	}
	public String getProductPhrase() {
		return productPhrase;
	}
	public void setProductPhrase(String productPhrase) {
		this.productPhrase = productPhrase;
	}
	public NotPreciseQuantity getQuantity() {
		return quantity;
	}
	public void setQuantity(NotPreciseQuantity quantity) {
		this.quantity = quantity;
	}
	public IngredientCategory getCategory() {
		return category;
	}
	public void setCategory(IngredientCategory category) {
		this.category = category;
	}
	
	
	
	
}
