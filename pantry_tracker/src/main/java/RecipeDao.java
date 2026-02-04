import java.sql.*;
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
        List<Recipe> list = new ArrayList<>();
        String sql = """
            SELECT r.recipe_id, r.servings, r.instructions,
                   m.meal_id, m.name AS meal_name
            FROM recipe r
            JOIN meal m ON r.meal_id = m.meal_id
            ORDER BY m.name
        """;
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Meal m = new Meal(rs.getInt("meal_id"), rs.getString("meal_name"));
                list.add(new Recipe(
                        rs.getInt("recipe_id"),
                        m,
                        rs.getInt("servings"),
                        rs.getString("instructions")
                ));
            }
        }
        return list;
    }

    public void update(Recipe r) throws Exception {
        String sql = "UPDATE recipe SET servings=?, instructions=? WHERE recipe_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getServings());
            ps.setString(2, r.getInstructions());
            ps.setInt(3, r.getId());
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
}
