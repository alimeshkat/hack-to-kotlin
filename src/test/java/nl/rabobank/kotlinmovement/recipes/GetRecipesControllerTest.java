package nl.rabobank.kotlinmovement.recipes;

import nl.rabobank.kotlinmovement.recipes.model.RecipeResponseTest;
import nl.rabobank.kotlinmovement.recipes.model.RecipesErrorResponseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static nl.rabobank.kotlinmovement.recipes.RecipeAssertUtil.assertRecipeResponse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GetRecipesControllerTest extends RecipeMockMvcTest {


    @Test
    @DisplayName("Should be able to get all recipes")
    void test1() throws Exception {
        Arrays.stream(getAllRecipes()).forEach((recipeResponse) -> assertRecipeResponse(initRecipeRequest, recipeResponse));
    }

    @Test
    @DisplayName("Should be able to get a recipe")
    void test2() throws Exception {
        final RecipeResponseTest recipeResponse = mockMvcPerformRequest(get("/recipes/{id}", 1L), RecipeResponseTest.class, status().isOk());
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
         setInitialState();
    }
}
