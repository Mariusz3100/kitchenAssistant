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
		
		Category processed=new Category("processed");
		processed.setParent(root);
		root.addChildren(processed);
		
		Category conserved=new Category("conserved");
		conserved.setParent(processed);
		processed.addChildren(conserved);
		
		Category pickled=new Category("pickled");
		pickled.setParent(conserved);
		conserved.addChildren(pickled);
		
		
		Category dried=new Category("dried");
		dried.setParent(processed);
		processed.addChildren(dried);
		
		
		
		
		//------------
		
		Category residual=new Category("residual");
		residual.setParent(root);
		root.addChildren(residual);
		
		
		
	}

	
	
	
}
