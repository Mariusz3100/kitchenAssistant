package mariusz.ambroziak.kassistant.ai.categorisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.tesco.TescoApiClient;
import mariusz.ambroziak.kassistant.tesco.TescoApiClientParticularProduct_notUsed;

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
	
	public static Map<Produkt, Category> testCategoriesFor(String phrase,int limit) {
		Category cat=CategoryHierarchy.getSingletonCategoryRoot();
		
		ArrayList<Produkt> produkts = createProductDetailsList(phrase);
		
		
		Map<Produkt, Category> categories = assignCategories(produkts);
		
		return categories;
		
		
	}





	private static ArrayList<Produkt> createProductDetailsList(String phrase) {
		ArrayList<Produkt> produkts = TescoApiClient.getProduktsFor(phrase);
		
		for(int i=0;i<produkts.size();i++) {
			Produkt p=produkts.get(i);
			p=TescoApiClientParticularProduct_notUsed.updateParticularProduct(p);
		}
		
		return produkts;
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
		Category root=CategoryHierarchy.getSingletonCategoryRoot();
		
		Category result=root.assignCategoryFromTree(p);
		
		
		return result;
		
	}
	
	
	
}
