package mariusz.ambroziak.kassistant.dao;

import java.util.List;

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

public class RecipeDAOImpl implements RecipeDAO {
	private SessionFactory sessionFactory;
	

	public RecipeDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

	}

	@Override
	@Transactional
	public List<Recipe> list() {
		@SuppressWarnings("unchecked")
		List<Recipe> listProdukt = (List<Recipe>) sessionFactory.getCurrentSession()
				.createSQLQuery("SELECT * FROM recipe")
				.addEntity(Recipe.class)
				.list();

		return listProdukt;
	}




	@Transactional
	@Override
	public Recipe getRecipeByURL(String url) {
		@SuppressWarnings("unchecked")
		List<Recipe> recipes =  sessionFactory.getCurrentSession()
				.createCriteria(Base_Word.class)
				.add(Restrictions.eq("url", url))
				.list();
		
		if(recipes.size()>1)
			throw new  DataIntegrityViolationException(
					"In the table recipe there is more than one recipe for url: "+url);
		
		if(recipes==null||recipes.size()<1)
			return null;
		else
			return recipes.get(0);
	}

	@Transactional
	@Override
	public void addRecipe(Recipe r) {
		this.sessionFactory.getCurrentSession().save(r);
		
	}
	
	
	


}
