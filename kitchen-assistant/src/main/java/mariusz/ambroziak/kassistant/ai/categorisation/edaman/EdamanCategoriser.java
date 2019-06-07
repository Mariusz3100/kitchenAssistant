package mariusz.ambroziak.kassistant.ai.categorisation.edaman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import mariusz.ambroziak.kassistant.ai.categorisation.Category;
import mariusz.ambroziak.kassistant.ai.categorisation.CategoryHierarchy;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.shopcom.ShopComApiClient;
import mariusz.ambroziak.kassistant.shopcom.ShopComApiClientParticularProduct;
import mariusz.ambroziak.kassistant.tesco.TescoApiClient;
import mariusz.ambroziak.kassistant.tesco.TescoApiClientParticularProduct_notUsed;

public class EdamanCategoriser {
	private static EdamanCategoriser singleton;
	//	private Category root;







	public static void main(String [] args){


		System.out.println();
	}





	public static EdamanCategoriser getSingleton() {
		if(singleton==null) {
			singleton=new EdamanCategoriser();

		}
		return singleton;

	}



	private EdamanCategoriser() {
		super();

	}

	
	public Category categorize(String ingredientPhrase) {
		Category root=CategoryHierarchy.getSingletonCategoryRoot();
		
		return root.assignCategoryFromTree(null);
	}


}
