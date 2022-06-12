# Controller Recipes

In this recipe we will convert
the [RecipeController](../../src/main/java/nl/rabobank/kotlinmovement/recipes/controller/RecipesController.java)
& [RecipeControllerAdvice](../../src/main/java/nl/rabobank/kotlinmovement/recipes/controller/RecipesControllerAdvice.java)
to
Kotlin.
The `RecipesController` contains the REST methods of our `Recipes` API, and the `RecipesControllerAdvice` catches
exceptions and returns the proper error code and response.

## Convert RecipesController & RecipesControllerAdvice

1) Convert the `RecipeController` Java files to Kotlin
2) As the log property was created based on Lombok annotation `@Slf4`, we will have to replace it.
   Add a `companion object` to the class, and
   declare `val log: Logger = LoggerFactory.getLogger(RecipeController::class.java)` to it
3) Declare the private `recipeService` property as a non-nullable argument to the primary constructor. This way you can
   call the members on it without a safe call (?.) or a bang-bang (!!)
5) Convert `RecipeControllerAdvice` to Kotlin
6) Use the `listOf()` instead of Java's `List.of()`
7) When ready, run all tests:

```shell
   (cd ../.. && ./mvnw clean verify)
   ```

[Go to next section](../4-util/Recipe.md)
