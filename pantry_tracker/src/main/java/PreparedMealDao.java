import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PreparedMealDao {

    public void insert(PreparedMeal pm) throws Exception {
        String sql = """
            INSERT INTO prepared_meal (name, location_id, servings_total, servings_remaining, prep_date, expiration_date, note)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pm.getName());
            ps.setInt(2, pm.getLocation().getId());
            ps.setInt(3, pm.getServingsTotal());
            ps.setInt(4, pm.getServingsRemaining());
            ps.setDate(5, Date.valueOf(pm.getPrepDate()));
            if (pm.getExpirationDate() == null) ps.setNull(6, Types.DATE);
            else ps.setDate(6, Date.valueOf(pm.getExpirationDate()));
            ps.setString(7, pm.getNote());
            ps.executeUpdate();
        }
    }

    public List<PreparedMeal> getAll() throws Exception {
        List<PreparedMeal> list = new ArrayList<>();
        String sql = """
            SELECT pm.prepared_meal_id, pm.name, pm.servings_total, pm.servings_remaining,
                   pm.prep_date, pm.expiration_date, pm.note,
                   l.location_id, l.name AS location_name
            FROM prepared_meal pm
            JOIN location l ON pm.location_id = l.location_id
            ORDER BY pm.prep_date DESC, pm.name
        """;
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Location loc = new Location(rs.getInt("location_id"), rs.getString("location_name"));
                LocalDate exp = (rs.getDate("expiration_date") == null) ? null : rs.getDate("expiration_date").toLocalDate();

                list.add(new PreparedMeal(
                        rs.getInt("prepared_meal_id"),
                        rs.getString("name"),
                        loc,
                        rs.getInt("servings_total"),
                        rs.getInt("servings_remaining"),
                        rs.getDate("prep_date").toLocalDate(),
                        exp,
                        rs.getString("note")
                ));
            }
        }
        return list;
    }

    public void update(PreparedMeal pm) throws Exception {
        String sql = """
            UPDATE prepared_meal
            SET name=?, location_id=?, servings_total=?, servings_remaining=?, prep_date=?, expiration_date=?, note=?
            WHERE prepared_meal_id=?
        """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pm.getName());
            ps.setInt(2, pm.getLocation().getId());
            ps.setInt(3, pm.getServingsTotal());
            ps.setInt(4, pm.getServingsRemaining());
            ps.setDate(5, Date.valueOf(pm.getPrepDate()));
            if (pm.getExpirationDate() == null) ps.setNull(6, Types.DATE);
            else ps.setDate(6, Date.valueOf(pm.getExpirationDate()));
            ps.setString(7, pm.getNote());
            ps.setInt(8, pm.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM prepared_meal WHERE prepared_meal_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
