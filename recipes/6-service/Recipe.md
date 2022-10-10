# Service Recipe

In this recipe we will convert the classes in
the [service](../../recipe-java/src/main/java/nl/rabobank/kotlinmovement/recipes/service) package to
kotlin.

There is 1 service class, 1 object mapper class and 1 exception class that will be converted.

## Convert RecipeService

1) Convert the `service` package to Kotlin.
2) Remove `String.format()` and use the variable directly in the string like so: `Recipe $id not found.`
3) Replace Java `Stream` API by Kotlin Collection extensions
4) Replace `findById()` by the Kotlin extension `findByIdOrNull()` and handle the null case as required
5) place the `recipeRepository` en `ingredientsRepository` on the default constructor and make them non-nullable
   properties
6) Clean-up Lombok annotations e.g. `@AllArgsConstructor` and `!!` operators
7) When ready, run all tests:

```shell
   (cd ../.. && ./mvnw clean verify)
   ```

9) If all tests have passed, continue to the next recipe.

[Go to next section](../7-test/Recipe.md)
