package mariusz.ambroziak.kassistant.dao;

public class DaoProvider {

	
	private UserDAO userDao;
	
	private ProduktDAO produktDao;
	
	private Variant_WordDAO variantWordDao;

	private Base_WordDAO baseWordDao;
	
	private RecipeDAO recipeDao;

	private ProblemDAO problemDao;

	private Basic_IngredientDAO basicIngredientDao;

	private Basic_Ingredient_NameDAO basicIngredientNameDao;

	private NutrientDAO nutrientDao;
	
	

	public NutrientDAO getNutrientDao() {
		return nutrientDao;
	}

	

	public Basic_IngredientDAO getBasicIngredientDao() {
		return basicIngredientDao;
	}

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

	public DaoProvider(Base_WordDAO baseWordDao,UserDAO userDao, ProduktDAO produktDao,
			Variant_WordDAO variantWordDao,RecipeDAO recipeDao,ProblemDAO problemDao,
			Basic_IngredientDAO basicIngredientDao, Basic_Ingredient_NameDAO basicIngredientNameDao,
			NutrientDAO nutrientsDao)
	{
		super();
		this.problemDao=problemDao;
		this.userDao = userDao;
		this.produktDao = produktDao;
		this.variantWordDao=variantWordDao;
		this.baseWordDao=baseWordDao;
		this.recipeDao=recipeDao;
		this.basicIngredientDao=basicIngredientDao;
		this.basicIngredientNameDao=basicIngredientNameDao;
		this.nutrientDao=nutrientsDao;
		
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
	
	public Basic_Ingredient_NameDAO getBasicIngredientNameDao() {
		return basicIngredientNameDao;
	}

	public ProblemDAO getProblemDao() {
		return problemDao;
	}

	private static DaoProvider singleton; 
}
