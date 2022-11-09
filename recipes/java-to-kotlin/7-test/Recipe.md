# Test

Now you have completed all previous recipes in this project, it is time to convert all the unit test code.
The test classes are located in the root
of [test resources](../../../java-to-kotlin/src/test/java/nl/rabobank/kotlinmovement/recipes).

As before, we will first convert the simplest classes and continue from there.

---

## Convert model package

1) Checkout the [test setup](TestSetup.MD)
2) Convert the domain models
   under [test/util/model](../../../java-to-kotlin/src/test/java/nl/rabobank/kotlinmovement/recipes/test/util/model).
3) Change the classes to `data classes`. Keep in mind that not all properties have to be nullable.
4) The `@JvmOverloads` (generation of a default constructor) on the constructor is not necessary anymore after adding
   the `jackson-module-kotlin`

````xml

<dependency>
    <groupId>com.fasterxml.jackson.module</groupId>
    <artifactId>jackson-module-kotlin</artifactId>
    <scope>test</scope>
</dependency>
````

7) Now, initialize the `objectMapper` at `RecipeMockMvcTest` class with `jacksonObjectMapper`

```shell
   (cd ../../.. && ./mvnw package -pl :java-to-kotlin)
```

---

[*peek solution*](../../../java-to-kotlin-complete/src/test/kotlin/nl/rabobank/kotlinmovement/recipes/test/util/model)

## RecipeTest class

1) convert
   the [RecipeTest](../../../java-to-kotlin/src/test/java/nl/rabobank/kotlinmovement/recipes/test/util/RecipeTest.java)
   class
2) The properties `mockMvc` is initialed when the test context is loaded, so we have to tell the compiler that this
   property is later initialised by making it a `lateinit var`
3) Let's refactor the `mockRequest()` function:
   1) so the class type argument (i.e. `Class<T> responseType`) can be removed and instead the
      type parameter i.e. `<T>` is used. Use `reified` type parameters (i.e. `<reified T>`). The
      reified keyword is only allowed in the context of a `inline` function!
   2) And the nullable parameter `body` becomes optional by providing a default value (i.e. body: String? = null)
4) Add the parameter type where the private inlined reified `mockMvcPerformRequest()` is called
5) When ready, run all tests:

---
![light-bulb](../../sources/png/light-bulb-xs.png)  
Note that the static field `objectMapper` is moved to the `companion object` of the class. And because the `objectMapper` is still called
from `Java` code, `Intellij` has added the annotation `@JvmField` to it. This annotation can be removed after all the code is converted to Kotlin
---

```shell
   (cd ../../.. && ./mvnw package -pl :java-to-kotlin)
```

[*peek solution*](../../../java-to-kotlin-complete/src/test/kotlin/nl/rabobank/kotlinmovement/recipes/test/util/model)

---

## Convert RecipeTestData

1) Convert [RecipeTestData](../../../java-to-kotlin/src/test/java/nl/rabobank/kotlinmovement/recipes/test/util/RecipeTestData.java)
2) Replace `java.util.Set.of()` with `setOf()`
3) Note that the annotations `@JvmField` can be removed once all the test code has been converted to `Kotlin`.
4) When ready, run all tests:

```shell
   (cd ../.. && ./mvnw clean verify)
```

[*peek solution*](../../../java-to-kotlin-complete/src/test/kotlin/nl/rabobank/kotlinmovement/recipes/test/util/RecipeTestData.kt)

--- 
![light-bulb](../../sources/png/light-bulb-xs.png)  
Do not provide a value as the parameter when the value is the same as the default value.
Take `RecipeRequestTest("test", null)` for example, it's not needed to provide `null` as value for the `ingredients`
because the default
value is already`null`.  
*Keep in mind*: this works well when omitting the last argument value, in other cases
you should be explicit about which arguments are actually provided.
Take for example `IngredientRequestTest("yeast", null, 100))`, after removing the `null` value the compiler thinks the
number `100` is provided as the value for the `type`! It works when explicitly assigning the value to a parameter
e.g.`IngredientRequestTest(name = "yeast", weight = 100))`.
---

## Convert RecipeAssert

1) Convert [RecipeAssert](../../../java-to-kotlin/src/test/java/nl/rabobank/kotlinmovement/recipes/test/util/RecipeAssert.java)
2) Replace the `Streams Api` with Kotlin Collection extension functions (e.g. map, filter, firstOrNull etc.)
3) Like always, think about if a variable or argument should be nullable and make sure you handle the nullables
   appropriately. Make sure that an assertion fails in case of an unexpected `null` value
4) In `matchIngredients` due to the lag of a `Tuple` implementation in Java we have used `AbstractMap.SimpleEntry<K,V>`
   to represent a key value pair but in Kotlin, you should use `Pair()`. Replace `AbstractMap.SimpleEntry<K,V>`
   with `Pair<K,V>`
5) The `matchIngredientAndAssert()` receives a Java `Consumer` (a functional interface) which represents an operation
   that accepts an argument
   and return nothing. In Kotlin, you can use the lambda notation i.e `(T) -> Unit`. Replace the consumer with a lambda
6) Like before, the `@JvmStatic` on the previously static function `assertRecipeResponse` can be removed later
7) When ready, run all tests:

```shell
   (cd ../.. && ./mvnw clean verify)
```

[*peek solution*](../../../java-to-kotlin-complete/src/test/kotlin/nl/rabobank/kotlinmovement/recipes/test/util/RecipeAssert.kt)


--- 
![light-bulb](../../sources/png/light-bulb-xs.png)  
To create a `Pair` in Kotlin you can use the infix operator `to` e.g. `val pair = k to v `.
And like `data` classes you can destruct pairs (e.g. `val (k,v) = Pair("key","value")`).
---

## Covert test classes

1) Last but not least, convert the
   [test classes](../../../java-to-kotlin/src/test/java/nl/rabobank/kotlinmovement/recipes/CreateUpdateRecipesControllerTest.java)
2) The protected property `objectMapper` from RecipeMockMvcTest should be moved out of the companion object to the
   class. Using protected members which are not `@JvmStatic` in the superclass companion is unsupported yet
3) In `GetRecipesControllerTest` the property `initRecipeRequest` should become a non-nullable `lateinit var` 
4) In `GetRecipesControllerTest` replace the forEach on the Streams API with Kotlin's forEach 
5) Now all the code is converted to Kotlin, there is no need for `@JvmField` or other annotation that were used to
   enable interoperability with `Java`. Go back and remove them all
6) When ready, run all tests:

```shell
   (cd ../.. && ./mvnw clean verify)
```

[*peek solution*](../../../java-to-kotlin-complete/src/test/kotlin/nl/rabobank/kotlinmovement/recipes)

---
![light-bulb](../../sources/png/light-bulb-xs.png)
The `@DisplayName` annotation to describe the tests in a readable is not needed in Kotlin.
Place the function name between backticks and you can use spaces in the function name.

```Kotlin
fun `forever foo bar `() {
//
}
```
Static function like in a parameterized test,
you should either declare the function in the companion class of the test class with the annotation `@JvmStatic` on top of it, or
put the annotation `@TestInstance(TestInstance.Lifecycle.PER_CLASS)` on top of the test class.
---


Well done! Now, you can rename test source directory from `java` to `kotlin` and you are done!

[Go to the finish](../Finish.md)
