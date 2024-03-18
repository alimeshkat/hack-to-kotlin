package nl.alimeshkat.recipes.test.model

data class IngredientResponseTest(
    val id: Long,
    val name: String,
    val type: IngredientTypeTest,
    val weight: Int
)
