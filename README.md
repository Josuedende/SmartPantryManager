# Smart Pantry Manager

An Android application written in Java that helps users reduce food waste by tracking the ingredients they have at home and suggesting recipes they can cook using **strictly** those ingredients — no shopping trip required.

## Overview

The Smart Pantry Manager lets a user:

- Track pantry ingredients (name, quantity, unit, optional expiry date)
- Add, edit and delete pantry items (full CRUD)
- Browse a collection of 18 recipes that are seeded into the database on first run
- View **Suggested Recipes** — a list that strictly matches every ingredient a recipe needs against what the user actually has
- View full recipe details (ingredients + method)
- Toggle preferences in a Settings screen
- Navigate between screens via a bottom navigation bar



## Database Choice

**SQLite via `SQLiteOpenHelper`.**

Chosen because:

- It stores data **locally on the device** — no internet connection required.
- It requires **no third-party account, API key, or external server**, unlike Firebase or PostgreSQL.
- It supports the **full CRUD** operations required by the brief (Create, Read, Update, Delete).
- Data **persists** across app restarts (the database file lives in the app's private storage).
- It is the persistence approach covered in the module's persistent data chapter.

All database logic is centralised in `DatabaseHelper.java`, which:

- Creates the `pantry`, `recipes`, and `recipe_ingredients` tables in `onCreate()`.
- Seeds 18 pre-loaded recipes the first time the app runs.
- Provides methods for pantry CRUD and recipe queries.

## Screens

| Screen | Activity | Purpose |
|---|---|---|
| Pantry List | `PantryListActivity` | Shows all ingredients in the pantry; entry point to add/edit/delete |
| Add / Edit Ingredient | `AddEditIngredientActivity` | Form with validation for creating or editing a pantry item |
| Suggested Recipes | `SuggestedRecipesActivity` | Runs the strict matcher and lists only recipes the user can cook right now |
| Recipe Detail | `RecipeDetailActivity` | Full ingredient list and preparation steps for a selected recipe |
| Settings | `SettingsActivity` | Toggle for expiring-soon alerts and metric units |

## Project Structure
app/src/main/
├── java/com/example/smartpantrymanager/
│ ├── MainActivity.java (redirects to Pantry List)
│ ├── PantryItem.java (pantry model)
│ ├── Recipe.java (recipe model)
│ ├── RecipeIngredient.java (recipe ingredient model)
│ ├── DatabaseHelper.java (SQLite schema, CRUD, seed data)
│ ├── RecipeMatcher.java (strict-matching logic)
│ ├── PantryListActivity.java (screen 1)
│ ├── AddEditIngredientActivity.java (screen 2)
│ ├── SuggestedRecipesActivity.java (screen 3)
│ ├── RecipeDetailActivity.java (screen 4)
│ ├── SettingsActivity.java (screen 5)
│ ├── PantryAdapter.java (RecyclerView adapter)
│ └── RecipeAdapter.java (RecyclerView adapter)
│
└── res/
├── layout/ (all screen and row layouts)
└── menu/bottom_nav_menu.xml (bottom navigation menu)


## Setup and Run Instructions

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Josuedende/SmartPantryManager.git










Author

402101331-BSC IT

Josue Dende — Mobile App Development ASSIGMENT 700, 2026
