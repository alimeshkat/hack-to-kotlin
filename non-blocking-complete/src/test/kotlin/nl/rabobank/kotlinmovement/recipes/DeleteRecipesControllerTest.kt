package nl.rabobank.kotlinmovement.recipes

import kotlinx.coroutines.runBlocking
import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod

internal class DeleteRecipesControllerTest : RecipeTest() {
    @Test
    fun `Should be able to delete a recipe `() {
        mockRequestNoContent(
            HttpMethod.DELETE, "/recipes/1",
        )
        val response = allRecipes()
        assertThat(response).isEmpty()
    }

    @BeforeEach
    fun setup() = runBlocking<Unit>{
        setInitialState()
    }


}
