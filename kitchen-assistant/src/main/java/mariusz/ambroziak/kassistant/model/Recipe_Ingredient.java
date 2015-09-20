package mariusz.ambroziak.kassistant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Recipe_Ingredient {

	 @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	Long rec_ing_id;

	 
	 @NotNull
	Long  rec_id;
	
	 @NotNull
	Long ing_id;
	 
	 @NotNull 
	float amount;
	 
	 @NotNull 
	String amount_type;

	public Long getRec_ing_id() {
		return rec_ing_id;
	}

	public void setRec_ing_id(Long rec_ing_id) {
		this.rec_ing_id = rec_ing_id;
	}

	public Long getRec_id() {
		return rec_id;
	}

	public void setRec_id(Long rec_id) {
		this.rec_id = rec_id;
	}

	public Long getIng_id() {
		return ing_id;
	}

	public void setIng_id(Long ing_id) {
		this.ing_id = ing_id;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getAmount_type() {
		return amount_type;
	}

	public void setAmount_type(String amount_type) {
		this.amount_type = amount_type;
	}


}
