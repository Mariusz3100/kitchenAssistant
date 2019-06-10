package mariusz.ambroziak.kassistant.ai.categorisation.edaman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import mariusz.ambroziak.kassistant.ai.categorisation.MetadataConstants;

public class Condition {
	//	ArrayList<String> nameInclusions;
	//	ArrayList<String> nameExclusions;


	//	ArrayList<String> categoryNameInclusions;
	ArrayList<String> attributesPresent;
	Map<String,String> attributeRegex;
	Map<String,String> attributeRegexNotMatched;

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
		this.attributeValues.put(attribute, existingValue);
	}
	
	
	public Map<String, String> getAttributeRegexes() {
		if(attributeRegex==null)
			attributeRegex=new HashMap<String, String>();

		return attributeRegex;
	}
	public void addAttributeRegex(String attribute, String regex) {
		if(attributeRegex==null)
			attributeRegex=new HashMap<String, String>();

		String existingValue = attributeRegex.get(attribute);

		if(existingValue==null||existingValue.isEmpty())
			existingValue=regex;
		else
			existingValue+=MetadataConstants.stringListSeparator+regex;
		this.attributeRegex.put(attribute, existingValue);
	}

	public Map<String, String> getAttributeRegexNotMatched() {
		if(attributeRegexNotMatched==null)
			attributeRegexNotMatched=new HashMap<String, String>();

		return attributeRegexNotMatched;
	}
	public void addAttributeRegexNotMatched(String attribute, String regex) {
		if(attributeRegexNotMatched==null)
			attributeRegexNotMatched=new HashMap<String, String>();

		String existingValue = attributeRegexNotMatched.get(attribute);

		if(existingValue==null||existingValue.isEmpty())
			existingValue=regex;
		else
			existingValue+=MetadataConstants.stringListSeparator+regex;
		this.attributeRegexNotMatched.put(attribute, existingValue);
	}



	public void replaceAttributeValues(String attribute, String inclusion) {
		if(attributeValues==null)
			attributeValues=new HashMap<String, String>();

		this.attributeValues.put(attribute, inclusion);
	}

	public void addAttributeNotContainsValue(String attribute, String exclusion) {
		if(attributeNotContainsValues==null)
			attributeNotContainsValues=new HashMap<String, String>();

		String existingValue = attributeNotContainsValues.get(attribute);

		if(existingValue==null||existingValue.isEmpty())
			existingValue=exclusion;
		else
			existingValue+=MetadataConstants.stringListSeparator+exclusion;

		this.attributeNotContainsValues.put(attribute, existingValue);
	}

	public void replaceAttributeNotContainsValue(String attribute, String exclusion) {
		if(attributeNotContainsValues==null)
			attributeNotContainsValues=new HashMap<String, String>();

		this.attributeNotContainsValues.put(attribute, exclusion);
	}



	public List<String> getNameExclusions() {
		String nameExclusions = attributeNotContainsValues.get(MetadataConstants.conditionProduktNameMapKey);
		if(nameExclusions==null)
			return new ArrayList<String>();
		else
			return Arrays.asList(nameExclusions.split(MetadataConstants.stringListSeparator));
	}
	public Map<String, String> getAttributeNotContainsValues() {
		return attributeNotContainsValues;
	}
	public void addNameExclusions(String nameExclusion) {
		this.addAttributeNotContainsValue(MetadataConstants.conditionProduktNameMapKey,nameExclusion );
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
	public List<String> getIngredientsRegexes() {
		String regexes = this.getAttributeRegexes().get(MetadataConstants.ingredientsConditionElementName);
		if(regexes==null)
			return new ArrayList<String>();
		else
			return Arrays.asList(regexes.split(MetadataConstants.stringListSeparator));

	}
	
	public List<String> getIngredientsNotMatchRegexes() {
		String regexes = this.getAttributeRegexNotMatched().get(MetadataConstants.ingredientsConditionElementName);
		if(regexes==null)
			return new ArrayList<String>();
		else
			return Arrays.asList(regexes.split(MetadataConstants.stringListSeparator));

	}
	public List<String> getIngredientsInclusions() {
		String incs = this.getAttributeValues().get(MetadataConstants.ingredientsConditionElementName);
		if(incs==null)
			return new ArrayList<String>();
		else
			return Arrays.asList(incs.split(MetadataConstants.stringListSeparator));

	}
	public List<String> getIngredientsExclusions() {
		String incs = this.getAttributeNotContainsValues().get(MetadataConstants.ingredientsConditionElementName);
		if(incs==null)
			return new ArrayList<String>();
		else
			return Arrays.asList(incs.split(MetadataConstants.stringListSeparator));

	}




	public List<String> getNameInclusions() {
		String nameInclusions = attributeValues.get(MetadataConstants.conditionProduktNameMapKey);
		if(nameInclusions==null)
			return new ArrayList<String>();
		else
			return Arrays.asList(nameInclusions.split(MetadataConstants.stringListSeparator));

	}
	public void addNameInclusion(String nameInclusion) {
		this.addAttributeValues(MetadataConstants.conditionProduktNameMapKey,nameInclusion );

	}


	public boolean check(IngredientCategoriationData ingredient) {
		boolean nameHasInclusions=checkNameInclusions(ingredient);
		boolean nameHasNoExclusions=checkNameExclusons(ingredient);

		
		boolean retValue = nameHasInclusions&&nameHasNoExclusions;
		if(retValue) {
			System.out.println("For ingredient:"+ingredient.getPhrase()
			+"\n condition object passed:"+this.toJsonRepresentation());
		}
		return retValue;
	}


	private boolean checkNameInclusions(IngredientCategoriationData ingredient) {
		boolean nameIsOk=true;

		String productName = ingredient.getPhrase();



		List<String> nameInclusions = getNameInclusions();
		if(nameInclusions!=null&&!nameInclusions.isEmpty()) {
			for(int i=0;nameIsOk&&i<nameInclusions.size();i++) {

				if(productName==null||!productName.toLowerCase().contains(nameInclusions.get(i).toLowerCase())) {
					nameIsOk=false;
				}
			}
		}
		return nameIsOk;
	}

	private boolean checkNameExclusons(IngredientCategoriationData ingredient) {
		List<String> nameExclusions = getNameExclusions();

		if(nameExclusions!=null&&!nameExclusions.isEmpty()) {
			for(int i=0;i<nameExclusions.size();i++) {

				String productName = ingredient.getPhrase();
				if(productName!=null&&productName.toLowerCase().contains(nameExclusions.get(i).toLowerCase())) {
					return false;
				}
			}
		}
		return true;
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

		JSONArray attributeNotContainsValuesArray=new JSONArray();
		for(Entry<String, String> c:getAttributeNotContainsValues().entrySet()) {
			attributeNotContainsValuesArray.put(c.getKey()+"='"+c.getValue()+"'");
		}

		retValue.put("attributeValuesNotContains", attributeNotContainsValuesArray);

		JSONArray attributeRegexMap=new JSONArray();
		for(Entry<String, String> c:getAttributeRegexes().entrySet()) {
			attributeRegexMap.put(c.getKey()+"='"+c.getValue()+"'");
		}

		retValue.put("attributeRegex", attributeRegexMap);
		
		JSONArray attributeRegexNotMatchMap=new JSONArray();
		for(Entry<String, String> c:getAttributeRegexNotMatched().entrySet()) {
			attributeRegexNotMatchMap.put(c.getKey()+"='"+c.getValue()+"'");
		}

		retValue.put("attributeRegexNotMatch", attributeRegexNotMatchMap);

	
		
		return retValue;

	}
	public Condition() {
		super();
		this.attributesPresent=new ArrayList<String>();
		this.attributeValues=new HashMap<String, String>();
		this.attributeNotContainsValues=new HashMap<String, String>();

	}
}
