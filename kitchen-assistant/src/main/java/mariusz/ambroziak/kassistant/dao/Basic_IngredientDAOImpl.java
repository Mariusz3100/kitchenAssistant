package mariusz.ambroziak.kassistant.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Basic_Ingredient;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.Variant_Word;
import mariusz.ambroziak.kassistant.utils.ProblemLogger;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

public class Basic_IngredientDAOImpl implements Basic_IngredientDAO {
	private SessionFactory sessionFactory;

	private static final String selectBase="select basic_ingredient.* from basic_ingredient_name right join basic_ingredient on basic_ingredient_name.bi_id=basic_ingredient.bi_id\r\n" + 
			" where possible_name  ='__name__' or name='__name__' limit 1";

	public Basic_IngredientDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

	}

	@Override
	@Transactional
	public List<Basic_Ingredient> list() {
		@SuppressWarnings("unchecked")
		List<Basic_Ingredient> listProdukt = (List<Basic_Ingredient>) sessionFactory.getCurrentSession()
		.createSQLQuery("SELECT * FROM Basic_Ingredient")
		.addEntity(Basic_Ingredient.class)
		.list();

		return listProdukt;
	}

	@Override
	@Transactional
	public Basic_Ingredient getIngredientByName(String name) {
		@SuppressWarnings("unchecked")
		List<Basic_Ingredient> listProdukt = (List<Basic_Ingredient>) sessionFactory.getCurrentSession()
		.createCriteria(Basic_Ingredient.class)
		.add(Restrictions.eq("name", name))
		.list();

		if(listProdukt==null||listProdukt.size()<1)
			return null;
		else
			return listProdukt.get(0);
	}

	@Override
	@Transactional
	public Basic_Ingredient getIngredientByAnyName(String name) {
		name=org.apache.commons.lang.StringEscapeUtils.escapeSql(name);

		@SuppressWarnings("unchecked")
		List<Basic_Ingredient> ingredients = (List<Basic_Ingredient>) sessionFactory.getCurrentSession()
		.createSQLQuery(selectBase.replaceAll("__name__", name))
		.addEntity(Basic_Ingredient.class)
		.list();

		if(ingredients==null||ingredients.size()<1)
			return null;
		else
			return ingredients.get(0);
	}

	@Transactional
	@Override
	public boolean checkIfRecordExistsUpdateArgumentIfNeedBe(Basic_Ingredient bi) {
		if(bi==null||(bi.getBi_id()==null&&bi.getName()==null))
		{
			//empty object doesn't exist in db
			return false;
		}else {
			return checkForNonEmptyBasicIngredientAndOneOfItsFields(bi);
		}
	}

	private boolean checkForNonEmptyBasicIngredientAndOneOfItsFields(Basic_Ingredient bi) {
		if(bi.getBi_id()!=null) {
			return checkForNonEmptyBasicIngredientId(bi);
		}else {
			return checkForNonEmptyBasicIngredientName(bi);
		}
	}

	private boolean checkForNonEmptyBasicIngredientName(Basic_Ingredient bi) {
		Basic_Ingredient bi_retrieved = getFromDbByName(bi.getName());
		if(bi_retrieved==null) {
			return false;
		}else{
			bi.setBi_id(bi_retrieved.getBi_id());
			return true;
		}
	}

	private boolean checkForNonEmptyBasicIngredientId(Basic_Ingredient bi) {
		Basic_Ingredient bi_retrieved = getFromDbById(bi.getBi_id());
		if(bi_retrieved==null) {
			return false;
		}else {
			if(bi.getName()==null)
			{	//update name
				bi.setName(bi_retrieved.getName());
				return true;
			}else {
				if(bi_retrieved.getName().equals(bi.getName())) {
					return true;
				}else {
					ProblemLogger.logProblem("Id and name of checked object are different from those in db.");
					return false;
				}
			}
		}
	}

	private Basic_Ingredient getFromDbByName(String name) {
		name=org.apache.commons.lang.StringEscapeUtils.escapeSql(name);

		Basic_Ingredient bi_retrieved = (Basic_Ingredient) sessionFactory.getCurrentSession()
				.createCriteria(Basic_Ingredient.class)
				.add(Restrictions.eq("name",name))
				.uniqueResult();
		return bi_retrieved;
	}

	private Basic_Ingredient getFromDbById(Long id) {
		Basic_Ingredient bi_retrieved = (Basic_Ingredient) sessionFactory.getCurrentSession()
				.createCriteria(Basic_Ingredient.class)
				.add(Restrictions.idEq(id))
				.uniqueResult();
		return bi_retrieved;
	}

	@Override
	@Transactional
	public void saveBasicIngredient(Basic_Ingredient bi) {
		//if(vw.getV_word().indexOf('\''))
		
		this.sessionFactory.getCurrentSession().save(bi);
	}

	@Override
	@Transactional
	public List<Basic_Ingredient> getIngredientByNames(Collection<String> name) {
		Criteria createdCriteria = sessionFactory.getCurrentSession()
				.createCriteria(Basic_Ingredient.class);
				
		for(String x:name){
			x=org.apache.commons.lang.StringEscapeUtils.escapeSql(x);

			createdCriteria=createdCriteria.add(Restrictions.ilike("name", "%"+x+"%"));
		}
		
		List<Basic_Ingredient> ingredients = (List<Basic_Ingredient>)createdCriteria.list();
		return ingredients;
	}
	@Transactional
	@Override
	public List<Basic_Ingredient> getIngredientBySpacedName(String phrase) {
		String[] parts=phrase.split(" ");
		
		ArrayList<String> x=new ArrayList<String>();
		
		for(String y:parts){
			y=org.apache.commons.lang.StringEscapeUtils.escapeSql(y);

			x.add(y);
		}
		
		return getIngredientByNames(x);
		
	}

	@Override
	@Transactional
	public Basic_Ingredient getIngredientById(Long id) {
		Basic_Ingredient bi_retrieved = (Basic_Ingredient) sessionFactory.getCurrentSession()
				.createCriteria(Basic_Ingredient.class)
				.add(Restrictions.idEq(id))
				.uniqueResult();
		return bi_retrieved;
	}



}
