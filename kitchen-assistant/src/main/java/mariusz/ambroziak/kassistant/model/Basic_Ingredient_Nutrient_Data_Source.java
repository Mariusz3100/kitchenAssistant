package mariusz.ambroziak.kassistant.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
public class Basic_Ingredient_Nutrient_Data_Source {

	public Set<Basic_Ingredient_Nutrient_amount> getAmounts() {
		return amounts;
	}


	public void setAmounts(Set<Basic_Ingredient_Nutrient_amount> amounts) {
		this.amounts = amounts;
	}


	public Long getBinds_id() {
		return binds_id;
	}


	public Basic_Ingredient getBasicIngredient() {
		return basicIngredient;
	}

	public void setBasicIngredient(Basic_Ingredient basicIngredient) {
		this.basicIngredient = basicIngredient;
	}

	public String getData_source_url() {
		return data_source_url;
	}

	public void setData_source_url(String data_source_url) {
		this.data_source_url = data_source_url;
	}

	public String getId_in_api() {
		return id_in_api;
	}

	public void setId_in_api(String id_in_api) {
		this.id_in_api = id_in_api;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long binds_id;

	
	@NotNull
	@ManyToOne
	@JoinColumn(name="bi_id")
	private Basic_Ingredient basicIngredient ;

	@NotNull
	private String data_source_url;

	@NotNull
	private String 	id_in_api;

	@OneToMany(fetch=FetchType.EAGER, mappedBy="dataSource")
	private Set<Basic_Ingredient_Nutrient_amount> amounts;
	
}
