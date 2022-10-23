package nl.rabobank.kotlinmovement.recipes.test.util.model

data class RecipeResponseTest(
    val id: Long,
    val recipeName: String,
    val ingredients: Set<IngredientResponseTest>
)
