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

import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Table(name="nutrient_name",uniqueConstraints = @UniqueConstraint(columnNames = "possible_name"))
public class Nutrient_Name {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long nn_id;

	@NotNull
	private String possible_name;

	@NotNull
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="nu_id")
	private Nutrient base;



	public Long getNn_id() {
		return nn_id;
	}

	public void setNn_id(Long nn_id) {
		this.nn_id = nn_id;
	}

	public String getPossible_name() {
		return possible_name;
	}

	public void setPossible_name(String possible_name) {
		this.possible_name = possible_name;
	}
	
	
	public Nutrient getBase() {
		//Hibernate.initialize(base);	
		return base;
	}

	public void setBase(Nutrient base) {
		this.base = base;
	}


}
