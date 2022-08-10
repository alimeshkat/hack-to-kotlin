package nl.rabobank.kotlinmovement.recipes;

import nl.rabobank.kotlinmovement.recipes.test.RecipeMockMvcTest;
import nl.rabobank.kotlinmovement.recipes.test.model.RecipeRequestTest;
import nl.rabobank.kotlinmovement.recipes.test.model.RecipeResponseTest;
import nl.rabobank.kotlinmovement.recipes.test.model.RecipesErrorResponseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static nl.rabobank.kotlinmovement.recipes.test.RecipeAssert.assertRecipeResponse;
import static nl.rabobank.kotlinmovement.recipes.test.RecipeTestData.emptyRequest;
import static nl.rabobank.kotlinmovement.recipes.test.RecipeTestData.emptyRequestIngredient;
import static nl.rabobank.kotlinmovement.recipes.test.RecipeTestData.errorMessageIncorrectIngredientName;
import static nl.rabobank.kotlinmovement.recipes.test.RecipeTestData.errorMessageIncorrectIngredientType;
import static nl.rabobank.kotlinmovement.recipes.test.RecipeTestData.errorMessageIncorrectIngredients;
import static nl.rabobank.kotlinmovement.recipes.test.RecipeTestData.errorMessageIncorrectRecipe;
import static nl.rabobank.kotlinmovement.recipes.test.RecipeTestData.errorMessageIncorrectRecipeName;
import static nl.rabobank.kotlinmovement.recipes.test.RecipeTestData.errorMessageIncorrectWeight;
import static nl.rabobank.kotlinmovement.recipes.test.RecipeTestData.ingredientMissingName;
import static nl.rabobank.kotlinmovement.recipes.test.RecipeTestData.ingredientMissingType;
import static nl.rabobank.kotlinmovement.recipes.test.RecipeTestData.ingredientMissingWeight;
import static nl.rabobank.kotlinmovement.recipes.test.RecipeTestData.nullIngredientsRequest;
import static nl.rabobank.kotlinmovement.recipes.test.RecipeTestData.nullRecipeNameRequest;
import static nl.rabobank.kotlinmovement.recipes.test.RecipeTestData.peperoniPizzaRecipeRequest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

class CreateUpdateRecipesControllerTest extends RecipeMockMvcTest {

    @Test
    @DisplayName("Should be able to update a recipe")
    void test1() throws Exception {
        final RecipeRequestTest updateRequest = peperoniPizzaRecipeRequest();
        final RecipeResponseTest updatedRecipeResponse = updateRecipe(1L, updateRequest);
        assertRecipeResponse(updateRequest, updatedRecipeResponse);
    }

    @Test
    @DisplayName("Should be to able create new recipe when recipe id doesn't not exist")
    void test2() throws Exception {
        final RecipeRequestTest updateRequest = peperoniPizzaRecipeRequest();
        final RecipeResponseTest updatedRecipeResponse = updateRecipe(2L, updateRequest);
        assertRecipeResponse(updateRequest, updatedRecipeResponse);
    }

    @ParameterizedTest
    @MethodSource("errorDataParams")
    @DisplayName("Should not be able to create/update if request object is invalid")
    void test3(RecipeRequestTest recipeRequest, RecipesErrorResponseTest errorResponse) throws Exception {
        final String content = objectMapper.writeValueAsString(recipeRequest);
        final RecipesErrorResponseTest invalidCreateRecipe = badRequestCall(post("/recipes").content(content));
        final RecipesErrorResponseTest invalidUpdateRecipe = badRequestCall(put("/recipes/{id}", 1).content(content));
        assertThat(invalidCreateRecipe).isEqualTo(errorResponse);
        assertThat(invalidUpdateRecipe).isEqualTo(errorResponse);
    }

    private static Stream<Arguments> errorDataParams() {
        return Stream.of(
                Arguments.of(emptyRequest, errorMessageIncorrectRecipe),
                Arguments.of(emptyRequestIngredient, errorMessageIncorrectIngredients),
                Arguments.of(nullRecipeNameRequest, errorMessageIncorrectRecipeName),
                Arguments.of(nullIngredientsRequest, errorMessageIncorrectIngredients),
                Arguments.of(ingredientMissingName, errorMessageIncorrectIngredientName),
                Arguments.of(ingredientMissingType, errorMessageIncorrectIngredientType),
                Arguments.of(ingredientMissingWeight, errorMessageIncorrectWeight)
        );
    }

    @BeforeEach
    void setup() throws Exception {
        setInitialState();
    }

}
