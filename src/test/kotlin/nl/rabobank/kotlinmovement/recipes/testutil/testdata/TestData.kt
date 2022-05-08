package nl.rabobank.kotlinmovement.recipes.testutil.testdata

import java.util.*

val defaultIngredientRequests: Set<IngredientRequest> = mutableSetOf(
    IngredientRequest("Flower", IngredientType.DRY, 1000),
    IngredientRequest("Water", IngredientType.WET, 8000),
    IngredientRequest("Salt", IngredientType.DRY, 20),
    IngredientRequest("Yeast", IngredientType.DRY, 2)
)

val pizzaRecipe: RecipeRequest = RecipeRequest("Pizza", defaultIngredientRequests)


fun peperoniPizzaRecipeRequest(): RecipeRequest = mutableSetOf(
    IngredientRequest("Flower", IngredientType.DRY, 1000),
    IngredientRequest("Water", IngredientType.WET, 8000),
    IngredientRequest("Salt", IngredientType.DRY, 20),
    IngredientRequest("Yeast", IngredientType.DRY, 2),
    IngredientRequest("Peperoni", IngredientType.DRY, 100),
    IngredientRequest("Tomato sauce", IngredientType.WET, 100)
).let {
    RecipeRequest("Pizza Peperoni", it)
}

fun generateRecipeRequests(i: Int): List<RecipeRequest> = (0..i).map {
    RecipeRequest(generateRandomString(), defaultIngredientRequests)
}

private fun generateRandomString(): String {
    val random = Random()
    return random.ints(97, 122 + 1)
        .limit(10)
        .collect(
            { StringBuilder() },
            { obj: StringBuilder, codePoint: Int -> obj.appendCodePoint(codePoint) }
        ) { obj: StringBuilder, s: StringBuilder? -> obj.append(s) }
        .toString()
}

