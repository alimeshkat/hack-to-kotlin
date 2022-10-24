package nl.rabobank.kotlinmovement.recipes.model

import lombok.Data

@Data
class IngredientResponse(
    val id: Long? = null,
    val name: String,
    val type: IngredientType,
    val weight: Int
)
