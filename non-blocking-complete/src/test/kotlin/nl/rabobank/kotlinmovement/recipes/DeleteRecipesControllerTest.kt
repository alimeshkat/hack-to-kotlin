package nl.rabobank.kotlinmovement.recipes

import kotlinx.coroutines.runBlocking
import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

internal class DeleteRecipesControllerTest : RecipeTest() {
    @Test
    fun `Should be able to delete a recipe `() {
        voidMockRequest(
            HttpMethod.DELETE, "/recipes/1", HttpStatus.NO_CONTENT
        )
        val response = allRecipes()
        assertThat(response).isEmpty()
    }

    @BeforeEach
    fun setup() = runBlocking<Unit>{
        setInitialState()
    }


}
