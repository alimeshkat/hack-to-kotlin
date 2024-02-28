package nl.rabobank.kotlinmovement.recipes.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import nl.rabobank.kotlinmovement.recipes.data.IngredientsEntity
import nl.rabobank.kotlinmovement.recipes.data.IngredientsRepository
import nl.rabobank.kotlinmovement.recipes.data.RecipesAndIngredientsRepository
import nl.rabobank.kotlinmovement.recipes.data.RecipesRepository
import nl.rabobank.kotlinmovement.recipes.model.RecipeRequest
import nl.rabobank.kotlinmovement.recipes.model.RecipeResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RecipesService(
    val recipeRepository: RecipesRepository,
    val ingredientsRepository: IngredientsRepository,
    val recipesAndIngredientsRepository: RecipesAndIngredientsRepository
) {
    @Transactional
    suspend fun getRecipes(): Flow<RecipeResponse> = recipesAndIngredientsRepository.findAllRecipesAndIngredients()
        .map { RecipesMapper.toRecipeResponse(it) }

    @Transactional
    suspend fun getRecipe(id: Long): RecipeResponse? =
        recipesAndIngredientsRepository.findRecipesAndIngredientsById(id)?.let {
            RecipesMapper.toRecipeResponse(it)
        } ?: throw ResourceNotFoundException("Recipe $id not found")

    @Transactional
    suspend fun saveRecipe(recipeRequest: RecipeRequest): RecipeResponse {
        val recipe = RecipesMapper.toRecipeEntity(recipeRequest)
        val recipes = recipeRepository.save(recipe)
        val ingredients = saveIngredients(recipeRequest, recipes.recipeId)
        return RecipesMapper.toRecipeResponse(recipes.copy(ingredients = ingredients.toList().toMutableList()))
    }

    @Transactional
    suspend fun updateOrCreateRecipe(id: Long, recipeRequest: RecipeRequest): RecipeResponse? {
        return recipeRepository.findById(id)?.let {
            checkNotNull(recipeRequest.recipeName)
            val ingredients = recreateIngredients(id, recipeRequest)
            val recipes = recipeRepository.save(RecipesMapper.toRecipeEntity(recipeRequest, id))
            RecipesMapper.toRecipeResponse(recipes.copy(ingredients = ingredients.toList().toMutableList()))
        } ?: saveRecipe(recipeRequest)
    }

    @Transactional
    suspend fun deleteRecipe(id: Long) {
        recipeRepository.deleteById(id)
    }

    private suspend fun recreateIngredients(id: Long, recipeRequest: RecipeRequest): Flow<IngredientsEntity> {
        ingredientsRepository.deleteByRecipeId(id)
        return saveIngredients(recipeRequest, id)
    }

    private suspend fun saveIngredients(
        recipeRequest: RecipeRequest,
        recipeId: Long?
    ): Flow<IngredientsEntity> {
        return ingredientsRepository.saveAll(RecipesMapper.toIngredientsEntity(recipeRequest, recipeId))
    }

}
