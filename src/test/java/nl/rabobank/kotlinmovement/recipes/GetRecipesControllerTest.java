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

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static nl.rabobank.kotlinmovement.recipes.RecipeAssertUtil.assertRecipeResponse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GetRecipesControllerTest extends MockMvcTest {
    private RecipeRequestTest initRecipeRequest;

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
        final RecipesErrorResponseTest actual = mockMvcPerformRequest(get("/recipes/{id}", 2L), RecipesErrorResponseTest.class, status().isNotFound());
        assertThat(actual).isEqualTo(new RecipesErrorResponseTest("Recipe 2 not found"));
    }

    @BeforeEach
    void setup() {
        setInitialState();
    }

    private void setInitialState() {
        final Set<IngredientRequestTest> ingredients = getDefaultIngredientRequests();
        initRecipeRequest = new RecipeRequestTest("Pizza", ingredients);
        createRecipes(List.of(initRecipeRequest));
    }

    private Set<IngredientRequestTest> getDefaultIngredientRequests() {
        return Set.of(
                new IngredientRequestTest("Flower", IngredientTypeTest.DRY, 1000),
                new IngredientRequestTest("Water", IngredientTypeTest.WET, 8000),
                new IngredientRequestTest("Salt", IngredientTypeTest.DRY, 20),
                new IngredientRequestTest("Yeast", IngredientTypeTest.DRY, 2)
        );
    }

    private void createRecipes(List<RecipeRequestTest> recipes) {
        recipes.forEach(it ->
                {
                    try {
                        mockMvcPerformRequest(
                                post("/recipes")
                                        .content(objectMapper.writeValueAsString(it))
                                        .contentType(MediaType.APPLICATION_JSON)
                                , status().isCreated());
                    } catch (Exception e) {
                        fail(e.getMessage());
                    }
                }
        );

    }

}
