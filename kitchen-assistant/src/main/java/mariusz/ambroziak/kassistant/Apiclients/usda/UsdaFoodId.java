package mariusz.ambroziak.kassistant.Apiclients.usda;

import mariusz.ambroziak.kassistant.utils.JspStringHolder;
import mariusz.ambroziak.kassistant.utils.StringHolder;

public class UsdaFoodId implements Comparable<UsdaFoodId> {
	//private String group;
	private String name;
	private String ndbno;
	private String parseLink;
	
	

	public String getParseLink() {
		return StringHolder.currentAppName+"/b_nutritientForNdbno?"+JspStringHolder.ndbno+"="+getNdbno();
	}
	
	
	@Override
	public String toString() {
		return "UsdaFoodId [name=\"" + name + "\", dbno=" + ndbno + "]";
	}
	public UsdaFoodId(String name, String dbno) {
		super();
		this.name = name;
		this.ndbno = dbno;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNdbno() {
		return ndbno;
	}
	public void setNdbno(String dbno) {
		this.ndbno = dbno;
	}
	@Override
	public int compareTo(UsdaFoodId o) {
		if(o==this||this.getName()==o.getName()
				||(this.getName()!=null&&this.getName().equals(o.getName()))
				)
		{
			return 0;
		}else {
			if(o==null||o.getName()==null
					||this.getName().length()>o.getName().length())
			{
				return 1;
			}else {
				return -1;
			}
		}

	}

}
