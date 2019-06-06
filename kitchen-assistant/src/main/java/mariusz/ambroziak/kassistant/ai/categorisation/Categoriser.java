package mariusz.ambroziak.kassistant.ai.categorisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.shopcom.ShopComApiClient;
import mariusz.ambroziak.kassistant.shopcom.ShopComApiClientParticularProduct;
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

	public static Map<Produkt, Category> testTescoCategoryProduct(String url) {
		Produkt produktByShopId = TescoApiClientParticularProduct_notUsed.getProduktByUrlWithExtensiveMetadata(url);
//		produktByShopId = TescoApiClientParticularProduct_notUsed.updateParticularProduct(produktByShopId);

		Map<Produkt, Category> retValue=new HashMap<Produkt, Category>();
		Category assignedCategory = assignCategory(produktByShopId);
		retValue.put(produktByShopId, assignedCategory);

		return retValue;

	}
	
	public static Map<Produkt, Category> testCategoryShopComProduct(String id) {
//		Produkt produktByShopId = TescoApiClientParticularProduct_notUsed.getProduktByUrlWithExtensiveMetadata(url);
//		produktByShopId = TescoApiClientParticularProduct_notUsed.updateParticularProduct(produktByShopId);
		Produkt produktByShopId =ShopComApiClientParticularProduct.getProduktByShopId(id);
		Map<Produkt, Category> retValue=new HashMap<Produkt, Category>();
		Category assignedCategory = assignCategory(produktByShopId);
		retValue.put(produktByShopId, assignedCategory);

		return retValue;

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

	

	public static Map<Produkt, Category> testCategoriesFor(List<String> urls) {
		Category cat=CategoryHierarchy.getSingletonCategoryRoot();

		ArrayList<Produkt> produkts = new ArrayList<>();
		
		for(int i=0;i<produkts.size();i++) {
			Produkt p=produkts.get(i);
			p=TescoApiClientParticularProduct_notUsed.updateParticularProduct(p);
		}
		
//		produkts.sort(new ProduktNameComparator());
		Map<Produkt, Category> categories = assignCategories(produkts);

		return categories;


	}

	public static Map<Produkt, Category> testCategoriesInShopComFor(String phrase) {
		ArrayList<Produkt> produkts = createShopComProductDetailsList(phrase,55);

//		produkts.sort(new ProduktNameComparator());
		ArrayList<Produkt> goodOnes=new ArrayList<Produkt>();
		for(Produkt p:produkts) {
			if(p.getNazwa().toLowerCase().contains(phrase.toLowerCase())) {
				goodOnes.add(p);
			}
		}
		Map<Produkt, Category> categories = assignCategories(goodOnes);

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


	private static ArrayList<Produkt> createShopComProductDetailsList(String phrase) {
		ArrayList<Produkt> produkts = ShopComApiClient.getProduktsFor(phrase);

//		for(int i=0;i<produkts.size();i++) {
//			Produkt p=produkts.get(i);
//			p=ShopComApiClientParticularProduct.getProduktByUrl(url)
//		}

		return produkts;
	}

	private static ArrayList<Produkt> createShopComProductDetailsList(String phrase, int productsToGet) {
		ArrayList<Produkt> produkts = ShopComApiClient.getDistinctProduktsFor(phrase,productsToGet);

//		for(int i=0;i<produkts.size();i++) {
//			Produkt p=produkts.get(i);
//			p=ShopComApiClientParticularProduct.getProduktByUrl(url)
//		}

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





	public static Category assignCategory(Produkt p) {
		Category root=CategoryHierarchy.getSingletonCategoryRoot();

		Category result=root.assignCategoryFromTree(p);
		
//		if(result.getName().equals("root")) {
			result=root.assignCategoryFromTree(p);

//		}

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
