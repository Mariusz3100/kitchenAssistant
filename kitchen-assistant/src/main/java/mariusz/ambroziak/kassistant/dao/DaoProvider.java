package mariusz.ambroziak.kassistant.dao;

public class DaoProvider {

	
	private UserDAO userDao;
	
	private ProduktDAO produktDao;
	
	private Variant_WordDAO variantWordDao;

	private Base_WordDAO baseWordDao;

	
	public Base_WordDAO getBaseWordDao() {
		return baseWordDao;
	}

	public Variant_WordDAO getVariantWordDao() {
		return variantWordDao;
	}

	public UserDAO getUserDao() {
		return userDao;
	}

	public ProduktDAO getProduktDao() {
		return produktDao;
	}

	public DaoProvider(Base_WordDAO baseWordDao,UserDAO userDao, ProduktDAO produktDao,Variant_WordDAO variantWordDao) {
		super();
		this.userDao = userDao;
		this.produktDao = produktDao;
		this.variantWordDao=variantWordDao;
		this.baseWordDao=baseWordDao;
		
		
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
