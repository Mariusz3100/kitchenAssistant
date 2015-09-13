package mariusz.ambroziak.kassistant.utils;

import mariusz.ambroziak.kassistant.model.Recipe_Ingredient;

public class PrzepisyPLQExtract {

	
	public static Recipe_Ingredient extractQuantity(String text){
		Recipe_Ingredient retValue=new Recipe_Ingredient();
		
		
		
		String[] elems=text.split(" ");
		
		retValue.setAmount(Float.parseFloat(elems[0]));
		
		for(AmountTypes at:AmountTypes.values()){
			if(at.getType().equals(elems[1])){
				retValue.setA_type_id(at.getDb_id());
			}else{
				
			}
		}
		
		
	}
}
