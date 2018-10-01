package mariusz.ambroziak.kassistant.Apiclients.usda;

public class UsdaFoodId {
	//private String group;
	private String name;
	private String dbno;
	
	@Override
	public String toString() {
		return "UsdaFoodId [name=\"" + name + "\", dbno=" + dbno + "]";
	}
	public UsdaFoodId(String name, String dbno) {
		super();
		this.name = name;
		this.dbno = dbno;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDbno() {
		return dbno;
	}
	public void setDbno(String dbno) {
		this.dbno = dbno;
	}
	
}
