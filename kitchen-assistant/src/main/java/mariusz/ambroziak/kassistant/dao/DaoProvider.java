package mariusz.ambroziak.kassistant.dao;

public class DaoProvider {

	
	private UserDAO userDao;
	
	private ProduktDAO produktDao;
	
	private Variant_WordDAO variantWordDao;

	private Base_WordDAO baseWordDao;
	
	private RecipeDAO recipeDao;

	private ProblemDAO problemDao;

	
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

	public DaoProvider(Base_WordDAO baseWordDao,UserDAO userDao, ProduktDAO produktDao,Variant_WordDAO variantWordDao,RecipeDAO recipeDao,ProblemDAO problemDao) {
		super();
		this.problemDao=problemDao;
		this.userDao = userDao;
		this.produktDao = produktDao;
		this.variantWordDao=variantWordDao;
		this.baseWordDao=baseWordDao;
		this.recipeDao=recipeDao;
		
		
		if(singleton==null)
			singleton=this;
		else
			throw new IllegalStateException("two instances of daoProvider");
	}
	
	public RecipeDAO getRecipeDao() {
		return recipeDao;
	}

	public static DaoProvider getInstance(){
		return singleton;
	}
	
	public ProblemDAO getProblemDao() {
		return problemDao;
	}

	private static DaoProvider singleton; 
}
