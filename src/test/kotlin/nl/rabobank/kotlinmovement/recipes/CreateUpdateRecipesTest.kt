package nl.rabobank.kotlinmovement.recipes

import nl.rabobank.kotlinmovement.recipes.testutil.RecipeMockMvcTest
import nl.rabobank.kotlinmovement.recipes.testutil.testdata.IngredientResponse
import nl.rabobank.kotlinmovement.recipes.testutil.testdata.RecipeRequest
import nl.rabobank.kotlinmovement.recipes.testutil.testdata.peperoniPizzaRecipeRequest
import nl.rabobank.kotlinmovement.recipes.testutil.testdata.pizzaRecipe
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.stream.Stream


internal class CreateUpdateRecipesTest() : RecipeMockMvcTest(){

    @ParameterizedTest
    @ValueSource(longs = [1L,2L])
    fun `Should be able to update a recipe`(recipeId: Long) {
        val updateRequest = peperoniPizzaRecipeRequest()
        val updatedRecipeResponse = updateRecipe(recipeId, updateRequest)
        AssertionsForClassTypes.assertThat(updatedRecipeResponse.id).isEqualTo(recipeId)
        AssertionsForClassTypes.assertThat(updatedRecipeResponse.recipeName)
            .isEqualTo(updatedRecipeResponse.recipeName)
        doForEach(updatedRecipeResponse.ingredients) { ingredientResponse: IngredientResponse? ->
            val expected = findIngredientRequestByName(updateRequest, ingredientResponse)
            AssertionsForClassTypes.assertThat(ingredientResponse?.name).isEqualTo(expected?.name)
            AssertionsForClassTypes.assertThat(ingredientResponse?.type).isEqualTo(expected?.type)
            AssertionsForClassTypes.assertThat(ingredientResponse?.weight).isEqualTo(expected?.weight)
        }
    }

    @ParameterizedTest
    @MethodSource("errorDataParams")
    fun `Should not be able to create update if request object is invalid`(recipeRequest: String) {
        assertRecipeStatus(
            MockMvcRequestBuilders.post("/recipes")
                .content(recipeRequest)
                .contentType(MediaType.APPLICATION_JSON), MockMvcResultMatchers.status()
                .is4xxClientError
        )
        assertRecipeStatus(
            MockMvcRequestBuilders.put("/recipes/{id}", 1)
                .content(recipeRequest)
                .contentType(MediaType.APPLICATION_JSON), MockMvcResultMatchers.status()
                .is4xxClientError
        )
    }

    private fun errorDataParams(): Stream<Arguments> {
        val emptyRequest = objectMapper.writeValueAsString(RecipeRequest("", mutableSetOf()))
        val nullRecipeNameRequest = objectMapper.writeValueAsString(RecipeRequest(null, mutableSetOf()))
        val nullIngredientsRequest = objectMapper.writeValueAsString(RecipeRequest("test", null))
        val noContent = "{}"
        return Stream.of(
            Arguments.of(noContent),
            Arguments.of(emptyRequest),
            Arguments.of(nullRecipeNameRequest),
            Arguments.of(nullIngredientsRequest)
        )
    }

    @BeforeEach
    fun setup() {
        createRecipes(listOf(pizzaRecipe))
    }
}
