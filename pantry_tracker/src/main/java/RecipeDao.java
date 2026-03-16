import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RecipeDao {

    public void insert(Recipe r) throws Exception {
        String sql = "INSERT INTO recipe (meal_id, servings, instructions) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getMeal().getId());
            ps.setInt(2, r.getServings());
            ps.setString(3, r.getInstructions());
            ps.executeUpdate();
        }
    }

    public List<Recipe> getAll() throws Exception {
        String sql = baseSelect() + " ORDER BY m.name";
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return mapRecipes(rs);
        }
    }

    public Recipe getById(int id) throws Exception {
        String sql = baseSelect() + " WHERE r.recipe_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                List<Recipe> list = mapRecipes(rs);
                return list.isEmpty() ? null : list.get(0);
            }
        }
    }

    public List<Recipe> getByMealId(int mealId) throws Exception {
        String sql = baseSelect() + " WHERE r.meal_id = ? ORDER BY r.recipe_id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mealId);
            try (ResultSet rs = ps.executeQuery()) {
                return mapRecipes(rs);
            }
        }
    }

    public void update(Recipe r) throws Exception {
        String sql = "UPDATE recipe SET meal_id=?, servings=?, instructions=? WHERE recipe_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getMeal().getId());
            ps.setInt(2, r.getServings());
            ps.setString(3, r.getInstructions());
            ps.setInt(4, r.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM recipe WHERE recipe_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private String baseSelect() {
        return """
            SELECT r.recipe_id, r.servings, r.instructions,
                   m.meal_id, m.name AS meal_name
            FROM recipe r
            JOIN meal m ON r.meal_id = m.meal_id
        """;
    }

    private List<Recipe> mapRecipes(ResultSet rs) throws Exception {
        List<Recipe> list = new ArrayList<>();
        while (rs.next()) {
            Meal m = new Meal(rs.getInt("meal_id"), rs.getString("meal_name"));
            list.add(new Recipe(
                    rs.getInt("recipe_id"),
                    m,
                    rs.getInt("servings"),
                    rs.getString("instructions")
            ));
        }
        return list;
    }
}