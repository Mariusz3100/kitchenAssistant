package mariusz.ambroziak.kassistant.model.utils;

public enum NutritionalValueType {
	Energia("warto�� energetyczna"),
	Bialko("bia�ko"),
	Tluszcz("t�uszcz"),
	TluszczNasycony("kwasy t�uszczowe nasycone"),
	Weglowodany("w�glowodany"),
	Cukry("cukry"),
	Blonnik("b�onnik"),
	Sod("s�d");
	
	
	
	public String getName() {
		return name;
	}

	private String name;

	private NutritionalValueType(String name) {
		this.name = name;
	}
	
	
	
	
	
	
	
	
	
	
	
}
