package nl.rabobank.kotlinmovement.recipes.test.util.model

data class IngredientResponseTest(
    val id: Long,
    val name: String,
    val type: IngredientTypeTest,
    val weight: Int
)
