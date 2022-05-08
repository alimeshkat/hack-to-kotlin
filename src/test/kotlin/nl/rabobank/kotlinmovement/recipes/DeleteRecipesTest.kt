package nl.rabobank.kotlinmovement.recipes

import nl.rabobank.kotlinmovement.recipes.testutil.RecipeMockMvcTest
import nl.rabobank.kotlinmovement.recipes.testutil.testdata.pizzaRecipe
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

internal class DeleteRecipesTest: RecipeMockMvcTest() {

    @Test
    fun `Should be able to delete a recipe`() {
        deleteRecipe(1)
        AssertionsForClassTypes.assertThat(allRecipePaginatedResponses).isEmpty()
    }

    @Test
    fun `Should return not found if resource does not exist`() {
        assertRecipeStatus(
            MockMvcRequestBuilders.delete("/recipes/{id}", 2L),
            MockMvcResultMatchers.status().isNotFound
        )
    }

    @BeforeEach
    fun setup() {
        createRecipes(listOf(pizzaRecipe))
    }
}
