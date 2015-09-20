package mariusz.ambroziak.kassistant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
public class Problem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long p_id;


	private Long next_p_id;
	@NotNull
	private String  message;
	
	
	private boolean solved;


	public Problem(){}
	
	public Problem(Long nextId, String message, boolean solved) {
		super();
		this.next_p_id = nextId;
		this.message = message;
		this.solved = solved;
	}




	public Long getP_id() {
		return p_id;
	}




	public void setP_id(Long p_id) {
		this.p_id = p_id;
	}




	public Long getNext_p_id() {
		return next_p_id;
	}




	public void setNext_p_id(Long next_p_id) {
		this.next_p_id = next_p_id;
	}




	public String getMessage() {
		return message;
	}




	public void setMessage(String message) {
		this.message = message;
	}




	public boolean isSolved() {
		return solved;
	}




	public void setSolved(boolean solved) {
		this.solved = solved;
	}




}
