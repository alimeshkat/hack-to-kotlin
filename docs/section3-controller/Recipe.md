# Controller Recipe

In this recipe we will convert
the [RecipeController](../../src/main/java/nl/rabobank/kotlinmovement/recipes/controller/RecipeController.java) to
Kotlin.
The `RecipeController` contains the REST methods of our `Recipes` API.

## Ingredients:

- [classes and companion objects](CLASSES_AND_COMPANION_OBJECTS.md)
- [functions](FUNCTIONS.md)

## Steps

1) Convert the `RecipeController` Java file to Kotlin
2) Read about `classes and companion objects`
3) As the log property was created based on Lombok annotation `@Slf4`, we will have to replace it.
   Add a `companion object` to the class, and
   declare `val log: Logger = LoggerFactory.getLogger(RecipeController::class.java)` to it
4) Declare the private `recipeService` property as a non-nullable argument to the primary constructor. This way you can call the members on it without a safe call (?.) or a bang-bang (!!)
5) 
