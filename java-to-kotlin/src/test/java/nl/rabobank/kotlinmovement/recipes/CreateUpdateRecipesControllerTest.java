package nl.rabobank.kotlinmovement.recipes;

import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTest;
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeRequestTest;
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeResponseTest;
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipesErrorResponseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpMethod;

import java.util.stream.Stream;

import static nl.rabobank.kotlinmovement.recipes.test.util.RecipeAssert.assertRecipeResponse;
import static nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.emptyRequest;
import static nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.emptyRequestIngredient;
import static nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.errorMessageIncorrectIngredientName;
import static nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.errorMessageIncorrectIngredientType;
import static nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.errorMessageIncorrectIngredients;
import static nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.errorMessageIncorrectRecipe;
import static nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.errorMessageIncorrectRecipeName;
import static nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.errorMessageIncorrectWeight;
import static nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.ingredientMissingName;
import static nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.ingredientMissingType;
import static nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.ingredientMissingWeight;
import static nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.nullIngredientsRequest;
import static nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.nullRecipeNameRequest;
import static nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.peperoniPizzaRecipeRequest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CreateUpdateRecipesControllerTest extends RecipeTest {

    @Test
    @DisplayName("Should be able to update a recipe")
    void test1() throws Exception {
        final RecipeRequestTest updateRequest = peperoniPizzaRecipeRequest;
        final RecipeResponseTest response = updateRecipe(1L, updateRequest);
        assertRecipeResponse(updateRequest, response);
    }

    @Test
    @DisplayName("Should be to able create new recipe when recipe id doesn't not exist")
    void test2() throws Exception {
        final RecipeRequestTest updateRequest = peperoniPizzaRecipeRequest;
        final RecipeResponseTest response = updateRecipe(2L, updateRequest);
        assertRecipeResponse(updateRequest, response);
    }

    @ParameterizedTest(name = "[{index}]: {2}")
    @MethodSource("errorDataParams")
    @DisplayName("Should not be able to create or update if request object is invalid")
    void test3(RecipeRequestTest recipeRequest, RecipesErrorResponseTest errorResponse, String testName) throws Exception {
        final String response = objectMapper.writeValueAsString(recipeRequest);
        final RecipesErrorResponseTest invalidCreateResponse = badRequestCall(HttpMethod.POST, "/recipes", response);
        final RecipesErrorResponseTest invalidUpdateResponse = badRequestCall(HttpMethod.PUT,"/recipes/1", response);
        assertThat(invalidCreateResponse).isEqualTo(errorResponse);
        assertThat(invalidUpdateResponse).isEqualTo(errorResponse);
    }

    private static Stream<Arguments> errorDataParams() {
        return Stream.of(
                Arguments.of(emptyRequest, errorMessageIncorrectRecipe, "create/update with empty RecipeRequest" ),
                Arguments.of(emptyRequestIngredient, errorMessageIncorrectIngredients, "create/update with empty RecipeRequest.ingredients"),
                Arguments.of(nullRecipeNameRequest, errorMessageIncorrectRecipeName, "create/update with missing RecipeRequest.recipeName field"),
                Arguments.of(nullIngredientsRequest, errorMessageIncorrectIngredients, "create/update with null for RecipeRequest.ingredients"),
                Arguments.of(ingredientMissingName, errorMessageIncorrectIngredientName, "create/update with missing IngredientRequest.name field"),
                Arguments.of(ingredientMissingType, errorMessageIncorrectIngredientType, "create/update with missing IngredientRequest.type field"),
                Arguments.of(ingredientMissingWeight, errorMessageIncorrectWeight, "create/update with missing IngredientRequest.weight field")
        );
    }

    @BeforeEach
    void setup() throws Exception {
        setInitialState(10);
    }

}
