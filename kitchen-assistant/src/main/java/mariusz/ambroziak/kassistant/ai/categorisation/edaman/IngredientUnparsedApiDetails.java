package mariusz.ambroziak.kassistant.ai.categorisation.edaman;

public class IngredientUnparsedApiDetails {

	private String originalPhrase;
	private String productPhrase;
	private String amountTypePhrase;
	private double amount;
	
	
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getProductPhrase() {
		return productPhrase;
	}

	public void setProductPhrase(String productPhrase) {
		this.productPhrase = productPhrase;
	}

	public String getAmountTypePhrase() {
		return amountTypePhrase;
	}

	public void setAmountTypePhrase(String amountTypePhrase) {
		this.amountTypePhrase = amountTypePhrase;
	}

	public String getOriginalPhrase() {
		return originalPhrase;
	}

	public void setOriginalPhrase(String originalPhrase) {
		this.originalPhrase = originalPhrase;
	}

}
