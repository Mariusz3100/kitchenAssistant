package mariusz.ambroziak.kassistant.ai.categorisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

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

	public static Map<Produkt, Category> testCategoryProduct(int id) {
		Produkt produktByShopId = TescoApiClientParticularProduct_notUsed.getProduktByShopId(id);
		produktByShopId = TescoApiClientParticularProduct_notUsed.updateParticularProduct(produktByShopId);

		Map<Produkt, Category> retValue=new HashMap<Produkt, Category>();
		Category assignedCategory = assignCategory(produktByShopId);
		retValue.put(produktByShopId, assignedCategory);

		return retValue;

	}

	public static Map<Produkt, Category> testCategoriesFor(String phrase,int limit) {
		Category cat=CategoryHierarchy.getSingletonCategoryRoot();

		ArrayList<Produkt> produkts = createProductDetailsList(phrase);

//		produkts.sort(new ProduktNameComparator());
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
		Map<Produkt,Category> retValue=new TreeMap<Produkt, Category>();


		for(Produkt p:produkts) {
			Category assignedCategory = assignCategory(p);
			retValue.put(p, assignedCategory);
		}

		return retValue;

	}





	private static Category assignCategory(Produkt p) {
		Category root=CategoryHierarchy.getSingletonCategoryRoot();

		Category result=root.assignCategoryFromTree(p);
		
		if(result.getName().equals("root")) {
			result=root.assignCategoryFromTree(p);

		}

		return result;

	}





	public static Map<Produkt, Category> testStaticCategoriesFor(String string, int i) {
		Category cat=CategoryHierarchy.initializeCategoriesStaticlly();

		ArrayList<Produkt> produkts = createProductDetailsList(string);

		Map<Produkt, Category> categories = new HashMap<Produkt, Category>();

		for(Produkt p:produkts) {
			Category assignedCategory = cat.assignCategoryFromTree(p);
			categories.put(p, assignedCategory);
		}

		return categories;

	}



}
