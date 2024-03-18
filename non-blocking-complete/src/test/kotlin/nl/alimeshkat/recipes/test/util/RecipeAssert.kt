package nl.alimeshkat.recipes.test.util

import nl.alimeshkat.recipes.test.model.IngredientRequestTest
import nl.alimeshkat.recipes.test.model.IngredientResponseTest
import nl.alimeshkat.recipes.test.model.RecipeRequestTest
import nl.alimeshkat.recipes.test.model.RecipeResponseTest
import org.assertj.core.api.Assertions.assertThat
import org.opentest4j.AssertionFailedError

object RecipeAssert {

    fun assertRecipeResponse(recipeRequest: RecipeRequestTest, recipeResponse: RecipeResponseTest) {
        assertThat(recipeResponse.id).isNotNull
        assertThat(recipeResponse.recipeName).isEqualTo(recipeRequest.recipeName)
        assertIngredients(recipeRequest, recipeResponse)
    }
    fun assertRecipeResponses(actual: Array<RecipeResponseTest>, expected: Array<RecipeResponseTest>) {
        assertThat(actual).isEqualTo(expected)
    }


    private fun assertIngredients(updateRequest: RecipeRequestTest, updatedRecipeResponse: RecipeResponseTest) {
        matchIngredientAndAssert(
            updateRequest.ingredients,
            updatedRecipeResponse.ingredients
        ) { (key, value): Pair<IngredientRequestTest, IngredientResponseTest> ->
            assertThat(key.name).isEqualTo(value.name)
            assertThat(key.type).isEqualTo(value.type)
            assertThat(value.weight).isEqualTo(value.weight)
        }
    }

    private fun matchIngredientAndAssert(
        ingredientRequests: Set<IngredientRequestTest>?,
        ingredientResponse: Set<IngredientResponseTest>,
        asserts: (Pair<IngredientRequestTest, IngredientResponseTest>) -> Unit
    ) {
        ingredientRequests
            ?.map { iReq: IngredientRequestTest -> matchIngredients(ingredientResponse, iReq) }
            ?.forEach(asserts) ?: AssertionFailedError("ingredientRequests should not be null")
    }

    private fun matchIngredients(
        ingredientResponse: Set<IngredientResponseTest>,
        iReq: IngredientRequestTest
    ): Pair<IngredientRequestTest, IngredientResponseTest> {
        val ingredientResponseTest = ingredientResponse
            .firstOrNull { (_, name): IngredientResponseTest -> name == iReq.name }
            ?: throw AssertionFailedError("Expected " + iReq.name)
        return iReq to ingredientResponseTest
    }
}
