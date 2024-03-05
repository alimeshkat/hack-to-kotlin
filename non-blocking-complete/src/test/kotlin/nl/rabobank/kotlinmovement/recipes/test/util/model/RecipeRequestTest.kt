package nl.rabobank.kotlinmovement.recipes.test.util.model

data class RecipeRequestTest(
    val recipeName: String? = null,
    val ingredients: Set<IngredientRequestTest>? = null
)
