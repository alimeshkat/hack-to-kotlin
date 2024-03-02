package nl.rabobank.kotlinmovement.recipes.service

import nl.rabobank.kotlinmovement.recipes.data.IngredientsEntity
import nl.rabobank.kotlinmovement.recipes.data.RecipesEntity
import nl.rabobank.kotlinmovement.recipes.model.*

object RecipesMapper {
    fun toRecipeEntity(recipeRequest: RecipeRequest, id: Long? = null): RecipesEntity {
        return RecipesEntity(id, checkNotNull(recipeRequest.recipeName))
    }

    fun toIngredientsEntity(recipeRequest: RecipeRequest, recipeId: Long?): List<IngredientsEntity> {
        return checkNotNull(recipeRequest.ingredients).map { (name, type, weight): IngredientRequest ->
            IngredientsEntity(
                recipeId = checkNotNull(recipeId),
                ingredientId = null,
                name =checkNotNull(name),
                weight = checkNotNull(weight),
                type = checkNotNull(type).name
            )
        }
    }

    fun toRecipeResponse(
        recipes: RecipesEntity,
    ): RecipeResponse =
        RecipeResponse(
            checkNotNull(recipes.recipeId),
            recipes.recipeName,
            recipes.ingredients.map { ingredient ->
                IngredientResponse(
                    checkNotNull(ingredient.ingredientId),
                    ingredient.name,
                    IngredientType.valueOf(ingredient.type),
                    ingredient.weight
                )
            }
        )

}
