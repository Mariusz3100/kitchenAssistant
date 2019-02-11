package mariusz.ambroziak.kassistant.dao;

public class DaoProvider {

	
	private UserDAO userDao;
	
	private ProduktDAO produktDao;
	
	private Variant_WordDAO variantWordDao;

	private Base_WordDAO baseWordDao;
	
	private RecipeDAO recipeDao;

	private ProblemDAO problemDao;

	private Basic_IngredientDAO basicIngredientDao;

	private Basic_Ingredient_Nutrient_AmountDAO basicIngredientNutrientAmountDao;

	private Basic_Ingredient_Nutrient_Data_SourceDAO basicIngredientNutrientDataSourceDao;

	private Basic_Ingredient_NameDAO basicIngredientNameDao;

	private NutrientDAO nutrientDao;
	
	private Nutrient_NameDAO nutrientNameDao;
	

	public Nutrient_NameDAO getNutrientNameDao() {
		return nutrientNameDao;
	}



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


	
	public Basic_Ingredient_Nutrient_AmountDAO getBasicIngredientNutrientAmountDao() {
		return basicIngredientNutrientAmountDao;
	}



	
	public DaoProvider(UserDAO userDao, ProduktDAO produktDao, Variant_WordDAO variantWordDao, Base_WordDAO baseWordDao,
			RecipeDAO recipeDao, ProblemDAO problemDao, Basic_IngredientDAO basicIngredientDao,
			Basic_Ingredient_Nutrient_AmountDAO basicIngredientNutrientAmountDao,
			Basic_Ingredient_Nutrient_Data_SourceDAO basicIngredientNutrientDataSourceDao,
			Basic_Ingredient_NameDAO basicIngredientNameDao, NutrientDAO nutrientDao,
			Nutrient_NameDAO nutrientNameDao) {
		super();
		this.userDao = userDao;
		this.produktDao = produktDao;
		this.variantWordDao = variantWordDao;
		this.baseWordDao = baseWordDao;
		this.recipeDao = recipeDao;
		this.problemDao = problemDao;
		this.basicIngredientDao = basicIngredientDao;
		this.basicIngredientNutrientAmountDao = basicIngredientNutrientAmountDao;
		this.basicIngredientNutrientDataSourceDao = basicIngredientNutrientDataSourceDao;
		this.basicIngredientNameDao = basicIngredientNameDao;
		this.nutrientDao = nutrientDao;
		this.nutrientNameDao = nutrientNameDao;
		

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
	
	public Basic_Ingredient_Nutrient_Data_SourceDAO getBasicIngredientNutrientDataSourceDao() {
		return basicIngredientNutrientDataSourceDao;
	}



	public Basic_Ingredient_NameDAO getBasicIngredientNameDao() {
		return basicIngredientNameDao;
	}

	public ProblemDAO getProblemDao() {
		return problemDao;
	}

	private static DaoProvider singleton; 
}
