# Test Recipe

Now you have completed all previous recipes in this project, it is time to convert all the unit test code.
The test classes are located in the root of [test resources](../../src/test/java/nl/rabobank/kotlinmovement/recipes).
As before, we will first convert the simplest classes and continue from there.  
But first, let's take a moment to understand how the things are set up in the tests.

## Test setup

To test our `Recipe service` we use Spring Boot Test
and [Spring MockMvc](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/web/servlet/MockMvc.html)
.
In the test we spin-up (with help of `Spring Boot Test`) a `Recipe service` and an in-memory `H2 database`.
With `MockMvc` we call the `Recipe service` endpoints to get/update/create/delete a recipe, and verify the responses of
each call.

### MockMvc Tests

We have three test classes:

- GetRecipesControllerTest
- CreateUpdateRecipesControllerTest
- DeleteRecipesControllerTest

All the classes mentioned above extend the `RecipeMockMvcTest` class that contains all `MockMvc` related methods used by
the tests.

### Test util

We have extracted the re-usable code from the tests, to keep the test classes clean as possible.  
Under [test/util](../../src/test/java/nl/rabobank/kotlinmovement/recipes/test/util) you can find:

- `RecipeAssert`: Contains all the assertion logic
- `RecipeMockMvcTest`: Is the parent class of all the test classes
- `RecipeTestData`: Contains the test data for each test.

### Test util model

Under [test/util/model](../../src/test/java/nl/rabobank/kotlinmovement/recipes/test/util/model) you can find the simple
POJO classes that represent the input/output of the `Recipe service`.
They are used by `Jackson ObjectMapper` to serialize/deserialize `MockMvc` requests/responses.

## Convert model package & RecipeMockMvcTest class

1) Convert the domain models
   under [test/util/model](../../src/test/java/nl/rabobank/kotlinmovement/recipes/test/util/model).
2) Change the classes to `data classes`
3) If you run the tests now, you will get a deserialization
   exception: `com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance of nl.rabobank.kotlinmovement.recipes.test.model.RecipeResponseTest ...cannot deserialize from Object value (no delegate- or property-based Creator)`
   . This is because `Jackson` expects a default constructor to exist on the Kotlin object.
4) Add [jackson-module-kotlin](https://github.com/FasterXML/jackson-module-kotlin) to fix this issue

````xml

<dependency>
    <groupId>com.fasterxml.jackson.module</groupId>
    <artifactId>jackson-module-kotlin</artifactId>
    <version>2.13.4</version>
    <scope>test</scope>
</dependency>
````

5) Next, convert
   the [RecipeMockMvcTest](../../src/test/java/nl/rabobank/kotlinmovement/recipes/test/util/RecipeMockMvcTest.java)
   class
6) Note that the class is marked `open` after the conversion, but as we have configured the `all-open` in
   the `kotlin-maven-plugin` you can remove the open keyword
7) Change the functions' parameter types that are not nullable to normal types, except for the Java's boxed primitive
   types (i.e. Long, Double etc.)
8) Instantiate the field `objectMapper` with `jacksonObjectMapper()`.
9) Note that the `objectMapper` field was `static`, and because `static` translates to `singleton` in Kotlin the
   property was moved to the `companion object`. And because the `objectMapper` is still called from `Java`
   code, `Intellij` has added the annotation `@JvmField` to it. This annotation can be removed after converting all the
   code to Kotlin
10) When ready, run all tests:

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


[Go to next section](../4-util/Recipe.md)
