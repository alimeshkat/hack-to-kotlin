# Util Recipe

Now we have converted the simplest Java classes, let's have a look at a class that has some logic,
the [ErrorMessageUtil](../../src/main/java/nl/rabobank/kotlinmovement/recipes/util/ErrorMessageUtil.java) class.

## Convert ErrorMessageUtil

1) Convert the `ErrorMessageUtil` Java files to Kotlin. Because this class contains only `static` methods, IntelliJ
   converts it into a singleton `object` 
2) Make use of the Kotlin Collection extension to replace Java Streams, to make the code more concise
3) Refactor the `toErrorMessage()` method the following way:
   1) Assign default values to the optional parameters
   2) Use `sortedBy()` to sort the messages
   3) Replace `StringBuilder` implementation by [`foldIndexed()`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/fold-indexed.html)
4) When ready, run all tests:

```shell
   (cd ../.. && ./mvnw clean verify)
   ```

[Go to next section](../4-util/Recipe.md)
