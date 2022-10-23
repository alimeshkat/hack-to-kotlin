# Service Recipe

In this recipe we will help you to convert the classes in
the [service](../../recipe-java/src/main/java/nl/rabobank/kotlinmovement/recipes/service) package to
kotlin.

## Convert RecipeService

1) Convert the `RecipesService` class to Kotlin.
2) Remove `String.format()` and use the variable directly in the string like so: `Recipe $id not found.`
3) Replace Java `Stream` API by Kotlin Collection extensions
4) Replace `findById()` by the Kotlin extension `findByIdOrNull()` and handle the null case as required
5) place the `recipeRepository` en `ingredientsRepository` on the default constructor and make them non-nullable
   properties
6) Clean-up Lombok annotations e.g. `@AllArgsConstructor`
7) When ready, run all tests:
```shell
   (cd ../.. && ./mvnw clean verify)
   ```
---
## Convert ResourceNotFoundException
1) Convert the `ResourceNotFoundException` class to Kotlin
3) When ready, run all tests:
```shell
   (cd ../.. && ./mvnw clean verify)
   ```
---
## Convert RecipeMapper

1) Convert the `RecipesMapper` class to Kotlin.
2) Replace Java `Stream` API by Kotlin Collection extensions
3) Replace the `!!` operator with a more proper implementation. Read the section below to learn more about that.

4) When ready, run all tests:
```shell
   (cd ../.. && ./mvnw clean verify)
   ```
9) If all tests have passed, continue to the next recipe.

--- 
![light-bulb](../sources/png/light-bulb-xs.png)  
In some cases we expect that at certain point a nullable property should not be null.
You can do a couple of things to assure the compiler that the value cannot be null.
You can use the operators `!!`, `?:` or functions such as `requiredNotNull` or `checkNotNull`.

- The `!!` will throw a kotlinnullpointer, so that one maybe less desirable
- The operator `?:` is quite powerful, you can
  return a default value, throw an exception or just return
- `requiredNotNull` or `checkNotNull` functions are similar, both return the not null value or throw an
  exception.
  It's just a matter of semantics which one you want to use. The `requiredNotNull` is usually defined at the beginning
  of a function to assert if the required values are provided and throws `IlligalArgumentException` if it is not. On the other
  hand, `checkNotNull` can be used at certain points to checks if an object is in a valid state, if not it
  throws `IllegalStateException`.

---

[Go to next section](../7-test/Recipe.md)
