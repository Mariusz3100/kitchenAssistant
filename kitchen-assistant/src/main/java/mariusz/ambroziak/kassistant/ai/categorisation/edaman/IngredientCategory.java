package mariusz.ambroziak.kassistant.ai.categorisation.edaman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import mariusz.ambroziak.kassistant.ai.categorisation.MetadataConstants;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;



public class IngredientCategory {

	private String name;
	private IngredientCategory parent;
	private List<IngredientCategory> children;

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
	
	public void addChildrenConditions(List<Condition> conditions) {
		if(this.childrenConditions==null)
			this.childrenConditions =new ArrayList<Condition>();

		this.childrenConditions.addAll(conditions);
	}
	public IngredientCategory(String name) {
		super();
		this.name = name;
		this.childrenConditions=new ArrayList<Condition>();
		this.children=new ArrayList<IngredientCategory>();
		this.conditions=new ArrayList<Condition>();
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
	public List<Condition> getConditions() {
		return conditions;
	}
	public void addConditions(Condition... conditions) {
		if(this.conditions==null)
			this.conditions =new ArrayList<Condition>();

		this.conditions.addAll(Arrays.asList(conditions));
	}

	public void addConditions(List<Condition> conditions) {
		if(this.conditions==null)
			this.conditions =new ArrayList<Condition>();

		this.conditions.addAll(conditions);
	}
	
	
	public IngredientCategory assignCategoryFromTree(IngredientCategoriationData ingredient) {
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
	public boolean checkListOfConditions(List<Condition> conditions, IngredientCategoriationData ingredient) {
		if(conditions==null||conditions.isEmpty()) 
			return true;
		
		for(Condition c:conditions) {
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


	private IngredientCategory checkChildren(IngredientCategoriationData ingredient) {
		IngredientCategory match=null;
		if(getChildren()!=null&&!getChildren().isEmpty()) {
			for(IngredientCategory ck:getChildren()) {
				IngredientCategory found=ck.assignCategoryFromTree(ingredient);
				if(match!=null&&found!=null&&!found.checkIfEmpty()) {
					ProblemLogger.logProblem("Two different categories assigned for "+ingredient.getPhrase()+": "+match.getName()+" and "+found.getName());
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
		for(Condition c:getConditions()) {
			conditionsJsonArray.put(c.toJsonRepresentation());
		}
		jsonRep.put("conditions", conditionsJsonArray);

		JSONArray branchTraverseConditions=new JSONArray();
		for(Condition c:getChildrenConditions()) {
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
