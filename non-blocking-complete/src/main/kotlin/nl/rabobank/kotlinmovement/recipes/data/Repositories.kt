package nl.rabobank.kotlinmovement.recipes.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.reactive.asFlow
import org.reactivestreams.Publisher
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

//https://neilwhite.ca/joins-with-spring-data-r2dbc/
interface RecipesRepository : CoroutineCrudRepository<RecipesEntity, Long>

interface IngredientsRepository : CoroutineCrudRepository<IngredientsEntity, Long> {
    suspend fun deleteByRecipeId(id: Long)
}

interface RecipesAndIngredientsRepository {
    suspend fun findAllRecipesAndIngredients(): Flow<RecipesEntity>
    suspend fun findRecipesAndIngredientsById(id: Long): RecipesEntity?

}

@Component
class RecipesAndIngredientsRepositoryImp(private val template: R2dbcEntityTemplate) : RecipesAndIngredientsRepository {

    override suspend fun findAllRecipesAndIngredients(): Flow<RecipesEntity> {
        return template.databaseClient.sql {
            """SELECT * FROM ingredients 
                   INNER JOIN recipes 
                   ON ingredients.recipe_id = recipes.recipe_id
                   ORDER BY recipes.recipe_id 
                   """.trimMargin()
        }.fetch()
            .all()
            .bufferUntilChanged { requireNotNull(it["recipe_id"]) }
            .flatMap(::mapRowToRecipeToIngredient2)
            .asFlow()
    }

    override suspend fun findRecipesAndIngredientsById(id: Long): RecipesEntity? {

           return template.databaseClient.sql {
                """SELECT * FROM ingredients 
                    INNER JOIN recipes 
                    ON ingredients.recipe_id = recipes.recipe_id
                    WHERE recipes.recipe_id = :recipeId""".trimMargin()
            }
                .bind("recipeId", id)
                .fetch()
                .all()
                .bufferUntilChanged { requireNotNull(it["recipe_id"]) }
                .flatMap(::mapRowToRecipeToIngredient2).asFlow().firstOrNull()
    }

}
//TODO give this method some love
fun mapRowToRecipeToIngredient2(recipes: MutableList<MutableMap<String, Any>>): Publisher<RecipesEntity> {
    return Mono.just(recipes.groupBy { it["recipe_id"] }
        .mapNotNull { (k, v) ->
            RecipesEntity(
                recipeId = (k as Int).toLong(),
                recipeName = v.first()["recipe_name"] as String,
                ingredients = v.map {
                    IngredientsEntity(
                        ingredientId = (it["ingredient_id"] as Int).toLong(),
                        name = it["name"] as String,
                        type = it["type"] as String,
                        weight = it["weight"] as Int,
                        recipeId = (it["recipe_id"] as Int).toLong()
                    )
                }
            )
        }.firstOrNull()) ?: Mono.empty()
}