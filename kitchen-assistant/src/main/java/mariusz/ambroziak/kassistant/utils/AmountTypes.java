package mariusz.ambroziak.kassistant.utils;

public enum AmountTypes {
	ml("ml",1l),
	mg("mg",2l),
	szt("szt",3l);
	
	
	
	String type;
	Long db_id;
	public String getType() {
		return type;
	}
	public Long getDb_id() {
		return db_id;
	}
	private AmountTypes(String type,Long id){
		this.type=type;
		db_id=id;
		
	}
}
