package nl.rabobank.kotlinmovement.recipes.testutil.testdata

import javax.validation.constraints.NotBlank

class IngredientRequest (
    val name: @NotBlank String? = null,
    val type: @NotBlank IngredientType? = null,
    val weight: Int = 0
)
