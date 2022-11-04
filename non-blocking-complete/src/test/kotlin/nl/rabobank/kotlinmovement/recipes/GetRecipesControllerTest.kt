package nl.rabobank.kotlinmovement.recipes

import kotlinx.coroutines.runBlocking
import nl.rabobank.kotlinmovement.recipes.test.util.RecipeAssert.assertRecipeResponse
import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTest
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeRequestTest
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeResponseTest
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipesErrorResponseTest
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod

internal class GetRecipesControllerTest : RecipeTest() {
    private lateinit var initRecipeRequest: RecipeRequestTest

    @Test
    @DisplayName("Should be able to get all recipes")
    @Throws(
        Exception::class
    )
    fun test1() = runBlocking<Unit> {
        allRecipes().forEach { recipeResponse: RecipeResponseTest ->
            assertRecipeResponse(
                initRecipeRequest, recipeResponse
            )
        }
    }

    @Test
    @DisplayName("Should be able to get a recipe")
    @Throws(Exception::class)
    fun test2() = runBlocking {
        val recipeResponse = getRecipe(1L)
        assertRecipeResponse(initRecipeRequest, recipeResponse)
    }

    @Test
    @DisplayName("Should return not found if resource does not exist")
    @Throws(
        Exception::class
    )
    fun test4() = runBlocking<Unit> {
        val actual = notFoundCall(HttpMethod.GET, "/recipes/2")
        AssertionsForClassTypes.assertThat(actual).isEqualTo(RecipesErrorResponseTest("Recipe 2 not found"))
    }

    @BeforeEach
    @Throws(Exception::class)
    fun setup() = runBlocking {
        initRecipeRequest = setInitialState()
    }
}
