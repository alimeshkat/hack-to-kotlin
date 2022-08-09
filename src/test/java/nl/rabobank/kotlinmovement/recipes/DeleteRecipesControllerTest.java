package nl.rabobank.kotlinmovement.recipes;

import nl.rabobank.kotlinmovement.recipes.model.IngredientRequestTest;
import nl.rabobank.kotlinmovement.recipes.model.IngredientTypeTest;
import nl.rabobank.kotlinmovement.recipes.model.RecipeRequestTest;
import nl.rabobank.kotlinmovement.recipes.model.RecipeResponseTest;
import nl.rabobank.kotlinmovement.recipes.model.RecipesErrorResponseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DeleteRecipesControllerTest extends MockMvcTest {

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
        final var actual = mockMvcPerformRequest(delete("/recipes/{id}", 2L), RecipesErrorResponseTest.class, status().isNotFound());
        assertThat(actual).isEqualTo(expected);
    }

    @BeforeEach
    void setup() throws Exception {
        setInitialState();
    }


    private void setInitialState() throws Exception {
        final Set<IngredientRequestTest> ingredients = getDefaultIngredientRequests();
        RecipeRequestTest initRecipe = new RecipeRequestTest("Pizza", ingredients);
        createRecipes(initRecipe);
    }

    private Set<IngredientRequestTest> getDefaultIngredientRequests() {
        return Set.of(
                new IngredientRequestTest("Flower", IngredientTypeTest.DRY, 1000),
                new IngredientRequestTest("Water", IngredientTypeTest.WET, 8000),
                new IngredientRequestTest("Salt", IngredientTypeTest.DRY, 20),
                new IngredientRequestTest("Yeast", IngredientTypeTest.DRY, 2)
        );
    }

    private void createRecipes(RecipeRequestTest recipes) throws Exception {
        mockMvcPerformRequest(post("/recipes")
                        .content(objectMapper.writeValueAsString(recipes))
                        .contentType(MediaType.APPLICATION_JSON)
                , status().isCreated());
    }


}
