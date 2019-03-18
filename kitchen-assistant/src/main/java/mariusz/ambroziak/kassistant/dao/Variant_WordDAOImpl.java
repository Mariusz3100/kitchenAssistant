package mariusz.ambroziak.kassistant.dao;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import mariusz.ambroziak.kassistant.model.Base_Word;
import mariusz.ambroziak.kassistant.model.Produkt;
import mariusz.ambroziak.kassistant.model.User;
import mariusz.ambroziak.kassistant.model.Variant_Word;
import mariusz.ambroziak.kassistant.utils.Converter;

import org.apache.commons.lang3.StringEscapeUtils;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

public class Variant_WordDAOImpl implements Variant_WordDAO {
	private SessionFactory sessionFactory;
	private Base_WordDAO base_wordDao;
	
	
	private static String joinBaseQ="select base_word.* from base_word  inner join variant_word on variant_word.base_word_id=base_word.id where v_word='__variant_word__'";
	private static String joinManyBasesQ="select base_word.* from base_word  inner join variant_word on variant_word.base_word_id=base_word.id where v_word in (__variant_words__)";
	
	public Variant_WordDAOImpl(SessionFactory sessionFactory,Base_WordDAO base_wordDao) {
		this.sessionFactory = sessionFactory;
		this.base_wordDao=base_wordDao;
	}

	@Override
	@Transactional
	public List<Variant_Word> list() {
		@SuppressWarnings("unchecked")
		List<Variant_Word> listProdukt = (List<Variant_Word>) sessionFactory.getCurrentSession()
				.createCriteria(Variant_Word.class)
				.list();

		return listProdukt;
	}

	@Transactional
	@Override
	public List<Variant_Word> getVariant_Name(String name) {
		name=org.apache.commons.lang.StringEscapeUtils.escapeSql(name);

		@SuppressWarnings("unchecked")
		List<Variant_Word> words =  sessionFactory.getCurrentSession()
				.createCriteria(Variant_Word.class)
				.add(Restrictions.eq("v_word", Converter.escapeSql(name)))
				.list();
		
		return words;
	}

	
	@Transactional
	@Override
	public Base_Word getBase_Name(String name) {
		
		
		String query=joinBaseQ.replaceAll("__variant_word__", Converter.escapeSql(Converter.escapeSql(name)));
		
		
		@SuppressWarnings("unchecked")
		List<Base_Word> words =  sessionFactory.getCurrentSession()
				.createSQLQuery(query)
				.addEntity(Base_Word.class)
		
				.list();
		
		if(words==null||words.size()==0)
			return null;
		else
			return words.get(0);
	}
	
	@Transactional
	@Override
	public List<Base_Word> getBase_Names(Collection<String> names) {
		String namesSet="";
		
		Iterator<String> i=names.iterator();
		while(i.hasNext()){
			namesSet+="'"+Converter.escapeSql(i.next())+"'";
			if(i.hasNext())
				namesSet+=", ";
		}
		
		String query=joinManyBasesQ.replaceAll("__variant_words__", namesSet);
		@SuppressWarnings("unchecked")
		List<Base_Word> words =  sessionFactory.getCurrentSession()
				.createSQLQuery(query)
				.addEntity(Base_Word.class)
		
				.list();
		return words;
	}
	
	@Override
	public List<String> getBase_NamesAsStrings(Collection<String> names) {
		List<Base_Word> base_Names = getBase_Names(names);
		
		ArrayList<String> al=new ArrayList<String>();
		
		for(Base_Word bw:base_Names){
			al.add(bw.getB_word());
		}
		
		return al;
		
	}
	@Transactional
	@Override
	public void addVariant_word(Variant_Word vw) {
		//if(vw.getV_word().indexOf('\''))
		
		this.sessionFactory.getCurrentSession().save(vw);
	}

	@Transactional
	@Override
	public void addRelation(String variantWord, String baseWord) {
		variantWord=Converter.escapeSql(variantWord);
		baseWord=Converter.escapeSql(baseWord);
		
		Base_Word bw=base_wordDao.getBase_Name(baseWord);
		
		if(bw==null){
			bw=new Base_Word(baseWord);
			this.sessionFactory.getCurrentSession().save(bw);
		}
		
		Variant_Word vw=new Variant_Word(variantWord,bw);
		
		this.sessionFactory.getCurrentSession().save(vw);
		
	}

	

}
