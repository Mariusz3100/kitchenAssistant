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

	@Override
	public String toString() {
		return "Basic_Ingredient [bi_id=" + bi_id + ", name=" + name + "]";
	}

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

	@Override
	public int hashCode() {
		return bi_id.intValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Basic_Ingredient other = (Basic_Ingredient) obj;
		if (bi_id == null) {
			if (other.bi_id != null)
				return false;
		} else if (!bi_id.equals(other.bi_id))
			return false;
		return true;
	}

	
}
