# Domain Model Recipe

In this recipe we will convert the [recipe models](../../src/main/java/nl/rabobank/kotlinmovement/recipes/model) to
kotlin. To keep the Java files concise, we have used `Lombok` library annotations to generate the `getters`, `contructor`, `equals` and `hashcode`.

## Ingredients

- [Convert Java File To Kotlin](CONVERT_JAVA_FILE_TO_KOTLIN.md)
- [Data Classes](DATA_CLASSES.md)

## Steps

1) Read the document about converting Java file to Kotlin, and Convert the classes in `model` package to Kotlin (can be done in one go!)
2) Clean-up Lombok annotations @Data
3) Read the document about the `Data classes`, and refactor the converted recipe models to `Data Classes`
4) Run all tests:
```shell
   (cd ../.. && ./mvnw clean verify)
   ```
5) If all tests have passed, continue to the next recipe.

[Go to next section](../section3-controller/Recipe.md)


