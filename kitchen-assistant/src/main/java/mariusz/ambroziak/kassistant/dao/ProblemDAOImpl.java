package mariusz.ambroziak.kassistant.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public static final int finishedMessageId= 0;

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
		if(id==null)return null;

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

	@Transactional
	@Override
	public List<Problem> getWholeMessage(Long id) {
		return getWholeMessage(id,false);
	}
	
	
	
	public List<Problem> getWholeMessage(Long id,boolean recurent) {
		List<Problem> retValue=new ArrayList<>();
		Problem byId = getById(id);
		if(byId==null)
			return null;

		retValue.add(byId);

		Long curr_id = byId.getP_id();
		Problem currProb=byId;
		while(!currProb.isFirstMessage()&&!recurent) {
			currProb=getByNext_pId(curr_id);
			retValue.add(currProb);
			curr_id=currProb.getP_id();

		}

		Collections.reverse(retValue);


		if(byId.getNext_p_id()==finishedMessageId||byId.getNext_p_id()==null)
		{
			return retValue;
		}else {
			List<Problem> wholeMessage = getWholeMessage(byId.getNext_p_id(),true);
			retValue.addAll(wholeMessage);
		}

		return retValue;
	}


	@Override
	public Problem getByNext_pId(Long id) {
		if(id==null)return null;

		@SuppressWarnings("unchecked")
		List<Problem> problems =  sessionFactory.getCurrentSession()
		.createCriteria(Problem.class)
		.add(Restrictions.eq("next_p_id", id))
		.list();
		if(problems.size()<1)
			return null;
		else
			return problems.get(0);
	}

	@Transactional
	@Override
	public Map<Long,String> listWholeMessages() {
		List<Problem> initialList=list(true, false);
		
		Map<Long,String> retValue=new HashMap<>();
		
		for(Problem p:initialList) {
			String result=p.getMessage();
			while(p.getNext_p_id()!=finishedMessageId) {
				p=getById(p.getNext_p_id());
				result+=p.getMessage();
			}
			retValue.put(p.getP_id(),result);
		}
		
		return retValue;
	}





}
