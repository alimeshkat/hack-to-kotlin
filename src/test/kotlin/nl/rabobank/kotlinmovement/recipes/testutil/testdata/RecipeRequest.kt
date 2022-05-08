package nl.rabobank.kotlinmovement.recipes.testutil.testdata

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class RecipeRequest(
    val recipeName: @NotBlank String?,
    val ingredients: @NotEmpty @NotNull Set<IngredientRequest>?
)
