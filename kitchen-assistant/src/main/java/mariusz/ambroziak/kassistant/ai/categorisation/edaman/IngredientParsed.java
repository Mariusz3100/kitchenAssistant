package mariusz.ambroziak.kassistant.ai.categorisation.edaman;

import mariusz.ambroziak.kassistant.model.quantity.NotPreciseQuantity;

public class IngredientParsed {
	private String originalPhrase;
	private String productPhrase;
	private NotPreciseQuantity quantity;
	private Category category;
	
	
	
	
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
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	
	
	
}
