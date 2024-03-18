# Controller 

In this recipe we will convert
the classes in the [controller](../../../java-to-kotlin/src/main/java/nl/alimeshkat/recipes/controller)
package
to Kotlin.

The `RecipesController` contains the REST methods of our `Recipes` API, and the `RecipesControllerAdvice` catches
exceptions and returns the proper error code and response.

## Recipe

##  RecipesController & RecipesControllerAdvice

1) Convert the `RecipesController` Java files to Kotlin
2) As the log property was created based on Lombok annotation `@Slf4`, we will have to replace it.
   Add a `companion object` to the class, and
   declare `val log: Logger = LoggerFactory.getLogger(RecipesController::class.java)` to it
3) Declare the private `recipeService` property as a non-nullable argument to the primary constructor. This way you can
   call the members on it without a safe call (?.) or a bang-bang (!!)
4) Make the controller method arguments non-nullable
5) Correct any misplaced annotations
6) *Note*: `IntelliJ` converts a Java method without an argument to a property and places the HTTP method annotation on
   it's `get` function. You can always change it to a normal function.
7) Now, convert the error controller `RecipesControllerAdvice` to Kotlin
8) Use the `listOf()` instead of Java's `List.of()`
9) When ready, run all tests:

```shell
   (cd ../../.. && ./mvnw package -pl :java-to-kotlin)
```

[*peek solutions*](../../../java-to-kotlin-complete/src/main/kotlin/nl/alimeshkat/recipes/controller)

##  ErrorMessageMapper

The `ErrorMessageMapper` is used by the `RecipesControllerAdvice` to transforms the error messages from the exceptions
thrown by the application to `RecipesErrorResponse`.
For example, if the client `POST` request contains multiple incorrect fields a `MethodArgumentNotValidException` is
thrown by the application, because of the constraints put on certain request fields (e.g. `@NotNull`, `@NotEmtpy` etc.).
The `ErrorMessageMapper` will map all the error messages (constraint violations) to a single `String` (with a pre-fix),
and creates an `RecipeErrorResponse`.  
Which gets serialised to `JSON` and returned to the client.

1) Convert the `ErrorMessageMapper` Java files to Kotlin.
2) Refactor the `toErrorMessage(messages: List<String?>, messagePrefix:String?,messagePostfix: String?)` function
   the following way:
    1) Use Kotlin's Collection `extension` (e.g. `filterNotNull{}, map{} etc.`) functions instead of Java Streams
    2) Assign default values to `toErrorMessage()` optional parameters (e.g. `messagePrefix: String? = null`)
    3) filter out the `null` items from the `messages` list
    4) Use `sorted()` to sort the error messages in te list
    5) Replace the mutable `StringBuilder`
       by more lean and
       safe [`joinToString()`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/join-to-string.html)
       implementation.
3) When ready, run all tests:

```shell
   (cd ../../.. && ./mvnw package -pl :java-to-kotlin)
```

[*peek solutions*](../../../java-to-kotlin-complete/src/main/kotlin/nl/alimeshkat/recipes/controller/ErrorMessageMapper.kt)

--- 

![light-bulb](../../sources/png/light-bulb-xs.png)  
Maybe you noticed that the `ErrorMessageMapper` is converted to an `object` instead of a class.
That is because in the `Java` class consist only out of `static` methods, and IntelliJ converts it into a singleton
i.e.`object`. You should consider doing the same in your own code.

---

[Go to next section](../6-service/Recipe.md)
