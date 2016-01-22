package mariusz.ambroziak.kassistant.dao;

import java.util.List;

import mariusz.ambroziak.kassistant.model.Amount_Type;
import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Problem;
import mariusz.ambroziak.kassistant.model.Recipe;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.Variant_Word;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

public class ProblemDAOImpl implements ProblemDAO {
	private SessionFactory sessionFactory;
	

	public ProblemDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

	}


	@Transactional
	@Override
	public List<Problem> list() {
		@SuppressWarnings("unchecked")
		List<Problem> types =  sessionFactory.getCurrentSession()
				.createCriteria(Problem.class)
				.list();
		
		return types;
	}

	@Transactional
	@Override
	public void addProblem(Problem p) {
		this.sessionFactory.getCurrentSession().save(p);
		
	}

	@Transactional
	@Override
	public List<Problem> list(boolean firstMessage, boolean solved) {
		@SuppressWarnings("unchecked")
		List<Problem> problems =  sessionFactory.getCurrentSession()
				.createCriteria(Problem.class)
				.add(Restrictions.eq("first_message", firstMessage))
				.add(Restrictions.eq("solved", solved))
				.list();
		
		
		return problems;
	}

	@Transactional
	@Override
	public Problem getById(Long id) {
		@SuppressWarnings("unchecked")
		List<Problem> problems =  sessionFactory.getCurrentSession()
				.createCriteria(Problem.class)
				.add(Restrictions.idEq(id))
				.list();
		if(problems.size()<1)
			return null;
		else
			return problems.get(0);
		
		
	}
	
	
	


}
