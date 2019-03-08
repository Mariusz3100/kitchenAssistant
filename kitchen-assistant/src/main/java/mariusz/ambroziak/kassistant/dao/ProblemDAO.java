package mariusz.ambroziak.kassistant.dao;

import java.util.List;
import java.util.Map;

import mariusz.ambroziak.kassistant.model.Problem;

public interface ProblemDAO {
	public List<Problem> list();
	public List<Problem> list(boolean firstMessage,boolean solved);
	public void addProblem(Problem p);
	public Problem getById(Long id);
	public Problem getByNext_pId(Long id);
	public Map<Long, String> listWholeMessages();

	
	public List<Problem> getWholeMessage(Long id);

}
