package nl.rabobank.kotlinmovement.recipes.testutil.testdata


class RecipeResponse (
    val id: Long?  = null,
    val recipeName: String? = null,
    val ingredients: Set<IngredientResponse> = emptySet()
)
