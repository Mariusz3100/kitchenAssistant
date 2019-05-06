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



public class CategoryHierarchy {
	
	private static Category root;
	
	
	private CategoryHierarchy() {
		super();
	
	}
	
	public static Category getSingletonCategoryRoot() {
		if(root==null) {
			initializeCategories();
		}
		return root;
	}

	public static void initializeCategories() {
		root=new Category("root");
		
		Category raw=new Category("raw");
		raw.setParent(root);
		root.addChildren(raw);
		//raw.addCategoryNameInclusions("fresh");
		raw.addConditions(Condition.createCategoryNameInclusionsContition("fresh","food"));

		
		Category processed=new Category("processed");
		processed.setParent(root);
		root.addChildren(processed);
//		processed.addCategoryNameInclusions("processed");
		processed.addConditions(Condition.createNameInclusionsContition("processed"));

		
		Category grinded=new Category("grinded");
		grinded.setParent(processed);
		processed.addChildren(grinded);
		grinded.addConditions(Condition.createNameInclusionsContition("paste"));

		
		Category conserved=new Category("conserved");
		conserved.setParent(processed);
		processed.addChildren(conserved);
//		conserved.addCategoryNameInclusions("canned");
		conserved.addConditions(Condition.createNameInclusionsContition("pickles"));
		conserved.addConditions(Condition.createAttributePresentCondition("drainedWeight"));

		
		
		
//		Category pickled=new Category("pickled");
//		pickled.setParent(conserved);
//		conserved.addChildren(pickled);
//		pickled.addCategoryNameInclusions("pickled");

		
		Category dried=new Category("dried");
		dried.setParent(processed);
		processed.addChildren(dried);
		dried.addConditions(Condition.createNameInclusionsContition("dried"));

		
		
		
		//------------
		
		Category residual=new Category("residual");
		residual.setParent(root);
		root.addChildren(residual);
		
		residual.addConditions(Condition.createNameInclusionsContition("flavoured"));
		residual.addConditions(Condition.createCategoryNameInclusionsContition("drinks"));
		residual.addConditions(Condition.createCategoryNameInclusionsContition("household"));
		residual.addConditions(Condition.createCategoryNameInclusionsContition("beauty"));
//		residual.addConditions(Condition.createCategoryNameInclusionsContition("Household"));
		
		


		
		
	}

	
	
	
}
