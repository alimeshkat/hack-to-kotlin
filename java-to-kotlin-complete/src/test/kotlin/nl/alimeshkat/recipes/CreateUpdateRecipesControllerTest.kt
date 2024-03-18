package nl.alimeshkat.recipes

import nl.alimeshkat.recipes.test.util.RecipeAssert.assertRecipeResponse
import nl.alimeshkat.recipes.test.util.RecipeTest
import nl.alimeshkat.recipes.test.util.RecipeTestData.emptyRequest
import nl.alimeshkat.recipes.test.util.RecipeTestData.emptyRequestIngredient
import nl.alimeshkat.recipes.test.util.RecipeTestData.errorMessageIncorrectIngredientName
import nl.alimeshkat.recipes.test.util.RecipeTestData.errorMessageIncorrectIngredientType
import nl.alimeshkat.recipes.test.util.RecipeTestData.errorMessageIncorrectIngredients
import nl.alimeshkat.recipes.test.util.RecipeTestData.errorMessageIncorrectRecipe
import nl.alimeshkat.recipes.test.util.RecipeTestData.errorMessageIncorrectRecipeName
import nl.alimeshkat.recipes.test.util.RecipeTestData.errorMessageIncorrectWeight
import nl.alimeshkat.recipes.test.util.RecipeTestData.ingredientMissingName
import nl.alimeshkat.recipes.test.util.RecipeTestData.ingredientMissingType
import nl.alimeshkat.recipes.test.util.RecipeTestData.ingredientMissingWeight
import nl.alimeshkat.recipes.test.util.RecipeTestData.nullIngredientsRequest
import nl.alimeshkat.recipes.test.util.RecipeTestData.nullRecipeNameRequest
import nl.alimeshkat.recipes.test.util.RecipeTestData.peperoniPizzaRecipeRequest
import nl.alimeshkat.recipes.test.util.model.RecipeRequestTest
import nl.alimeshkat.recipes.test.util.model.RecipesErrorResponseTest
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.http.HttpMethod
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CreateUpdateRecipesControllerTest : RecipeTest() {
    @Test
      fun `Should be able to update a recipe`()  {
        val updateRequest: RecipeRequestTest = peperoniPizzaRecipeRequest
        val response = updateRecipe(1L, updateRequest)
        assertRecipeResponse(updateRequest, response)
    }

    @Test
    fun `Should be to able create new recipe when recipe id doesn't not exist`() {
        val updateRequest: RecipeRequestTest = peperoniPizzaRecipeRequest
        val response = updateRecipe(2L, updateRequest)
        assertRecipeResponse(updateRequest, response)
    }

    @ParameterizedTest
    @MethodSource("errorDataParams")
    fun `Should not be able to create or update if request object is invalid`(
        recipeRequest: RecipeRequestTest?,
        errorResponse: RecipesErrorResponseTest?
    )  {
        val body = objectMapper.writeValueAsString(recipeRequest)
        val invalidCreateResponse = badRequestCall(HttpMethod.POST, "/recipes", body)
        val invalidUpdateResponse = badRequestCall(HttpMethod.PUT, "/recipes/1", body)
        AssertionsForClassTypes.assertThat(invalidCreateResponse).isEqualTo(errorResponse)
        AssertionsForClassTypes.assertThat(invalidUpdateResponse).isEqualTo(errorResponse)
    }

    @BeforeEach
      fun setup()  {
        setInitialState()
    }

    private fun errorDataParams(): Stream<Arguments> {
        return Stream.of(
            Arguments.of(emptyRequest, errorMessageIncorrectRecipe),
            Arguments.of(emptyRequestIngredient, errorMessageIncorrectIngredients),
            Arguments.of(nullRecipeNameRequest, errorMessageIncorrectRecipeName),
            Arguments.of(nullIngredientsRequest, errorMessageIncorrectIngredients),
            Arguments.of(ingredientMissingName, errorMessageIncorrectIngredientName),
            Arguments.of(ingredientMissingType, errorMessageIncorrectIngredientType),
            Arguments.of(ingredientMissingWeight, errorMessageIncorrectWeight)
        )
    }

}
