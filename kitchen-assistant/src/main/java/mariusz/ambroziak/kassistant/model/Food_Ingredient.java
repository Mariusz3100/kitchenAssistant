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
public class Food_Ingredient {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fi_id;

	@NotNull
	private String nazwa;

	public Long getFi_id() {
		return fi_id;
	}

	public void setFi_id(Long fi_id) {
		this.fi_id = fi_id;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}
	
	
}
