const BASE_URL = "http://localhost:7070";

// ── Human-readable field labels ──────────────────────────────────────────────
const LABELS = {
    id:                 "ID",
    name:               "Name",
    category:           "Category",
    defaultUnit:        "Default Unit",
    ingredient:         "Ingredient",
    location:           "Location",
    quantity:           "Quantity",
    unit:               "Unit",
    expirationDate:     "Expiration Date",
    note:               "Notes",
    meal:               "Meal",
    servings:           "Servings",
    instructions:       "Instructions",
    recipe:             "Recipe",
    amount:             "Amount",
    prepDate:           "Prep Date",
    servingsTotal:      "Total Servings",
    servingsRemaining:  "Servings Remaining",
};

function labelFor(key) {
    return LABELS[key] || key.replace(/([A-Z])/g, " $1")
                              .replace(/^./, s => s.toUpperCase());
}

function formatValue(val) {
    if (val === null || val === undefined || val === "") return "—";
    if (typeof val === "object" && !Array.isArray(val)) {
        return val.name != null ? val.name : (val.id != null ? `ID ${val.id}` : "—");
    }
    if (typeof val === "string" && /^\d{4}-\d{2}-\d{2}$/.test(val)) {
        return new Date(val + "T00:00:00").toLocaleDateString("en-US",
            { year: "numeric", month: "long", day: "numeric" });
    }
    return String(val);
}

function showResults(data) {
    const container = document.getElementById("results");

    if (data && data.message) {
        container.innerHTML =
            `<div class="result-success">&#10003;&nbsp; ${data.message}</div>`;
        return;
    }

    if (Array.isArray(data)) {
        if (data.length === 0) {
            container.innerHTML = `<p class="result-empty">No records found.</p>`;
            return;
        }
        const keys  = Object.keys(data[0]).filter(k => k !== "id");
        const thead = keys.map(k => `<th>${labelFor(k)}</th>`).join("");
        const tbody = data.map(row =>
            `<tr>${keys.map(k => `<td>${formatValue(row[k])}</td>`).join("")}</tr>`
        ).join("");
        container.innerHTML =
            `<table class="result-table">
               <thead><tr>${thead}</tr></thead>
               <tbody>${tbody}</tbody>
             </table>`;
        return;
    }

    if (typeof data === "object") {
        const rows = Object.entries(data)
            .filter(([k]) => k !== "id")
            .map(([k, v]) =>
                `<tr><th>${labelFor(k)}</th><td>${formatValue(v)}</td></tr>`)
            .join("");
        container.innerHTML =
            `<table class="result-detail"><tbody>${rows}</tbody></table>`;
        return;
    }

    container.textContent = String(data);
}

function showError(error) {
    document.getElementById("results").innerHTML =
        `<div class="result-error">&#9888;&nbsp; ${error?.message || "An error occurred."}</div>`;
}

// ── Core helpers ─────────────────────────────────────────────────────────────

function showSection(sectionId, clickedButton) {
    document.querySelectorAll(".panel").forEach(p => p.classList.remove("active-panel"));
    document.querySelectorAll(".nav-btn").forEach(b => b.classList.remove("active"));
    document.getElementById(sectionId).classList.add("active-panel");
    clickedButton.classList.add("active");
    refreshSectionSelects(sectionId);
}

function value(id) { return document.getElementById(id).value; }
function numVal(id) { return Number(document.getElementById(id).value); }
function optDate(id) { const v = value(id); return v === "" ? null : v; }

async function request(path, options = {}) {
    const response = await fetch(`${BASE_URL}${path}`, {
        headers: { "Content-Type": "application/json" },
        ...options
    });
    if (response.status === 204) return { message: "Record deleted successfully." };
    const text = await response.text();
    let data;
    try { data = text ? JSON.parse(text) : {}; }
    catch { data = { raw: text }; }
    if (!response.ok) throw new Error(data.error || `HTTP ${response.status}`);
    return data;
}

// ── Select population ─────────────────────────────────────────────────────────

async function fillSelect(selectId, url, labelFn, valueFn = item => item.id) {
    const sel = document.getElementById(selectId);
    if (!sel) return;
    try {
        const items = await request(url);
        sel.innerHTML = `<option value="">— Select —</option>` +
            items.map(item =>
                `<option value="${valueFn(item)}">${labelFn(item)}</option>`
            ).join("");
    } catch {
        sel.innerHTML = `<option value="">Error loading</option>`;
    }
}

function fillLocations(...ids) {
    ids.forEach(id => fillSelect(id, "/locations", i => i.name));
}
function fillCategories(...ids) {
    ids.forEach(id => fillSelect(id, "/categories", i => i.name));
}
function fillIngredients(...ids) {
    ids.forEach(id => fillSelect(id, "/ingredients", i => i.name));
}
function fillMeals(...ids) {
    ids.forEach(id => fillSelect(id, "/meals", i => i.name));
}
function fillRecipes(...ids) {
    ids.forEach(id => fillSelect(id, "/recipes",
        i => i.meal?.name ? `${i.meal.name} (${i.servings} servings)` : `Recipe ${i.id}`
    ));
}
function fillInventory(...ids) {
    ids.forEach(id => fillSelect(id, "/inventory",
        i => `${i.ingredient?.name || "?"} — ${i.location?.name || "?"} (${i.quantity} ${i.unit})`
    ));
}
function fillPreparedMeals(...ids) {
    ids.forEach(id => fillSelect(id, "/prepared-meals", i => i.name));
}
function fillRecipeIngredients(...ids) {
    ids.forEach(id => fillSelect(id, "/recipe-ingredients",
        i => `${i.ingredient?.name || "?"} for ${i.recipe?.meal?.name || "recipe"}`,
        i => `${i.recipe?.id}_${i.ingredient?.id}`
    ));
}

function refreshSectionSelects(sectionId) {
    switch (sectionId) {
        case "locations":
            fillLocations("updateLocationSelect", "deleteLocationSelect");
            break;
        case "categories":
            fillCategories("updateCategorySelect", "deleteCategorySelect");
            break;
        case "ingredients":
            fillCategories("ingredientByCategorySelect", "newIngredientCategorySelect",
                           "updateIngredientCategorySelect");
            fillIngredients("updateIngredientSelect", "deleteIngredientSelect");
            break;
        case "inventory":
            fillLocations("inventoryByLocationSelect", "newInventoryLocationSelect",
                          "updateInventoryLocationSelect");
            fillIngredients("inventoryByIngredientSelect", "newInventoryIngredientSelect",
                            "updateInventoryIngredientSelect");
            fillInventory("updateInventorySelect", "deleteInventorySelect");
            break;
        case "meals":
            fillMeals("updateMealSelect", "deleteMealSelect");
            break;
        case "recipes":
            fillMeals("recipeByMealSelect", "newRecipeMealSelect", "updateRecipeMealSelect");
            fillRecipes("updateRecipeSelect", "deleteRecipeSelect");
            break;
        case "preparedMeals":
            fillLocations("preparedMealByLocationSelect", "newPreparedMealLocationSelect",
                          "updatePreparedMealLocationSelect");
            fillPreparedMeals("updatePreparedMealSelect", "deletePreparedMealSelect");
            break;
        case "recipeIngredients":
            fillRecipes("riByRecipeSelect", "newRiRecipeSelect");
            fillIngredients("riByIngredientSelect", "newRiIngredientSelect");
            fillRecipeIngredients("updateRiSelect", "deleteRiSelect");
            break;
    }
}

// ── LOCATIONS ────────────────────────────────────────────────────────────────

async function getAllLocations() {
    try { showResults(await request("/locations")); }
    catch (e) { showError(e); }
}
async function searchLocations() {
    try { showResults(await request(`/locations/search?name=${encodeURIComponent(value("locationSearchInput"))}`)); }
    catch (e) { showError(e); }
}
async function createLocation() {
    try {
        showResults(await request("/locations", { method: "POST", body: JSON.stringify({ name: value("newLocationName") }) }));
        fillLocations("updateLocationSelect", "deleteLocationSelect");
    } catch (e) { showError(e); }
}
async function updateLocation() {
    try {
        showResults(await request("/locations", { method: "PUT",
            body: JSON.stringify({ id: numVal("updateLocationSelect"), name: value("updateLocationName") }) }));
        fillLocations("updateLocationSelect", "deleteLocationSelect");
    } catch (e) { showError(e); }
}
async function deleteLocation() {
    try {
        showResults(await request(`/locations/${value("deleteLocationSelect")}`, { method: "DELETE" }));
        fillLocations("updateLocationSelect", "deleteLocationSelect");
    } catch (e) { showError(e); }
}

// ── CATEGORIES ───────────────────────────────────────────────────────────────

async function getAllCategories() {
    try { showResults(await request("/categories")); }
    catch (e) { showError(e); }
}
async function searchCategories() {
    try { showResults(await request(`/categories/search?name=${encodeURIComponent(value("categorySearchInput"))}`)); }
    catch (e) { showError(e); }
}
async function createCategory() {
    try {
        showResults(await request("/categories", { method: "POST", body: JSON.stringify({ name: value("newCategoryName") }) }));
        fillCategories("updateCategorySelect", "deleteCategorySelect");
    } catch (e) { showError(e); }
}
async function updateCategory() {
    try {
        showResults(await request("/categories", { method: "PUT",
            body: JSON.stringify({ id: numVal("updateCategorySelect"), name: value("updateCategoryName") }) }));
        fillCategories("updateCategorySelect", "deleteCategorySelect");
    } catch (e) { showError(e); }
}
async function deleteCategory() {
    try {
        showResults(await request(`/categories/${value("deleteCategorySelect")}`, { method: "DELETE" }));
        fillCategories("updateCategorySelect", "deleteCategorySelect");
    } catch (e) { showError(e); }
}

// ── INGREDIENTS ──────────────────────────────────────────────────────────────

async function getAllIngredients() {
    try { showResults(await request("/ingredients")); }
    catch (e) { showError(e); }
}
async function searchIngredients() {
    try { showResults(await request(`/ingredients/search?name=${encodeURIComponent(value("ingredientSearchInput"))}`)); }
    catch (e) { showError(e); }
}
async function getIngredientsByCategory() {
    try { showResults(await request(`/ingredients/category/${value("ingredientByCategorySelect")}`)); }
    catch (e) { showError(e); }
}
async function createIngredient() {
    try {
        showResults(await request("/ingredients", { method: "POST", body: JSON.stringify({
            name: value("newIngredientName"),
            category: { id: numVal("newIngredientCategorySelect") },
            defaultUnit: value("newIngredientUnit")
        })}));
        fillIngredients("updateIngredientSelect", "deleteIngredientSelect");
    } catch (e) { showError(e); }
}
async function updateIngredient() {
    try {
        showResults(await request("/ingredients", { method: "PUT", body: JSON.stringify({
            id: numVal("updateIngredientSelect"),
            name: value("updateIngredientName"),
            category: { id: numVal("updateIngredientCategorySelect") },
            defaultUnit: value("updateIngredientUnit")
        })}));
        fillIngredients("updateIngredientSelect", "deleteIngredientSelect");
    } catch (e) { showError(e); }
}
async function deleteIngredient() {
    try {
        showResults(await request(`/ingredients/${value("deleteIngredientSelect")}`, { method: "DELETE" }));
        fillIngredients("updateIngredientSelect", "deleteIngredientSelect");
    } catch (e) { showError(e); }
}

// ── INVENTORY ────────────────────────────────────────────────────────────────

async function getAllInventory() {
    try { showResults(await request("/inventory")); }
    catch (e) { showError(e); }
}
async function getInventoryByLocation() {
    try { showResults(await request(`/inventory/location/${value("inventoryByLocationSelect")}`)); }
    catch (e) { showError(e); }
}
async function getInventoryByIngredient() {
    try { showResults(await request(`/inventory/ingredient/${value("inventoryByIngredientSelect")}`)); }
    catch (e) { showError(e); }
}
async function createInventory() {
    try {
        showResults(await request("/inventory", { method: "POST", body: JSON.stringify({
            ingredient: { id: numVal("newInventoryIngredientSelect") },
            location:   { id: numVal("newInventoryLocationSelect") },
            quantity:   Number(value("newInventoryQuantity")),
            unit:       value("newInventoryUnit"),
            expirationDate: optDate("newInventoryExpirationDate"),
            note:       value("newInventoryNote")
        })}));
        fillInventory("updateInventorySelect", "deleteInventorySelect");
    } catch (e) { showError(e); }
}
async function updateInventory() {
    try {
        showResults(await request("/inventory", { method: "PUT", body: JSON.stringify({
            id:         numVal("updateInventorySelect"),
            ingredient: { id: numVal("updateInventoryIngredientSelect") },
            location:   { id: numVal("updateInventoryLocationSelect") },
            quantity:   Number(value("updateInventoryQuantity")),
            unit:       value("updateInventoryUnit"),
            expirationDate: optDate("updateInventoryExpirationDate"),
            note:       value("updateInventoryNote")
        })}));
        fillInventory("updateInventorySelect", "deleteInventorySelect");
    } catch (e) { showError(e); }
}
async function deleteInventory() {
    try {
        showResults(await request(`/inventory/${value("deleteInventorySelect")}`, { method: "DELETE" }));
        fillInventory("updateInventorySelect", "deleteInventorySelect");
    } catch (e) { showError(e); }
}

// ── MEALS ────────────────────────────────────────────────────────────────────

async function getAllMeals() {
    try { showResults(await request("/meals")); }
    catch (e) { showError(e); }
}
async function searchMeals() {
    try { showResults(await request(`/meals/search?name=${encodeURIComponent(value("mealSearchInput"))}`)); }
    catch (e) { showError(e); }
}
async function createMeal() {
    try {
        showResults(await request("/meals", { method: "POST", body: JSON.stringify({ name: value("newMealName") }) }));
        fillMeals("updateMealSelect", "deleteMealSelect");
    } catch (e) { showError(e); }
}
async function updateMeal() {
    try {
        showResults(await request("/meals", { method: "PUT",
            body: JSON.stringify({ id: numVal("updateMealSelect"), name: value("updateMealName") }) }));
        fillMeals("updateMealSelect", "deleteMealSelect");
    } catch (e) { showError(e); }
}
async function deleteMeal() {
    try {
        showResults(await request(`/meals/${value("deleteMealSelect")}`, { method: "DELETE" }));
        fillMeals("updateMealSelect", "deleteMealSelect");
    } catch (e) { showError(e); }
}

// ── RECIPES ──────────────────────────────────────────────────────────────────

async function getAllRecipes() {
    try { showResults(await request("/recipes")); }
    catch (e) { showError(e); }
}
async function getRecipesByMeal() {
    try { showResults(await request(`/recipes/meal/${value("recipeByMealSelect")}`)); }
    catch (e) { showError(e); }
}
async function createRecipe() {
    try {
        showResults(await request("/recipes", { method: "POST", body: JSON.stringify({
            meal:         { id: numVal("newRecipeMealSelect") },
            servings:     numVal("newRecipeServings"),
            instructions: value("newRecipeInstructions")
        })}));
        fillRecipes("updateRecipeSelect", "deleteRecipeSelect");
    } catch (e) { showError(e); }
}
async function updateRecipe() {
    try {
        showResults(await request("/recipes", { method: "PUT", body: JSON.stringify({
            id:           numVal("updateRecipeSelect"),
            meal:         { id: numVal("updateRecipeMealSelect") },
            servings:     numVal("updateRecipeServings"),
            instructions: value("updateRecipeInstructions")
        })}));
        fillRecipes("updateRecipeSelect", "deleteRecipeSelect");
    } catch (e) { showError(e); }
}
async function deleteRecipe() {
    try {
        showResults(await request(`/recipes/${value("deleteRecipeSelect")}`, { method: "DELETE" }));
        fillRecipes("updateRecipeSelect", "deleteRecipeSelect");
    } catch (e) { showError(e); }
}

// ── PREPARED MEALS ───────────────────────────────────────────────────────────

async function getAllPreparedMeals() {
    try { showResults(await request("/prepared-meals")); }
    catch (e) { showError(e); }
}
async function getPreparedMealsByLocation() {
    try { showResults(await request(`/prepared-meals/location/${value("preparedMealByLocationSelect")}`)); }
    catch (e) { showError(e); }
}
async function createPreparedMeal() {
    try {
        showResults(await request("/prepared-meals", { method: "POST", body: JSON.stringify({
            name:              value("newPreparedMealName"),
            location:          { id: numVal("newPreparedMealLocationSelect") },
            servingsTotal:     numVal("newPreparedMealServingsTotal"),
            servingsRemaining: numVal("newPreparedMealServingsRemaining"),
            prepDate:          value("newPreparedMealPrepDate"),
            expirationDate:    optDate("newPreparedMealExpirationDate"),
            note:              value("newPreparedMealNote")
        })}));
        fillPreparedMeals("updatePreparedMealSelect", "deletePreparedMealSelect");
    } catch (e) { showError(e); }
}
async function updatePreparedMeal() {
    try {
        showResults(await request("/prepared-meals", { method: "PUT", body: JSON.stringify({
            id:                numVal("updatePreparedMealSelect"),
            name:              value("updatePreparedMealName"),
            location:          { id: numVal("updatePreparedMealLocationSelect") },
            servingsTotal:     numVal("updatePreparedMealServingsTotal"),
            servingsRemaining: numVal("updatePreparedMealServingsRemaining"),
            prepDate:          value("updatePreparedMealPrepDate"),
            expirationDate:    optDate("updatePreparedMealExpirationDate"),
            note:              value("updatePreparedMealNote")
        })}));
        fillPreparedMeals("updatePreparedMealSelect", "deletePreparedMealSelect");
    } catch (e) { showError(e); }
}
async function deletePreparedMeal() {
    try {
        showResults(await request(`/prepared-meals/${value("deletePreparedMealSelect")}`, { method: "DELETE" }));
        fillPreparedMeals("updatePreparedMealSelect", "deletePreparedMealSelect");
    } catch (e) { showError(e); }
}

// ── RECIPE INGREDIENTS ───────────────────────────────────────────────────────

async function getAllRecipeIngredients() {
    try { showResults(await request("/recipe-ingredients")); }
    catch (e) { showError(e); }
}
async function getRecipeIngredientsByRecipe() {
    try { showResults(await request(`/recipe-ingredients/recipe/${value("riByRecipeSelect")}`)); }
    catch (e) { showError(e); }
}
async function getRecipeIngredientsByIngredient() {
    try { showResults(await request(`/recipe-ingredients/ingredient/${value("riByIngredientSelect")}`)); }
    catch (e) { showError(e); }
}
async function createRecipeIngredient() {
    try {
        showResults(await request("/recipe-ingredients", { method: "POST", body: JSON.stringify({
            recipe:     { id: numVal("newRiRecipeSelect") },
            ingredient: { id: numVal("newRiIngredientSelect") },
            amount:     Number(value("newRecipeIngredientAmount")),
            unit:       value("newRecipeIngredientUnit")
        })}));
        fillRecipeIngredients("updateRiSelect", "deleteRiSelect");
    } catch (e) { showError(e); }
}
async function updateRecipeIngredient() {
    const compound = value("updateRiSelect");
    const [recipeId, ingredientId] = compound.split("_").map(Number);
    try {
        showResults(await request("/recipe-ingredients", { method: "PUT", body: JSON.stringify({
            recipe:     { id: recipeId },
            ingredient: { id: ingredientId },
            amount:     Number(value("updateRecipeIngredientAmount")),
            unit:       value("updateRecipeIngredientUnit")
        })}));
        fillRecipeIngredients("updateRiSelect", "deleteRiSelect");
    } catch (e) { showError(e); }
}
async function deleteRecipeIngredient() {
    const compound = value("deleteRiSelect");
    const [recipeId, ingredientId] = compound.split("_").map(Number);
    try {
        showResults(await request(
            `/recipe-ingredients/recipe/${recipeId}/ingredient/${ingredientId}`,
            { method: "DELETE" }
        ));
        fillRecipeIngredients("updateRiSelect", "deleteRiSelect");
    } catch (e) { showError(e); }
}

// ── Bootstrap ────────────────────────────────────────────────────────────────
window.addEventListener("load", () => refreshSectionSelects("locations"));
