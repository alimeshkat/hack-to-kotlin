package nl.rabobank.kotlinmovement.recipes

import nl.rabobank.kotlinmovement.recipes.testutil.RecipeMockMvcTest
import nl.rabobank.kotlinmovement.recipes.testutil.testdata.generateRecipeRequests
import nl.rabobank.kotlinmovement.recipes.testutil.testdata.pizzaRecipe
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.stream.Stream

internal class GetRecipesTest : RecipeMockMvcTest() {

    @Test
    fun `Should be able to get all recipes`() {
        allRecipePaginatedResponses[0].also {
            AssertionsForClassTypes.assertThat(it.id).isNotNull
            AssertionsForClassTypes.assertThat(it.recipeName).isEqualTo(pizzaRecipe.recipeName)
            doForEach(it.ingredients) { ingredientResponse ->
                val expected = findIngredientRequestByName(pizzaRecipe, ingredientResponse)
                AssertionsForClassTypes.assertThat(ingredientResponse?.name).isEqualTo(expected?.name)
                AssertionsForClassTypes.assertThat(ingredientResponse?.type).isEqualTo(expected?.type)
                AssertionsForClassTypes.assertThat(ingredientResponse?.weight).isEqualTo(expected?.weight)
            }
            AssertionsForClassTypes.assertThat(it.recipeName).isEqualTo(pizzaRecipe.recipeName)
        }
    }

    @Test
    fun `Should be able to get a recipe`() {
        getRecipeResponses(1).also {
            AssertionsForClassTypes.assertThat(it.id).isNotNull
            AssertionsForClassTypes.assertThat(it.recipeName).isEqualTo(pizzaRecipe.recipeName)
            doForEach(it.ingredients) { ingredientResponse ->
                val expected = findIngredientRequestByName(pizzaRecipe, ingredientResponse)
                AssertionsForClassTypes.assertThat(ingredientResponse?.name).isEqualTo(expected?.name)
                AssertionsForClassTypes.assertThat(ingredientResponse?.type).isEqualTo(expected?.type)
                AssertionsForClassTypes.assertThat(ingredientResponse?.weight).isEqualTo(expected?.weight)
            }
            AssertionsForClassTypes.assertThat(it.recipeName).isEqualTo(pizzaRecipe.recipeName)

        }
    }

    @Test
    fun `Should return not found if resource does not exist`() {
        assertRecipeStatus(MockMvcRequestBuilders.get("/recipes/{id}", 2L), MockMvcResultMatchers.status().isNotFound)
    }

    @ParameterizedTest
    @MethodSource("sortedPagesRecipeDataProvider")
    fun `Should be able to get sorted paged recipes`(
        page: Int,
        pageSize: Int?,
        expectedPageSize: Int,
        sortPropertyValue: String?,
        sortDirectionDesc: String?
    ) {
        createRecipes(generateRecipeRequests(40))
        getAllRecipePaginatedResponses(page, pageSize, sortPropertyValue, sortDirectionDesc).also {
            AssertionsForClassTypes.assertThat(it).hasSize(expectedPageSize)
        }
    }

    private fun sortedPagesRecipeDataProvider(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(0, 10, 10, "recipeName", "desc"),
            Arguments.of(1, 10, 10, "recipeName", "desc"),
            Arguments.of(2, 10, 10, "recipeName", "asc"),
            Arguments.of(3, 10, 10, "recipeName", "desc"),
            Arguments.of(3, 10, 10, "recipeName", null),
            Arguments.of(0, null, 20, null, null)
        )
    }

    @BeforeEach
    fun setup() {
        createRecipes(listOf(pizzaRecipe))
    }

}
