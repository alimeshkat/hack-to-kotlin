package nl.rabobank.kotlinmovement.recipes.model

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class IngredientRequest(
    @field:NotBlank(message = "ingredient.name")
    val name: String?,
    @field:NotNull(message = "ingredient.type")
    val type: IngredientType?,
    @field:NotNull(message = "ingredient.weight")
    val weight: Int?
)
