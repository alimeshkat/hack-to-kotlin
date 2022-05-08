
package nl.rabobank.kotlinmovement.recipes;

import nl.rabobank.kotlinmovement.recipes.testutil.RecipeMockMvcJTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nl.rabobank.kotlinmovement.recipes.testutil.TestData.pizzaRecipeJ;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class DeleteRecipesControllerJTest extends RecipeMockMvcJTest {

    @Test
    @DisplayName("Should be able to delete a recipe ")
    void test1() throws Exception {
        deleteRecipe(getRecipes()[0].getId());
        assertThat(getRecipes()).isEmpty();
    }

    @Test
    @DisplayName("Should return not found if resource does not exist")
    void test2() throws Exception {
        assertCallStatus(delete("/recipes/{id}", 2L), status().isNotFound());
    }

    @BeforeEach
    void setup() {
        createRecipes(List.of(pizzaRecipeJ));
    }

}
