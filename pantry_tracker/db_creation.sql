-- ============================================================
-- db_creation.sql
-- Pantry Tracker DB (with recipes, no planned meals)
-- ============================================================

-- Always create + select DB first
CREATE DATABASE IF NOT EXISTS pantry_tracker
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

USE pantry_tracker;

-- ------------------------------------------------------------
-- Optional: drop tables in FK-safe order (ONLY if you want reset)
-- ------------------------------------------------------------
-- SET FOREIGN_KEY_CHECKS = 0;
-- DROP TABLE IF EXISTS recipe_ingredient;
-- DROP TABLE IF EXISTS recipe;
-- DROP TABLE IF EXISTS prepared_meal;
-- DROP TABLE IF EXISTS inventory_item;
-- DROP TABLE IF EXISTS meal;
-- DROP TABLE IF EXISTS ingredient;
-- DROP TABLE IF EXISTS ingredient_category;
-- DROP TABLE IF EXISTS location;
-- SET FOREIGN_KEY_CHECKS = 1;

-- ------------------------------------------------------------
-- 1) location
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS location (
  location_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 2) ingredient_category
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS ingredient_category (
  category_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 3) ingredient
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS ingredient (
  ingredient_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL UNIQUE,
  category_id INT NOT NULL,
  default_unit VARCHAR(30) NOT NULL,
  CONSTRAINT fk_ingredient_category
    FOREIGN KEY (category_id) REFERENCES ingredient_category(category_id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 4) inventory_item  (multiple lots allowed; expiration optional)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS inventory_item (
  inventory_id INT AUTO_INCREMENT PRIMARY KEY,
  ingredient_id INT NOT NULL,
  location_id INT NOT NULL,
  quantity DECIMAL(10,2) NOT NULL DEFAULT 0,
  unit VARCHAR(30) NOT NULL,
  expiration_date DATE NULL,
  note VARCHAR(255) NULL,
  CONSTRAINT chk_inventory_quantity_nonneg CHECK (quantity >= 0),
  CONSTRAINT fk_inventory_ingredient
    FOREIGN KEY (ingredient_id) REFERENCES ingredient(ingredient_id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  CONSTRAINT fk_inventory_location
    FOREIGN KEY (location_id) REFERENCES location(location_id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 5) meal
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS meal (
  meal_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(200) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 6) recipe  (1:1 with meal)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS recipe (
  recipe_id INT AUTO_INCREMENT PRIMARY KEY,
  meal_id INT NOT NULL UNIQUE,
  servings INT NOT NULL,
  instructions TEXT NOT NULL,
  CONSTRAINT chk_recipe_servings_pos CHECK (servings > 0),
  CONSTRAINT fk_recipe_meal
    FOREIGN KEY (meal_id) REFERENCES meal(meal_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 7) recipe_ingredient  (junction table)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS recipe_ingredient (
  recipe_id INT NOT NULL,
  ingredient_id INT NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  unit VARCHAR(30) NOT NULL,
  PRIMARY KEY (recipe_id, ingredient_id),
  CONSTRAINT chk_recipe_amount_pos CHECK (amount > 0),
  CONSTRAINT fk_ri_recipe
    FOREIGN KEY (recipe_id) REFERENCES recipe(recipe_id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  CONSTRAINT fk_ri_ingredient
    FOREIGN KEY (ingredient_id) REFERENCES ingredient(ingredient_id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ------------------------------------------------------------
-- 8) prepared_meal (leftovers / ready-to-eat)
-- Gone bad logic = expiration_date < CURRENT_DATE (if date exists)
-- Default leftover expiration (+7 days) should be set in APP logic
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS prepared_meal (
  prepared_meal_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  location_id INT NOT NULL,
  servings_total INT NOT NULL,
  servings_remaining INT NOT NULL,
  prep_date DATE NOT NULL,
  expiration_date DATE NULL,
  note VARCHAR(255) NULL,
  CONSTRAINT chk_pm_servings_total_pos CHECK (servings_total > 0),
  CONSTRAINT chk_pm_servings_remaining_nonneg CHECK (servings_remaining >= 0),
  CONSTRAINT chk_pm_servings_remaining_le_total CHECK (servings_remaining <= servings_total),
  CONSTRAINT fk_prepared_location
    FOREIGN KEY (location_id) REFERENCES location(location_id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB;
