package mariusz.ambroziak.kassistant.ai.categorisation.shops;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mariusz.ambroziak.kassistant.ai.categorisation.MetadataConstants;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

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
	public void addCategoryNameInclusions(String inclusion) {
		String categoryNameInclusions = attributeValues.get(MetadataConstants.categoryNameJsonName);
		if(categoryNameInclusions==null)
			attributeValues.put(MetadataConstants.categoryNameJsonName, inclusion);
		else
			attributeValues.put(MetadataConstants.categoryNameJsonName, categoryNameInclusions+MetadataConstants.stringListSeparator+inclusion);


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


	public boolean check(Produkt p) {
		boolean nameHasInclusions=checkNameInclusions(p);
		boolean nameHasNoExclusions=checkNameExclusons(p);

		boolean categoryIsOk=checkCategory(p);
		boolean areAttributesOk=checkMetadataAttributesPresence(p);
		boolean isServingPhraseOk=checkServingPhraseInclusions(p);
		boolean areIngredientInclusionsOk=checkIngredientsInclusions(p);
		boolean areIngredientExclusionsOk=checkIngredientsExclusons(p);

		boolean areIngredientRegexesOk=checkIngredientRegexes(p);
		boolean areIngredientNotMatchRegexesOk=checkIngredientNotMatchRegexes(p);

		
		boolean retValue = nameHasInclusions&&categoryIsOk&&areAttributesOk&&nameHasNoExclusions&&isServingPhraseOk
				&&areIngredientInclusionsOk&&areIngredientExclusionsOk&&areIngredientRegexesOk&&areIngredientNotMatchRegexesOk;
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
				String serving =null;
				try {
					serving = retrieveMetadataProperty(metadane,MetadataConstants.servingPhraseNameJsonName);
				}catch(JSONException e) {
					phraseIsOk=false;
				}
				if(serving==null||!serving.toLowerCase().contains(servingPhraseInclusions.get(i).toLowerCase())) {
					phraseIsOk=false;
				}
			}
		}
		return phraseIsOk;
	}
	private String retrieveMetadataProperty(String metadane,String propertyName) {
		try {
			String propertyValue;
			JSONObject json=metadane==null||metadane.isEmpty()?new JSONObject():new JSONObject(metadane);

			propertyValue = json.has(propertyName)?json.getString(propertyName):null;
			return propertyValue;
		}catch(JSONException e) {
			return "";
		}
	}


	private boolean checkIngredientsInclusions(Produkt p) {
		boolean ingredientsAreOk=true;
		List<String> ingredientsInclusions = getIngredientsInclusions();
		if(ingredientsInclusions!=null&&!ingredientsInclusions.isEmpty()) {
			for(int i=0;ingredientsAreOk&&i<ingredientsInclusions.size();i++) {
				String metadane=p.getMetadata();
				String ingredientsList =null;

				//		JSONObject json=metadane==null||metadane.isEmpty()?new JSONObject():new JSONObject(metadane);
				ingredientsList = retrieveMetadataProperty(metadane,MetadataConstants.ingredientsJsonName);

				if(ingredientsList==null||!ingredientsList.toLowerCase().contains(ingredientsInclusions.get(i).toLowerCase())) {
					ingredientsAreOk=false;
				}
			}
		}
		return ingredientsAreOk;
	}
	
	private boolean checkIngredientRegexes(Produkt p) {
		boolean ingredientsAreOk=true;
		List<String> ingredientsRegex = getIngredientsRegexes();
		if(ingredientsRegex!=null&&!ingredientsRegex.isEmpty()) {
			for(int i=0;ingredientsAreOk&&i<ingredientsRegex.size();i++) {
				String metadane=p.getMetadata();
				String ingredientsList =null;

				//		JSONObject json=metadane==null||metadane.isEmpty()?new JSONObject():new JSONObject(metadane);
				ingredientsList = retrieveMetadataProperty(metadane,MetadataConstants.ingredientsJsonName);

				if(ingredientsList==null||!ingredientsList.matches(ingredientsRegex.get(i))) {
					ingredientsAreOk=false;
				}
			}
		}
		return ingredientsAreOk;
	}

	
	
	private boolean checkIngredientNotMatchRegexes(Produkt p) {
		List<String> ingredientsNotMatchRegex = getIngredientsNotMatchRegexes();
		if(ingredientsNotMatchRegex!=null&&!ingredientsNotMatchRegex.isEmpty()) {
			for(int i=0;i<ingredientsNotMatchRegex.size();i++) {
				String metadane=p.getMetadata();
				String ingredientsList =null;

				//		JSONObject json=metadane==null||metadane.isEmpty()?new JSONObject():new JSONObject(metadane);
				ingredientsList = retrieveMetadataProperty(metadane,MetadataConstants.ingredientsJsonName);

				if(ingredientsList!=null&&ingredientsList.matches(ingredientsNotMatchRegex.get(i))) {
					return false;
				}
			}
		}
		return true;
	}
	private boolean checkIngredientsExclusons(Produkt p) {
		List<String> ingredientExclusions = getIngredientsExclusions();

		if(ingredientExclusions!=null&&!ingredientExclusions.isEmpty()) {
			for(int i=0;i<ingredientExclusions.size();i++) {

				String ingredientList =retrieveMetadataProperty(p.getMetadata(),MetadataConstants.ingredientsJsonName) ;
				if(ingredientList!=null&&ingredientList.toLowerCase().contains(ingredientExclusions.get(i).toLowerCase())) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean checkNameInclusions(Produkt p) {
		boolean nameIsOk=true;

		String productName = calculateNameOfAProduct(p);



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
	private String calculateNameOfAProduct(Produkt p) {
		String metadane=p.getMetadata();
		String productName ="";

		try {
			JSONObject json=metadane==null?new JSONObject():new JSONObject(metadane);

			if(json.has(MetadataConstants.productNameMetaPropertyName)) {
				productName= json.getString(MetadataConstants.productNameMetaPropertyName);
			}else {
				productName = p.getNazwa();
			}

		}catch(JSONException e) {
			ProblemLogger.logProblem("bad metadata for product id="+p.getP_id()+"("+p.getUrl()+")");
			productName = p.getNazwa();

		}
		return productName;
	}

	private boolean checkNameExclusons(Produkt p) {
		List<String> nameExclusions = getNameExclusions();

		if(nameExclusions!=null&&!nameExclusions.isEmpty()) {
			for(int i=0;i<nameExclusions.size();i++) {

				String productName = calculateNameOfAProduct(p);
				if(productName!=null&&productName.toLowerCase().contains(nameExclusions.get(i).toLowerCase())) {
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
