package mariusz.ambroziak.kassistant.dao;

import org.springframework.beans.factory.annotation.Autowired;

public class DaoProvider {

	
	private UserDAO userDao;
	
	private ProduktDAO produktDao;
	
	public UserDAO getUserDao() {
		return userDao;
	}

	public ProduktDAO getProduktDao() {
		return produktDao;
	}

	public DaoProvider(UserDAO userDao, ProduktDAO produktDao) {
		super();
		this.userDao = userDao;
		this.produktDao = produktDao;
		if(singleton==null)
			singleton=this;
		else
			throw new IllegalStateException("two instances of daoProvider");
	}
	
	public static DaoProvider getInstance(){
		return singleton;
	}
	
	private static DaoProvider singleton; 
}
