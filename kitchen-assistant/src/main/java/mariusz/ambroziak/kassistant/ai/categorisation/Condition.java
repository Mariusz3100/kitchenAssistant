package mariusz.ambroziak.kassistant.ai.categorisation;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONObject;


import mariusz.ambroziak.kassistant.model.Produkt;

public class Condition {
	ArrayList<String> nameInclusions;
	ArrayList<String> categoryNameInclusions;
	
	public ArrayList<String> getCategoryNameInclusions() {
		return categoryNameInclusions;
	}
	public void addCategoryNameInclusions(String categoryNameInclusion) {
		if(categoryNameInclusions==null)
			categoryNameInclusions=new ArrayList<String>();

		this.categoryNameInclusions.add(categoryNameInclusion);
	}
	public ArrayList<String> getNameInclusions() {
		return nameInclusions;
	}
	public void addNameInclusion(String nameInclusion) {
		if(nameInclusions==null)
			nameInclusions=new ArrayList<String>();

		this.nameInclusions.add(nameInclusion);
	}

	
	public boolean check(Produkt p) {
		boolean nameIsOk=checkName(p);
		boolean categoryIsOk=checkCategory(p);
		
		return nameIsOk&&categoryIsOk;
	}
	
	
	
	private boolean checkName(Produkt p) {
		boolean nameIsOk=true;

		if(nameInclusions!=null&&!nameInclusions.isEmpty()) {
			for(int i=0;nameIsOk&&i<nameInclusions.size();i++) {

				if(p.getNazwa()==null||!p.getNazwa().toLowerCase().contains(nameInclusions.get(i).toLowerCase())) {
					nameIsOk=false;
				}
			}
		}
		return nameIsOk;
	}
	private boolean checkCategory(Produkt p) {
		boolean categoryIsOk=true;
		if(p.getMetadata()==null||p.getMetadata().equals("")) {
			return false;
		}else {
			String metadane=p.getMetadata();
			JSONObject json=new JSONObject(metadane);
			String category = json.getString(MetadataConstants.categoryName1)+" "+json.getString(MetadataConstants.categoryName2);



			if(categoryNameInclusions!=null&&!categoryNameInclusions.isEmpty()) {
				for(int i=0;categoryIsOk&&i<categoryNameInclusions.size();i++) {

					if(category==null||!category.toLowerCase().contains(categoryNameInclusions.get(i).toLowerCase())) {
						categoryIsOk=false;
					}
				}
			}

			return categoryIsOk;
		}
	}
	
	public static Condition createNameInclusionsContition(String...strings) {
		Condition retValue=new Condition();
		if(retValue.nameInclusions==null)
			retValue.nameInclusions=new ArrayList<String>();
		
		retValue.nameInclusions.addAll(Arrays.asList(strings));
		
		return retValue;
		
	}
	
	public static Condition createCategoryNameInclusionsContition(String... strings) {
		Condition retValue=new Condition();
		if(retValue.categoryNameInclusions==null)
			retValue.categoryNameInclusions=new ArrayList<String>();
		
		retValue.categoryNameInclusions.addAll(Arrays.asList(strings));
		
		return retValue;
		
	}

}
