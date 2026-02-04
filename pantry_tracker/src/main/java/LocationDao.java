import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDao {

    public void insert(Location l) throws Exception {
        String sql = "INSERT INTO location (name) VALUES (?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, l.getName());
            ps.executeUpdate();
        }
    }

    public List<Location> getAll() throws Exception {
        List<Location> list = new ArrayList<>();
        String sql = "SELECT location_id, name FROM location ORDER BY name";
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Location(rs.getInt("location_id"), rs.getString("name")));
            }
        }
        return list;
    }

    public void update(Location l) throws Exception {
        String sql = "UPDATE location SET name=? WHERE location_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, l.getName());
            ps.setInt(2, l.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM location WHERE location_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
