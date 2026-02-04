import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientDao {

    public void insert(RecipeIngredient ri) throws Exception {
        String sql = "INSERT INTO recipe_ingredient (recipe_id, ingredient_id, amount, unit) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ri.getRecipe().getId());
            ps.setInt(2, ri.getIngredient().getId());
            ps.setDouble(3, ri.getAmount());
            ps.setString(4, ri.getUnit());
            ps.executeUpdate();
        }
    }

    public List<RecipeIngredient> getAllForRecipe(int recipeId) throws Exception {
        List<RecipeIngredient> list = new ArrayList<>();
        String sql = """
            SELECT ri.amount, ri.unit,
                   r.recipe_id, r.servings, r.instructions,
                   m.meal_id, m.name AS meal_name,
                   ing.ingredient_id, ing.name AS ingredient_name, ing.default_unit,
                   c.category_id, c.name AS category_name
            FROM recipe_ingredient ri
            JOIN recipe r ON ri.recipe_id = r.recipe_id
            JOIN meal m ON r.meal_id = m.meal_id
            JOIN ingredient ing ON ri.ingredient_id = ing.ingredient_id
            JOIN ingredient_category c ON ing.category_id = c.category_id
            WHERE ri.recipe_id = ?
            ORDER BY ing.name
        """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, recipeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Meal meal = new Meal(rs.getInt("meal_id"), rs.getString("meal_name"));
                    Recipe recipe = new Recipe(rs.getInt("recipe_id"), meal, rs.getInt("servings"), rs.getString("instructions"));

                    IngredientCategory cat = new IngredientCategory(rs.getInt("category_id"), rs.getString("category_name"));
                    Ingredient ing = new Ingredient(rs.getInt("ingredient_id"), rs.getString("ingredient_name"), cat, rs.getString("default_unit"));

                    list.add(new RecipeIngredient(recipe, ing, rs.getDouble("amount"), rs.getString("unit")));
                }
            }
        }
        return list;
    }

    public void update(RecipeIngredient ri) throws Exception {
        String sql = "UPDATE recipe_ingredient SET amount=?, unit=? WHERE recipe_id=? AND ingredient_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, ri.getAmount());
            ps.setString(2, ri.getUnit());
            ps.setInt(3, ri.getRecipe().getId());
            ps.setInt(4, ri.getIngredient().getId());
            ps.executeUpdate();
        }
    }

    public void delete(int recipeId, int ingredientId) throws Exception {
        String sql = "DELETE FROM recipe_ingredient WHERE recipe_id=? AND ingredient_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, recipeId);
            ps.setInt(2, ingredientId);
            ps.executeUpdate();
        }
    }
}
