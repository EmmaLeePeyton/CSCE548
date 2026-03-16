import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;

public class PantryServiceApi {

    public static void main(String[] args) {
        PantryBusinessLayer business = new PantryBusinessLayer();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
            config.jsonMapper(new JavalinJackson(mapper));

            config.plugins.enableCors(cors -> {
                cors.add(it -> it.anyHost());
            });
        });

        app.get("/health", ctx -> ctx.result("{\"status\":\"ok\"}"));

        app.exception(ValidationException.class, (e, ctx) -> {
            ctx.status(400).json(new ErrorResponse(e.getMessage()));
        });
        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.status(500).json(new ErrorResponse("Internal server error: " + e.getMessage()));
        });

        // Ingredient Categories
        app.post("/categories", ctx -> {
            IngredientCategory c = ctx.bodyAsClass(IngredientCategory.class);
            business.createIngredientCategory(c);
            ctx.status(201).json(c);
        });
        app.get("/categories/search", ctx -> {
            String name = ctx.queryParam("name");
            ctx.json(business.searchIngredientCategoriesByName(name));
        });
        app.get("/categories/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            IngredientCategory category = business.getIngredientCategoryById(id);
            respondOr404(ctx, category, "Category not found");
        });
        app.get("/categories", ctx -> ctx.json(business.getAllIngredientCategories()));
        app.put("/categories", ctx -> {
            IngredientCategory c = ctx.bodyAsClass(IngredientCategory.class);
            business.updateIngredientCategory(c);
            ctx.status(200).json(c);
        });
        app.delete("/categories/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            business.deleteIngredientCategory(id);
            ctx.status(204);
        });

        // Ingredients
        app.post("/ingredients", ctx -> {
            Ingredient i = ctx.bodyAsClass(Ingredient.class);
            business.createIngredient(i);
            ctx.status(201).json(i);
        });
        app.get("/ingredients/search", ctx -> {
            String name = ctx.queryParam("name");
            ctx.json(business.searchIngredientsByName(name));
        });
        app.get("/ingredients/category/{categoryId}", ctx -> {
            int categoryId = Integer.parseInt(ctx.pathParam("categoryId"));
            ctx.json(business.getIngredientsByCategoryId(categoryId));
        });
        app.get("/ingredients/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Ingredient ingredient = business.getIngredientById(id);
            respondOr404(ctx, ingredient, "Ingredient not found");
        });
        app.get("/ingredients", ctx -> ctx.json(business.getAllIngredients()));
        app.put("/ingredients", ctx -> {
            Ingredient i = ctx.bodyAsClass(Ingredient.class);
            business.updateIngredient(i);
            ctx.status(200).json(i);
        });
        app.delete("/ingredients/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            business.deleteIngredient(id);
            ctx.status(204);
        });

        // Locations
        app.post("/locations", ctx -> {
            Location l = ctx.bodyAsClass(Location.class);
            business.createLocation(l);
            ctx.status(201).json(l);
        });
        app.get("/locations/search", ctx -> {
            String name = ctx.queryParam("name");
            ctx.json(business.searchLocationsByName(name));
        });
        app.get("/locations/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Location location = business.getLocationById(id);
            respondOr404(ctx, location, "Location not found");
        });
        app.get("/locations", ctx -> ctx.json(business.getAllLocations()));
        app.put("/locations", ctx -> {
            Location l = ctx.bodyAsClass(Location.class);
            business.updateLocation(l);
            ctx.status(200).json(l);
        });
        app.delete("/locations/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            business.deleteLocation(id);
            ctx.status(204);
        });

        // Inventory
        app.post("/inventory", ctx -> {
            InventoryItem it = ctx.bodyAsClass(InventoryItem.class);
            business.createInventoryItem(it);
            ctx.status(201).json(it);
        });
        app.get("/inventory/location/{locationId}", ctx -> {
            int locationId = Integer.parseInt(ctx.pathParam("locationId"));
            ctx.json(business.getInventoryItemsByLocationId(locationId));
        });
        app.get("/inventory/ingredient/{ingredientId}", ctx -> {
            int ingredientId = Integer.parseInt(ctx.pathParam("ingredientId"));
            ctx.json(business.getInventoryItemsByIngredientId(ingredientId));
        });
        app.get("/inventory/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            InventoryItem item = business.getInventoryItemById(id);
            respondOr404(ctx, item, "Inventory item not found");
        });
        app.get("/inventory", ctx -> ctx.json(business.getAllInventoryItems()));
        app.put("/inventory", ctx -> {
            InventoryItem it = ctx.bodyAsClass(InventoryItem.class);
            business.updateInventoryItem(it);
            ctx.status(200).json(it);
        });
        app.delete("/inventory/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            business.deleteInventoryItem(id);
            ctx.status(204);
        });

        // Meals
        app.post("/meals", ctx -> {
            Meal m = ctx.bodyAsClass(Meal.class);
            business.createMeal(m);
            ctx.status(201).json(m);
        });
        app.get("/meals/search", ctx -> {
            String name = ctx.queryParam("name");
            ctx.json(business.searchMealsByName(name));
        });
        app.get("/meals/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Meal meal = business.getMealById(id);
            respondOr404(ctx, meal, "Meal not found");
        });
        app.get("/meals", ctx -> ctx.json(business.getAllMeals()));
        app.put("/meals", ctx -> {
            Meal m = ctx.bodyAsClass(Meal.class);
            business.updateMeal(m);
            ctx.status(200).json(m);
        });
        app.delete("/meals/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            business.deleteMeal(id);
            ctx.status(204);
        });

        // Recipes
        app.post("/recipes", ctx -> {
            Recipe r = ctx.bodyAsClass(Recipe.class);
            business.createRecipe(r);
            ctx.status(201).json(r);
        });
        app.get("/recipes/meal/{mealId}", ctx -> {
            int mealId = Integer.parseInt(ctx.pathParam("mealId"));
            ctx.json(business.getRecipesByMealId(mealId));
        });
        app.get("/recipes/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Recipe recipe = business.getRecipeById(id);
            respondOr404(ctx, recipe, "Recipe not found");
        });
        app.get("/recipes", ctx -> ctx.json(business.getAllRecipes()));
        app.put("/recipes", ctx -> {
            Recipe r = ctx.bodyAsClass(Recipe.class);
            business.updateRecipe(r);
            ctx.status(200).json(r);
        });
        app.delete("/recipes/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            business.deleteRecipe(id);
            ctx.status(204);
        });

        // Prepared Meals
        app.post("/prepared-meals", ctx -> {
            PreparedMeal pm = ctx.bodyAsClass(PreparedMeal.class);
            business.createPreparedMeal(pm);
            ctx.status(201).json(pm);
        });
        app.get("/prepared-meals/location/{locationId}", ctx -> {
            int locationId = Integer.parseInt(ctx.pathParam("locationId"));
            ctx.json(business.getPreparedMealsByLocationId(locationId));
        });
        app.get("/prepared-meals/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            PreparedMeal pm = business.getPreparedMealById(id);
            respondOr404(ctx, pm, "Prepared meal not found");
        });
        app.get("/prepared-meals", ctx -> ctx.json(business.getAllPreparedMeals()));
        app.put("/prepared-meals", ctx -> {
            PreparedMeal pm = ctx.bodyAsClass(PreparedMeal.class);
            business.updatePreparedMeal(pm);
            ctx.status(200).json(pm);
        });
        app.delete("/prepared-meals/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            business.deletePreparedMeal(id);
            ctx.status(204);
        });

        // Recipe Ingredients
        app.post("/recipe-ingredients", ctx -> {
            RecipeIngredient ri = ctx.bodyAsClass(RecipeIngredient.class);
            business.createRecipeIngredient(ri);
            ctx.status(201).json(ri);
        });
        app.get("/recipe-ingredients/recipe/{recipeId}/ingredient/{ingredientId}", ctx -> {
            int recipeId = Integer.parseInt(ctx.pathParam("recipeId"));
            int ingredientId = Integer.parseInt(ctx.pathParam("ingredientId"));
            RecipeIngredient ri = business.getRecipeIngredientByIds(recipeId, ingredientId);
            respondOr404(ctx, ri, "Recipe ingredient not found");
        });
        app.get("/recipe-ingredients/recipe/{recipeId}", ctx -> {
            int recipeId = Integer.parseInt(ctx.pathParam("recipeId"));
            ctx.json(business.getAllRecipeIngredientsForRecipe(recipeId));
        });
        app.get("/recipe-ingredients/ingredient/{ingredientId}", ctx -> {
            int ingredientId = Integer.parseInt(ctx.pathParam("ingredientId"));
            ctx.json(business.getAllRecipeIngredientsForIngredient(ingredientId));
        });
        app.get("/recipe-ingredients", ctx -> ctx.json(business.getAllRecipeIngredients()));
        app.put("/recipe-ingredients", ctx -> {
            RecipeIngredient ri = ctx.bodyAsClass(RecipeIngredient.class);
            business.updateRecipeIngredient(ri);
            ctx.status(200).json(ri);
        });
        app.delete("/recipe-ingredients/recipe/{recipeId}/ingredient/{ingredientId}", ctx -> {
            int recipeId = Integer.parseInt(ctx.pathParam("recipeId"));
            int ingredientId = Integer.parseInt(ctx.pathParam("ingredientId"));
            business.deleteRecipeIngredient(recipeId, ingredientId);
            ctx.status(204);
        });

        app.start(7070);
        System.out.println("PantryServiceApi started on http://localhost:7070");
    }

    private static void respondOr404(io.javalin.http.Context ctx, Object body, String notFoundMessage) {
        if (body == null) {
            ctx.status(404).json(new ErrorResponse(notFoundMessage));
        } else {
            ctx.json(body);
        }
    }

    public static class ErrorResponse {
        public String error;
        public ErrorResponse(String error) { this.error = error; }
    }
}