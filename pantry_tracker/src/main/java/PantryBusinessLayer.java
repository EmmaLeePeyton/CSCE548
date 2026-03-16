import java.util.List;

public class PantryBusinessLayer {

    private final IngredientCategoryDao ingredientCategoryDao = new IngredientCategoryDao();
    private final IngredientDao ingredientDao = new IngredientDao();
    private final LocationDao locationDao = new LocationDao();
    private final InventoryItemDao inventoryItemDao = new InventoryItemDao();
    private final MealDao mealDao = new MealDao();
    private final RecipeDao recipeDao = new RecipeDao();
    private final PreparedMealDao preparedMealDao = new PreparedMealDao();
    private final RecipeIngredientDao recipeIngredientDao = new RecipeIngredientDao();

    // -------------------------
    // Ingredient Categories
    // -------------------------

    public void createIngredientCategory(IngredientCategory category) throws Exception {
        requireNotNull(category, "IngredientCategory is required");
        requireNotBlank(category.getName(), "IngredientCategory.name is required");
        ingredientCategoryDao.insert(category);
    }

    public List<IngredientCategory> getAllIngredientCategories() throws Exception {
        return ingredientCategoryDao.getAll();
    }

    public IngredientCategory getIngredientCategoryById(int categoryId) throws Exception {
        requirePositive(categoryId, "categoryId must be > 0");
        return ingredientCategoryDao.getById(categoryId);
    }

    public List<IngredientCategory> searchIngredientCategoriesByName(String name) throws Exception {
        requireNotBlank(name, "name is required");
        return ingredientCategoryDao.searchByName(name);
    }

    public void updateIngredientCategory(IngredientCategory category) throws Exception {
        requireNotNull(category, "IngredientCategory is required");
        requirePositive(category.getId(), "IngredientCategory.id must be > 0");
        requireNotBlank(category.getName(), "IngredientCategory.name is required");
        ingredientCategoryDao.update(category);
    }

    public void deleteIngredientCategory(int categoryId) throws Exception {
        requirePositive(categoryId, "categoryId must be > 0");
        ingredientCategoryDao.delete(categoryId);
    }

    // -------------------------
    // Ingredients
    // -------------------------

    public void createIngredient(Ingredient ingredient) throws Exception {
        validateIngredient(ingredient, false);
        ingredientDao.insert(ingredient);
    }

    public List<Ingredient> getAllIngredients() throws Exception {
        return ingredientDao.getAll();
    }

    public Ingredient getIngredientById(int ingredientId) throws Exception {
        requirePositive(ingredientId, "ingredientId must be > 0");
        return ingredientDao.getById(ingredientId);
    }

    public List<Ingredient> searchIngredientsByName(String name) throws Exception {
        requireNotBlank(name, "name is required");
        return ingredientDao.searchByName(name);
    }

    public List<Ingredient> getIngredientsByCategoryId(int categoryId) throws Exception {
        requirePositive(categoryId, "categoryId must be > 0");
        return ingredientDao.getByCategoryId(categoryId);
    }

    public void updateIngredient(Ingredient ingredient) throws Exception {
        validateIngredient(ingredient, true);
        ingredientDao.update(ingredient);
    }

    public void deleteIngredient(int ingredientId) throws Exception {
        requirePositive(ingredientId, "ingredientId must be > 0");
        ingredientDao.delete(ingredientId);
    }

    private static void validateIngredient(Ingredient ingredient, boolean requireId) {
        requireNotNull(ingredient, "Ingredient is required");
        if (requireId) requirePositive(ingredient.getId(), "Ingredient.id must be > 0");
        requireNotBlank(ingredient.getName(), "Ingredient.name is required");
        requireNotNull(ingredient.getCategory(), "Ingredient.category is required");
        requirePositive(ingredient.getCategory().getId(), "Ingredient.category.id must be > 0");
        requireNotBlank(ingredient.getDefaultUnit(), "Ingredient.defaultUnit is required");
    }

    // -------------------------
    // Locations
    // -------------------------

    public void createLocation(Location location) throws Exception {
        requireNotNull(location, "Location is required");
        requireNotBlank(location.getName(), "Location.name is required");
        locationDao.insert(location);
    }

    public List<Location> getAllLocations() throws Exception {
        return locationDao.getAll();
    }

    public Location getLocationById(int locationId) throws Exception {
        requirePositive(locationId, "locationId must be > 0");
        return locationDao.getById(locationId);
    }

    public List<Location> searchLocationsByName(String name) throws Exception {
        requireNotBlank(name, "name is required");
        return locationDao.searchByName(name);
    }

    public void updateLocation(Location location) throws Exception {
        requireNotNull(location, "Location is required");
        requirePositive(location.getId(), "Location.id must be > 0");
        requireNotBlank(location.getName(), "Location.name is required");
        locationDao.update(location);
    }

    public void deleteLocation(int locationId) throws Exception {
        requirePositive(locationId, "locationId must be > 0");
        locationDao.delete(locationId);
    }

    // -------------------------
    // Inventory Items
    // -------------------------

    public void createInventoryItem(InventoryItem item) throws Exception {
        validateInventoryItem(item, false);
        inventoryItemDao.insert(item);
    }

    public List<InventoryItem> getAllInventoryItems() throws Exception {
        return inventoryItemDao.getAll();
    }

    public InventoryItem getInventoryItemById(int inventoryItemId) throws Exception {
        requirePositive(inventoryItemId, "inventoryItemId must be > 0");
        return inventoryItemDao.getById(inventoryItemId);
    }

    public List<InventoryItem> getInventoryItemsByLocationId(int locationId) throws Exception {
        requirePositive(locationId, "locationId must be > 0");
        return inventoryItemDao.getByLocationId(locationId);
    }

    public List<InventoryItem> getInventoryItemsByIngredientId(int ingredientId) throws Exception {
        requirePositive(ingredientId, "ingredientId must be > 0");
        return inventoryItemDao.getByIngredientId(ingredientId);
    }

    public void updateInventoryItem(InventoryItem item) throws Exception {
        validateInventoryItem(item, true);
        inventoryItemDao.update(item);
    }

    public void deleteInventoryItem(int inventoryItemId) throws Exception {
        requirePositive(inventoryItemId, "inventoryItemId must be > 0");
        inventoryItemDao.delete(inventoryItemId);
    }

    private static void validateInventoryItem(InventoryItem item, boolean requireId) {
        requireNotNull(item, "InventoryItem is required");
        if (requireId) requirePositive(item.getId(), "InventoryItem.id must be > 0");

        requireNotNull(item.getIngredient(), "InventoryItem.ingredient is required");
        requirePositive(item.getIngredient().getId(), "InventoryItem.ingredient.id must be > 0");

        requireNotNull(item.getLocation(), "InventoryItem.location is required");
        requirePositive(item.getLocation().getId(), "InventoryItem.location.id must be > 0");

        if (item.getQuantity() < 0) throw new ValidationException("InventoryItem.quantity must be >= 0");
        requireNotBlank(item.getUnit(), "InventoryItem.unit is required");
    }

    // -------------------------
    // Meals
    // -------------------------

    public void createMeal(Meal meal) throws Exception {
        requireNotNull(meal, "Meal is required");
        requireNotBlank(meal.getName(), "Meal.name is required");
        mealDao.insert(meal);
    }

    public List<Meal> getAllMeals() throws Exception {
        return mealDao.getAll();
    }

    public Meal getMealById(int mealId) throws Exception {
        requirePositive(mealId, "mealId must be > 0");
        return mealDao.getById(mealId);
    }

    public List<Meal> searchMealsByName(String name) throws Exception {
        requireNotBlank(name, "name is required");
        return mealDao.searchByName(name);
    }

    public void updateMeal(Meal meal) throws Exception {
        requireNotNull(meal, "Meal is required");
        requirePositive(meal.getId(), "Meal.id must be > 0");
        requireNotBlank(meal.getName(), "Meal.name is required");
        mealDao.update(meal);
    }

    public void deleteMeal(int mealId) throws Exception {
        requirePositive(mealId, "mealId must be > 0");
        mealDao.delete(mealId);
    }

    // -------------------------
    // Recipes
    // -------------------------

    public void createRecipe(Recipe recipe) throws Exception {
        validateRecipe(recipe, false);
        recipeDao.insert(recipe);
    }

    public List<Recipe> getAllRecipes() throws Exception {
        return recipeDao.getAll();
    }

    public Recipe getRecipeById(int recipeId) throws Exception {
        requirePositive(recipeId, "recipeId must be > 0");
        return recipeDao.getById(recipeId);
    }

    public List<Recipe> getRecipesByMealId(int mealId) throws Exception {
        requirePositive(mealId, "mealId must be > 0");
        return recipeDao.getByMealId(mealId);
    }

    public void updateRecipe(Recipe recipe) throws Exception {
        validateRecipe(recipe, true);
        recipeDao.update(recipe);
    }

    public void deleteRecipe(int recipeId) throws Exception {
        requirePositive(recipeId, "recipeId must be > 0");
        recipeDao.delete(recipeId);
    }

    private static void validateRecipe(Recipe recipe, boolean requireId) {
        requireNotNull(recipe, "Recipe is required");
        if (requireId) requirePositive(recipe.getId(), "Recipe.id must be > 0");
        requireNotNull(recipe.getMeal(), "Recipe.meal is required");
        requirePositive(recipe.getMeal().getId(), "Recipe.meal.id must be > 0");
        if (recipe.getServings() <= 0) throw new ValidationException("Recipe.servings must be > 0");
        requireNotBlank(recipe.getInstructions(), "Recipe.instructions is required");
    }

    // -------------------------
    // Prepared Meals
    // -------------------------

    public void createPreparedMeal(PreparedMeal pm) throws Exception {
        validatePreparedMeal(pm, false);
        preparedMealDao.insert(pm);
    }

    public List<PreparedMeal> getAllPreparedMeals() throws Exception {
        return preparedMealDao.getAll();
    }

    public PreparedMeal getPreparedMealById(int preparedMealId) throws Exception {
        requirePositive(preparedMealId, "preparedMealId must be > 0");
        return preparedMealDao.getById(preparedMealId);
    }

    public List<PreparedMeal> getPreparedMealsByLocationId(int locationId) throws Exception {
        requirePositive(locationId, "locationId must be > 0");
        return preparedMealDao.getByLocationId(locationId);
    }

    public void updatePreparedMeal(PreparedMeal pm) throws Exception {
        validatePreparedMeal(pm, true);
        preparedMealDao.update(pm);
    }

    public void deletePreparedMeal(int preparedMealId) throws Exception {
        requirePositive(preparedMealId, "preparedMealId must be > 0");
        preparedMealDao.delete(preparedMealId);
    }

    private static void validatePreparedMeal(PreparedMeal pm, boolean requireId) {
        requireNotNull(pm, "PreparedMeal is required");
        if (requireId) requirePositive(pm.getId(), "PreparedMeal.id must be > 0");

        requireNotBlank(pm.getName(), "PreparedMeal.name is required");
        requireNotNull(pm.getLocation(), "PreparedMeal.location is required");
        requirePositive(pm.getLocation().getId(), "PreparedMeal.location.id must be > 0");

        if (pm.getServingsTotal() <= 0) throw new ValidationException("PreparedMeal.servingsTotal must be > 0");
        if (pm.getServingsRemaining() < 0) throw new ValidationException("PreparedMeal.servingsRemaining must be >= 0");
        if (pm.getServingsRemaining() > pm.getServingsTotal()) {
            throw new ValidationException("PreparedMeal.servingsRemaining cannot exceed servingsTotal");
        }
        if (pm.getPrepDate() == null) throw new ValidationException("PreparedMeal.prepDate is required");
    }

    // -------------------------
    // Recipe Ingredients (Join table)
    // -------------------------

    public void createRecipeIngredient(RecipeIngredient ri) throws Exception {
        validateRecipeIngredient(ri);
        recipeIngredientDao.insert(ri);
    }

    public List<RecipeIngredient> getAllRecipeIngredients() throws Exception {
        return recipeIngredientDao.getAll();
    }

    public List<RecipeIngredient> getAllRecipeIngredientsForRecipe(int recipeId) throws Exception {
        requirePositive(recipeId, "recipeId must be > 0");
        return recipeIngredientDao.getAllForRecipe(recipeId);
    }

    public List<RecipeIngredient> getAllRecipeIngredientsForIngredient(int ingredientId) throws Exception {
        requirePositive(ingredientId, "ingredientId must be > 0");
        return recipeIngredientDao.getAllForIngredient(ingredientId);
    }

    public RecipeIngredient getRecipeIngredientByIds(int recipeId, int ingredientId) throws Exception {
        requirePositive(recipeId, "recipeId must be > 0");
        requirePositive(ingredientId, "ingredientId must be > 0");
        return recipeIngredientDao.getByIds(recipeId, ingredientId);
    }

    public void updateRecipeIngredient(RecipeIngredient ri) throws Exception {
        validateRecipeIngredient(ri);
        recipeIngredientDao.update(ri);
    }

    public void deleteRecipeIngredient(int recipeId, int ingredientId) throws Exception {
        requirePositive(recipeId, "recipeId must be > 0");
        requirePositive(ingredientId, "ingredientId must be > 0");
        recipeIngredientDao.delete(recipeId, ingredientId);
    }

    private static void validateRecipeIngredient(RecipeIngredient ri) {
        requireNotNull(ri, "RecipeIngredient is required");
        requireNotNull(ri.getRecipe(), "RecipeIngredient.recipe is required");
        requirePositive(ri.getRecipe().getId(), "RecipeIngredient.recipe.id must be > 0");
        requireNotNull(ri.getIngredient(), "RecipeIngredient.ingredient is required");
        requirePositive(ri.getIngredient().getId(), "RecipeIngredient.ingredient.id must be > 0");
        if (ri.getAmount() <= 0) throw new ValidationException("RecipeIngredient.amount must be > 0");
        requireNotBlank(ri.getUnit(), "RecipeIngredient.unit is required");
    }

    private static void requireNotNull(Object o, String msg) {
        if (o == null) throw new ValidationException(msg);
    }

    private static void requireNotBlank(String s, String msg) {
        if (s == null || s.trim().isEmpty()) throw new ValidationException(msg);
    }

    private static void requirePositive(int n, String msg) {
        if (n <= 0) throw new ValidationException(msg);
    }
}