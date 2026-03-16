import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class IngredientCategoryDao {

    public void insert(IngredientCategory c) throws Exception {
        String sql = "INSERT INTO ingredient_category (name) VALUES (?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.executeUpdate();
        }
    }

    public List<IngredientCategory> getAll() throws Exception {
        List<IngredientCategory> list = new ArrayList<>();
        String sql = "SELECT category_id, name FROM ingredient_category ORDER BY name";
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new IngredientCategory(rs.getInt("category_id"), rs.getString("name")));
            }
        }
        return list;
    }

    public IngredientCategory getById(int id) throws Exception {
        String sql = "SELECT category_id, name FROM ingredient_category WHERE category_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new IngredientCategory(rs.getInt("category_id"), rs.getString("name"));
                }
            }
        }
        return null;
    }

    public List<IngredientCategory> searchByName(String name) throws Exception {
        List<IngredientCategory> list = new ArrayList<>();
        String sql = "SELECT category_id, name FROM ingredient_category WHERE name LIKE ? ORDER BY name";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new IngredientCategory(rs.getInt("category_id"), rs.getString("name")));
                }
            }
        }
        return list;
    }

    public void update(IngredientCategory c) throws Exception {
        String sql = "UPDATE ingredient_category SET name=? WHERE category_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setInt(2, c.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM ingredient_category WHERE category_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}