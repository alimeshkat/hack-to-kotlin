# Data

In this recipe, we will create an entirely new repository where we will use `Spring Data` and  `R2DBC` instead of `JDBC`.
We will create the new repositories and put next to the existing ones, and use them in the service layer later.

The foremost import goal of this recipe is to show you how you can fetch entities from a relational database in a non-blocking way.
We will use `coroutines` instead of `Mono` and `Flux`  in the repository where we can to make the code more
readable and maintainable; there are Kotlin extension available for on Reactive specification, which `Spring Reactor` and `R2DBC` implement, to make that effortless.

## R2DBC

The R2DBC (Reactive Relational Database Connectivity) is a specification for non-blocking database drivers. It is an
alternative to the JDBC (Java Database Connectivity) specification, which is blocking.
The [R2DBC](https://r2dbc.io/) specification is designed to be non-blocking and reactive, which makes it a good fit for
reactive programming.

While the R2DBC specification is still in its early stages, it is already supported by several databases, including H2,
PostgreSQL, and Microsoft SQL Server.
The downside of R2DBC is that is not mature yet, so it might require you to write more code than you would with JDBC,
especially when it comes to the Object Relational Mapping (ORM) part.

## Recipe

1) REMOVE the H2 dependency from the [pom](../../../java-to-kotlin-complete/pom.xml) file:
    ```xml
    <dependecies>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
   </dependecies>
    ```

### Entities

2) CREATE a  `r2dbc` package
   under [data](../../../java-to-kotlin-complete/src/main/kotlin/nl/alimeshkat/recipes/data).
3) CREATE a Kotlin file named `RecipesIngredientsEntities.kt` under
   the [r2dbc](../../../java-to-kotlin-complete/src/main/kotlin/nl/alimeshkat/recipes/data/r2dbc) package.
    1) We will declare both of the entities here. Notice that the object to table mapping is still done for us without
       the need of JPA annotations, so that is pretty nice. Add the following code to
       the `RecipesIngredientsEntities.kt` file:
        ```kotlin
          import org.springframework.data.annotation.Id
          import org.springframework.data.annotation.ReadOnlyProperty
          import org.springframework.data.relational.core.mapping.Table
          
          @Table(name = "ingredients")
          data class IngredientEntity(
          @Id
          val ingredientId: Long? = null,
          val name: String,
          val type: String,
          val weight: Int,
          val recipeId: Long
          )
          
          @Table(name = "recipes")
          data class RecipeEntity(
          @Id
          val recipeId: Long? = null,
          val recipeName: String,
          @ReadOnlyProperty
          val ingredients: List<IngredientsEntity>  = emptyList()
          )
        ``` 

### Repositories

4) CREATE a Kotlin file named  `RecipesIngredientsRepositories.kt` under
   the [r2dbc](../../../java-to-kotlin-complete/src/main/kotlin/nl/alimeshkat/recipes/data/r2dbc) package.
   This file will contain all of our repository code.
5) We will start adding the `RecipeRepository`, which will be a `CoroutineCrudRepository`. This an easy way to leverage
   the`Spring Data`
   for generating the "simple" statement, such as saving and deleting entities.
    1) Add the following code to the `RecipesIngredientsRepository.kt` file:
        ```kotlin
        import org.springframework.data.repository.kotlin.CoroutineCrudRepository
 
        interface RecipesRepository : CoroutineCrudRepository<RecipeEntity, Long>
        ```
6) Next is the `IngredientsRepository`, which is also a `CoroutineCrudRepository`. As an example of what you can do with
   the `CoroutineCrudRepository` we declared the delete by Recipe id method with the `Query Creation` method.
    1) Add the following code to the `RecipesIngredientsRepository.kt` file:
        ```Kotlin
        interface IngredientsRepository : CoroutineCrudRepository<IngredientEntity, Long> {
           suspend fun deleteByRecipeId(id: Long)
        }
        ```
       *Notice* that the `deleteByRecipeId` is of `suspend`type, meaning, they can be suspended without blocking a
       thread, and, they can only
       be executed from within the scope of a Coroutine.
7) Now we have the save and delete covered, lets have a look at operations that get recipes and its ingredient in one
   go.
   Remember, this is the ORM part we were talking about where things get complicated. But, fortunately, `R2DBC` API
   offers us
   a template for creating custom SQL statements we desire, let's dig into that.
    1) We will first declare an interface called `RecipesAndIngredientsRepository` to
       the `RecipesIngredientsRepository.kt` :
          ```Kotlin
           interface RecipesAndIngredientsRepository {
           fun findAll(): Flow<RecipesEntity> 
           suspend fun findByIdOrNull(id: Long): RecipesEntity?
           }
         ```
       *Notice* that the return type of  `findAllRecipesAndIngredients` is of type `Flow` which emits the Recipes and
       the Ingredients belonging to it one by one.

    2) Let's declare the implementation class in the `RecipesIngredientsRepository.kt`, there we inject
       the `R2dbcEntityTemplate`.
        ```Kotlin
              @Component
              class RecipesAndIngredientsRepositoryImp(private val template: R2dbcEntityTemplate) : RecipesAndIngredientsRepository {} 
        ```
    3) Now we have to implement the functions  `findAllRecipesAndIngredients` and `findRecipesAndIngredientsById`.
        1) First, we will implement the `findAllRecipesAndIngredients`  the following way, and later we go through it
           line-by-line:
            ```Kotlin
               override fun findAllRecipesAndIngredients(): Flow<RecipesEntity> {
                    return template.databaseClient.sql { //a
                    """SELECT * FROM ingredients  
                    INNER JOIN recipes
                    ON ingredients.recipe_id = recipes.recipe_id
                    ORDER BY recipes.recipe_id
                    """.trimMargin()
                    }.fetch() 
                    .all() //b
                    .bufferUntilChanged { requireNotNull(it["recipe_id"]) } //c
                    .flatMap(::mapRowToRecipeToIngredient) //d
                    .asFlow() //e
               } 
           
            //  Maps each fetched row from the database into a `RecipeEntity` and wraps it into  a `Mono` to be emitted further down the pipeline.
               fun mapRowToRecipeToIngredient(recipes: MutableList<MutableMap<String, Any>>): Publisher<RecipeEntity> {
                       return recipes.groupBy { it["recipe_id"] } // Create a Map based each unique recipe_id
                           .mapNotNull { (k, v) ->
                                RecipeEntity(
                                    recipeId = k.toString().toLong(),
                                    recipeName = v.first()["recipe_name"].toString(),
                                    ingredients = v.map {
                                        IngredientEntity(
                                            ingredientId = it["ingredient_id"].toString().toLong(),
                                            name = it["name"].toString(),
                                            type = it["type"].toString(),
                                            weight = it["weight"].toString().toInt(),
                                            recipeId = it["recipe_id"].toString().toLong()
                                        )
                                    }
                                )
                           }.firstOrNull()
                           .let { Mono.justOrEmpty(it) }
                   }
           ```
            1) The SQL query we execute here fetches all the columns from `ingredients` and `recipes` table where
               the `recipes_id` in both matches. Then we sort the results in ascending order by the `recipes.recipe_id`
            2) We fetch all the results from the query.
            3) Because each of the result emitted contains a `Recipe` an `Ingredient` in a from of a map, we have to
               collect all the result per `recipe_id` first before we can create a complete `RecipeEntity`; we have to  buffer the results for each
               unique `recipe_id`.
            4) And, finally, we transform the `Flux` into a `Flow` with help of the `kotlinx.coroutines.reactive.asFlow` extension.
        2) Now we understand how the mapping of the SQL query result to `RecipeEntity` works, lets add the final function that gets a `Recipe` and its `Ingredients` by  `recipe_id`.
           1) The key difference between the `findAllRecipesAndIngredients` and `findRecipesAndIngredientsById` is that the latter is a `suspend` functions
               ```Kotlin
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
                            .firstOrNull() // here is the suspension point  
                   }
           

              ```
8) Now we have the repository in place, we can use it in the service layer. We will do that in the next section.     


[Go to next section](../3-service/Recipe.md)       
    