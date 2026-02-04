USE pantry_tracker;

-- ============================================================
-- OPTIONAL RESET (safe to comment out if not needed)
-- ============================================================
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE recipe_ingredient;
TRUNCATE TABLE recipe;
TRUNCATE TABLE prepared_meal;
TRUNCATE TABLE inventory_item;
TRUNCATE TABLE meal;
TRUNCATE TABLE ingredient;
TRUNCATE TABLE ingredient_category;
TRUNCATE TABLE location;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- LOCATIONS
-- ============================================================
INSERT INTO location (name) VALUES
('Fridge'),
('Freezer'),
('Pantry'),
('Spice Rack');

-- ============================================================
-- INGREDIENT CATEGORIES
-- ============================================================
INSERT INTO ingredient_category (name) VALUES
('Vegetables'),
('Fruits'),
('Meats'),
('Dairy'),
('Grains'),
('Canned/Packaged'),
('Spices/Condiments'),
('Baking');

-- ============================================================
-- INGREDIENTS
-- ============================================================
INSERT INTO ingredient (name, category_id, default_unit) VALUES
('Carrots', 1, 'serving'),
('Onion', 1, 'each'),
('Garlic', 1, 'clove'),
('Bell Pepper', 1, 'each'),
('Spinach', 1, 'serving'),
('Potato', 1, 'each'),
('Tomato', 1, 'each'),
('Banana', 2, 'each'),
('Apple', 2, 'each'),
('Chicken Breast', 3, 'serving'),
('Ground Beef', 3, 'serving'),
('Bacon', 3, 'serving'),
('Milk', 4, 'serving'),
('Eggs', 4, 'each'),
('Butter', 4, 'tbsp'),
('Cheddar Cheese', 4, 'serving'),
('Rice', 5, 'serving'),
('Pasta', 5, 'serving'),
('Bread', 5, 'slice'),
('Tortillas', 5, 'each'),
('Canned Soup', 6, 'can'),
('Canned Beans', 6, 'can'),
('Tomato Sauce', 6, 'can'),
('Chicken Broth', 6, 'serving'),
('Olive Oil', 7, 'tbsp'),
('Salt', 7, 'tsp'),
('Black Pepper', 7, 'tsp'),
('Flour', 8, 'cup'),
('Sugar', 8, 'tbsp');

-- ============================================================
-- MEALS
-- ============================================================
INSERT INTO meal (name) VALUES
('Chicken Stir Fry'),
('Spaghetti with Meat Sauce'),
('Tacos'),
('Breakfast Scramble'),
('Grilled Cheese'),
('Chicken Soup'),
('Rice and Beans'),
('Baked Potatoes');

-- ============================================================
-- RECIPES
-- ============================================================
INSERT INTO recipe (meal_id, servings, instructions) VALUES
((SELECT meal_id FROM meal WHERE name='Chicken Stir Fry'), 2,
 'Cook chicken, sauté vegetables, add sauce, serve over rice.'),
((SELECT meal_id FROM meal WHERE name='Spaghetti with Meat Sauce'), 4,
 'Boil pasta, cook beef, add sauce, simmer.'),
((SELECT meal_id FROM meal WHERE name='Tacos'), 3,
 'Cook beef, warm tortillas, assemble.'),
((SELECT meal_id FROM meal WHERE name='Breakfast Scramble'), 1,
 'Scramble eggs with butter and cheese.'),
((SELECT meal_id FROM meal WHERE name='Grilled Cheese'), 1,
 'Butter bread, add cheese, grill.'),
((SELECT meal_id FROM meal WHERE name='Chicken Soup'), 2,
 'Simmer broth, chicken, and vegetables.'),
((SELECT meal_id FROM meal WHERE name='Rice and Beans'), 3,
 'Cook rice, warm beans, combine.'),
((SELECT meal_id FROM meal WHERE name='Baked Potatoes'), 2,
 'Bake potatoes, add toppings.');

-- ============================================================
-- RECIPE INGREDIENTS
-- ============================================================
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, amount, unit) VALUES
-- Chicken Stir Fry
((SELECT recipe_id FROM recipe r JOIN meal m ON r.meal_id=m.meal_id WHERE m.name='Chicken Stir Fry'),
 (SELECT ingredient_id FROM ingredient WHERE name='Chicken Breast'), 2, 'serving'),
((SELECT recipe_id FROM recipe r JOIN meal m ON r.meal_id=m.meal_id WHERE m.name='Chicken Stir Fry'),
 (SELECT ingredient_id FROM ingredient WHERE name='Bell Pepper'), 1, 'each'),
((SELECT recipe_id FROM recipe r JOIN meal m ON r.meal_id=m.meal_id WHERE m.name='Chicken Stir Fry'),
 (SELECT ingredient_id FROM ingredient WHERE name='Rice'), 2, 'serving'),

-- Spaghetti
((SELECT recipe_id FROM recipe r JOIN meal m ON r.meal_id=m.meal_id WHERE m.name='Spaghetti with Meat Sauce'),
 (SELECT ingredient_id FROM ingredient WHERE name='Pasta'), 4, 'serving'),
((SELECT recipe_id FROM recipe r JOIN meal m ON r.meal_id=m.meal_id WHERE m.name='Spaghetti with Meat Sauce'),
 (SELECT ingredient_id FROM ingredient WHERE name='Ground Beef'), 3, 'serving'),
((SELECT recipe_id FROM recipe r JOIN meal m ON r.meal_id=m.meal_id WHERE m.name='Spaghetti with Meat Sauce'),
 (SELECT ingredient_id FROM ingredient WHERE name='Tomato Sauce'), 1, 'can'),

-- Tacos
((SELECT recipe_id FROM recipe r JOIN meal m ON r.meal_id=m.meal_id WHERE m.name='Tacos'),
 (SELECT ingredient_id FROM ingredient WHERE name='Ground Beef'), 2, 'serving'),
((SELECT recipe_id FROM recipe r JOIN meal m ON r.meal_id=m.meal_id WHERE m.name='Tacos'),
 (SELECT ingredient_id FROM ingredient WHERE name='Tortillas'), 6, 'each'),

-- Breakfast Scramble
((SELECT recipe_id FROM recipe r JOIN meal m ON r.meal_id=m.meal_id WHERE m.name='Breakfast Scramble'),
 (SELECT ingredient_id FROM ingredient WHERE name='Eggs'), 3, 'each'),
((SELECT recipe_id FROM recipe r JOIN meal m ON r.meal_id=m.meal_id WHERE m.name='Breakfast Scramble'),
 (SELECT ingredient_id FROM ingredient WHERE name='Butter'), 1, 'tbsp'),

-- Grilled Cheese
((SELECT recipe_id FROM recipe r JOIN meal m ON r.meal_id=m.meal_id WHERE m.name='Grilled Cheese'),
 (SELECT ingredient_id FROM ingredient WHERE name='Bread'), 2, 'slice'),
((SELECT recipe_id FROM recipe r JOIN meal m ON r.meal_id=m.meal_id WHERE m.name='Grilled Cheese'),
 (SELECT ingredient_id FROM ingredient WHERE name='Cheddar Cheese'), 1, 'serving');

-- ============================================================
-- INVENTORY ITEMS (multiple lots allowed)
-- ============================================================
INSERT INTO inventory_item
(ingredient_id, location_id, quantity, unit, expiration_date, note)
VALUES
((SELECT ingredient_id FROM ingredient WHERE name='Carrots'),
 (SELECT location_id FROM location WHERE name='Fridge'),
 6, 'serving', NULL, 'Large bag'),

((SELECT ingredient_id FROM ingredient WHERE name='Milk'),
 (SELECT location_id FROM location WHERE name='Fridge'),
 4, 'serving', '2026-02-06', 'Half gallon'),

((SELECT ingredient_id FROM ingredient WHERE name='Eggs'),
 (SELECT location_id FROM location WHERE name='Fridge'),
 12, 'each', '2026-02-15', 'Carton'),

((SELECT ingredient_id FROM ingredient WHERE name='Rice'),
 (SELECT location_id FROM location WHERE name='Pantry'),
 20, 'serving', NULL, 'Bulk'),

((SELECT ingredient_id FROM ingredient WHERE name='Chicken Breast'),
 (SELECT location_id FROM location WHERE name='Freezer'),
 5, 'serving', NULL, 'Frozen portions'),

((SELECT ingredient_id FROM ingredient WHERE name='Bread'),
 (SELECT location_id FROM location WHERE name='Pantry'),
 10, 'slice', '2026-02-04', 'Loaf');

-- ============================================================
-- PREPARED MEALS / LEFTOVERS
-- ============================================================
INSERT INTO prepared_meal
(name, location_id, servings_total, servings_remaining, prep_date, expiration_date, note)
VALUES
('Leftover Chili',
 (SELECT location_id FROM location WHERE name='Fridge'),
 4, 2, '2026-01-30', '2026-02-06', 'Homemade'),

('Frozen Lasagna',
 (SELECT location_id FROM location WHERE name='Freezer'),
 6, 6, '2026-01-20', NULL, 'Store bought'),

('Leftover Stir Fry',
 (SELECT location_id FROM location WHERE name='Fridge'),
 3, 1, '2026-01-31', '2026-02-07', 'Eat soon');
