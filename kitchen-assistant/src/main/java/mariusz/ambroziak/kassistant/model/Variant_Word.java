package mariusz.ambroziak.kassistant.model;

import javax.persistence.Entity;
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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "v_word"))
public class Variant_Word {

	   @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   private Long id;
	   
	   @NotNull
	   private String v_word;
	   
	   @NotNull
////	   @ManyToOne
////	   @JoinColumn(name="base_word_id")
//	   private Long base_word_id;
	   
	   @ManyToOne
	   @JoinColumn(name="base_word_id")
	   private Base_Word base_word;
	   
	   
	   
	public Base_Word getBase_word() {
		return base_word;
	}

	public void setBase_word(Base_Word base_word) {
		this.base_word = base_word;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getV_word() {
		return v_word;
	}

	public void setV_word(String v_word) {
		this.v_word = v_word;
	}

	public Variant_Word() {
		super();
	}

	public Variant_Word(String v_word, Base_Word bw) {
		super();

		this.v_word = v_word;
		this.base_word=bw;
	}

//	public Base_Word getBase() {
//		return base;
//	}
//
//	public void setBase(Base_Word base) {
//		this.base = base;
//	}
//	
}
