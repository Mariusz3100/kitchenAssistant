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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nu_id == null) ? 0 : nu_id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Nutrient other = (Nutrient) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nu_id == null) {
			if (other.nu_id != null)
				return false;
		} else if (!nu_id.equals(other.nu_id))
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	@Override
	public String toString() {
		return "Nutrient [nu_id=" + nu_id + ", name=" + name + "]";
	}

	
}
