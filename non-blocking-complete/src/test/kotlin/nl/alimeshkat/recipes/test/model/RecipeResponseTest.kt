package nl.alimeshkat.recipes.test.model

data class RecipeResponseTest(
    val id: Long,
    val recipeName: String,
    val ingredients: Set<IngredientResponseTest>
)
