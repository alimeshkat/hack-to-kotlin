package nl.rabobank.kotlinmovement.recipes

import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTest
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod

internal class DeleteRecipesControllerTest : RecipeTest() {
    @Test
    @Throws(Exception::class)
    fun `Should be able to delete a recipe `() {
        mockRequestNoContent(
            HttpMethod.DELETE, "/recipes/1",
        )
        val response = allRecipes()
        AssertionsForClassTypes.assertThat(response).isEmpty()
    }

    @BeforeEach
    @Throws(Exception::class)
    fun setup() {
        setInitialState()
    }


}
