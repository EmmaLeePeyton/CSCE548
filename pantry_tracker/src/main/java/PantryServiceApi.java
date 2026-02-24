import io.javalin.Javalin;

/**
 * SERVICE LAYER (Project 2 - Task 2)
 *
 * This file hosts a REST API that exposes ALL methods in PantryBusinessLayer.
 * Controllers/endpoints call ONLY the business layer (never DAOs directly).
 *
 * HOW TO HOST (Local platform):
 * 1) Open a terminal in the "pantry_tracker" folder (the folder that contains pom.xml)
 * 2) Run: mvn clean package
 * 3) Run: mvn -q exec:java -Dexec.mainClass=PantryServiceApi
 *
 * Alternative run (if you add a jar main manifest / shade plugin later):
 * - java -jar target/<your-jar>.jar
 *
 * Default URL:
 * - http://localhost:7070
 * Health check:
 * - GET http://localhost:7070/health
 */

/**
 * PROJECT 2 - TASK 3 (Hosting)
 *
 * PLATFORM:
 * - Localhost (Windows 11) using Maven + Javalin (Jetty) as the HTTP server.
 *
 * PREREQUISITES:
 * 1) MySQL Server must be running.
 * 2) DBUtil.java must contain correct JDBC URL/username/password.
 * 3) Database/tables must exist (run db_creation.sql and db_insertion.sql once).
 *
 * HOW TO HOST THE SERVICE (Windows PowerShell):
 * 1) cd C:\Users\emmal\Documents\CSCE548\pantry_tracker
 * 2) mvn clean compile
 * 3) mvn exec:java "-Dexec.mainClass=PantryServiceApi"
 *
 * SUCCESS CHECK:
 * - Visit: http://localhost:7070/health
 * - You should see: {"status":"ok"}
 *
 * STOPPING THE SERVICE:
 * - Press Ctrl + C in the terminal window where the server is running.
 *
 * NOTES:
 * - This service layer invokes PantryBusinessLayer, which invokes DAO classes (JDBC) to access MySQL.
 * - Endpoints are REST-style (GET/POST/PUT/DELETE) and return JSON.
 */

public class PantryServiceApi {

    public static void main(String[] args) {
        PantryBusinessLayer business = new PantryBusinessLayer();

        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
        });

        // Basic health endpoint (useful for screenshots)
        app.get("/health", ctx -> ctx.result("{\"status\":\"ok\"}"));

        // Global error handling (helps debugging + makes API predictable)
        app.exception(ValidationException.class, (e, ctx) -> {
            ctx.status(400).json(new ErrorResponse(e.getMessage()));
        });
        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.status(500).json(new ErrorResponse("Internal server error: " + e.getMessage()));
        });

        // -------------------------
        // Ingredient Categories
        // -------------------------
        app.post("/categories", ctx -> {
            IngredientCategory c = ctx.bodyAsClass(IngredientCategory.class);
            business.createIngredientCategory(c);
            ctx.status(201).json(c);
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

        // -------------------------
        // Ingredients
        // -------------------------
        app.post("/ingredients", ctx -> {
            Ingredient i = ctx.bodyAsClass(Ingredient.class);
            business.createIngredient(i);
            ctx.status(201).json(i);
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

        // -------------------------
        // Locations
        // -------------------------
        app.post("/locations", ctx -> {
            Location l = ctx.bodyAsClass(Location.class);
            business.createLocation(l);
            ctx.status(201).json(l);
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

        // -------------------------
        // Inventory Items
        // -------------------------
        app.post("/inventory", ctx -> {
            InventoryItem it = ctx.bodyAsClass(InventoryItem.class);
            business.createInventoryItem(it);
            ctx.status(201).json(it);
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

        // -------------------------
        // Meals
        // -------------------------
        app.post("/meals", ctx -> {
            Meal m = ctx.bodyAsClass(Meal.class);
            business.createMeal(m);
            ctx.status(201).json(m);
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

        // -------------------------
        // Recipes
        // -------------------------
        app.post("/recipes", ctx -> {
            Recipe r = ctx.bodyAsClass(Recipe.class);
            business.createRecipe(r);
            ctx.status(201).json(r);
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

        // -------------------------
        // Prepared Meals
        // -------------------------
        app.post("/prepared-meals", ctx -> {
            PreparedMeal pm = ctx.bodyAsClass(PreparedMeal.class);
            business.createPreparedMeal(pm);
            ctx.status(201).json(pm);
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

        // -------------------------
        // Recipe Ingredients (join table)
        // -------------------------
        app.post("/recipe-ingredients", ctx -> {
            RecipeIngredient ri = ctx.bodyAsClass(RecipeIngredient.class);
            business.createRecipeIngredient(ri);
            ctx.status(201).json(ri);
        });

        app.get("/recipe-ingredients/recipe/{recipeId}", ctx -> {
            int recipeId = Integer.parseInt(ctx.pathParam("recipeId"));
            ctx.json(business.getAllRecipeIngredientsForRecipe(recipeId));
        });

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

    // Tiny JSON error DTO
    public static class ErrorResponse {
        public String error;
        public ErrorResponse(String error) { this.error = error; }
    }
}