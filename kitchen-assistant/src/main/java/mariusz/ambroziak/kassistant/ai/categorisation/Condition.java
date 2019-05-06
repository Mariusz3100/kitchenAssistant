package mariusz.ambroziak.kassistant.ai.categorisation;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONObject;


import mariusz.ambroziak.kassistant.model.Produkt;

public class Condition {
	ArrayList<String> nameInclusions;
	ArrayList<String> categoryNameInclusions;
	ArrayList<String> attributesPresent;

	
	public ArrayList<String> getAttributesPresent() {
		return attributesPresent;
	}
	public void addAttributePresent(String attributePresent) {
		if(attributesPresent==null)
			attributesPresent=new ArrayList<String>();

		this.attributesPresent.add(attributePresent);
	}
	
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
		boolean areAttributesOk=checkMetadataAttributesPresence(p);
		
		return nameIsOk&&categoryIsOk&&areAttributesOk;
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
	
	private boolean checkMetadataAttributesPresence(Produkt p) {
		boolean areAttrsPresest=true;
		if(p==null||p.getMetadata()==null||p.getMetadata().equals("")) {
			return false;
		}
		JSONObject metadataObject=new JSONObject(p.getMetadata().toLowerCase());
		if(attributesPresent!=null&&!attributesPresent.isEmpty()) {
			for(int i=0;areAttrsPresest&&i<attributesPresent.size();i++) {

				if(!metadataObject.has(attributesPresent.get(i).toLowerCase())) {
					areAttrsPresest=false;
				}
			}
		}
		return areAttrsPresest;
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
	
	public static Condition createAttributePresentCondition(String... strings) {
		Condition retValue=new Condition();
		if(retValue.attributesPresent==null)
			retValue.attributesPresent=new ArrayList<String>();
		
		retValue.attributesPresent.addAll(Arrays.asList(strings));
		
		return retValue;
	}

}
