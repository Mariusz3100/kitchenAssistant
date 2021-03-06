package mariusz.ambroziak.kassistant.ai.categorisation.edaman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import mariusz.ambroziak.kassistant.ai.categorisation.MetadataConstants;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;



public class IngredientCategory {
	private String name;
	private IngredientCategory parent;
	private List<IngredientCategory> children;


	private List<IngredientCondition> conditions;
	private List<IngredientCondition> childrenConditions;


	public static Map<String,IngredientCategory> categories=new HashMap<String,IngredientCategory>();


	public List<IngredientCondition> getChildrenConditions() {
		return childrenConditions;
	}
	public void addChildrenConditions(IngredientCondition condition) {
		if(this.childrenConditions==null)
			this.childrenConditions =new ArrayList<IngredientCondition>();

		this.childrenConditions.add(condition);
	}
	
	public void addChildrenConditions(List<IngredientCondition> conditions) {
		if(this.childrenConditions==null)
			this.childrenConditions =new ArrayList<IngredientCondition>();

		this.childrenConditions.addAll(conditions);
	}
	public IngredientCategory(String name) {
		super();
		this.name = name;
		this.childrenConditions=new ArrayList<IngredientCondition>();
		this.children=new ArrayList<IngredientCategory>();
		this.conditions=new ArrayList<IngredientCondition>();
		categories.put(name, this);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public IngredientCategory getParent() {
		return parent;
	}
	public void setParent(IngredientCategory parent) {
		this.parent = parent;
	}
	public List<IngredientCategory> getChildren() {

		return children;
	}
	public void addChildren(IngredientCategory child) {
		if(children==null)
			children=new ArrayList<IngredientCategory>();

		this.children.add(child);
	}
	public void addChildren(List<IngredientCategory> childs) {
		if(children==null)
			children=new ArrayList<IngredientCategory>();

		this.children.addAll(childs);
	}
	public List<IngredientCondition> getConditions() {
		return conditions;
	}
	public void addConditions(IngredientCondition... conditions) {
		if(this.conditions==null)
			this.conditions =new ArrayList<IngredientCondition>();

		this.conditions.addAll(Arrays.asList(conditions));
	}

	public void addConditions(List<IngredientCondition> conditions) {
		if(this.conditions==null)
			this.conditions =new ArrayList<IngredientCondition>();

		this.conditions.addAll(conditions);
	}
	
	
	public IngredientCategory assignCategoryFromTree(IngredientUnparsedApiDetails ingredient) {
		if(ingredient==null)
			return null;

		if(checkListOfConditions(childrenConditions, ingredient)) {
			IngredientCategory child=checkChildren(ingredient);
			if(child!=null&&!child.checkIfEmpty()) {
				return child;
			}
		}
		
		if(conditions==null||conditions.isEmpty()) {
			return this;
		}else {
			if(checkListOfConditions(conditions, ingredient)) {
				return this;
			}
		}

		return createEmpty();
	}
	public boolean checkListOfConditions(List<IngredientCondition> conditions, IngredientUnparsedApiDetails ingredient) {
		if(conditions==null||conditions.isEmpty()) 
			return true;
		
		for(IngredientCondition c:conditions) {
			if(c.check(ingredient)) {
				return true;
			}
		}
		return false;
	}

	public IngredientCategory createEmpty() {
		return new IngredientCategory(MetadataConstants.emptyCategoryName);
	}
	public boolean checkIfEmpty() {
		return MetadataConstants.emptyCategoryName.equals(this.getName());
	}


	private IngredientCategory checkChildren(IngredientUnparsedApiDetails ingredient) {
		IngredientCategory match=null;
		if(getChildren()!=null&&!getChildren().isEmpty()) {
			for(IngredientCategory ck:getChildren()) {
				IngredientCategory found=ck.assignCategoryFromTree(ingredient);
				if(match!=null&&found!=null&&!found.checkIfEmpty()) {
					ProblemLogger.logProblem("Two different categories assigned for "+ingredient.getOriginalPhrase()+": "+match.getName()+" and "+found.getName());
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


	
	public JSONObject toJsonRepresentation() {
		JSONObject jsonRep=new JSONObject();
		jsonRep.put("name", getName());
		jsonRep.put("parent", getParent()==null?"missing":getParent().getName());

		
		JSONArray childrenJsonArray=new JSONArray();
		for(IngredientCategory c:getChildren()) {
			childrenJsonArray.put(c.toJsonRepresentation());
		}
		jsonRep.put("children", childrenJsonArray);
		
		JSONArray conditionsJsonArray=new JSONArray();
		for(IngredientCondition c:getConditions()) {
			conditionsJsonArray.put(c.toJsonRepresentation());
		}
		jsonRep.put("conditions", conditionsJsonArray);

		JSONArray branchTraverseConditions=new JSONArray();
		for(IngredientCondition c:getChildrenConditions()) {
			branchTraverseConditions.put(c.toJsonRepresentation());
		}
		jsonRep.put("branchTraverseCondition", branchTraverseConditions);

		return jsonRep;
	}

	
	@Override
	public String toString() {
		return name;
	}


}
