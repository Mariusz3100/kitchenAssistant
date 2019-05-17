package mariusz.ambroziak.kassistant.ai.categorisation;

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
import mariusz.ambroziak.kassistant.model.Problem;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;



public class CategoryHierarchy {

	private static Category root;


	private CategoryHierarchy() {
		super();

	}

	public static Category getSingletonCategoryRoot() {
		if(root==null) {
			root=createCategoriesFromXmlFile();
		}
		return root;
	}

	public static void initializeCategoriesFromXml() {
		Category rootCandidate = createCategoriesFromXmlFile();
		root=rootCandidate;
	}

	public static Category createCategoriesFromXmlFile() {
		Document doc=getXmlDocumentContentHandleExceptions();

		Element xmlRoot = doc.getDocumentElement();	

		Category rootCandidate=parseCategoryXmlNode(xmlRoot);
		return rootCandidate;
	}
	public static void testXmlVsStaticInitialisation(Node node) {
		Category rootFromXml= createCategoriesFromXmlFile();
		Category rootStatically=initializeCategoriesStaticlly();
		
		testIfTwoCategoriesAreSame(rootFromXml,rootStatically);
	}


	private static void testIfTwoCategoriesAreSame(Category rootFromXml, Category rootStatically) {
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

	public static Category parseCategoryXmlNode(Node node) {
		NamedNodeMap attributesMap = node.getAttributes();

		Attr nameAttribute = (Attr) attributesMap.getNamedItem("name");
		System.out.println(nameAttribute);
		Category retValue=new Category(nameAttribute.getValue());
		NodeList childNodes = node.getChildNodes();
		Node childrenElement=null;

		for(int i=0;i<childNodes.getLength();i++) {
			Node childNode = childNodes.item(i);
			if(node.getNodeType()==Node.TEXT_NODE||node.getNodeType()==Node.COMMENT_NODE) {
				//no handling for text node now
			}else {
				if(MetadataConstants.childrenElementName.equals(childNode.getNodeName())) {
					childrenElement=childNode;
				}else if(MetadataConstants.descendantsConditionsElementName.equals(childNode.getNodeName())) {
					List<Condition> childrenConditionsPassed=parseElementWithConditions(childNode);
					retValue.addChildrenConditions(childrenConditionsPassed);
				}else if(MetadataConstants.conditionsElementName.equals(childNode.getNodeName())) {
					List<Condition> conditions=parseElementWithConditions(childNode);
					retValue.addConditions(conditions);
				}
			}
		}

		List<Category> childCategoriesResults = handleChildCategories(childrenElement);
		retValue.addChildren(childCategoriesResults);
		for(Category c:childCategoriesResults) {
			c.setParent(retValue);
		}
		return retValue;

	}

	private static Condition parseConditionElement(Node node) {
		NodeList childNodes = node.getChildNodes();
		Condition retValue=new Condition();
		for(int i=0;i<childNodes.getLength();i++) {
			Node conditionNode = childNodes.item(i);
			if(conditionNode.getNodeType()==Node.TEXT_NODE||conditionNode.getNodeType()==Node.COMMENT_NODE) {
				//no handling for text node now
			}else {
				if(MetadataConstants.departmentNameConditionElementName.equals(conditionNode.getNodeName())) {
					NamedNodeMap attributesMap = conditionNode.getAttributes();
					Node contains=attributesMap.getNamedItem(MetadataConstants.containsAttribute);
					addNewSearchPhraseToMap(retValue, MetadataConstants.categoryNameJsonPrefix,contains);
				}else if(MetadataConstants.nameConditionElementName.equals(conditionNode.getNodeName())) {
					NamedNodeMap attributesMap = conditionNode.getAttributes();
					Node contains=attributesMap.getNamedItem(MetadataConstants.containsAttribute);
					addNewSearchPhraseToMap(retValue, MetadataConstants.produktNameJsonPrefix,contains);

				}else if(MetadataConstants.notContainsAttribute.equals(conditionNode.getNodeName())) {
					NamedNodeMap attributesMap = conditionNode.getAttributes();
					Node contains=attributesMap.getNamedItem(MetadataConstants.notContainsAttribute);
					addNewSearchPhraseToMap(retValue, MetadataConstants.notContainsAttribute,contains);

				}else if(MetadataConstants.propertiesConditionElementName.equals(conditionNode.getNodeName())) {
					NamedNodeMap attributesMap = conditionNode.getAttributes();
					Node contains=attributesMap.getNamedItem(MetadataConstants.containsAttribute);
					addNewSearchPhraseToMap(retValue, MetadataConstants.containsAttribute,contains);

				}
			}
		}
		return retValue;

	}

	private static void addNewSearchPhraseToMap(Condition condition,String keyInMap, Node containsAttribute) {
		String existingCategoryConditions =null;
		if(condition==null||containsAttribute==null)
			return;
//		if(condition.getAttributeValues().containsKey(keyInMap)) {
			existingCategoryConditions = condition.getAttributeValues().get(keyInMap);
			if(existingCategoryConditions==null||existingCategoryConditions.isEmpty())
				existingCategoryConditions=containsAttribute.getTextContent();
			else
				existingCategoryConditions+=MetadataConstants.stringListSeparator+containsAttribute.getTextContent();

//		}

		condition.addAttributeValues(keyInMap, existingCategoryConditions);
	}

	//	private static void retrieveAttributesValue(Node node,String name) {
	//		NamedNodeMap attributes = node.getAttributes();
	//		
	//		return attributes.getNamedItem(name);
	//		
	//	}

	private static List<Condition> parseElementWithConditions(Node node) {
		NodeList childNodes = node.getChildNodes();
		List<Condition> retValue=new ArrayList<Condition>();
		for(int i=0;i<childNodes.getLength();i++) {
			Node childNode = childNodes.item(i);
			if(childNode.getNodeType()==Node.TEXT_NODE||childNode.getNodeType()==Node.COMMENT_NODE) {
				//no handling for text node now
			}else {
				Condition parsedCondition = parseConditionElement(childNode);
				retValue.add(parsedCondition);
			}
		}

		return retValue;
	}

	private static List<Category> handleChildCategories(Node childrenListNode) {
		List<Category> retValue=new ArrayList<Category>();

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
					Category parsedCategory = parseCategoryXmlNode(childCategory);
					retValue.add(parsedCategory);
				}else {
					ProblemLogger.logProblem("Children element contains elements besides category:"+childCategory.getNodeName());
				}
			}
		}
		return retValue; 
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
		Resource categoriesFile = FilesProvider.getInstance().getCategoriesFile();
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

	public static Category initializeCategoriesStaticlly() {
		Category root=new Category("root");

		Category raw=new Category("raw");
		raw.setParent(root);
		root.addChildren(raw);
		//raw.addCategoryNameInclusions("fresh");
		raw.addConditions(Condition.createCategoryNameInclusionsCondition("fresh","food"));


		Category processed=new Category("processed");
		processed.setParent(root);
		root.addChildren(processed);
		//		processed.addCategoryNameInclusions("processed");
		processed.addConditions(Condition.createNameInclusionsCondition("processed"));


		Category grinded=new Category("grinded");
		grinded.setParent(processed);
		processed.addChildren(grinded);
		grinded.addConditions(Condition.createNameInclusionsCondition("paste"));
		grinded.addConditions(Condition.createNameInclusionsCondition("puree"));
		grinded.addConditions(Condition.createCategoryNameInclusionsCondition("puree"));



		Category conserved=new Category("conserved");
		conserved.setParent(processed);
		processed.addChildren(conserved);
		//		conserved.addCategoryNameInclusions("canned");
		conserved.addConditions(Condition.createNameInclusionsCondition("pickles"));
		conserved.addConditions(Condition.createAttributePresentCondition("drainedWeight"));
		Condition createCategoryNameInclusionsCondition = Condition.createCategoryNameInclusionsCondition("cans");
		createCategoryNameInclusionsCondition.addNameExclusions("puree");
		conserved.addConditions(createCategoryNameInclusionsCondition);




		//		Category pickled=new Category("pickled");
		//		pickled.setParent(conserved);
		//		conserved.addChildren(pickled);
		//		pickled.addCategoryNameInclusions("pickled");


		Category dried=new Category("dried");
		dried.setParent(processed);
		processed.addChildren(dried);
		dried.addConditions(Condition.createNameInclusionsCondition("dried"));

		Category compoundDish=new Category("compoundDish");
		compoundDish.setParent(processed);
		processed.addChildren(compoundDish);
		compoundDish.addConditions(Condition.createNameInclusionsCondition("soup"));




		//------------

		Category residual=new Category("residual");
		residual.setParent(root);
		root.addChildren(residual);

		residual.addConditions(Condition.createNameInclusionsCondition("flavoured"));
		residual.addConditions(Condition.createCategoryNameInclusionsCondition("drinks"));
		residual.addConditions(Condition.createCategoryNameInclusionsCondition("household"));
		residual.addConditions(Condition.createCategoryNameInclusionsCondition("beauty"));
		//		residual.addConditions(Condition.createCategoryNameInclusionsContition("Household"));



		return root;


	}




}
