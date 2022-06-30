# Controller Recipes

In this recipe we will convert
the classes in the [controller](../../src/main/java/nl/rabobank/kotlinmovement/recipes/controller) package to Kotlin.

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

## Convert ErrorMessageMapper

The `ErrorMessageMapper` is used by the `RecipesControllerAdvice` to transforms the error messages from the exceptions
thrown by the application to the body of an error response.
For example, if the client `POST` request contains multiple incorrect fields a `MethodArgumentNotValidException` is
thrown by the application, because of the constraints put on certain request fields (e.g. `@NotNull`, `@NotEmtpy` etc.).
The `ErrorMessageMapper` will map all the error messages (constraint violations) to a single `String` (with a pre-fix),
and creates an `RecipeErrorResponse`.  
Which gets serialised to `JSON` and returned to the client.

1) Convert the `ErrorMessageMapper` Java files to Kotlin. Because this class contains only `static` methods, IntelliJ
   converts it into a singleton `object`
2) Refactor both the `toErrorMessage()` method the following way:
    1) Use Kotlin's Collection `extension` functions instead of Java Streams
    2) Assign default values to the optional parameters
    3) Use `sortedBy()` to sort the messages
    4) Replace the mutable `StringBuilder`
       by more lean and
       safe [`joinToString()`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/join-to-string.html)
       implementation. 
   
4) When ready, run all tests:

```shell
   (cd ../.. && ./mvnw clean verify)
   ```

8) If all tests have passed, continue to the next recipe.

[Go to next section](../5-service/Recipe.md)
