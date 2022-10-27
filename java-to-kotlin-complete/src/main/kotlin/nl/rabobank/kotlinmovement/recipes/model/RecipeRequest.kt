package nl.rabobank.kotlinmovement.recipes.model

import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

data class RecipeRequest(
    @field:NotBlank(message = "recipeName")
    val recipeName: String?,
    @field:NotEmpty(message = "ingredients") @field:Valid
    val ingredients: MutableSet<IngredientRequest>?
)
