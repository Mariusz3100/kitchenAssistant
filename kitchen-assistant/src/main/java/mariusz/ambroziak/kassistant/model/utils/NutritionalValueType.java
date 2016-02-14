package mariusz.ambroziak.kassistant.model.utils;

public enum NutritionalValueType {
	Energia("wartoœæ energetyczna"),
	Bialko("bia³ko"),
	Tluszcz("t³uszcz"),
	TluszczNasycony("kwasy t³uszczowe nasycone"),
	Weglowodany("wêglowodany"),
	Cukry("cukry"),
	Blonnik("b³onnik"),
	Sod("sód");
	
	
	
	public String getName() {
		return name;
	}

	private String name;

	private NutritionalValueType(String name) {
		this.name = name;
	}
	
	
	
	
	
	
	
	
	
	
	
}
