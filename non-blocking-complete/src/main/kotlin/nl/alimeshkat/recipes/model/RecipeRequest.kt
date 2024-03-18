package nl.alimeshkat.recipes.model

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class RecipeRequest(
    @field:NotBlank(message = "recipeName")
    val recipeName: String?,
    @field:NotEmpty(message = "ingredients") @field:Valid
    val ingredients: MutableSet<IngredientRequest>?
)
