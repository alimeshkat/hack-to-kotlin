package nl.rabobank.kotlinmovement.recipes;

import nl.rabobank.kotlinmovement.recipes.model.IngredientRequestTest;
import nl.rabobank.kotlinmovement.recipes.model.IngredientTypeTest;
import nl.rabobank.kotlinmovement.recipes.model.RecipeRequestTest;
import nl.rabobank.kotlinmovement.recipes.model.RecipeResponseTest;
import nl.rabobank.kotlinmovement.recipes.model.RecipesErrorResponseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static nl.rabobank.kotlinmovement.recipes.RecipeAssertUtil.assertRecipeResponse;
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
    @DisplayName("Should be able create when resource id doesn't not exist")
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
        final var emptyRequest = new RecipeRequestTest("", Set.of());
        final var emptyRequestIngredient = new RecipeRequestTest("pizza!", Set.of());
        final var nullRecipeNameRequest = new RecipeRequestTest(null, Set.of(new IngredientRequestTest("flower", IngredientTypeTest.DRY, 100)));
        final var nullIngredientsRequest = new RecipeRequestTest("test", null);
        final var ingredientMissingName = new RecipeRequestTest("test", Set.of(new IngredientRequestTest("", IngredientTypeTest.DRY, 100)));
        final var ingredientMissingType = new RecipeRequestTest("test", Set.of(new IngredientRequestTest("yeast", null, 100)));
        final var ingredientMissingWeight = new RecipeRequestTest("test", Set.of(new IngredientRequestTest("flower", IngredientTypeTest.DRY, null)));

        final var errorMessageIncorrectRecipe = new RecipesErrorResponseTest("Incorrect fields:ingredients,recipeName.");
        final var errorMessageIncorrectRecipeName = new RecipesErrorResponseTest("Incorrect fields:recipeName.");
        final var errorMessageIncorrectIngredients = new RecipesErrorResponseTest("Incorrect fields:ingredients.");
        final var errorMessageIncorrectIngredientName = new RecipesErrorResponseTest("Incorrect fields:ingredient.name.");
        final var errorMessageIncorrectIngredientType = new RecipesErrorResponseTest("Incorrect fields:ingredient.type.");
        final var errorMessageIncorrectWeight = new RecipesErrorResponseTest("Incorrect fields:ingredient.weight.");

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
