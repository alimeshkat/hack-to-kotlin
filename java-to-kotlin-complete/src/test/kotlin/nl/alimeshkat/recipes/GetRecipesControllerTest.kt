package nl.alimeshkat.recipes

import nl.alimeshkat.recipes.test.util.RecipeAssert
import nl.alimeshkat.recipes.test.util.RecipeTest
import nl.alimeshkat.recipes.test.util.model.RecipeResponseTest
import nl.alimeshkat.recipes.test.util.model.RecipesErrorResponseTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod

internal class GetRecipesControllerTest : RecipeTest() {
    private lateinit var initRecipes: Array<RecipeResponseTest>

    @Test
    fun `Should be able to get all recipes`() {
        val actual = allRecipes()
        RecipeAssert.assertRecipeResponses(actual, initRecipes)
    }


    @Test
    fun `Should be able to get a recipe`() {
        val response = getRecipe(1L)
        assertThat(initRecipes).contains(response)
    }

    @Test
    fun `Should return not found if resource does not exist`() {
        val response = notFoundCall(HttpMethod.GET, "/recipes/2")
        AssertionsForClassTypes.assertThat(response).isEqualTo(RecipesErrorResponseTest("Recipe 2 not found"))
    }

    @BeforeEach
    fun setup() {
        initRecipes = setInitialState()
    }
}
