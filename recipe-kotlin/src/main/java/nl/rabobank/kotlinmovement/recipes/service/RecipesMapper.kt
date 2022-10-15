package nl.rabobank.kotlinmovement.recipes.service

import nl.rabobank.kotlinmovement.recipes.data.IngredientsEntity
import nl.rabobank.kotlinmovement.recipes.data.RecipesEntity
import nl.rabobank.kotlinmovement.recipes.model.IngredientRequest
import nl.rabobank.kotlinmovement.recipes.model.IngredientResponse
import nl.rabobank.kotlinmovement.recipes.model.IngredientType
import nl.rabobank.kotlinmovement.recipes.model.RecipeRequest
import nl.rabobank.kotlinmovement.recipes.model.RecipeResponse

object RecipesMapper {
    fun toRecipeEntity(recipeRequest: RecipeRequest): RecipesEntity {
        return RecipesEntity(null, checkNotNull(recipeRequest.recipeName), emptySet())
    }

    fun toIngredientsEntity(recipeRequest: RecipeRequest, recipe: RecipesEntity?): Set<IngredientsEntity> {
        return checkNotNull(recipeRequest.ingredients).map { (name, type, weight): IngredientRequest ->
            IngredientsEntity(
                recipe,
                null,
                checkNotNull(name),
                checkNotNull(type).name,
                checkNotNull(weight)
            )
        }.toSet()
    }

    fun toRecipeResponse(
        recipes: RecipesEntity,
        ingredientsEntities: Set<IngredientsEntity>
    ): RecipeResponse =
        RecipeResponse(
            checkNotNull(recipes.id),
            recipes.recipeName,
            ingredientsEntities.map { ingredient ->
                IngredientResponse(
                    ingredient.id,
                    ingredient.name,
                    IngredientType.valueOf(ingredient.type),
                    ingredient.weight
                )
            }.toSet()
        )

}
