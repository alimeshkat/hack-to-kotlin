package nl.rabobank.kotlinmovement.recipes

import nl.rabobank.kotlinmovement.recipes.test.util.RecipeMockMvcTest
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipesErrorResponseTest
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

internal class DeleteRecipesControllerTest : RecipeMockMvcTest() {
    @Test
    @DisplayName("Should be able to delete a recipe ")
    @Throws(
        Exception::class
    )
    fun test1() {
        mockMvcPerformRequest(
            MockMvcRequestBuilders.delete("/recipes/{id}", 1L),
            MockMvcResultMatchers.status().isNoContent
        )
        val actualRecipeResponseLatest = allRecipes
        AssertionsForClassTypes.assertThat(actualRecipeResponseLatest).isEmpty()
    }

    @Test
    @DisplayName("Should return not found if resource does not exist")
    @Throws(
        Exception::class
    )
    fun test2() {
        val expected = RecipesErrorResponseTest("Recipe 2 not found")
        val actual = notFoundCall(MockMvcRequestBuilders.delete("/recipes/{id}", 2L))
        AssertionsForClassTypes.assertThat(actual).isEqualTo(expected)
    }

    @BeforeEach
    @Throws(Exception::class)
    fun setup() {
        setInitialState()
    }
}
