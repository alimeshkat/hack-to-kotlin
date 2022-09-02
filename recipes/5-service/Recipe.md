# Service Recipe

In this recipe we will convert the classes in the [service](../../src/main/java/nl/rabobank/kotlinmovement/recipes/service) package to
kotlin.

There is 1 service class, 1 object mapper class and 1 exception class that will be converted.

## Convert service

1) Convert the `service` package to Kotlin.
3) Remove String.format(). In Kotlin you can directly is a variable in a string like so: `Recipe $id not found.`
4) Remove `!!` operators by making the variables.
   1) Make `recipeRepository` en `ingredientsRepository` non nullable by removing `? = null`.
   2) Move the variables from the class to the constructor:

      Old: 
      ```
      @Service
      open class RecipesService {
         private val recipeRepository: RecipesRepository,
         private val ingredientsRepository: IngredientsRepository
         ...
      }
      ```

      New:
      ```
      @Service
      open class RecipesService(
         private val recipeRepository: RecipesRepository
         private val ingredientsRepository: IngredientsRepository
      ) 
      {
         ...
      }
      ```

      Notice how Kotlin will generate getters on these variables. It is also possible to add spring beans without annotating them with `@Autowired` here.

5) Remove unused imports.
6) When using a stream with .map, you can reference the value as `it` (from the word iterator). There is no need to keep this assignment in the code so we can refactor this part.

   Old:
   ```
   private fun updateOrCreateRecipes(id: Long, recipeRequest: RecipeRequest): RecipesEntity {
      return recipeRepository.findById(id)
            .map { it: RecipesEntity -> RecipesEntity(it.id, recipeRequest.recipeName, null) }.orElse(
               recipeRepository.save(toRecipeEntity(recipeRequest)))
   }
   ```

   New:
   ```
   private fun updateOrCreateRecipes(id: Long, recipeRequest: RecipeRequest): RecipesEntity {
      return recipeRepository.findById(id)
            .map { RecipesEntity(it.id, recipeRequest.recipeName, null) }.orElse(
               recipeRepository.save(toRecipeEntity(recipeRequest)))
   }
   ```
7) Clean-up Lombok annotations e.g. `@AllArgsConstructor`
8) When ready, run all tests:
```shell
   (cd ../.. && ./mvnw clean verify)
   ```
9) If all tests have passed, continue to the next recipe.

[Go to next section](../6-test/Recipe.md)