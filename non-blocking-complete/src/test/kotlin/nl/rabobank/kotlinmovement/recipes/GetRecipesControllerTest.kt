package nl.rabobank.kotlinmovement.recipes

import nl.rabobank.kotlinmovement.recipes.test.util.RecipeAssert.assertRecipeResponse
import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTest
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeRequestTest
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeResponseTest
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipesErrorResponseTest
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod

internal class GetRecipesControllerTest : RecipeTest() {
    private lateinit var initRecipeRequest: RecipeRequestTest

    @Test
    fun `Should be able to get all recipes`() {
        allRecipes().forEach { recipeResponse: RecipeResponseTest ->
            assertRecipeResponse(
                initRecipeRequest, recipeResponse
            )
        }
    }

    @Test
    fun `Should be able to get a recipe`() {
        val recipeResponse = getRecipe(1L)
        assertRecipeResponse(initRecipeRequest, recipeResponse)
    }

    @Test
    fun `Should return not found if resource does not exist`()  {
        val actual = notFoundCall(HttpMethod.GET, "/recipes/2")
        AssertionsForClassTypes.assertThat(actual).isEqualTo(RecipesErrorResponseTest("Recipe 2 not found"))
    }

    @BeforeEach
    fun setup(){
        initRecipeRequest = setInitialState()
    }
}
