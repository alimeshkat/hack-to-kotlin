package nl.rabobank.kotlinmovement.recipes

import nl.rabobank.kotlinmovement.recipes.test.util.RecipeAssert.assertRecipeResponse
import nl.rabobank.kotlinmovement.recipes.test.util.RecipeMockMvcTest
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeRequestTest
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeResponseTest
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipesErrorResponseTest
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.util.*

internal class GetRecipesControllerTest : RecipeMockMvcTest() {
    private var initRecipeRequest: RecipeRequestTest? = null

    @Test
    @DisplayName("Should be able to get all recipes")
    @Throws(
        Exception::class
    )
    fun test1() {
        Arrays.stream(allRecipes).forEach { recipeResponse: RecipeResponseTest? ->
            assertRecipeResponse(
                initRecipeRequest!!, recipeResponse!!
            )
        }
    }

    @Test
    @DisplayName("Should be able to get a recipe")
    @Throws(Exception::class)
    fun test2() {
        val recipeResponse = getRecipe(1L)
        assertRecipeResponse(initRecipeRequest!!, recipeResponse)
    }

    @Test
    @DisplayName("Should return not found if resource does not exist")
    @Throws(
        Exception::class
    )
    fun test4() {
        val actual = notFoundCall(MockMvcRequestBuilders.get("/recipes/{id}", 2L))
        AssertionsForClassTypes.assertThat(actual).isEqualTo(RecipesErrorResponseTest("Recipe 2 not found"))
    }

    @BeforeEach
    @Throws(Exception::class)
    fun setup() {
        initRecipeRequest = setInitialState()
    }
}
