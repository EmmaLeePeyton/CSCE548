import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealDao {

    public void insert(Meal m) throws Exception {
        String sql = "INSERT INTO meal (name) VALUES (?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getName());
            ps.executeUpdate();
        }
    }

    public List<Meal> getAll() throws Exception {
        List<Meal> list = new ArrayList<>();
        String sql = "SELECT meal_id, name FROM meal ORDER BY name";
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Meal(rs.getInt("meal_id"), rs.getString("name")));
            }
        }
        return list;
    }

    public void update(Meal m) throws Exception {
        String sql = "UPDATE meal SET name=? WHERE meal_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getName());
            ps.setInt(2, m.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM meal WHERE meal_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
