package mariusz.ambroziak.kassistant.ai.categorisation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.*;

import org.json.JSONObject;
import org.springframework.core.io.Resource;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import mariusz.ambroziak.kassistant.ai.FilesProvider;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;



public class Category {
	@Override
	public String toString() {
		return name;
	}
	private String name;
	private Category parent;
	private List<Category> children;
	ArrayList<String> nameInclusions;
	ArrayList<String> categoryNameInclusions;


	Category(String name) {
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
	public Category assignCategoryFromTree(Produkt p) {
		if(p==null)
			return null;
		boolean nameIsOk=checkName(p);
		boolean categoryIsOk=checkCategory(p);


		if(nameIsOk&&categoryIsOk) {
			Category child=checkChildren(p);
			if(child!=null&&!child.checkIfEmpty()) {
				return child;
			}else {
				return this;
			}
		}



		return createEmpty();
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
			String category = json.getString(MetadataConstants.categoryName);



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
	public Category createEmpty() {
		return new Category(MetadataConstants.emptyCategoryName);
	}
	public boolean checkIfEmpty() {
		return MetadataConstants.emptyCategoryName.equals(this.getName());
	}


	private Category checkChildren(Produkt p) {
		Category match=null;
		if(getChildren()!=null&&!getChildren().isEmpty()) {
			for(Category ck:getChildren()) {
				Category found=ck.assignCategoryFromTree(p);
				if(match!=null&&found!=null&&!found.checkIfEmpty()) {
					ProblemLogger.logProblem("Two different categories assigned for "+p.getUrl()+": "+match+" and "+found);
					return createEmpty();
				}else {
					if((match==null||match.checkIfEmpty())&&found!=null&&!found.checkIfEmpty()) {
						match=found;
					}
				}
			}
			return match;
		}
		return createEmpty();
	}









}
