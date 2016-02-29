package mariusz.ambroziak.kassistant.dao;

import java.util.List;

import mariusz.ambroziak.kassistant.model.Problem;

public interface ProblemDAO {
	public List<Problem> list();
	public List<Problem> list(boolean firstMessage,boolean solved);
	public void addProblem(Problem p);
	public Problem getById(Long id);

	

}
