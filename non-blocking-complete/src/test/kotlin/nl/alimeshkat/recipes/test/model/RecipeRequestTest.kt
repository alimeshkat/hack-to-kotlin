package nl.alimeshkat.recipes.test.model

data class RecipeRequestTest(
    val recipeName: String? = null,
    val ingredients: Set<IngredientRequestTest>? = null
)
