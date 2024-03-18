package nl.alimeshkat.recipes.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class IngredientRequest(
    @field:NotBlank(message = "ingredient.name")
    val name: String?,
    @field:NotNull(message = "ingredient.type")
    val type: IngredientType?,
    @field:NotNull(message = "ingredient.weight")
    val weight: Int?
)
