package mariusz.ambroziak.kassistant.ai.categorisation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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

	private List<Condition> conditions;
	private List<Condition> childrenConditions;



	public List<Condition> getChildrenConditions() {
		return childrenConditions;
	}
	public void addChildrenConditions(Condition condition) {
		if(this.childrenConditions==null)
			this.childrenConditions =new ArrayList<Condition>();

		this.childrenConditions.add(condition);
	}
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
	public List<Condition> getConditions() {
		return conditions;
	}
	public void addConditions(Condition... conditions) {
		if(this.conditions==null)
			this.conditions =new ArrayList<Condition>();

		this.conditions.addAll(Arrays.asList(conditions));
	}
	public Category assignCategoryFromTree(Produkt p) {
		if(p==null)
			return null;




		if(childrenConditions==null||childrenConditions.isEmpty()) {
			Category child=checkChildren(p);
			if(child!=null&&!child.checkIfEmpty()) {
				return child;
			}
		}
		
		if(conditions==null||conditions.isEmpty()) {
			return this;
		}else {
			for(Condition c:conditions) {
				if(c.check(p)) {
					return this;
				}
			}
		}









		return createEmpty();
	}
	//	private Category conditionsPassed(Produkt p) {
	//		Category child=checkChildren(p);
	//		if(child!=null&&!child.checkIfEmpty())
	//			return child;
	//		else				
	//			return null;
	//	}
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
