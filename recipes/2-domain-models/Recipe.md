# Domain Model Recipe

In this recipe we will convert the [recipe models](../../recipe-java/src/main/java/nl/rabobank/kotlinmovement/recipes/model) to
kotlin. To keep the Java files concise, we have used `Lombok` library annotations to generate the `getters`, `contructor`, `equals` and `hashcode`.

## Convert domain models 

1) Convert the classes in `model` package to Kotlin (Read the document about [converting Java file to Kotlin](CONVERT_JAVA_FILE_TO_KOTLIN.md)).
2) Clean-up Lombok annotations e.g. `@Data`
3) Convert recipe models to `Data Classes` (ok but how?)
4) Make sure the properties are:
   1) `nullable` (e.g. name:String?) were it's needed, like the request object properties
   2) declared in the primary constructor 
   3) **public** (without any modifier)
   4) and, immutable (`val`)
5) The bean validation annotations should be:
   1) on the backing field i.e. `@field:NotBlank()`
   2) on the left side of the property declaration e.g. `@field:NotNull val name:String?`
6) When ready, run all tests:
```shell
   (cd ../.. && ./mvnw clean verify)
   ```
5) If all tests have passed, continue to the next recipe.

[Go to next section](../3-data/Recipe.md)



