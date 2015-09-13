package mariusz.ambroziak.kassistant.dao;

import java.util.List;

import mariusz.ambroziak.kassistant.model.Amount_Type;
import mariusz.ambroziak.kassistant.model.Base_Word;
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

public class Amount_TypeDAOImpl implements Amount_TypeDAO {
	private SessionFactory sessionFactory;
	

	public Amount_TypeDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

	}

	@Override
	@Transactional
	public List<Amount_Type> list() {
		@SuppressWarnings("unchecked")
		List<Amount_Type> listProdukt = (List<Amount_Type>) sessionFactory.getCurrentSession()
				.createCriteria(Amount_Type.class)
				.list();

		return listProdukt;
	}







	@Override
	public Amount_Type getTypeByName(String typeName) {
		@SuppressWarnings("unchecked")
		List<Amount_Type> types =  sessionFactory.getCurrentSession()
				.createCriteria(Amount_Type.class)
				.add(Restrictions.eq("nazwa", typeName))
				.list();
		
		if(types.size()>1)
			throw new  DataIntegrityViolationException(
					"In the table Amount_Type there is more than one recipe for nazwa: "+typeName);
		
		if(types==null||types.size()<1)
			return null;
		else
			return types.get(0);
	}
	
	
	


}
