package mariusz.ambroziak.kassistant.ai.categorisation.edaman;

import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.utils.ApiIngredientAmount;

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

	
	public Category assignCategory(IngredientCategoriationData toBeCategorised) {
		Category root=CategoryHierarchy.getSingletonCategoryRoot();
		
		return root.assignCategoryFromTree(toBeCategorised);
	}

//	public Category assignCategory(ApiIngredientAmount product) {
//		Category root=CategoryHierarchy.getSingletonCategoryRoot();
//		
//		return root.assignCategoryFromTree(product);
//	}

}