package nl.alimeshkat.recipes.service

import nl.alimeshkat.recipes.data.IngredientsEntity
import nl.alimeshkat.recipes.data.IngredientsRepository
import nl.alimeshkat.recipes.data.RecipesEntity
import nl.alimeshkat.recipes.data.RecipesRepository
import nl.alimeshkat.recipes.model.RecipeRequest
import nl.alimeshkat.recipes.model.RecipeResponse
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
    fun getRecipe(id: Long): RecipeResponse = recipeRepository.findByIdOrNull(id)
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
        return recipeRepository.findByIdOrNull(id)?.let {
            requireNotNull(recipeRequest.recipeName)
            val recipe = recipeRepository.save(RecipesEntity(it.recipeId, recipeRequest.recipeName))
            val ingredients = saveIngredients(recipeRequest, recipe)
            return RecipesMapper.toRecipeResponse(recipe, ingredients)
        } ?: saveRecipe(recipeRequest)
    }

    @Transactional
    fun deleteRecipe(id: Long) {
            recipeRepository.deleteById(id)
    }

    private fun saveIngredients(recipeRequest: RecipeRequest, recipe: RecipesEntity): Set<IngredientsEntity> =
        ingredientsRepository.saveAll(RecipesMapper.toIngredientsEntity(recipeRequest, recipe)).toSet()
}
