package mariusz.ambroziak.kassistant.model.utils;

import java.util.ArrayList;
import java.util.Map;

import webscrappers.auchan.ProduktDetails;
import mariusz.ambroziak.kassistant.model.Nutrient;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.quantity.PreciseQuantity;

public class ProduktWithAllNutrients {
	private ProduktDetails produkt;
	
	private Map<Nutrient,PreciseQuantity> basicsFromLabelTable;

	public ProduktWithAllNutrients(ProduktDetails produkt, Map<Nutrient, PreciseQuantity> basicsFromLabelTable) {
		super();
		this.produkt = produkt;
		this.basicsFromLabelTable = basicsFromLabelTable;
	}

	public ProduktDetails getProdukt() {
		return produkt;
	}

	public Map<Nutrient, PreciseQuantity> getBasicsFromLabelTable() {
		return basicsFromLabelTable;
	}


	
}
