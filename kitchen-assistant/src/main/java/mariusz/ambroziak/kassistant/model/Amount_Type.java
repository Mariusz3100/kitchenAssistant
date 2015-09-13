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
public class Amount_Type {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long a_type_id;

	@NotNull
	private String nazwa;

	public Long getA_type_id() {
		return a_type_id;
	}

	public void setA_type_id(Long a_type_id) {
		this.a_type_id = a_type_id;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}


}
