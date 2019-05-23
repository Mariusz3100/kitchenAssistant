package mariusz.ambroziak.kassistant.ai.categorisation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;


import mariusz.ambroziak.kassistant.model.Produkt;

public class Condition {
//	ArrayList<String> nameInclusions;
//	ArrayList<String> nameExclusions;


	//	ArrayList<String> categoryNameInclusions;
	ArrayList<String> attributesPresent;
	Map<String,String> attributeValues;
	Map<String,String> attributeNotContainsValues;

	public Map<String, String> getAttributeValues() {
		if(attributeValues==null)
			attributeValues=new HashMap<String, String>();

		return attributeValues;
	}
	public void addAttributeValues(String attribute, String inclusion) {
		if(attributeValues==null)
			attributeValues=new HashMap<String, String>();

		String existingValue = attributeValues.get(attribute);
		
		if(existingValue==null||existingValue.isEmpty())
			existingValue=inclusion;
		else
			existingValue+=MetadataConstants.stringListSeparator+inclusion;
		this.attributeValues.put(attribute, inclusion);
	}
	
	public void replaceAttributeValues(String attribute, String inclusion) {
		if(attributeValues==null)
			attributeValues=new HashMap<String, String>();

		this.attributeValues.put(attribute, inclusion);
	}
	
	public void addAttributeNotContainsValue(String attribute, String exclusion) {
		if(attributeNotContainsValues==null)
			attributeNotContainsValues=new HashMap<String, String>();

		this.attributeNotContainsValues.put(attribute, exclusion);
	}
	
	public void replaceAttributeNotContainsValue(String attribute, String exclusion) {
		if(attributeNotContainsValues==null)
			attributeNotContainsValues=new HashMap<String, String>();

		this.attributeNotContainsValues.put(attribute, exclusion);
	}
	
	
	
	public List<String> getNameExclusions() {
		String nameExclusions = attributeNotContainsValues.get(MetadataConstants.produktNameJsonPrefix);
		if(nameExclusions==null)
			return new ArrayList<String>();
		else
			return Arrays.asList(nameExclusions.split(MetadataConstants.stringListSeparator));
	}
	public void addNameExclusions(String nameExclusion) {
		this.addAttributeNotContainsValue(MetadataConstants.produktNameJsonPrefix,nameExclusion );
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
	public void addServingPhraseInclusion(String inclusion) {

			String servingNameInclusions = attributeValues.get(MetadataConstants.servingPhraseNameJsonName);
			if(servingNameInclusions==null)
				attributeValues.put(MetadataConstants.servingPhraseNameJsonName, inclusion);
			else
				attributeValues.put(MetadataConstants.servingPhraseNameJsonName, servingNameInclusions+MetadataConstants.stringListSeparator+inclusion);


		}
	
	public List<String> getServingPhraseInclusions() {
		String incs = this.getAttributeValues().get(MetadataConstants.servingPhraseNameJsonName);
		if(incs==null)
			return new ArrayList<String>();
		else
			return Arrays.asList(incs.split(MetadataConstants.stringListSeparator));

	}
		
	public void addCategoryNameInclusions(String inclusion) {
			String categoryNameInclusions = attributeValues.get(MetadataConstants.categoryNameJsonName);
			if(categoryNameInclusions==null)
				attributeValues.put(MetadataConstants.categoryNameJsonName, inclusion);
			else
				attributeValues.put(MetadataConstants.categoryNameJsonName, categoryNameInclusions+MetadataConstants.stringListSeparator+inclusion);


		}
		

		
	public List<String> getNameInclusions() {
		String nameInclusions = attributeValues.get(MetadataConstants.produktNameJsonPrefix);
		if(nameInclusions==null)
			return new ArrayList<String>();
		else
			return Arrays.asList(nameInclusions.split(MetadataConstants.stringListSeparator));

	}
	public void addNameInclusion(String nameInclusion) {
		this.addAttributeValues(MetadataConstants.produktNameJsonPrefix,nameInclusion );

	}


	public boolean check(Produkt p) {
		boolean nameHasInclusions=checkNameInclusions(p);
		boolean nameHasNoExclusions=checkNameExclusons(p);

		boolean categoryIsOk=checkCategory(p);
		boolean areAttributesOk=checkMetadataAttributesPresence(p);
		boolean isServingPhraseOk=checkServingPhraseInclusions(p);
		
		boolean retValue = nameHasInclusions&&categoryIsOk&&areAttributesOk&&nameHasNoExclusions&&isServingPhraseOk;
		if(retValue) {
			System.out.println("For produkt:"+p.getUrl()+" called "+p.getNazwa()
					+"\n condition object passed:"+this.toJsonRepresentation());
		}
		return retValue;
	}

	private boolean checkServingPhraseInclusions(Produkt p) {
		boolean phraseIsOk=true;
		List<String> servingPhraseInclusions = getServingPhraseInclusions();
		if(servingPhraseInclusions!=null&&!servingPhraseInclusions.isEmpty()) {
			for(int i=0;phraseIsOk&&i<servingPhraseInclusions.size();i++) {
				String metadane=p.getMetadata();
				JSONObject json=metadane==null?new JSONObject():new JSONObject(metadane);
				String serving = json.has(MetadataConstants.servingPhraseNameJsonName)?json.getString(MetadataConstants.servingPhraseNameJsonName):null;

				if(serving==null||!serving.toLowerCase().contains(servingPhraseInclusions.get(i).toLowerCase())) {
					phraseIsOk=false;
				}
			}
		}
		return phraseIsOk;
	}

	private boolean checkNameInclusions(Produkt p) {
		boolean nameIsOk=true;
		List<String> nameInclusions = getNameInclusions();
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
		List<String> nameExclusions = getNameExclusions();

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

		if(getAttributesPresent()!=null&&!getAttributesPresent().isEmpty()) {
			if(p==null||p.getMetadata()==null||p.getMetadata().equals("")) {
				return false;
			}
			JSONObject metadataObject=new JSONObject(p.getMetadata().toLowerCase());
			for(int i=0;areAttrsPresest&&i<getAttributesPresent().size();i++) {

				if(!metadataObject.has(getAttributesPresent().get(i).toLowerCase())) {
					areAttrsPresest=false;
				}
			}
		}
		return areAttrsPresest;
	}

	private boolean checkCategory(Produkt p) {
		List<String> categoryNameInclusions = getCategoryNameInclusions();

		if(categoryNameInclusions==null||categoryNameInclusions.isEmpty()) {
			return true;
		}
		boolean categoryIsOk=true;

		if(p.getMetadata()==null||p.getMetadata().equals("")) {
			return false;
		}else {
			String metadane=p.getMetadata();
			JSONObject json=new JSONObject(metadane);
			String category = json.has(MetadataConstants.categoryNameJsonName)?json.getString(MetadataConstants.categoryNameJsonName):"";



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
	private List<String> getCategoryNameInclusions() {
		String incs = this.getAttributeValues().get(MetadataConstants.categoryNameJsonName);
		if(incs==null)
			return new ArrayList<String>();
		else
			return Arrays.asList(incs.split(MetadataConstants.stringListSeparator));

	}

	
	
	public static Condition createNameInclusionsCondition(String...strings) {
		Condition retValue=new Condition();
		for(String a:strings) {
			retValue.addNameInclusion(a);

		}
		return retValue;

	}
	
	public static Condition createServingPhraseInclusionsCondition(String...strings) {
		Condition retValue=new Condition();
		for(String a:strings) {
			retValue.addServingPhraseInclusion(a);

		}
		return retValue;

	}
	public static Condition createCategoryNameInclusionsCondition(String... strings) {
		Condition retValue=new Condition();

		for(String a:strings) {
			retValue.addCategoryNameInclusions(a);

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
		this.attributeNotContainsValues=new HashMap<String, String>();
		
	}
}
