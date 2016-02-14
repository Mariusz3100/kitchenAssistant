package mariusz.ambroziak.kassistant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "nazwa"))
public class Produkt_Ingredient {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long pi_id;

	@NotNull
	private String nazwa;

	public Long getPi_id() {
		return pi_id;
	}

	public void setPi_id(Long pi_id) {
		this.pi_id = pi_id;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}
	
	
}
