package nl.alimeshkat.recipes.service

import nl.alimeshkat.recipes.data.IngredientsEntity
import nl.alimeshkat.recipes.data.RecipesEntity
import nl.alimeshkat.recipes.model.IngredientRequest
import nl.alimeshkat.recipes.model.IngredientResponse
import nl.alimeshkat.recipes.model.IngredientType
import nl.alimeshkat.recipes.model.RecipeRequest
import nl.alimeshkat.recipes.model.RecipeResponse

object RecipesMapper {
    fun toRecipeEntity(recipeRequest: RecipeRequest): RecipesEntity {
        return RecipesEntity(null, checkNotNull(recipeRequest.recipeName), emptySet())
    }

    fun toIngredientsEntity(recipeRequest: RecipeRequest, recipe: RecipesEntity?): Set<IngredientsEntity> {
        return checkNotNull(recipeRequest.ingredients).map { (name, type, weight): IngredientRequest ->
          IngredientsEntity(
                recipes = recipe,
                ingredientId = null ,
                name = checkNotNull(name),
                type = checkNotNull(type).name,
                weight = checkNotNull(weight),
            )
        }.toSet()
    }

    fun toRecipeResponse(
        recipes: RecipesEntity,
        ingredientsEntities: Set<IngredientsEntity>
    ): RecipeResponse =
        RecipeResponse(
            checkNotNull(recipes.recipeId),
            recipes.recipeName,
            ingredientsEntities.map { ingredient ->
                IngredientResponse(
                    ingredient.ingredientId,
                    ingredient.name,
                    IngredientType.valueOf(ingredient.type),
                    ingredient.weight
                )
            }.toSet()
        )

}
