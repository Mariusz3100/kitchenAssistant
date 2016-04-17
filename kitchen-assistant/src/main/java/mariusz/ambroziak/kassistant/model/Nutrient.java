package mariusz.ambroziak.kassistant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Nutrient {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long nu_id;

	@NotNull
	private String name;

	public Long getNu_id() {
		return nu_id;
	}

	public void setNu_id(Long nu_id) {
		this.nu_id = nu_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	


	
}
