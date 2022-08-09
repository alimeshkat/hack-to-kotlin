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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Set;
import java.util.stream.Stream;

import static nl.rabobank.kotlinmovement.recipes.RecipeAssertUtil.assertRecipeResponse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CreateUpdateRecipesControllerTest extends MockMvcTest {

    @Test
    @DisplayName("Should be able to update a recipe")
    void test1() throws Exception {
        final RecipeResponseTest firstRecipe = mockMvcPerformRequest(get("/recipes"), RecipeResponseTest[].class, status().isOk())[0];
        final RecipeRequestTest updateRequest = peperoniPizzaRecipeRequest();
        final Long actualRecipeResponseId = firstRecipe.getId();
        final RecipeResponseTest updatedRecipeResponse = updateRecipe(actualRecipeResponseId, updateRequest);
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
        final RecipesErrorResponseTest invalidCreateRecipe = invalidRecipeCall(post("/recipes"), recipeRequest);
        final RecipesErrorResponseTest invalidUpdateRecipe = invalidRecipeCall(put("/recipes/{id}", 1), recipeRequest);
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

    private RecipesErrorResponseTest invalidRecipeCall(MockHttpServletRequestBuilder put, RecipeRequestTest recipeRequest) throws Exception {
        final MockHttpServletRequestBuilder requestBuilder =
                put.content(objectMapper.writeValueAsString(recipeRequest)).contentType(MediaType.APPLICATION_JSON);
        return mockMvcPerformRequest(requestBuilder, RecipesErrorResponseTest.class, status().is4xxClientError());
    }

    private RecipeResponseTest updateRecipe(Long id, RecipeRequestTest recipeRequest) throws Exception {
        final MockHttpServletRequestBuilder requestBuilder =
                put("/recipes/{id}", id)
                        .content(objectMapper.writeValueAsString(recipeRequest))
                        .contentType(MediaType.APPLICATION_JSON);
        return mockMvcPerformRequest(requestBuilder, RecipeResponseTest.class, status().isOk());
    }

    private RecipeRequestTest peperoniPizzaRecipeRequest() {
        final Set<IngredientRequestTest> ingredients = Set.of(
                new IngredientRequestTest("Flower", IngredientTypeTest.DRY, 1000),
                new IngredientRequestTest("Water", IngredientTypeTest.WET, 8000),
                new IngredientRequestTest("Salt", IngredientTypeTest.DRY, 20),
                new IngredientRequestTest("Yeast", IngredientTypeTest.DRY, 2),
                new IngredientRequestTest("Peperoni", IngredientTypeTest.DRY, 100),
                new IngredientRequestTest("Tomato sauce", IngredientTypeTest.WET, 100)

        );
        final String newRecipeName = "Pizza Peperoni";
        return new RecipeRequestTest(newRecipeName, ingredients);
    }

    private void setInitialState() throws Exception {
        final Set<IngredientRequestTest> ingredients = getDefaultIngredientRequests();
        RecipeRequestTest initRecipe = new RecipeRequestTest("Pizza", ingredients);
        createRecipe(initRecipe);
    }

    private Set<IngredientRequestTest> getDefaultIngredientRequests() {
        return Set.of(
                new IngredientRequestTest("Flower", IngredientTypeTest.DRY, 1000),
                new IngredientRequestTest("Water", IngredientTypeTest.WET, 8000),
                new IngredientRequestTest("Salt", IngredientTypeTest.DRY, 20),
                new IngredientRequestTest("Yeast", IngredientTypeTest.DRY, 2)
        );
    }

    private void createRecipe(RecipeRequestTest recipe) throws Exception {
        final MockHttpServletRequestBuilder requestBuilder =
                post("/recipes")
                        .content(objectMapper.writeValueAsString(recipe))
                        .contentType(MediaType.APPLICATION_JSON);
        mockMvcPerformRequest(requestBuilder, RecipeResponseTest.class, status().isCreated());
    }

}