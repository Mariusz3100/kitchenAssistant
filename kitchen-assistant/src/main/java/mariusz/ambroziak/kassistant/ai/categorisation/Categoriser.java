package mariusz.ambroziak.kassistant.ai.categorisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.tesco.TescoApiClient;

public class Categoriser {
	private static Categoriser singleton;
//	private Category root;
	
	
	
	
	


	public static void main(String [] args){

		
		System.out.println();
	}
	

	
	
	
	public static Categoriser getSingleton() {
		if(singleton==null) {
			singleton=new Categoriser();
			
		}
		return singleton;
		
	}
	
	
	
	private Categoriser() {
		super();

	}
	
	public static void testCategoriesFor(String phrase,int limit) {
		Category cat=CategoryHierarchy.getSingletonCategoryRoot();
		
		ArrayList<Produkt> produkts = TescoApiClient.getProduktsFor(phrase);
		
		
		Map<Produkt, Category> categories = assignCategories(produkts);
		
		
		System.out.println();
		
	}





	private static Map<Produkt, Category> assignCategories(ArrayList<Produkt> produkts) {
		Map<Produkt,Category> retValue=new HashMap<Produkt, Category>();
		
		
		for(Produkt p:produkts) {
			Category assignedCategory = assignCategory(p);
			retValue.put(p, assignedCategory);
		}
		
		return retValue;
		
	}





	private static Category assignCategory(Produkt p) {
		
		return CategoryHierarchy.getSingletonCategoryRoot();
		
	}
	
	
	
}
