package nl.rabobank.kotlinmovement.recipes.test.util;

import nl.rabobank.kotlinmovement.recipes.test.util.model.IngredientRequestTest;
import nl.rabobank.kotlinmovement.recipes.test.util.model.IngredientTypeTest;
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeRequestTest;
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipesErrorResponseTest;

import java.util.Set;

public class RecipeTestData {
    public static final RecipeRequestTest emptyRequest = new RecipeRequestTest("", Set.of());
    public static final RecipeRequestTest emptyRequestIngredient = new RecipeRequestTest("pizza!", Set.of());
    public static final RecipeRequestTest nullRecipeNameRequest = new RecipeRequestTest(null, Set.of(new IngredientRequestTest("flower", IngredientTypeTest.DRY, 100)));
    public static final RecipeRequestTest nullIngredientsRequest = new RecipeRequestTest("test", null);
    public static final RecipeRequestTest ingredientMissingName = new RecipeRequestTest("test", Set.of(new IngredientRequestTest("", IngredientTypeTest.DRY, 100)));
    public static final RecipeRequestTest ingredientMissingType = new RecipeRequestTest("test", Set.of(new IngredientRequestTest("yeast", null, 100)));
    public static final RecipeRequestTest ingredientMissingWeight = new RecipeRequestTest("test", Set.of(new IngredientRequestTest("flower", IngredientTypeTest.DRY, null)));
    public static final RecipesErrorResponseTest errorMessageIncorrectRecipe = new RecipesErrorResponseTest("Incorrect fields:ingredients,recipeName.");
    public static final RecipesErrorResponseTest errorMessageIncorrectRecipeName = new RecipesErrorResponseTest("Incorrect fields:recipeName.");
    public static final RecipesErrorResponseTest errorMessageIncorrectIngredients = new RecipesErrorResponseTest("Incorrect fields:ingredients.");
    public static final RecipesErrorResponseTest errorMessageIncorrectIngredientName = new RecipesErrorResponseTest("Incorrect fields:ingredient.name.");
    public static final RecipesErrorResponseTest errorMessageIncorrectIngredientType = new RecipesErrorResponseTest("Incorrect fields:ingredient.type.");
    public static final RecipesErrorResponseTest errorMessageIncorrectWeight = new RecipesErrorResponseTest("Incorrect fields:ingredient.weight.");
    public static RecipeRequestTest peperoniPizzaRecipeRequest = new RecipeRequestTest("Pizza Peperoni", Set.of(
            new IngredientRequestTest("Flower", IngredientTypeTest.DRY, 1000),
            new IngredientRequestTest("Water", IngredientTypeTest.WET, 8000),
            new IngredientRequestTest("Salt", IngredientTypeTest.DRY, 20),
            new IngredientRequestTest("Yeast", IngredientTypeTest.DRY, 2),
            new IngredientRequestTest("Peperoni", IngredientTypeTest.DRY, 100),
            new IngredientRequestTest("Tomato sauce", IngredientTypeTest.WET, 100)
    ));
    public static Set<IngredientRequestTest> getDefaultIngredientRequests = Set.of(
            new IngredientRequestTest("Flower", IngredientTypeTest.DRY, 1000),
            new IngredientRequestTest("Water", IngredientTypeTest.WET, 8000),
            new IngredientRequestTest("Salt", IngredientTypeTest.DRY, 20),
            new IngredientRequestTest("Yeast", IngredientTypeTest.DRY, 2)
    );

}
