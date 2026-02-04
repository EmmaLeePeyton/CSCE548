import java.time.LocalDate;

public class InventoryItem {
    private int id;
    private Ingredient ingredient;
    private Location location;
    private double quantity;
    private String unit;
    private LocalDate expirationDate; // nullable
    private String note; // nullable

    public InventoryItem() {}

    public InventoryItem(int id, Ingredient ingredient, Location location,
                         double quantity, String unit, LocalDate expirationDate, String note) {
        this.id = id;
        this.ingredient = ingredient;
        this.location = location;
        this.quantity = quantity;
        this.unit = unit;
        this.expirationDate = expirationDate;
        this.note = note;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Ingredient getIngredient() { return ingredient; }
    public void setIngredient(Ingredient ingredient) { this.ingredient = ingredient; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
