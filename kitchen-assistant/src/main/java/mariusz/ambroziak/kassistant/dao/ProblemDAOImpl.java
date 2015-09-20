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
	
	
	


}
