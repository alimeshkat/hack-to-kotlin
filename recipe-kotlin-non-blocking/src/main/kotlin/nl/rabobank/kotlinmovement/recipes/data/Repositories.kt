package nl.rabobank.kotlinmovement.recipes.data

import io.r2dbc.spi.Row
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Service


interface RecipesRepository : CoroutineCrudRepository<RecipesEntity, Long>

interface IngredientsRepository : CoroutineCrudRepository<IngredientsEntity, Long> {
    suspend fun deleteByRecipeId(id: Long): Unit
}

interface RecipesAndIngredientsRepository {
    suspend fun findAllRecipesAndIngredients(): Map<RecipesEntity, List<IngredientsEntity>>
    suspend fun findRecipesAndIngredientsById(id: Long): Pair<RecipesEntity, List<IngredientsEntity>>?

}

@Service
class RecipesAndIngredientsRepositoryImp(private val template: R2dbcEntityTemplate) : RecipesAndIngredientsRepository {

    override suspend fun findAllRecipesAndIngredients(): Map<RecipesEntity, List<IngredientsEntity>> {

        return template.databaseClient.sql {
            "SELECT * FROM ingredients " +
                    "INNER JOIN recipes ON ingredients.recipe_id = recipes.recipe_id"
        }
            .map(::mapRowToRecipeToIngredient)
            .all()
            .asFlow()
            .toList()
            .groupBy({ (r, _) -> r }) { it.second }
    }

    override suspend fun findRecipesAndIngredientsById(id: Long): Pair<RecipesEntity, List<IngredientsEntity>>? {
        return template.databaseClient.sql {
            "SELECT * FROM ingredients " +
                    "INNER JOIN recipes ON ingredients.recipe_id = recipes.recipe_id" +
                    "WHERE recipes.recipe_id = :recipeId"
        }
            .bind("recipeId", id)
            .map(::mapRowToRecipeToIngredient)
            .all()
            .asFlow()
            .toList()
            .groupBy({ (r, _) -> r }) { it.second }.entries.firstOrNull()?.toPair()
    }
}

fun mapRowToRecipeToIngredient(r: Row): Pair<RecipesEntity, IngredientsEntity> = RecipesEntity(
    recipeId = checkNotNull(r.get("recipe_id", java.lang.Integer::class.java)).toLong(),
    recipeName = checkNotNull(r.get("recipe_name", String::class.java)),
) to IngredientsEntity(
    ingredientId = checkNotNull(r.get("ingredient_id", java.lang.Integer::class.java)).toLong(),
    name = checkNotNull(r.get("name", String::class.java)),
    type = checkNotNull(r.get("type", String::class.java)),
    weight = checkNotNull(r.get("weight", java.lang.Integer::class.java)).toInt(),
    recipeId = checkNotNull(r.get("recipe_id", java.lang.Integer::class.java)).toLong()
)
