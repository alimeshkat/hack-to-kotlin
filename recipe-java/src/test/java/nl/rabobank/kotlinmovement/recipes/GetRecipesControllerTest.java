package nl.rabobank.kotlinmovement.recipes;

import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTest;
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeRequestTest;
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeResponseTest;
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipesErrorResponseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import java.util.Arrays;

import static nl.rabobank.kotlinmovement.recipes.test.util.RecipeAssert.assertRecipeResponse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class GetRecipesControllerTest extends RecipeTest {
    private RecipeRequestTest initRecipeRequest;
    @Test
    @DisplayName("Should be able to get all recipes")
    void test1() throws Exception {
        Arrays.stream(getAllRecipes()).forEach((recipeResponse) -> assertRecipeResponse(initRecipeRequest, recipeResponse));
    }

    @Test
    @DisplayName("Should be able to get a recipe")
    void test2() throws Exception {
        final RecipeResponseTest response = getRecipe(1L);
        assertRecipeResponse(initRecipeRequest, response);
    }

    @Test
    @DisplayName("Should return not found if resource does not exist")
    void test4() throws Exception {
        final RecipesErrorResponseTest response = notFoundCall(HttpMethod.GET, "/recipes/2");
        assertThat(response).isEqualTo(new RecipesErrorResponseTest("Recipe 2 not found"));
    }

    @BeforeEach
    void setup() throws Exception {
        initRecipeRequest = setInitialState();
    }
}
