package mariusz.ambroziak.kassistant.ai.categorisation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.*;

import org.springframework.core.io.Resource;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import mariusz.ambroziak.kassistant.ai.FilesProvider;



public class Category {
	private String name;
	private Category parent;
	private List<Category> children;
	
	private static Category root;
	
	
	private Category(String name) {
		super();
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}
	public List<Category> getChildren() {
		return children;
	}
	public void addChildren(Category child) {
		if(children==null)
			children=new ArrayList<Category>();
		
		this.children.add(child);
	}
	
	public static Category getSingletonRoot() {
		if(root==null) {
			initializeCategories();
		}
		return root;
	}
	
	public static void initializeCategories() {
		Resource categoriesFile = FilesProvider.getInstance().getCategoriesFile();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        Document doc;
		try {
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			doc = dBuilder.parse(categoriesFile.getInputStream());
	        Element documentElement = doc.getDocumentElement();

	        NodeList childNodes = documentElement.getChildNodes();
	        
	        InputStream inputStream = categoriesFile.getInputStream();
			BufferedReader br=new BufferedReader(new InputStreamReader(inputStream));
			List<String> content=new ArrayList<String>();
			String temp=br.readLine();
			while(temp!=null) {
				content.add(temp);
				temp=br.readLine();
			}
			
	        System.out.println();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
	
	}
	
}
