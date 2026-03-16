const BASE_URL = "http://localhost:7070";

function showSection(sectionId, clickedButton) {
    document.querySelectorAll(".panel").forEach(panel => {
        panel.classList.remove("active-panel");
    });

    document.querySelectorAll(".nav-btn").forEach(btn => {
        btn.classList.remove("active");
    });

    document.getElementById(sectionId).classList.add("active-panel");
    clickedButton.classList.add("active");
}

function showResults(data) {
    document.getElementById("results").textContent = JSON.stringify(data, null, 2);
}

function showError(error) {
    document.getElementById("results").textContent =
        "ERROR:\n" + (error?.message || JSON.stringify(error, null, 2));
}

function value(id) {
    return document.getElementById(id).value;
}

function numberValue(id) {
    return Number(document.getElementById(id).value);
}

function optionalDateValue(id) {
    const v = document.getElementById(id).value;
    return v === "" ? null : v;
}

async function request(path, options = {}) {
    const response = await fetch(`${BASE_URL}${path}`, {
        headers: {
            "Content-Type": "application/json"
        },
        ...options
    });

    if (response.status === 204) {
        return { message: "Operation completed successfully (204 No Content)" };
    }

    const text = await response.text();
    let data;

    try {
        data = text ? JSON.parse(text) : {};
    } catch {
        data = { raw: text };
    }

    if (!response.ok) {
        throw new Error(data.error || `HTTP ${response.status}`);
    }

    return data;
}

/* =========================
   LOCATIONS
========================= */

async function getAllLocations() {
    try {
        showResults(await request("/locations"));
    } catch (error) {
        showError(error);
    }
}

async function getLocationById() {
    try {
        showResults(await request(`/locations/${value("locationIdInput")}`));
    } catch (error) {
        showError(error);
    }
}

async function searchLocations() {
    try {
        showResults(await request(`/locations/search?name=${encodeURIComponent(value("locationSearchInput"))}`));
    } catch (error) {
        showError(error);
    }
}

async function createLocation() {
    try {
        const body = { name: value("newLocationName") };
        showResults(await request("/locations", {
            method: "POST",
            body: JSON.stringify(body)
        }));
    } catch (error) {
        showError(error);
    }
}

async function updateLocation() {
    try {
        const body = {
            id: numberValue("updateLocationId"),
            name: value("updateLocationName")
        };

        showResults(await request("/locations", {
            method: "PUT",
            body: JSON.stringify(body)
        }));
    } catch (error) {
        showError(error);
    }
}

async function deleteLocation() {
    try {
        showResults(await request(`/locations/${value("deleteLocationId")}`, {
            method: "DELETE"
        }));
    } catch (error) {
        showError(error);
    }
}

/* =========================
   CATEGORIES
========================= */

async function getAllCategories() {
    try {
        showResults(await request("/categories"));
    } catch (error) {
        showError(error);
    }
}

async function getCategoryById() {
    try {
        showResults(await request(`/categories/${value("categoryIdInput")}`));
    } catch (error) {
        showError(error);
    }
}

async function searchCategories() {
    try {
        showResults(await request(`/categories/search?name=${encodeURIComponent(value("categorySearchInput"))}`));
    } catch (error) {
        showError(error);
    }
}

async function createCategory() {
    try {
        const body = { name: value("newCategoryName") };
        showResults(await request("/categories", {
            method: "POST",
            body: JSON.stringify(body)
        }));
    } catch (error) {
        showError(error);
    }
}

async function updateCategory() {
    try {
        const body = {
            id: numberValue("updateCategoryId"),
            name: value("updateCategoryName")
        };

        showResults(await request("/categories", {
            method: "PUT",
            body: JSON.stringify(body)
        }));
    } catch (error) {
        showError(error);
    }
}

async function deleteCategory() {
    try {
        showResults(await request(`/categories/${value("deleteCategoryId")}`, {
            method: "DELETE"
        }));
    } catch (error) {
        showError(error);
    }
}

/* =========================
   INGREDIENTS
========================= */

async function getAllIngredients() {
    try {
        showResults(await request("/ingredients"));
    } catch (error) {
        showError(error);
    }
}

async function getIngredientById() {
    try {
        showResults(await request(`/ingredients/${value("ingredientIdInput")}`));
    } catch (error) {
        showError(error);
    }
}

async function searchIngredients() {
    try {
        showResults(await request(`/ingredients/search?name=${encodeURIComponent(value("ingredientSearchInput"))}`));
    } catch (error) {
        showError(error);
    }
}

async function getIngredientsByCategory() {
    try {
        showResults(await request(`/ingredients/category/${value("ingredientCategoryIdInput")}`));
    } catch (error) {
        showError(error);
    }
}

async function createIngredient() {
    try {
        const body = {
            name: value("newIngredientName"),
            category: { id: numberValue("newIngredientCategoryId") },
            defaultUnit: value("newIngredientUnit")
        };

        showResults(await request("/ingredients", {
            method: "POST",
            body: JSON.stringify(body)
        }));
    } catch (error) {
        showError(error);
    }
}

async function updateIngredient() {
    try {
        const body = {
            id: numberValue("updateIngredientId"),
            name: value("updateIngredientName"),
            category: { id: numberValue("updateIngredientCategoryId") },
            defaultUnit: value("updateIngredientUnit")
        };

        showResults(await request("/ingredients", {
            method: "PUT",
            body: JSON.stringify(body)
        }));
    } catch (error) {
        showError(error);
    }
}

async function deleteIngredient() {
    try {
        showResults(await request(`/ingredients/${value("deleteIngredientId")}`, {
            method: "DELETE"
        }));
    } catch (error) {
        showError(error);
    }
}

/* =========================
   INVENTORY
========================= */

async function getAllInventory() {
    try {
        showResults(await request("/inventory"));
    } catch (error) {
        showError(error);
    }
}

async function getInventoryById() {
    try {
        showResults(await request(`/inventory/${value("inventoryIdInput")}`));
    } catch (error) {
        showError(error);
    }
}

async function getInventoryByLocation() {
    try {
        showResults(await request(`/inventory/location/${value("inventoryLocationIdInput")}`));
    } catch (error) {
        showError(error);
    }
}

async function getInventoryByIngredient() {
    try {
        showResults(await request(`/inventory/ingredient/${value("inventoryIngredientIdInput")}`));
    } catch (error) {
        showError(error);
    }
}

async function createInventory() {
    try {
        const body = {
            ingredient: { id: numberValue("newInventoryIngredientId") },
            location: { id: numberValue("newInventoryLocationId") },
            quantity: Number(value("newInventoryQuantity")),
            unit: value("newInventoryUnit"),
            expirationDate: optionalDateValue("newInventoryExpirationDate"),
            note: value("newInventoryNote")
        };

        showResults(await request("/inventory", {
            method: "POST",
            body: JSON.stringify(body)
        }));
    } catch (error) {
        showError(error);
    }
}

async function updateInventory() {
    try {
        const body = {
            id: numberValue("updateInventoryId"),
            ingredient: { id: numberValue("updateInventoryIngredientId") },
            location: { id: numberValue("updateInventoryLocationId") },
            quantity: Number(value("updateInventoryQuantity")),
            unit: value("updateInventoryUnit"),
            expirationDate: optionalDateValue("updateInventoryExpirationDate"),
            note: value("updateInventoryNote")
        };

        showResults(await request("/inventory", {
            method: "PUT",
            body: JSON.stringify(body)
        }));
    } catch (error) {
        showError(error);
    }
}

async function deleteInventory() {
    try {
        showResults(await request(`/inventory/${value("deleteInventoryId")}`, {
            method: "DELETE"
        }));
    } catch (error) {
        showError(error);
    }
}

/* =========================
   MEALS
========================= */

async function getAllMeals() {
    try {
        showResults(await request("/meals"));
    } catch (error) {
        showError(error);
    }
}

async function getMealById() {
    try {
        showResults(await request(`/meals/${value("mealIdInput")}`));
    } catch (error) {
        showError(error);
    }
}

async function searchMeals() {
    try {
        showResults(await request(`/meals/search?name=${encodeURIComponent(value("mealSearchInput"))}`));
    } catch (error) {
        showError(error);
    }
}

async function createMeal() {
    try {
        const body = { name: value("newMealName") };
        showResults(await request("/meals", {
            method: "POST",
            body: JSON.stringify(body)
        }));
    } catch (error) {
        showError(error);
    }
}

async function updateMeal() {
    try {
        const body = {
            id: numberValue("updateMealId"),
            name: value("updateMealName")
        };

        showResults(await request("/meals", {
            method: "PUT",
            body: JSON.stringify(body)
        }));
    } catch (error) {
        showError(error);
    }
}

async function deleteMeal() {
    try {
        showResults(await request(`/meals/${value("deleteMealId")}`, {
            method: "DELETE"
        }));
    } catch (error) {
        showError(error);
    }
}

/* =========================
   RECIPES
========================= */

async function getAllRecipes() {
    try {
        showResults(await request("/recipes"));
    } catch (error) {
        showError(error);
    }
}

async function getRecipeById() {
    try {
        showResults(await request(`/recipes/${value("recipeIdInput")}`));
    } catch (error) {
        showError(error);
    }
}

async function getRecipesByMeal() {
    try {
        showResults(await request(`/recipes/meal/${value("recipeMealIdInput")}`));
    } catch (error) {
        showError(error);
    }
}

async function createRecipe() {
    try {
        const body = {
            meal: { id: numberValue("newRecipeMealId") },
            servings: numberValue("newRecipeServings"),
            instructions: value("newRecipeInstructions")
        };

        showResults(await request("/recipes", {
            method: "POST",
            body: JSON.stringify(body)
        }));
    } catch (error) {
        showError(error);
    }
}

async function updateRecipe() {
    try {
        const body = {
            id: numberValue("updateRecipeId"),
            meal: { id: numberValue("updateRecipeMealId") },
            servings: numberValue("updateRecipeServings"),
            instructions: value("updateRecipeInstructions")
        };

        showResults(await request("/recipes", {
            method: "PUT",
            body: JSON.stringify(body)
        }));
    } catch (error) {
        showError(error);
    }
}

async function deleteRecipe() {
    try {
        showResults(await request(`/recipes/${value("deleteRecipeId")}`, {
            method: "DELETE"
        }));
    } catch (error) {
        showError(error);
    }
}

/* =========================
   PREPARED MEALS
========================= */

async function getAllPreparedMeals() {
    try {
        showResults(await request("/prepared-meals"));
    } catch (error) {
        showError(error);
    }
}

async function getPreparedMealById() {
    try {
        showResults(await request(`/prepared-meals/${value("preparedMealIdInput")}`));
    } catch (error) {
        showError(error);
    }
}

async function getPreparedMealsByLocation() {
    try {
        showResults(await request(`/prepared-meals/location/${value("preparedMealLocationIdInput")}`));
    } catch (error) {
        showError(error);
    }
}

async function createPreparedMeal() {
    try {
        const body = {
            name: value("newPreparedMealName"),
            location: { id: numberValue("newPreparedMealLocationId") },
            servingsTotal: numberValue("newPreparedMealServingsTotal"),
            servingsRemaining: numberValue("newPreparedMealServingsRemaining"),
            prepDate: value("newPreparedMealPrepDate"),
            expirationDate: optionalDateValue("newPreparedMealExpirationDate"),
            note: value("newPreparedMealNote")
        };

        showResults(await request("/prepared-meals", {
            method: "POST",
            body: JSON.stringify(body)
        }));
    } catch (error) {
        showError(error);
    }
}

async function updatePreparedMeal() {
    try {
        const body = {
            id: numberValue("updatePreparedMealId"),
            name: value("updatePreparedMealName"),
            location: { id: numberValue("updatePreparedMealLocationId") },
            servingsTotal: numberValue("updatePreparedMealServingsTotal"),
            servingsRemaining: numberValue("updatePreparedMealServingsRemaining"),
            prepDate: value("updatePreparedMealPrepDate"),
            expirationDate: optionalDateValue("updatePreparedMealExpirationDate"),
            note: value("updatePreparedMealNote")
        };

        showResults(await request("/prepared-meals", {
            method: "PUT",
            body: JSON.stringify(body)
        }));
    } catch (error) {
        showError(error);
    }
}

async function deletePreparedMeal() {
    try {
        showResults(await request(`/prepared-meals/${value("deletePreparedMealId")}`, {
            method: "DELETE"
        }));
    } catch (error) {
        showError(error);
    }
}

/* =========================
   RECIPE INGREDIENTS
========================= */

async function getAllRecipeIngredients() {
    try {
        showResults(await request("/recipe-ingredients"));
    } catch (error) {
        showError(error);
    }
}

async function getRecipeIngredientsByRecipe() {
    try {
        showResults(await request(`/recipe-ingredients/recipe/${value("recipeIngredientRecipeIdInput")}`));
    } catch (error) {
        showError(error);
    }
}

async function getRecipeIngredientsByIngredient() {
    try {
        showResults(await request(`/recipe-ingredients/ingredient/${value("recipeIngredientIngredientIdInput")}`));
    } catch (error) {
        showError(error);
    }
}

async function getRecipeIngredientByIds() {
    try {
        showResults(await request(`/recipe-ingredients/recipe/${value("recipeIngredientRecipeIdSingle")}/ingredient/${value("recipeIngredientIngredientIdSingle")}`));
    } catch (error) {
        showError(error);
    }
}

async function createRecipeIngredient() {
    try {
        const body = {
            recipe: { id: numberValue("newRecipeIngredientRecipeId") },
            ingredient: { id: numberValue("newRecipeIngredientIngredientId") },
            amount: Number(value("newRecipeIngredientAmount")),
            unit: value("newRecipeIngredientUnit")
        };

        showResults(await request("/recipe-ingredients", {
            method: "POST",
            body: JSON.stringify(body)
        }));
    } catch (error) {
        showError(error);
    }
}

async function updateRecipeIngredient() {
    try {
        const body = {
            recipe: { id: numberValue("updateRecipeIngredientRecipeId") },
            ingredient: { id: numberValue("updateRecipeIngredientIngredientId") },
            amount: Number(value("updateRecipeIngredientAmount")),
            unit: value("updateRecipeIngredientUnit")
        };

        showResults(await request("/recipe-ingredients", {
            method: "PUT",
            body: JSON.stringify(body)
        }));
    } catch (error) {
        showError(error);
    }
}

async function deleteRecipeIngredient() {
    try {
        showResults(await request(`/recipe-ingredients/recipe/${value("deleteRecipeIngredientRecipeId")}/ingredient/${value("deleteRecipeIngredientIngredientId")}`, {
            method: "DELETE"
        }));
    } catch (error) {
        showError(error);
    }
}