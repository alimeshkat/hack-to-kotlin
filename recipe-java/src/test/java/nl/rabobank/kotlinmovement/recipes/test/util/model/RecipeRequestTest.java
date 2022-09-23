package nl.rabobank.kotlinmovement.recipes.test.util.model;

import java.util.Collections;
import java.util.Set;

public class RecipeRequestTest {
    private final String recipeName;
    private final Set<IngredientRequestTest> ingredients;

    public RecipeRequestTest() {
        this(null, Collections.emptySet());
    }

    public RecipeRequestTest(String recipeName, Set<IngredientRequestTest> ingredientRequests) {
        this.recipeName = recipeName;
        this.ingredients = ingredientRequests;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public Set<IngredientRequestTest> getIngredients() {
        return ingredients;
    }
}
