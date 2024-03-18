package nl.alimeshkat.recipes.model

data class RecipeResponse (
    val id: Long,
    val recipeName: String,
    val ingredients: List<IngredientResponse>
)
