package mariusz.ambroziak.kassistant.ai.categorisation.edaman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.*;

import org.springframework.core.io.Resource;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import mariusz.ambroziak.kassistant.ai.FilesProvider;
import mariusz.ambroziak.kassistant.ai.categorisation.MetadataConstants;
import mariusz.ambroziak.kassistant.model.Problem;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;



public class IngredientCategoryHierarchy {

	private static IngredientCategory root;


	private IngredientCategoryHierarchy() {
		super();

	}

	public static IngredientCategory getSingletonCategoryRoot() {
		if(root==null) {
			root=createCategoriesFromXmlFile();
		}
		return root;
	}

	public static void initializeCategoriesFromXml() {
		IngredientCategory rootCandidate = createCategoriesFromXmlFile();
		root=rootCandidate;
	}

	
	public static IngredientCategory createCategoryFromTestXmlFile() {
		Document doc=getXmlTestDocumentContentHandleExceptions();
		
		Element xmlRoot = doc.getDocumentElement();	

		IngredientCategory rootCandidate=parseCategoryXmlNode(xmlRoot);
		return rootCandidate;
		
	}
	public static IngredientCategory createCategoriesFromXmlFile() {
		Document doc=getXmlDocumentContentHandleExceptions();

		Element xmlRoot = doc.getDocumentElement();	

		IngredientCategory rootCandidate=parseCategoryXmlNode(xmlRoot);
		return rootCandidate;
	}
	public static void testXmlVsStaticInitialisation(Node node) {
		IngredientCategory rootFromXml= createCategoriesFromXmlFile();
		IngredientCategory rootStatically=null;//initializeCategoriesStaticlly();

		testIfTwoCategoriesAreSame(rootFromXml,rootStatically);
	}


	private static void testIfTwoCategoriesAreSame(IngredientCategory rootFromXml, IngredientCategory rootStatically) {
		//		if(rootFromXml.getName()==rootStatically.getName()
		//				||rootFromXml.getName().equals(rootStatically.getName())) {
		//			if(rootFromXml.getChildren())
		//			
		//			for(int i=0;i<) {
		//				
		//			}
		//		}else {
		//			ProblemLogger.logProblem("not equal category names");
		//			
		//		}
		//		


	}

	public static IngredientCategory parseCategoryXmlNode(Node node) {
		NamedNodeMap attributesMap = node.getAttributes();

		Attr nameAttribute = (Attr) attributesMap.getNamedItem("name");
		System.out.println(nameAttribute);
		IngredientCategory retValue=new IngredientCategory(nameAttribute.getValue());
		NodeList childNodes = node.getChildNodes();
		Node childrenElement=null;

		for(int i=0;i<childNodes.getLength();i++) {
			Node childNode = childNodes.item(i);
			if(childNode.getNodeType()==Node.TEXT_NODE||childNode.getNodeType()==Node.COMMENT_NODE) {
				//no handling for text node now
			}else {
				if(MetadataConstants.childrenElementName.equals(childNode.getNodeName())) {
					childrenElement=childNode;
				}else if(MetadataConstants.descendantsConditionsElementName.equals(childNode.getNodeName())) {
					List<IngredientCondition> childrenConditionsPassed=parseElementWithConditions(childNode);
					retValue.addChildrenConditions(childrenConditionsPassed);
				}else if(MetadataConstants.conditionsElementName.equals(childNode.getNodeName())) {
					List<IngredientCondition> conditions=parseElementWithConditions(childNode);
					retValue.addConditions(conditions);
				}
			}
		}

		List<IngredientCategory> childCategoriesResults = handleChildCategories(childrenElement);
		retValue.addChildren(childCategoriesResults);
		for(IngredientCategory c:childCategoriesResults) {
			c.setParent(retValue);
		}
		return retValue;

	}

	private static IngredientCondition parseConditionElement(Node node) {
		NodeList childNodes = node.getChildNodes();
		IngredientCondition retValue=new IngredientCondition();
		for(int i=0;i<childNodes.getLength();i++) {
			Node conditionNode = childNodes.item(i);
			if(conditionNode.getNodeType()==Node.TEXT_NODE||conditionNode.getNodeType()==Node.COMMENT_NODE) {
				//no handling for text node now
			}else {
				if(MetadataConstants.ingredientsConditionElementName.equals(conditionNode.getNodeName())) {
					NamedNodeMap attributesMap = conditionNode.getAttributes();
					Node contains=attributesMap.getNamedItem(MetadataConstants.containsAttribute);
					Node notContains=attributesMap.getNamedItem(MetadataConstants.notContainsAttribute);
					Node regex=attributesMap.getNamedItem(MetadataConstants.regexMatchedAttribute);


					if(contains!=null) {
						addNewSearchPhraseToMap(retValue, MetadataConstants.ingredientsJsonName,contains);
					}
					if(notContains!=null) {
						addNewExclusionPhraseToMap(retValue, MetadataConstants.ingredientsJsonName,notContains);
					}
					if(regex!=null) {
						addNewRegexToMap(retValue, MetadataConstants.ingredientsJsonName,regex);
					}
					Node notRegex=attributesMap.getNamedItem(MetadataConstants.regexNotMatchedAttribute);

					if(notRegex!=null) {
						addNewRegexExclusionToMap(retValue, MetadataConstants.ingredientsJsonName,notRegex);
					}
				}else if(MetadataConstants.departmentNameConditionElementName.equals(conditionNode.getNodeName())) {
					NamedNodeMap attributesMap = conditionNode.getAttributes();
					Node contains=attributesMap.getNamedItem(MetadataConstants.containsAttribute);
					Node notContains=attributesMap.getNamedItem(MetadataConstants.notContainsAttribute);
					Node regex=attributesMap.getNamedItem(MetadataConstants.regexMatchedAttribute);

					if(regex!=null) {
						addNewRegexToMap(retValue, MetadataConstants.categoryNameJsonName,regex);
					}
					if(contains!=null) {
						addNewSearchPhraseToMap(retValue, MetadataConstants.categoryNameJsonName,contains);
					}
					if(notContains!=null) {
						addNewExclusionPhraseToMap(retValue, MetadataConstants.categoryNameJsonName,notContains);
					}
					Node notRegex=attributesMap.getNamedItem(MetadataConstants.regexNotMatchedAttribute);

					if(notRegex!=null) {
						addNewRegexExclusionToMap(retValue, MetadataConstants.categoryNameJsonName,notRegex);
					}
				}else if(MetadataConstants.nameConditionElementName.equals(conditionNode.getNodeName())) {
					NamedNodeMap attributesMap = conditionNode.getAttributes();
					Node contains=attributesMap.getNamedItem(MetadataConstants.containsAttribute);
					Node notContains=attributesMap.getNamedItem(MetadataConstants.notContainsAttribute);

					
					Node regex=attributesMap.getNamedItem(MetadataConstants.regexMatchedAttribute);

					if(regex!=null) {
						addNewRegexToMap(retValue, MetadataConstants.conditionProduktNameMapKey,regex);
					}
					if(contains!=null) {
						addNewSearchPhraseToMap(retValue, MetadataConstants.conditionProduktNameMapKey,contains);
					}
					if(notContains!=null) {
						addNewExclusionPhraseToMap(retValue, MetadataConstants.conditionProduktNameMapKey,notContains);
					}
					Node notRegex=attributesMap.getNamedItem(MetadataConstants.regexNotMatchedAttribute);

					if(notRegex!=null) {
						addNewRegexExclusionToMap(retValue, MetadataConstants.conditionProduktNameMapKey,notRegex);
					}
				}else if(MetadataConstants.propertyPresentConditionElementName.equals(conditionNode.getNodeName())) {
					NamedNodeMap attributesMap = conditionNode.getAttributes();
					Node contains=attributesMap.getNamedItem(MetadataConstants.containsAttribute);

					retValue.addAttributePresent(contains.getTextContent());
				}else if(MetadataConstants.servingPhraseConditionElementName.equals(conditionNode.getNodeName())) {
					NamedNodeMap attributesMap = conditionNode.getAttributes();
					Node contains=attributesMap.getNamedItem(MetadataConstants.containsAttribute);
					Node notContains=attributesMap.getNamedItem(MetadataConstants.notContainsAttribute);
					Node regex=attributesMap.getNamedItem(MetadataConstants.regexMatchedAttribute);

					if(regex!=null) {
						addNewRegexToMap(retValue, MetadataConstants.servingPhraseNameJsonName,regex);
					}
					
					if(contains!=null) {
						addNewSearchPhraseToMap(retValue, MetadataConstants.servingPhraseNameJsonName,contains);
					}
					if(notContains!=null) {
						addNewExclusionPhraseToMap(retValue, MetadataConstants.servingPhraseNameJsonName,notContains);
					}
					Node notRegex=attributesMap.getNamedItem(MetadataConstants.regexNotMatchedAttribute);

					if(notRegex!=null) {
						addNewRegexExclusionToMap(retValue, MetadataConstants.servingPhraseNameJsonName,notRegex);
					}
					
				}else if(MetadataConstants.unparsedPhraseConditionElementName.equals(conditionNode.getNodeName())) {
					NamedNodeMap attributesMap = conditionNode.getAttributes();
					Node contains=attributesMap.getNamedItem(MetadataConstants.containsAttribute);
					Node notContains=attributesMap.getNamedItem(MetadataConstants.notContainsAttribute);
					Node regex=attributesMap.getNamedItem(MetadataConstants.regexMatchedAttribute);

					if(regex!=null) {
						addNewRegexToMap(retValue, MetadataConstants.unparsedPhraseNameJsonName,regex);
					}
					
					if(contains!=null) {
						addNewSearchPhraseToMap(retValue, MetadataConstants.unparsedPhraseNameJsonName,contains);
					}
					if(notContains!=null) {
						addNewExclusionPhraseToMap(retValue, MetadataConstants.unparsedPhraseNameJsonName,notContains);
					}
					Node notRegex=attributesMap.getNamedItem(MetadataConstants.regexNotMatchedAttribute);

					if(notRegex!=null) {
						addNewRegexExclusionToMap(retValue, MetadataConstants.unparsedPhraseNameJsonName,notRegex);
					}
					
				}
			}
		}
		return retValue;

	}

	private static void addNewSearchPhraseToMap(IngredientCondition condition,String keyInMap, Node containsAttribute) {
		String existingCategoryConditions =null;
		if(condition==null||containsAttribute==null)
		{
			ProblemLogger.logProblem("Wow, either condition or addon is null, how??");
			return;
		}
//		existingCategoryConditions = condition.getAttributeValues().get(keyInMap);
//		if(existingCategoryConditions==null||existingCategoryConditions.isEmpty())
//			existingCategoryConditions=containsAttribute.getTextContent();
//		else
//			existingCategoryConditions+=MetadataConstants.stringListSeparator+containsAttribute.getTextContent();
		condition.addAttributeValues(keyInMap, containsAttribute.getTextContent());
	}

	
	private static void addNewExclusionPhraseToMap(IngredientCondition condition,String keyInMap, Node containsAttribute) {
		String existingCategoryConditions =null;
		if(condition==null||containsAttribute==null)
		{
			ProblemLogger.logProblem("Wow, either condition or addon is null, how??");
			return;
		}
//		existingCategoryConditions = condition.getAttributeNotContainsValues().get(keyInMap);
//		if(existingCategoryConditions==null||existingCategoryConditions.isEmpty())
//			existingCategoryConditions=containsAttribute.getTextContent();
//		else
//			existingCategoryConditions+=MetadataConstants.stringListSeparator+containsAttribute.getTextContent();
		condition.addAttributeNotContainsValue(keyInMap, containsAttribute.getTextContent());
	}
	
	private static void addNewRegexToMap(IngredientCondition condition,String keyInMap, Node regex) {
		String existingCategoryConditions =null;
		if(condition==null||regex==null)
		{
			ProblemLogger.logProblem("Wow, either condition or addon is null, how??");
			return;
		}
//		existingCategoryConditions = condition.getAttributeRegexes().get(keyInMap);
//		if(existingCategoryConditions==null||existingCategoryConditions.isEmpty())
//			existingCategoryConditions=regex.getTextContent();
//		else
			//existingCategoryConditions+=MetadataConstants.stringListSeparator+regex.getTextContent();
		condition.addAttributeRegex(keyInMap, regex.getTextContent());
	}
	
	private static void addNewRegexExclusionToMap(IngredientCondition condition,String keyInMap, Node notRegex) {
		String existingCategoryConditions =null;
		if(condition==null||notRegex==null)
		{
			ProblemLogger.logProblem("Wow, either condition or addon is null, how??");
			return;
		}
//		existingCategoryConditions = condition.getAttributeRegexes().get(keyInMap);
//		if(existingCategoryConditions==null||existingCategoryConditions.isEmpty())
//			existingCategoryConditions=notRegex.getTextContent();
//		else
//			existingCategoryConditions+=MetadataConstants.stringListSeparator+notRegex.getTextContent();
		condition.addAttributeRegexNotMatched(keyInMap, notRegex.getTextContent());
	}
	//	private static void retrieveAttributesValue(Node node,String name) {
	//		NamedNodeMap attributes = node.getAttributes();
	//		
	//		return attributes.getNamedItem(name);
	//		
	//	}

	private static List<IngredientCondition> parseElementWithConditions(Node node) {
		NodeList childNodes = node.getChildNodes();
		List<IngredientCondition> retValue=new ArrayList<IngredientCondition>();
		for(int i=0;i<childNodes.getLength();i++) {
			Node childNode = childNodes.item(i);
			if(childNode.getNodeType()==Node.TEXT_NODE||childNode.getNodeType()==Node.COMMENT_NODE) {
				//no handling for text node now
			}else {
				IngredientCondition parsedCondition = parseConditionElement(childNode);
				retValue.add(parsedCondition);
			}
		}

		return retValue;
	}

	private static List<IngredientCategory> handleChildCategories(Node childrenListNode) {
		List<IngredientCategory> retValue=new ArrayList<IngredientCategory>();

		NodeList childCategories = childrenListNode.getChildNodes();
		if(childCategories==null||childCategories.getLength()<1) {
			return retValue;
		}

		for(int i=0;i<childCategories.getLength();i++) {
			Node childCategory = childCategories.item(i);
			if(childCategory.getNodeType()==Node.TEXT_NODE||childCategory.getNodeType()==Node.COMMENT_NODE) {
				//no handling for text node now
			}else {
				if(MetadataConstants.categoryElementName.equals(childCategory.getNodeName())) {
					IngredientCategory parsedCategory = parseCategoryXmlNode(childCategory);
					retValue.add(parsedCategory);
				}else {
					ProblemLogger.logProblem("Children element contains elements besides category:"+childCategory.getNodeName());
				}
			}
		}
		return retValue; 
	}
	private static Document getXmlTestDocumentContentHandleExceptions() {
		String xmlContent = getTestXmlContent();

		DocumentBuilderFactory factory =DocumentBuilderFactory.newInstance();
		Document doc=null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();

			doc = builder.parse(new StringBufferInputStream(xmlContent));
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			ProblemLogger.logProblem("Some unexpected exception trying to import xml categories file");
			ProblemLogger.logStackTrace(e.getStackTrace());

		}

		return doc;
	}


	private static Document getXmlDocumentContentHandleExceptions() {
		String xmlContent = getXmlContent();

		DocumentBuilderFactory factory =DocumentBuilderFactory.newInstance();
		Document doc=null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();

			doc = builder.parse(new StringBufferInputStream(xmlContent));
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			ProblemLogger.logProblem("Some unexpected exception trying to import xml categories file");
			ProblemLogger.logStackTrace(e.getStackTrace());

		}

		return doc;
	}

	private static String getXmlContent() {
		Resource categoriesFile = FilesProvider.getInstance().getEdamanCategoriesFile();
		StringBuilder content=new StringBuilder();

		InputStream inputStream;
		try {
			inputStream = categoriesFile.getInputStream();

			BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
			String temp=br.readLine();
			while(temp!=null) {
				content.append(temp);
				temp=br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return content.toString();
	}
	
	
	private static String getTestXmlContent() {
		Resource categoriesFile = FilesProvider.getInstance().getCategoriesTestFile();
		StringBuilder content=new StringBuilder();

		InputStream inputStream;
		try {
			inputStream = categoriesFile.getInputStream();

			BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
			String temp=br.readLine();
			while(temp!=null) {
				content.append(temp);
				temp=br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return content.toString();
	}




}
