package mariusz.ambroziak.kassistant.ai.categorisation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;


import mariusz.ambroziak.kassistant.model.Produkt;

public class Condition {
	ArrayList<String> nameInclusions;
	ArrayList<String> nameExclusions;


	//	ArrayList<String> categoryNameInclusions;
	ArrayList<String> attributesPresent;
	Map<String,String> attributeValues;


	public Map<String, String> getAttributeValues() {
		if(attributeValues==null)
			attributeValues=new HashMap<String, String>();

		return attributeValues;
	}
	public void addAttributeValues(String attribute, String inclusion) {
		if(attributeValues==null)
			attributeValues=new HashMap<String, String>();

		this.attributeValues.put(attribute, inclusion);
	}
	public ArrayList<String> getNameExclusions() {
		if(nameExclusions==null)
			nameExclusions=new ArrayList<String>();

		return nameExclusions;
	}
	public void addNameExclusions(String nameExclusion) {
		if(nameExclusions==null)
			nameExclusions=new ArrayList<String>();

		this.nameExclusions.add(nameExclusion);
	}


	public ArrayList<String> getAttributesPresent() {
		if(attributesPresent==null)
			attributesPresent=new ArrayList<String>();

		return attributesPresent;
	}
	public void addAttributePresent(String attributePresent) {
		if(attributesPresent==null)
			attributesPresent=new ArrayList<String>();

		this.attributesPresent.add(attributePresent);
	}

	//	public ArrayList<String> getCategoryNameInclusions() {
	//		return categoryNameInclusions;
	//	}
	//	public void addCategoryNameInclusions(String categoryNameInclusion) {
	//		if(categoryNameInclusions==null)
	//			categoryNameInclusions=new ArrayList<String>();
	//
	//		this.categoryNameInclusions.add(categoryNameInclusion);
	//	}
	public ArrayList<String> getNameInclusions() {
		if(nameInclusions==null)
			nameInclusions=new ArrayList<String>();

		return nameInclusions;
	}
	public void addNameInclusion(String nameInclusion) {
		if(nameInclusions==null)
			nameInclusions=new ArrayList<String>();

		this.nameInclusions.add(nameInclusion);
	}


	public boolean check(Produkt p) {
		boolean nameHasInclusions=checkNameInclusions(p);
		boolean nameHasNoExclusions=checkNameExclusons(p);

		boolean categoryIsOk=checkCategory(p);
		boolean areAttributesOk=checkMetadataAttributesPresence(p);

		return nameHasInclusions&&categoryIsOk&&areAttributesOk&&nameHasNoExclusions;
	}



	private boolean checkNameInclusions(Produkt p) {
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

	private boolean checkNameExclusons(Produkt p) {
		if(nameExclusions!=null&&!nameExclusions.isEmpty()) {
			for(int i=0;i<nameExclusions.size();i++) {

				if(p.getNazwa()!=null&&p.getNazwa().toLowerCase().contains(nameExclusions.get(i).toLowerCase())) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean checkMetadataAttributesPresence(Produkt p) {
		boolean areAttrsPresest=true;

		if(attributesPresent!=null&&!attributesPresent.isEmpty()) {
			if(p==null||p.getMetadata()==null||p.getMetadata().equals("")) {
				return false;
			}
			JSONObject metadataObject=new JSONObject(p.getMetadata().toLowerCase());
			for(int i=0;areAttrsPresest&&i<attributesPresent.size();i++) {

				if(!metadataObject.has(attributesPresent.get(i).toLowerCase())) {
					areAttrsPresest=false;
				}
			}
		}
		return areAttrsPresest;
	}

	private boolean checkCategory(Produkt p) {
		ArrayList<String> categoryNameInclusions = getCategoryNameInclusions();

		if(categoryNameInclusions==null||categoryNameInclusions.isEmpty()) {
			return true;
		}
		boolean categoryIsOk=true;

		if(p.getMetadata()==null||p.getMetadata().equals("")) {
			return false;
		}else {
			String metadane=p.getMetadata();
			JSONObject json=new JSONObject(metadane);
			String category = json.getString(MetadataConstants.categoryNameJsonPrefix);



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
	private ArrayList<String> getCategoryNameInclusions() {
		ArrayList<String> categoryNameInclusions=new ArrayList<String>();
		if(this.attributeValues!=null&&!this.attributeValues.isEmpty()) {
			for(Entry<String,String> entry:this.attributeValues.entrySet()) {
				if(entry.getKey()!=null&&entry.getKey().startsWith(MetadataConstants.categoryNameJsonPrefix)) {
					categoryNameInclusions.add(entry.getValue());
				}
			}
		}
		return categoryNameInclusions;
	}

	public static Condition createNameInclusionsCondition(String...strings) {
		Condition retValue=new Condition();
		if(retValue.nameInclusions==null)
			retValue.nameInclusions=new ArrayList<String>();

		retValue.nameInclusions.addAll(Arrays.asList(strings));

		return retValue;

	}

	public static Condition createCategoryNameInclusionsCondition(String... strings) {
		Condition retValue=new Condition();

		int currentAmount = retValue.getCategoryNameInclusions()==null?0
				:retValue.getCategoryNameInclusions().size();

		for(String toBeAdded:strings) {
			retValue.addAttributeValues(MetadataConstants.categoryNameJsonPrefix+currentAmount, toBeAdded);
		}
		return retValue;

	}

	public static Condition createAttributePresentCondition(String... strings) {
		Condition retValue=new Condition();
		if(retValue.attributesPresent==null)
			retValue.attributesPresent=new ArrayList<String>();

		retValue.attributesPresent.addAll(Arrays.asList(strings));

		return retValue;
	}
	
	public String toString() {
		String retValue=toJsonRepresentation().toString();
		
		return retValue;
	}
	
	public JSONObject toJsonRepresentation(){
		JSONObject retValue=new JSONObject();
		JSONArray attrPresentConditionsJsonArray=new JSONArray();
		for(String c:getAttributesPresent()) {
			attrPresentConditionsJsonArray.put(c);
		}
		
		retValue.put("attributesPresentConditions", attrPresentConditionsJsonArray);
		
		JSONArray attributeValuesArray=new JSONArray();
		for(Entry<String, String> c:getAttributeValues().entrySet()) {
			attributeValuesArray.put(c.getKey()+"='"+c.getValue()+"'");
		}
		
		retValue.put("attributeValuesConditions", attributeValuesArray);
		
		JSONArray nameKeywordsConditionsJsonArray=new JSONArray();

		for(String c:getNameInclusions()) {
			nameKeywordsConditionsJsonArray.put(c);
		}
		
		retValue.put("nameInclusionsConditions", nameKeywordsConditionsJsonArray);
		
		JSONArray nameNotContainsConditionsJsonArray=new JSONArray();

		for(String c:getNameExclusions()) {
			nameNotContainsConditionsJsonArray.put(c);
		}
		
		retValue.put("nameExclusionsConditions", nameNotContainsConditionsJsonArray);
		
		
		return retValue;
		
	}
	public Condition() {
		super();
		this.attributesPresent=new ArrayList<String>();
		this.attributeValues=new HashMap<String, String>();
		
	}
}
