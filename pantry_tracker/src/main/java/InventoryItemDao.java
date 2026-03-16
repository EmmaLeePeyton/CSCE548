import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InventoryItemDao {

    public void insert(InventoryItem inv) throws Exception {
        String sql = """
            INSERT INTO inventory_item (ingredient_id, location_id, quantity, unit, expiration_date, note)
            VALUES (?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, inv.getIngredient().getId());
            ps.setInt(2, inv.getLocation().getId());
            ps.setDouble(3, inv.getQuantity());
            ps.setString(4, inv.getUnit());
            if (inv.getExpirationDate() == null) ps.setNull(5, Types.DATE);
            else ps.setDate(5, Date.valueOf(inv.getExpirationDate()));
            ps.setString(6, inv.getNote());
            ps.executeUpdate();
        }
    }

    public List<InventoryItem> getAll() throws Exception {
        String sql = baseSelect() + " ORDER BY l.name, ing.name";
        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            return mapInventoryItems(rs);
        }
    }

    public InventoryItem getById(int id) throws Exception {
        String sql = baseSelect() + " WHERE inv.inventory_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                List<InventoryItem> list = mapInventoryItems(rs);
                return list.isEmpty() ? null : list.get(0);
            }
        }
    }

    public List<InventoryItem> getByLocationId(int locationId) throws Exception {
        String sql = baseSelect() + " WHERE inv.location_id = ? ORDER BY ing.name";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, locationId);
            try (ResultSet rs = ps.executeQuery()) {
                return mapInventoryItems(rs);
            }
        }
    }

    public List<InventoryItem> getByIngredientId(int ingredientId) throws Exception {
        String sql = baseSelect() + " WHERE inv.ingredient_id = ? ORDER BY l.name";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ingredientId);
            try (ResultSet rs = ps.executeQuery()) {
                return mapInventoryItems(rs);
            }
        }
    }

    public void update(InventoryItem inv) throws Exception {
        String sql = """
            UPDATE inventory_item
            SET ingredient_id=?, location_id=?, quantity=?, unit=?, expiration_date=?, note=?
            WHERE inventory_id=?
        """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, inv.getIngredient().getId());
            ps.setInt(2, inv.getLocation().getId());
            ps.setDouble(3, inv.getQuantity());
            ps.setString(4, inv.getUnit());
            if (inv.getExpirationDate() == null) ps.setNull(5, Types.DATE);
            else ps.setDate(5, Date.valueOf(inv.getExpirationDate()));
            ps.setString(6, inv.getNote());
            ps.setInt(7, inv.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM inventory_item WHERE inventory_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private String baseSelect() {
        return """
            SELECT inv.inventory_id, inv.quantity, inv.unit, inv.expiration_date, inv.note,
                   ing.ingredient_id, ing.name AS ingredient_name, ing.default_unit,
                   c.category_id, c.name AS category_name,
                   l.location_id, l.name AS location_name
            FROM inventory_item inv
            JOIN ingredient ing ON inv.ingredient_id = ing.ingredient_id
            JOIN ingredient_category c ON ing.category_id = c.category_id
            JOIN location l ON inv.location_id = l.location_id
        """;
    }

    private List<InventoryItem> mapInventoryItems(ResultSet rs) throws Exception {
        List<InventoryItem> list = new ArrayList<>();
        while (rs.next()) {
            IngredientCategory cat = new IngredientCategory(rs.getInt("category_id"), rs.getString("category_name"));
            Ingredient ing = new Ingredient(rs.getInt("ingredient_id"), rs.getString("ingredient_name"), cat, rs.getString("default_unit"));
            Location loc = new Location(rs.getInt("location_id"), rs.getString("location_name"));
            LocalDate exp = (rs.getDate("expiration_date") == null) ? null : rs.getDate("expiration_date").toLocalDate();

            list.add(new InventoryItem(
                    rs.getInt("inventory_id"),
                    ing,
                    loc,
                    rs.getDouble("quantity"),
                    rs.getString("unit"),
                    exp,
                    rs.getString("note")
            ));
        }
        return list;
    }
}