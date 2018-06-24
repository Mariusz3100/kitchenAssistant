package mariusz.ambroziak.kassistant.Apiclients.edaman;

public class EdamanNutrientFood {
	private String label;
	private String uri;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public EdamanNutrientFood(String label, String uri) {
		super();
		this.label = label;
		this.uri = uri;
	}
	public EdamanNutrientFood() {
		// TODO Auto-generated constructor stub
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
}
