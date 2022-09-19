# Test util Recipe

Now you have completed all previous recipes in this project, it is time to convert all the unit test code.
The test classes are located in the root of [test resources](../../src/test/java/nl/rabobank/kotlinmovement/recipes).

As before, we will first convert the simplest classes and continue from there.

## Convert model package & RecipeMockMvcTest class

1) Checkout the [test setup](TestSetup.MD)
2) Convert the domain models
   under [test/util/model](../../src/test/java/nl/rabobank/kotlinmovement/recipes/test/util/model).
3) Change the classes to `data classes`
4) If you run the tests now, you will get a deserialization
   exception: `com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance of nl.rabobank.kotlinmovement.recipes.test.model.RecipeResponseTest ...cannot deserialize from Object value (no delegate- or property-based Creator)`
   . This is because `Jackson` expects a default constructor to exist on the Kotlin object.
5) Add [jackson-module-kotlin](https://github.com/FasterXML/jackson-module-kotlin) to fix this issue

````xml

<dependency>
    <groupId>com.fasterxml.jackson.module</groupId>
    <artifactId>jackson-module-kotlin</artifactId>
    <version>2.13.4</version>
    <scope>test</scope>
</dependency>
````

6) Next, convert
   the [RecipeMockMvcTest](../../src/test/java/nl/rabobank/kotlinmovement/recipes/test/util/RecipeMockMvcTest.java)
   class
7) Note that the class is marked `open` after the conversion, but as we have configured the `all-open` in
   the `kotlin-maven-plugin` you can remove the `open` keyword. Change the functions' parameter types that are not
   nullable to normal types, except for the Java's boxed primitive
   types (i.e. Long, Double etc.)
8) Instantiate the field `objectMapper` with `jacksonObjectMapper()`.
9) Note that the `objectMapper` field was `static`, because in Kotlin `static` translate to `singleton` after conversion
   the
   property was moved to the `companion object` of the class. And because the `objectMapper` is still called from `Java`
   code, `Intellij` has added the annotation `@JvmField` to it. This annotation can be removed after all the
   code is converted to Kotlin
10) Let's refactor the `mockMvcPerformRequest()` function, so it will not need a class type argument but instead use the
    generic parameter i.e. <T>. We can
    simply achieve that by using reified type parameters i.e. `<reified T>`. Reified type parameters are possible
    `inline` functions, these functions are inlined on the call-site. After refactoring the function signature looks
    like
    this: `private inline fun <reified T> mockMvcPerformRequest(requestBuilder: MockHttpServletRequestBuilder?, status: ResultMatcher?)`

11) When ready, run all tests:

```shell
   (cd ../.. && ./mvnw clean verify)
   ```

## Convert RecipeTestData

1) Convert [RecipeTestData](../../src/test/java/nl/rabobank/kotlinmovement/recipes/test/util/RecipeTestData.java)
2) Replace `java.util.Set.of()` with `setOf()`
3) Note that the annotations `@JvmField` can be removed once all the test code has been converted to `Kotlin`.
4) When ready, run all tests:

```shell
   (cd ../.. && ./mvnw clean verify)
   ```

## Convert RecipeMockMvcTest

1) Convert [RecipeAssert](../../src/test/java/nl/rabobank/kotlinmovement/recipes/test/util/RecipeAssert.java)
2) Replace the Java Stream Api with Kotlin Collection extension functions (e.g. map, filter, firstOrNull etc.)
3) Like always, think about if a variable or argument should be nullable and make sure you handle the nullable types in
   an
   appropriate way
4) The `matchIngredientAndAssert()` receives a Java `Consumer` (a functional interface) which represents an operation
   that accepts an argument
   and return nothing. In Kotlin, you can simply replace it with a lambda i.e `(T) -> Unit`.
5) In `matchIngredients` due to the lag of a `Tuple` implementation in Java we have used `AbstractMap.SimpleEntry<K,V>`
   to represent a key value pair. In Kotlin, you can replace `SimpleEntry<K,V>` with `Pair()`
6) When ready, run all tests:

```shell
   (cd ../.. && ./mvnw clean verify)
   ```

## Covert the test classes

1) Last but not least, convert the
   [test classes](../../src/test/java/nl/rabobank/kotlinmovement/recipes/CreateUpdateRecipesControllerTest.java)
2) We have used Junits annotation `@DisplayName` to describe our tests in a readable way. Now we use Kotlin, you can use
   spaces in the function name when writing it within backticks e.g. Fewer annotations all the better!

```Kotlin
fun `forever foo bar `() {
    //
}
```

3) When ready, run all tests:

```shell
   (cd ../.. && ./mvnw clean verify)
   ```

3) Well done! You have migrated a Java Spring Boot project to Kotlin!
