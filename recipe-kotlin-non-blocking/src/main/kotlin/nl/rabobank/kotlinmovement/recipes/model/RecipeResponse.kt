package nl.rabobank.kotlinmovement.recipes.model

data class RecipeResponse (
    val id: Long,
    val recipeName: String,
    val ingredients: Set<IngredientResponse>
)
