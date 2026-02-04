import java.time.LocalDate;

public class PreparedMeal {
    private int id;
    private String name;
    private Location location;
    private int servingsTotal;
    private int servingsRemaining;
    private LocalDate prepDate;
    private LocalDate expirationDate; // nullable
    private String note; // nullable

    public PreparedMeal() {}

    public PreparedMeal(int id, String name, Location location, int servingsTotal, int servingsRemaining,
                        LocalDate prepDate, LocalDate expirationDate, String note) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.servingsTotal = servingsTotal;
        this.servingsRemaining = servingsRemaining;
        this.prepDate = prepDate;
        this.expirationDate = expirationDate;
        this.note = note;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public int getServingsTotal() { return servingsTotal; }
    public void setServingsTotal(int servingsTotal) { this.servingsTotal = servingsTotal; }

    public int getServingsRemaining() { return servingsRemaining; }
    public void setServingsRemaining(int servingsRemaining) { this.servingsRemaining = servingsRemaining; }

    public LocalDate getPrepDate() { return prepDate; }
    public void setPrepDate(LocalDate prepDate) { this.prepDate = prepDate; }

    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
