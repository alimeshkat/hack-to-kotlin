package nl.rabobank.kotlinmovement.recipes.service

import nl.rabobank.kotlinmovement.recipes.data.IngredientsEntity
import nl.rabobank.kotlinmovement.recipes.data.RecipesEntity
import nl.rabobank.kotlinmovement.recipes.model.IngredientRequest
import nl.rabobank.kotlinmovement.recipes.model.IngredientResponse
import nl.rabobank.kotlinmovement.recipes.model.IngredientType
import nl.rabobank.kotlinmovement.recipes.model.RecipeRequest
import nl.rabobank.kotlinmovement.recipes.model.RecipeResponse

object RecipesMapper {
    fun toRecipeEntity(recipeRequest: RecipeRequest, id: Long? = null): RecipesEntity {
        return RecipesEntity(id, checkNotNull(recipeRequest.recipeName))
    }

    fun toIngredientsEntity(recipeRequest: RecipeRequest, recipeId: Long?): List<IngredientsEntity> {
        return checkNotNull(recipeRequest.ingredients).map { (name, type, weight): IngredientRequest ->
            IngredientsEntity(
                null,
                checkNotNull(name),
                checkNotNull(type).name,
                checkNotNull(weight),
                checkNotNull(recipeId)
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
