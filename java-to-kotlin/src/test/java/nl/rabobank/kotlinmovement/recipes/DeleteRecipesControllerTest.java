package nl.rabobank.kotlinmovement.recipes;

import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTest;
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeResponseTest;
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipesErrorResponseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DeleteRecipesControllerTest extends RecipeTest {

    @Test
    @DisplayName("Should be able to delete a recipe")
    void test1() throws Exception {
        simpleMockRequest(HttpMethod.DELETE, "/recipes/1", HttpStatus.NO_CONTENT);
        final RecipeResponseTest[] response = getAllRecipes();
        assertThat(response).isEmpty();
    }

    @Test
    @DisplayName("Should return not found if resource does not exist")
    void test2() throws Exception {
        final var expected = new RecipesErrorResponseTest("Recipe 2 not found");
        final var response = notFoundCall(HttpMethod.DELETE, "/recipes/2");
        assertThat(response).isEqualTo(expected);
    }

    @BeforeEach
    void setup() throws Exception {
        setInitialState();
    }

}
