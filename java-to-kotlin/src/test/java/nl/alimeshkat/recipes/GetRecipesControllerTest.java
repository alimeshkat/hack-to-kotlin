package nl.alimeshkat.recipes;

import nl.alimeshkat.recipes.test.util.RecipeTest;
import nl.alimeshkat.recipes.test.model.RecipeResponseTest;
import nl.alimeshkat.recipes.test.model.RecipesErrorResponseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import static nl.alimeshkat.recipes.test.util.RecipeAssert.assertRecipeResponses;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GetRecipesControllerTest extends RecipeTest {
    private RecipeResponseTest[] initRecipes;
    @Test
    @DisplayName("Should be able to get all recipes")
    void test1() throws Exception {
       assertRecipeResponses(getAllRecipes() , initRecipes);
    }

    @Test
    @DisplayName("Should be able to get a recipe")
    void test2() throws Exception {
        final RecipeResponseTest response = getRecipe(1L);
        assertThat(initRecipes).contains(response);
    }

    @Test
    @DisplayName("Should return not found if resource does not exist")
    void test4() throws Exception {
        final RecipesErrorResponseTest response = notFoundCall(HttpMethod.GET, "/recipes/102");
        assertThat(response).isEqualTo(new RecipesErrorResponseTest("Recipe 102 not found"));
    }

    @BeforeEach
    void setup() throws Exception {
        initRecipes = setInitialState(1);
    }
}
