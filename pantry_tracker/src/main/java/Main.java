
import java.time.LocalDate;
import java.util.List;

public class Main {

    // ---------- tiny helpers to fetch the DB-generated IDs ----------
    private static Location findLocationByName(List<Location> list, String name) {
        for (Location l : list) if (l.getName().equalsIgnoreCase(name)) return l;
        return null;
    }

    private static IngredientCategory findCategoryByName(List<IngredientCategory> list, String name) {
        for (IngredientCategory c : list) if (c.getName().equalsIgnoreCase(name)) return c;
        return null;
    }

    private static Meal findMealByName(List<Meal> list, String name) {
        for (Meal m : list) if (m.getName().equalsIgnoreCase(name)) return m;
        return null;
    }

    private static Ingredient findIngredientByName(List<Ingredient> list, String name) {
        for (Ingredient i : list) if (i.getName().equalsIgnoreCase(name)) return i;
        return null;
    }

    private static Recipe findRecipeForMeal(List<Recipe> list, int mealId) {
        for (Recipe r : list) if (r.getMeal() != null && r.getMeal().getId() == mealId) return r;
        return null;
    }

    private static PreparedMeal findPreparedMealByName(List<PreparedMeal> list, String name) {
        for (PreparedMeal pm : list) if (pm.getName().equalsIgnoreCase(name)) return pm;
        return null;
    }

    private static InventoryItem findInventoryItem(List<InventoryItem> list, int ingredientId, int locationId) {
        for (InventoryItem inv : list) {
            if (inv.getIngredient() != null && inv.getLocation() != null
                    && inv.getIngredient().getId() == ingredientId
                    && inv.getLocation().getId() == locationId) {
                return inv;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            LocationDao locationDao = new LocationDao();
            IngredientCategoryDao categoryDao = new IngredientCategoryDao();
            IngredientDao ingredientDao = new IngredientDao();
            InventoryItemDao inventoryDao = new InventoryItemDao();
            MealDao mealDao = new MealDao();
            RecipeDao recipeDao = new RecipeDao();
            RecipeIngredientDao recipeIngredientDao = new RecipeIngredientDao();
            PreparedMealDao preparedMealDao = new PreparedMealDao();

            // -------------------- READ (existing seeded data) --------------------
            System.out.println("=== READ (existing seeded data) ===");
            System.out.println("Locations: " + locationDao.getAll().size());
            System.out.println("Categories: " + categoryDao.getAll().size());
            System.out.println("Ingredients: " + ingredientDao.getAll().size());
            System.out.println("Meals: " + mealDao.getAll().size());
            System.out.println("Recipes: " + recipeDao.getAll().size());
            System.out.println("Inventory Items: " + inventoryDao.getAll().size());
            System.out.println("Prepared Meals: " + preparedMealDao.getAll().size());

            // -------------------- CREATE --------------------
            System.out.println("\n=== CREATE (demo records) ===");

            // Location
            String locName = "CRUD Test Shelf";
            try {
                locationDao.insert(new Location(0, locName));
                System.out.println("Inserted Location: " + locName);
            } catch (java.sql.SQLIntegrityConstraintViolationException e) {
                System.out.println("Location already exists -> skipping: " + locName);
            }
            Location loc = findLocationByName(locationDao.getAll(), locName);

            // Category
            String catName = "CRUD Test Category";
            try {
                categoryDao.insert(new IngredientCategory(0, catName));
                System.out.println("Inserted Category: " + catName);
            } catch (java.sql.SQLIntegrityConstraintViolationException e) {
                System.out.println("Category already exists -> skipping: " + catName);
            }
            IngredientCategory cat = findCategoryByName(categoryDao.getAll(), catName);

            // Ingredient
            String ingName = "CRUD Test Ingredient";
            String ingUnit = "unit";
            try {
                ingredientDao.insert(new Ingredient(0, ingName, cat, ingUnit));
                System.out.println("Inserted Ingredient: " + ingName);
            } catch (java.sql.SQLIntegrityConstraintViolationException e) {
                System.out.println("Ingredient already exists -> skipping: " + ingName);
            }
            Ingredient ing = findIngredientByName(ingredientDao.getAll(), ingName);

            // Meal
            String mealName = "CRUD Test Meal";
            try {
                mealDao.insert(new Meal(0, mealName));
                System.out.println("Inserted Meal: " + mealName);
            } catch (java.sql.SQLIntegrityConstraintViolationException e) {
                System.out.println("Meal already exists -> skipping: " + mealName);
            }
            Meal meal = findMealByName(mealDao.getAll(), mealName);

            // Recipe (unique per meal)
            String instructions = "CRUD demo instructions";
            try {
                recipeDao.insert(new Recipe(0, meal, 1, instructions));
                System.out.println("Inserted Recipe for meal: " + mealName);
            } catch (java.sql.SQLIntegrityConstraintViolationException e) {
                System.out.println("Recipe already exists for meal -> skipping: " + mealName);
            }
            Recipe recipe = findRecipeForMeal(recipeDao.getAll(), meal.getId());

            // RecipeIngredient (composite key recipe_id + ingredient_id)
            try {
                recipeIngredientDao.insert(new RecipeIngredient(recipe, ing, 1.0, ingUnit));
                System.out.println("Inserted RecipeIngredient: " + ingName);
            } catch (java.sql.SQLIntegrityConstraintViolationException e) {
                System.out.println("RecipeIngredient already exists -> skipping");
            }

            // InventoryItem
            try {
                inventoryDao.insert(new InventoryItem(0, ing, loc, 1.0, ingUnit, null, "demo"));
                System.out.println("Inserted Inventory Item");
            } catch (java.sql.SQLIntegrityConstraintViolationException e) {
                System.out.println("Inventory Item already exists -> skipping");
            }
            InventoryItem inv = findInventoryItem(inventoryDao.getAll(), ing.getId(), loc.getId());

            // PreparedMeal
            String pmName = "CRUD Test Leftovers";
            LocalDate today = LocalDate.now();
            try {
                preparedMealDao.insert(new PreparedMeal(
                        0,
                        pmName,
                        loc,
                        2,
                        2,
                        today,
                        today.plusDays(7),
                        "demo leftovers"
                ));
                System.out.println("Inserted Prepared Meal");
            } catch (java.sql.SQLIntegrityConstraintViolationException e) {
                System.out.println("Prepared meal already exists -> skipping");
            }
            PreparedMeal pm = findPreparedMealByName(preparedMealDao.getAll(), pmName);

            // -------------------- UPDATE --------------------
            System.out.println("\n=== UPDATE (demo records) ===");

            // update location name
            String locName2 = "CRUD Test Shelf (Updated)";
            loc.setName(locName2);
            locationDao.update(loc);
            System.out.println("Updated Location name -> " + locName2);

            // update category name
            String catName2 = "CRUD Test Category (Updated)";
            cat.setName(catName2);
            categoryDao.update(cat);
            System.out.println("Updated Category name -> " + catName2);

            // update ingredient name + unit
            String ingName2 = "CRUD Test Ingredient (Updated)";
            ing.setName(ingName2);
            ing.setDefaultUnit("pcs");
            ingredientDao.update(ing);
            System.out.println("Updated Ingredient -> " + ingName2);

            // update meal name
            String mealName2 = "CRUD Test Meal (Updated)";
            meal.setName(mealName2);
            mealDao.update(meal);
            System.out.println("Updated Meal -> " + mealName2);

            // update recipe servings + instructions
            recipe.setServings(3);
            recipe.setInstructions("Updated instructions");
            recipeDao.update(recipe);
            System.out.println("Updated Recipe");

            // update recipe ingredient amount/unit
            RecipeIngredient ri = new RecipeIngredient(recipe, ing, 2.5, "pcs");
            recipeIngredientDao.update(ri);
            System.out.println("Updated RecipeIngredient");

            // update inventory item quantity/unit
            inv.setQuantity(2.0);
            inv.setUnit("pcs");
            inv.setNote("updated demo");
            inventoryDao.update(inv);
            System.out.println("Updated Inventory Item");

            // update prepared meal remaining + note
            pm.setServingsRemaining(1);
            pm.setNote("updated demo leftovers");
            preparedMealDao.update(pm);
            System.out.println("Updated Prepared Meal");

            // -------------------- DELETE --------------------
            System.out.println("\n=== DELETE (demo records) ===");

            // delete in FK-safe order
            recipeIngredientDao.delete(recipe.getId(), ing.getId());
            System.out.println("Deleted RecipeIngredient");

            preparedMealDao.delete(pm.getId());
            System.out.println("Deleted Prepared Meal");

            inventoryDao.delete(inv.getId());
            System.out.println("Deleted Inventory Item");

            recipeDao.delete(recipe.getId());
            System.out.println("Deleted Recipe");

            mealDao.delete(meal.getId());
            System.out.println("Deleted Meal");

            ingredientDao.delete(ing.getId());
            System.out.println("Deleted Ingredient");

            categoryDao.delete(cat.getId());
            System.out.println("Deleted Category");

            locationDao.delete(loc.getId());
            System.out.println("Deleted Location");

            System.out.println("\nDONE");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
