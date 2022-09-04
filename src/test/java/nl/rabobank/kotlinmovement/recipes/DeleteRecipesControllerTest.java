package nl.rabobank.kotlinmovement.recipes;

import nl.rabobank.kotlinmovement.recipes.test.util.RecipeMockMvcTest;
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeResponseTest;
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipesErrorResponseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DeleteRecipesControllerTest extends RecipeMockMvcTest {

    @Test
    @DisplayName("Should be able to delete a recipe ")
    void test1() throws Exception {
        mockMvcPerformRequest(delete("/recipes/{id}", 1L), status().isNoContent());
        final RecipeResponseTest[] actualRecipeResponseLatest = getAllRecipes();
        assertThat(actualRecipeResponseLatest).isEmpty();
    }

    @Test
    @DisplayName("Should return not found if resource does not exist")
    void test2() throws Exception {
        final var expected = new RecipesErrorResponseTest("Recipe 2 not found");
        final var actual = notFoundCall(delete("/recipes/{id}", 2L));
        assertThat(actual).isEqualTo(expected);
    }

    @BeforeEach
    void setup() throws Exception {
        setInitialState();
    }

}
