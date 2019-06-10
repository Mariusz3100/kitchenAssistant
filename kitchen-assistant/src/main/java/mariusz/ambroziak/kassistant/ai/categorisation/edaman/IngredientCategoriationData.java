package mariusz.ambroziak.kassistant.ai.categorisation.edaman;

import mariusz.ambroziak.kassistant.model.quantity.NotPreciseQuantity;

public class IngredientCategoriationData {
	private String phrase;

	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public IngredientCategoriationData(String phrase) {
		super();
		this.phrase = phrase;
	}

}
