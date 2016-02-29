package mariusz.ambroziak.kassistant.model.utils;

import java.util.ArrayList;

import mariusz.ambroziak.kassistant.QuantityExtractor.AmountTypes;

public class CompoundIngredientQuantity implements ProduktIngredientQuantity{

	private String name;
	private PreciseQuantity quan;
	public AbstractQuantity getQuan() {
		return quan;
	}

	public void setQuan(PreciseQuantity quan) {
		this.quan = quan;
	}

	private ArrayList<CompoundIngredientQuantity> compoundIngredients;
	
	private ArrayList<BasicIngredientQuantity> basicIngredients;



	public String getName() {
		return name;
	}

	public ArrayList<CompoundIngredientQuantity> getCompoundIngredients() {
		return compoundIngredients;
	}

	public ArrayList<BasicIngredientQuantity> getBasicIngredients() {
		return basicIngredients;
	}

	public CompoundIngredientQuantity(String name, PreciseQuantity quan,
			ArrayList<CompoundIngredientQuantity> compoundIngredients,
			ArrayList<BasicIngredientQuantity> basicIngredients) {
		super();
		this.name = name;
		this.quan = quan;
		this.compoundIngredients = compoundIngredients;
		this.basicIngredients = basicIngredients;
	}

	@Override
	public AmountTypes getAmountType() {
		return quan!=null?quan.type:null;

	}

	@Override
	public AbstractQuantity getAmount() {
		return quan;
	}

	@Override
	public String toString(){
		String retValue=getName()+" : "+getAmount()+getAmountType()+"->[";
		
		for(BasicIngredientQuantity biq:basicIngredients){
			retValue+="<"+biq.getName()+" : "+biq.getAmount()+biq.getAmountType()+">, ";
		}
		retValue+="]";
		
		for(CompoundIngredientQuantity ciq:compoundIngredients){
			retValue+="{"+ciq.toString()+"}, ";
		}
		
		return retValue;
	}


	 
	
}
