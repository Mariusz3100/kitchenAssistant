package mariusz.ambroziak.kassistant.dao;

import java.util.List;

import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.Variant_Word;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

public class Base_WordDAOImpl implements Base_WordDAO {
	private SessionFactory sessionFactory;
	
	public static String niepoprawneSlowoBazowe="niepoprawne";

	public Base_WordDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

	}

	@Override
	@Transactional
	public List<Base_Word> list() {
		@SuppressWarnings("unchecked")
		List<Base_Word> listProdukt = (List<Base_Word>) sessionFactory.getCurrentSession()
				.createSQLQuery("SELECT * FROM base_word")
				.addEntity(Base_Word.class)
				.list();

		return listProdukt;
	}

	@Transactional
	@Override
	public Base_Word getBase_Name(String name) {
		@SuppressWarnings("unchecked")
		List<Base_Word> words =  sessionFactory.getCurrentSession()
				.createCriteria(Base_Word.class)
				.add(Restrictions.eq("b_word", name))
				.list();
		
		if(words==null||words.size()<1)
			return null;
		else
			return words.get(0);
	}

	@Override
	public void addBase_Word(Base_Word bw) {
		this.sessionFactory.getCurrentSession().save(bw);
		
	}


}
