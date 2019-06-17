package mariusz.ambroziak.kassistant.ai.categorisation.edaman;

import mariusz.ambroziak.kassistant.model.quantity.NotPreciseQuantity;

public class IngredientCategoriationData {
	private String originalPhrase;
	
	
	
	
	public String getPhrase() {
		return originalPhrase;
	}

	public void setPhrase(String phrase) {
		this.originalPhrase = phrase;
	}

	public IngredientCategoriationData(String phrase) {
		super();
		this.originalPhrase = phrase;
	}

}
