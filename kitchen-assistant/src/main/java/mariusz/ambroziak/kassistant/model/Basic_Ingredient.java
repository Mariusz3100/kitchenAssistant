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
public class Basic_Ingredient {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bi_id;

	@NotNull
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getBi_id() {
		return bi_id;
	}

	public void setBi_id(Long pi_id) {
		this.bi_id = pi_id;
	}


	
}
