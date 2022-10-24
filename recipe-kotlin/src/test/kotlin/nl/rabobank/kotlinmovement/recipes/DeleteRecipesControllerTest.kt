package nl.rabobank.kotlinmovement.recipes

import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTest
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipesErrorResponseTest
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

internal class DeleteRecipesControllerTest : RecipeTest() {
    @Test
    @Throws(Exception::class)
    fun `Should be able to delete a recipe `() {
        assertSimpleMockRequest(
            HttpMethod.DELETE,"/recipes/1",
            HttpStatus.NO_CONTENT
        )
        val response = allRecipes
        AssertionsForClassTypes.assertThat(response).isEmpty()
    }

    @Test
    @Throws(Exception::class)
    fun `Should return not found if resource does not exist`() {
        val expected = RecipesErrorResponseTest("Recipe 2 not found")
        val response = notFoundCall(HttpMethod.DELETE,"/recipes/2")
        AssertionsForClassTypes.assertThat(response).isEqualTo(expected)
    }

    @BeforeEach
    @Throws(Exception::class)
    fun setup() {
        setInitialState()
    }
}
