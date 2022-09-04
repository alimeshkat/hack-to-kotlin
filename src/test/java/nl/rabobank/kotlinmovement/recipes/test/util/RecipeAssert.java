package nl.rabobank.kotlinmovement.recipes.test.util;

import nl.rabobank.kotlinmovement.recipes.test.util.model.IngredientRequestTest;
import nl.rabobank.kotlinmovement.recipes.test.util.model.IngredientResponseTest;
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeRequestTest;
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeResponseTest;
import org.opentest4j.AssertionFailedError;

import java.util.AbstractMap;
import java.util.Set;
import java.util.function.Consumer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RecipeAssert {

    public static void assertRecipeResponse(RecipeRequestTest recipeRequest, RecipeResponseTest recipeResponse) {
        assertThat(recipeResponse.getId()).isNotNull();
        assertThat(recipeResponse.getRecipeName()).isEqualTo(recipeRequest.getRecipeName());
        assertIngredients(recipeRequest, recipeResponse);
    }

    private static void assertIngredients(RecipeRequestTest updateRequest, RecipeResponseTest updatedRecipeResponse) {
        matchIngredientAndAssert(updateRequest.getIngredients(), updatedRecipeResponse.getIngredients(), (AbstractMap.SimpleEntry<IngredientRequestTest, IngredientResponseTest> matchedIngredientEntry) -> {
            assertThat(matchedIngredientEntry.getKey().getName()).isEqualTo(matchedIngredientEntry.getValue().getName());
            assertThat(matchedIngredientEntry.getKey().getType()).isEqualTo(matchedIngredientEntry.getValue().getType());
            assertThat(matchedIngredientEntry.getValue().getWeight()).isEqualTo(matchedIngredientEntry.getValue().getWeight());
        });
    }
    private static void matchIngredientAndAssert(Set<IngredientRequestTest> recipeRequest,
                                                 Set<IngredientResponseTest> ingredientResponse,
                                                 Consumer<AbstractMap.SimpleEntry<IngredientRequestTest, IngredientResponseTest>> asserts) {
        recipeRequest
                .stream()
                .map(iReq -> matchIngredients(ingredientResponse, iReq))
                .forEach(asserts);
    }

    private static AbstractMap.SimpleEntry<IngredientRequestTest, IngredientResponseTest> matchIngredients(Set<IngredientResponseTest> ingredientResponse, IngredientRequestTest iReq) {
        final IngredientResponseTest ingredientResponseTest =
                ingredientResponse.stream()
                        .filter(iResp -> iResp.getName().equals(iReq.getName()))
                        .findFirst()
                        .orElseThrow(() -> new AssertionFailedError("Expected " + iReq.getName()));
        return new AbstractMap.SimpleEntry<>(iReq, ingredientResponseTest);
    }
}
