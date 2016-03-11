package mariusz.ambroziak.kassistant.model.utils;

import java.util.ArrayList;

import mariusz.ambroziak.kassistant.QuantityExtractor.AmountTypes;

public class CompoundIngredientQuantity implements ProduktIngredientQuantity{

	private String name;
	private AbstractQuantity quan;
	private ArrayList<CompoundIngredientQuantity> compoundIngredients;
	
	private ArrayList<BasicIngredientQuantity> basicIngredients;
	
	private ArrayList<BasicIngredientQuantity> allBasicIngredients;
	
	
	public ArrayList<BasicIngredientQuantity> getAllBasicIngredients() {
		return allBasicIngredients;
	}

	public AbstractQuantity getQuan() {
		return quan;
	}

	public void setQuan(AbstractQuantity quan) {
		this.quan = quan;
	}





	public String getName() {
		return name;
	}

	public ArrayList<CompoundIngredientQuantity> getCompoundIngredients() {
		return compoundIngredients;
	}

	public ArrayList<BasicIngredientQuantity> getBasicIngredients() {
		return basicIngredients;
	}

	public CompoundIngredientQuantity(String name, AbstractQuantity quan,
			ArrayList<CompoundIngredientQuantity> compoundIngredients,
			ArrayList<BasicIngredientQuantity> basicIngredients) {
		super();
		this.name = name;
		this.quan = quan;
		this.compoundIngredients = compoundIngredients;
		this.basicIngredients = basicIngredients;
		
		this.allBasicIngredients=new ArrayList<BasicIngredientQuantity>();
		
		allBasicIngredients.addAll(basicIngredients);
		
		for(CompoundIngredientQuantity ciq:compoundIngredients){
			allBasicIngredients.addAll(ciq.getAllBasicIngredients());
		}
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
		String retValue=getName()+" : "+getAmount()+"->[basics:";
		
		for(BasicIngredientQuantity biq:basicIngredients){
			retValue+="{"+biq.getName()+" : "+biq.getAmount()+"}, ";
		}
		retValue+=" compouds: ";
		
		for(CompoundIngredientQuantity ciq:compoundIngredients){
			retValue+="{"+ciq.toString()+"}, ";
		}
		
		retValue+="]";
		return retValue;
	}


	 
	
}
