import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class IngredientDao {

    public void insert(Ingredient i) throws Exception {
        String sql = "INSERT INTO ingredient (name, category_id, default_unit) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, i.getName());
            ps.setInt(2, i.getCategory().getId());
            ps.setString(3, i.getDefaultUnit());
            ps.executeUpdate();
        }
    }

    public List<Ingredient> getAll() throws Exception {
        String sql = """
            SELECT ing.ingredient_id,
                   ing.name AS ingredient_name,
                   ing.default_unit,
                   c.category_id,
                   c.name AS category_name
            FROM ingredient ing
            JOIN ingredient_category c ON ing.category_id = c.category_id
            ORDER BY ing.name
        """;
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return mapIngredients(rs);
        }
    }

    public Ingredient getById(int id) throws Exception {
        String sql = """
            SELECT ing.ingredient_id,
                   ing.name AS ingredient_name,
                   ing.default_unit,
                   c.category_id,
                   c.name AS category_name
            FROM ingredient ing
            JOIN ingredient_category c ON ing.category_id = c.category_id
            WHERE ing.ingredient_id = ?
        """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                List<Ingredient> list = mapIngredients(rs);
                return list.isEmpty() ? null : list.get(0);
            }
        }
    }

    public List<Ingredient> searchByName(String name) throws Exception {
        String sql = """
            SELECT ing.ingredient_id,
                   ing.name AS ingredient_name,
                   ing.default_unit,
                   c.category_id,
                   c.name AS category_name
            FROM ingredient ing
            JOIN ingredient_category c ON ing.category_id = c.category_id
            WHERE ing.name LIKE ?
            ORDER BY ing.name
        """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                return mapIngredients(rs);
            }
        }
    }

    public List<Ingredient> getByCategoryId(int categoryId) throws Exception {
        String sql = """
            SELECT ing.ingredient_id,
                   ing.name AS ingredient_name,
                   ing.default_unit,
                   c.category_id,
                   c.name AS category_name
            FROM ingredient ing
            JOIN ingredient_category c ON ing.category_id = c.category_id
            WHERE c.category_id = ?
            ORDER BY ing.name
        """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                return mapIngredients(rs);
            }
        }
    }

    public void update(Ingredient i) throws Exception {
        String sql = "UPDATE ingredient SET name=?, category_id=?, default_unit=? WHERE ingredient_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, i.getName());
            ps.setInt(2, i.getCategory().getId());
            ps.setString(3, i.getDefaultUnit());
            ps.setInt(4, i.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM ingredient WHERE ingredient_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private List<Ingredient> mapIngredients(ResultSet rs) throws Exception {
        List<Ingredient> list = new ArrayList<>();
        while (rs.next()) {
            IngredientCategory cat = new IngredientCategory(
                    rs.getInt("category_id"),
                    rs.getString("category_name")
            );
            list.add(new Ingredient(
                    rs.getInt("ingredient_id"),
                    rs.getString("ingredient_name"),
                    cat,
                    rs.getString("default_unit")
            ));
        }
        return list;
    }
}