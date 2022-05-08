package nl.rabobank.kotlinmovement.recipes.testutil;

import nl.rabobank.kotlinmovement.recipes.domain.IngredientRequestJ;
import nl.rabobank.kotlinmovement.recipes.domain.IngredientTypeJ;
import nl.rabobank.kotlinmovement.recipes.domain.RecipeRequestJ;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TestData {

    public final static RecipeRequestJ pizzaRecipeJ =  new RecipeRequestJ("Pizza", getDefaultIngredientRequests());

    public static Set<IngredientRequestJ> getDefaultIngredientRequests() {
        return Set.of(
                new IngredientRequestJ("Flower", IngredientTypeJ.DRY, 1000),
                new IngredientRequestJ("Water", IngredientTypeJ.WET, 8000),
                new IngredientRequestJ("Salt", IngredientTypeJ.DRY, 20),
                new IngredientRequestJ("Yeast", IngredientTypeJ.DRY, 2)

        );
    }

    public static RecipeRequestJ peperoniPizzaRecipeJ() {
        final Set<IngredientRequestJ> ingredients = Set.of(
                new IngredientRequestJ("Flower", IngredientTypeJ.DRY, 1000),
                new IngredientRequestJ("Water", IngredientTypeJ.WET, 8000),
                new IngredientRequestJ("Salt", IngredientTypeJ.DRY, 20),
                new IngredientRequestJ("Yeast", IngredientTypeJ.DRY, 2),
                new IngredientRequestJ("Peperoni", IngredientTypeJ.DRY, 100),
                new IngredientRequestJ("Tomato sauce", IngredientTypeJ.WET, 100)

        );
        final String newRecipeName = "Pizza Peperoni";
        return new RecipeRequestJ(newRecipeName, ingredients);
    }
    public static List<RecipeRequestJ> generateRecipeRequest(int i) {
        final var recipeRequests = new ArrayList<RecipeRequestJ>(1);
        final String generatedString = generateRandomString();
        while (i > 0) {
            i--;
            recipeRequests.add(new RecipeRequestJ(generatedString, getDefaultIngredientRequests()));
        }
        return recipeRequests;
    }

    public static String generateRandomString() {
        final var random = new Random();
        return random.ints(97, 122 + 1)
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}
