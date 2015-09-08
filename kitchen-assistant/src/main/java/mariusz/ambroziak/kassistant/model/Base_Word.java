package mariusz.ambroziak.kassistant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "b_word"))
public class Base_Word {
	
	   public Base_Word(String b_word) {
		super();

		this.b_word = b_word;
	}

   public Base_Word() {
		super();
	}

	   
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getB_word() {
		return b_word;
	}

	public void setB_word(String b_word) {
		this.b_word = b_word;
	}

	@Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   private Long id;
	   
	   @NotNull
	   private String b_word;
	   
	   
	   
	   
	   
}
