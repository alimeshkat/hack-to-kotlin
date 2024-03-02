package nl.rabobank.kotlinmovement.recipes.data

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.asFlow
import org.reactivestreams.Publisher
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

interface RecipesRepository : CoroutineCrudRepository<RecipesEntity, Long>

interface IngredientsRepository : CoroutineCrudRepository<IngredientsEntity, Long> {
    suspend fun deleteByRecipeId(id: Long)
}

interface RecipesAndIngredientsRepository {
    fun findAllRecipesAndIngredients(): Flow<RecipesEntity>
    suspend fun findRecipesAndIngredientsById(id: Long): RecipesEntity?

}

@Component
class RecipesAndIngredientsRepositoryImp(private val template: R2dbcEntityTemplate) : RecipesAndIngredientsRepository {

    override  fun findAllRecipesAndIngredients(): Flow<RecipesEntity> {
        return template.databaseClient.sql {
            """SELECT * FROM ingredients 
                   INNER JOIN recipes 
                   ON ingredients.recipe_id = recipes.recipe_id
                   ORDER BY recipes.recipe_id 
                   """.trimMargin()
        }.fetch()
            .all()
            .bufferUntilChanged { requireNotNull(it["recipe_id"]) }
            .flatMap(::mapRowToRecipeToIngredient)
            .asFlow()
    }

    override suspend fun findRecipesAndIngredientsById(id: Long): RecipesEntity? {

        return template.databaseClient.sql {
            """SELECT * FROM ingredients 
                    INNER JOIN recipes 
                    ON ingredients.recipe_id = recipes.recipe_id
                    WHERE recipes.recipe_id = :recipeId
                    ORDER BY recipes.recipe_id 
                    """.trimMargin()
        }
            .bind("recipeId", id)
            .fetch()
            .all()
            .bufferUntilChanged { requireNotNull(it["recipe_id"]) }
            .flatMap(::mapRowToRecipeToIngredient)
            .asFlow()
            .firstOrNull()
    }


    /**
     *   Maps each fetched row from the database into a `RecipesEntity` and wraps it into  a `Mono` to be emitted further down the pipeline.
     *
     *   @param `recipes` a `MutableList<MutableMap<String, Any>>`
     *   @return `Publisher<RecipesEntity>`
     */
    fun mapRowToRecipeToIngredient(recipes: MutableList<MutableMap<String, Any>>): Publisher<RecipesEntity> {
        return recipes.groupBy { it["recipe_id"] }
            .mapNotNull { (k, v) ->
                RecipesEntity(
                    recipeId = k.toString().toLong(),
                    recipeName = v.first()["recipe_name"].toString(),
                    ingredients = v.map { ingredients ->
                        IngredientsEntity(
                            ingredientId = ingredients["ingredient_id"].toString().toLong(),
                            name = ingredients["name"].toString(),
                            type = ingredients["type"].toString(),
                            weight = ingredients["weight"].toString().toInt(),
                            recipeId = ingredients["recipe_id"].toString().toLong()
                        )
                    }.toSet()
                )
            }.firstOrNull()
            .let { Mono.justOrEmpty(it) }
    }
}
