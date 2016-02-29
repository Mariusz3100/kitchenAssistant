package mariusz.ambroziak.kassistant.QuantityExtractor;

public enum AmountTypes {
	ml("ml"),
	mg("mg"),
	szt("szt"),
	kalorie("kcal");
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	String type;

	private AmountTypes(String type){
		this.type=type;
		
	}
}
