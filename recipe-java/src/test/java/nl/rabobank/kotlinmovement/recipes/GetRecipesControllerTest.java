package nl.rabobank.kotlinmovement.recipes;

import nl.rabobank.kotlinmovement.recipes.test.util.RecipeMockMvcTest;
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeRequestTest;
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeResponseTest;
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipesErrorResponseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nl.rabobank.kotlinmovement.recipes.test.util.RecipeAssert.assertRecipeResponse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class GetRecipesControllerTest extends RecipeMockMvcTest {
    private RecipeRequestTest initRecipeRequest;
    @Test
    @DisplayName("Should be able to get all recipes")
    void test1() throws Exception {
        Arrays.stream(getAllRecipes()).forEach((recipeResponse) -> assertRecipeResponse(initRecipeRequest, recipeResponse));
    }

    @Test
    @DisplayName("Should be able to get a recipe")
    void test2() throws Exception {
        final RecipeResponseTest recipeResponse = getRecipe(1L);
        assertRecipeResponse(initRecipeRequest, recipeResponse);
    }

    @Test
    @DisplayName("Should return not found if resource does not exist")
    void test4() throws Exception {
        final RecipesErrorResponseTest actual = notFoundCall(get("/recipes/{id}", 2L));
        assertThat(actual).isEqualTo(new RecipesErrorResponseTest("Recipe 2 not found"));
    }

    @BeforeEach
    void setup() throws Exception {
        initRecipeRequest = setInitialState();
    }
}
