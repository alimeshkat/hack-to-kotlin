package nl.rabobank.kotlinmovement.recipes.service

import nl.rabobank.kotlinmovement.recipes.data.IngredientsEntity
import nl.rabobank.kotlinmovement.recipes.data.IngredientsRepository
import nl.rabobank.kotlinmovement.recipes.data.RecipesEntity
import nl.rabobank.kotlinmovement.recipes.data.RecipesRepository
import nl.rabobank.kotlinmovement.recipes.model.RecipeRequest
import nl.rabobank.kotlinmovement.recipes.model.RecipeResponse
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RecipesService(
    val recipeRepository: RecipesRepository,
    val ingredientsRepository: IngredientsRepository
) {

    @get:Transactional
    val recipes: List<RecipeResponse>
        get() = recipeRepository.findAll()
            .mapNotNull { r: RecipesEntity -> r.ingredients?.let { RecipesMapper.toRecipeResponse(r, r.ingredients) } }

    @Transactional
    fun getRecipe(id: Long): RecipeResponse? = recipeRepository.findByIdOrNull(id)
        ?.let { recipesEntity ->
            checkNotNull(recipesEntity.ingredients)
            RecipesMapper.toRecipeResponse(recipesEntity, recipesEntity.ingredients)
        }
        ?: throw ResourceNotFoundException("Recipe $id not found")

    @Transactional
    fun saveRecipe(recipeRequest: RecipeRequest): RecipeResponse? {
        val recipe = RecipesMapper.toRecipeEntity(recipeRequest)
        val recipes = recipeRepository.save(recipe)
        val ingredients = saveIngredients(recipeRequest, recipes)
        return RecipesMapper.toRecipeResponse(recipes, ingredients)
    }

    @Transactional
    fun updateOrCreateRecipe(id: Long, recipeRequest: RecipeRequest): RecipeResponse? {
        val recipes = updateOrCreateRecipes(id, recipeRequest)
        val ingredients = saveIngredients(recipeRequest, recipes)
        return RecipesMapper.toRecipeResponse(recipes, ingredients)
    }

    @Transactional
    fun deleteRecipe(id: Long) {
        try {
            recipeRepository.deleteById(id)
        } catch (e: EmptyResultDataAccessException) {
            throw ResourceNotFoundException("Recipe $id not found")
        }
    }

    private fun updateOrCreateRecipes(id: Long, recipeRequest: RecipeRequest): RecipesEntity {
        return recipeRepository.findByIdOrNull(id)?.let {
            requireNotNull(recipeRequest.recipeName) {}
            RecipesEntity(it.id, recipeRequest.recipeName, null)
        } ?: recipeRepository.save(RecipesMapper.toRecipeEntity(recipeRequest))
    }

    private fun saveIngredients(recipeRequest: RecipeRequest, recipe: RecipesEntity): Set<IngredientsEntity> =
        ingredientsRepository.saveAll(RecipesMapper.toIngredientsEntity(recipeRequest, recipe)).toSet()
}
