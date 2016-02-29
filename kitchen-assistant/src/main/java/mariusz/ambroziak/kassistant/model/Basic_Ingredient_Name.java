package mariusz.ambroziak.kassistant.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;
import org.jsoup.Connection.Base;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "possible_name"))
public class Basic_Ingredient_Name {

	   @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   private Long bin_id;
	   
	   @NotNull
	   private String possible_name;
	   
	   @NotNull
	   @ManyToOne(fetch=FetchType.EAGER)
	   @JoinColumn(name="bi_id")
	   private Basic_Ingredient base;
	   
	   
	   
	public Basic_Ingredient getBase() {
		return base;
	}

	public void setBase(Basic_Ingredient base) {
		this.base = base;
	}

	

	public Basic_Ingredient_Name() {
		super();
	}

	public Basic_Ingredient_Name(Long bin_id, String possible_name,
			Basic_Ingredient base) {
		super();
		this.bin_id = bin_id;
		this.possible_name = possible_name;
		this.base = base;
	}

	public Long getBin_id() {
		return bin_id;
	}

	public void setBin_id(Long bin_id) {
		this.bin_id = bin_id;
	}

	public String getPossible_name() {
		return possible_name;
	}

	public void setPossible_name(String possible_name) {
		this.possible_name = possible_name;
	}



}
