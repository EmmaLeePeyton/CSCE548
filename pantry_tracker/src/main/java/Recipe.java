public class Recipe {
    private int id;
    private Meal meal;
    private int servings;
    private String instructions;

    public Recipe() {}

    public Recipe(int id, Meal meal, int servings, String instructions) {
        this.id = id;
        this.meal = meal;
        this.servings = servings;
        this.instructions = instructions;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Meal getMeal() { return meal; }
    public void setMeal(Meal meal) { this.meal = meal; }

    public int getServings() { return servings; }
    public void setServings(int servings) { this.servings = servings; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
}
