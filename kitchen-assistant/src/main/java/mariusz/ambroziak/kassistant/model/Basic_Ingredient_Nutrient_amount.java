package mariusz.ambroziak.kassistant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
public class Basic_Ingredient_Nutrient_amount {


	public Basic_Ingredient_Nutrient_amount(Nutrient nutritient, Basic_Ingredient basicIngredient, float coefficient) {
		super();
		this.nutritient = nutritient;
		this.basicIngredient = basicIngredient;
		this.coefficient = coefficient;
	}

	public Basic_Ingredient_Nutrient_amount() {
		// TODO Auto-generated constructor stub
	}

	public Long getBinu_id() {
		return binu_id;
	}

	public void setBinu_id(Long binu_id) {
		this.binu_id = binu_id;
	}

	public Nutrient getNutritient() {
		return nutritient;
	}

	public void setNutritient(Nutrient nutritient) {
		this.nutritient = nutritient;
	}

	public Basic_Ingredient getBasicIngredient() {
		return basicIngredient;
	}

	public void setBasicIngredient(Basic_Ingredient basicIngredient) {
		this.basicIngredient = basicIngredient;
	}

	public float getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(float coefficient) {
		this.coefficient = coefficient;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long binu_id;

	@NotNull
	@ManyToOne
	@JoinColumn(name="nu_id")
	private Nutrient nutritient;

	@NotNull
	@ManyToOne
	@JoinColumn(name="bi_id")
	private Basic_Ingredient basicIngredient ;

	@NotNull
	private float coefficient;

	
	
}
