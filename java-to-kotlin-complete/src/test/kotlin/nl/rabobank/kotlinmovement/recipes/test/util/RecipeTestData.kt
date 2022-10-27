package nl.rabobank.kotlinmovement.recipes.test.util

import nl.rabobank.kotlinmovement.recipes.test.util.model.IngredientRequestTest
import nl.rabobank.kotlinmovement.recipes.test.util.model.IngredientTypeTest
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeRequestTest
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipesErrorResponseTest

object RecipeTestData {

    val emptyRequest = RecipeRequestTest("", mutableSetOf())

    val emptyRequestIngredient = RecipeRequestTest("pizza!", mutableSetOf())

    val nullRecipeNameRequest =
        RecipeRequestTest(ingredients = mutableSetOf(IngredientRequestTest("flower", IngredientTypeTest.DRY, 100)))

    val nullIngredientsRequest = RecipeRequestTest("test")

    val ingredientMissingName =
        RecipeRequestTest("test", mutableSetOf(IngredientRequestTest("", IngredientTypeTest.DRY, 100)))

    val ingredientMissingType =
        RecipeRequestTest("test", mutableSetOf(IngredientRequestTest(name = "yeast", weight = 100)))

    val ingredientMissingWeight =
        RecipeRequestTest("test", mutableSetOf(IngredientRequestTest("flower", IngredientTypeTest.DRY)))

    val errorMessageIncorrectRecipe = RecipesErrorResponseTest("Incorrect fields:ingredients,recipeName.")

    val errorMessageIncorrectRecipeName = RecipesErrorResponseTest("Incorrect fields:recipeName.")

    val errorMessageIncorrectIngredients = RecipesErrorResponseTest("Incorrect fields:ingredients.")

    val errorMessageIncorrectIngredientName = RecipesErrorResponseTest("Incorrect fields:ingredient.name.")

    val errorMessageIncorrectIngredientType = RecipesErrorResponseTest("Incorrect fields:ingredient.type.")

    val errorMessageIncorrectWeight = RecipesErrorResponseTest("Incorrect fields:ingredient.weight.")

    val peperoniPizzaRecipeRequest = RecipeRequestTest(
        "Pizza Peperoni", mutableSetOf(
            IngredientRequestTest("Flower", IngredientTypeTest.DRY, 1000),
            IngredientRequestTest("Water", IngredientTypeTest.WET, 8000),
            IngredientRequestTest("Salt", IngredientTypeTest.DRY, 20),
            IngredientRequestTest("Yeast", IngredientTypeTest.DRY, 2),
            IngredientRequestTest("Peperoni", IngredientTypeTest.DRY, 100),
            IngredientRequestTest("Tomato sauce", IngredientTypeTest.WET, 100)
        )
    )
    val getDefaultIngredientRequests = mutableSetOf(
        IngredientRequestTest("Flower", IngredientTypeTest.DRY, 1000),
        IngredientRequestTest("Water", IngredientTypeTest.WET, 8000),
        IngredientRequestTest("Salt", IngredientTypeTest.DRY, 20),
        IngredientRequestTest("Yeast", IngredientTypeTest.DRY, 2)
    )
}
