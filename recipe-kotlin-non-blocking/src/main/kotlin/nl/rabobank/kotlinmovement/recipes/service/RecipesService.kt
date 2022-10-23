package nl.rabobank.kotlinmovement.recipes.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.toSet
import nl.rabobank.kotlinmovement.recipes.data.IngredientsEntity
import nl.rabobank.kotlinmovement.recipes.data.IngredientsRepository
import nl.rabobank.kotlinmovement.recipes.data.RecipesEntity
import nl.rabobank.kotlinmovement.recipes.data.RecipesRepository
import nl.rabobank.kotlinmovement.recipes.model.RecipeRequest
import nl.rabobank.kotlinmovement.recipes.model.RecipeResponse
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RecipesService(
    val recipeRepository: RecipesRepository,
    val ingredientsRepository: IngredientsRepository
) {

    @get:Transactional
    val recipes: Flow<RecipeResponse>
        get() = recipeRepository.findAll()
            .mapNotNull { r: RecipesEntity -> r.ingredients?.let { RecipesMapper.toRecipeResponse(r, r.ingredients) } }

    @Transactional
    suspend fun getRecipe(id: Long): RecipeResponse? = recipeRepository.findById(id)
        ?.let { recipesEntity ->
            checkNotNull(recipesEntity.ingredients)
            RecipesMapper.toRecipeResponse(recipesEntity, recipesEntity.ingredients)
        }
        ?: throw ResourceNotFoundException("Recipe $id not found")

    @Transactional
    suspend fun saveRecipe(recipeRequest: RecipeRequest): RecipeResponse? {
        val recipe = RecipesMapper.toRecipeEntity(recipeRequest)
        val recipes = recipeRepository.save(recipe)
        val ingredients = saveIngredients(recipeRequest, recipes)
        return RecipesMapper.toRecipeResponse(recipes, ingredients)
    }

    @Transactional
    suspend fun updateOrCreateRecipe(id: Long, recipeRequest: RecipeRequest): RecipeResponse? {
        val recipes = updateOrCreateRecipes(id, recipeRequest)
        val ingredients = saveIngredients(recipeRequest, recipes)
        return RecipesMapper.toRecipeResponse(recipes, ingredients)
    }

    @Transactional
    suspend fun deleteRecipe(id: Long) {
        try {
            recipeRepository.deleteById(id)
        } catch (e: EmptyResultDataAccessException) {
            throw ResourceNotFoundException("Recipe $id not found")
        }
    }

    private suspend fun updateOrCreateRecipes(id: Long, recipeRequest: RecipeRequest): RecipesEntity {
        return recipeRepository.findById(id)?.let {
            requireNotNull(recipeRequest.recipeName) {}
            RecipesEntity(it.id, recipeRequest.recipeName, null)
        } ?: recipeRepository.save(RecipesMapper.toRecipeEntity(recipeRequest))
    }

    private suspend fun saveIngredients(recipeRequest: RecipeRequest, recipe: RecipesEntity): Set<IngredientsEntity> =
        ingredientsRepository.saveAll(RecipesMapper.toIngredientsEntity(recipeRequest, recipe)).toSet()
}
