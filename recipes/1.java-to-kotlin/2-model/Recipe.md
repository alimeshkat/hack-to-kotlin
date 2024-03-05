# Model

In this recipe we will help you convert
the [recipe models](../../../java-to-kotlin/src/main/java/nl/rabobank/kotlinmovement/recipes/model) to
kotlin. To keep the Java files concise, we have used `Lombok` library annotations to generate the `getters`, `setters`
, `contructor`, `equals` and `hashcode`. At the end you, when all `Java` has been converted, we can simply remove
dependency on Lombok.

## Recipe

1) Read the document about [converting Java file to Kotlin](CONVERT_JAVA_FILE_TO_KOTLIN.md)
2) Convert the classes in `model` package to Kotlin.
3) Make sure the properties are:
    1) `nullable` (e.g. name:String?) were it's needed, like the request object properties. Remove '= null' when
       possible.
    2) declared in the primary constructor
    3) **public** (without any modifier)
    4) and, immutable (`val`)
4) The bean validation annotations should be on the field e.g. `@field:NotBlank() val name: String` and not **on the
   type**
   e.g. `val name: @field:NotNull String`! The Kotlin converter is not smart enough to understand that yet.
5) Convert recipe models to `data` Classes.
6) For example, the `RecipeRequest` class should look like this after the conversion and refactoring:
    ```Kotlin
     data class RecipeRequest(
        @field:NotBlank(message = "recipeName")
        val recipeName: String?,
        @field:NotEmpty(message = "ingredients") @field:Valid
        val ingredients: MutableSet<IngredientRequest>?
    )
    ```
7) Clean-up Lombok annotations e.g. `@Data`
8) When ready, run all tests:

```shell
   (cd ../../.. && ./mvnw clean package -pl :java-to-kotlin)
 ```

[*peek solutions*](../../../java-to-kotlin-complete/src/main/kotlin/nl/rabobank/kotlinmovement/recipes/model)

--- 
![light-bulb](../../sources/png/light-bulb-xs.png)  
The [Data class](https://kotlinlang.org/docs/data-classes.html) in Kotlin is quit powerful. It generates a `copy`
function which can be handy when working with immutable objects, next to the `equals()` & `hashCode()` functions.
Another cool thing about the `Data` class is, you can `destruct` it. Let's see how that looks:

```kotlin
class Box(val length: Int, val width: Int, val depth: Int)
val (length, width, depth) = Box(10, 10, 10) //destructed

listOf<Box>(Box(10, 10, 11), Box(11, 11, 12)).forEach { length, width, depth -> //destructed in a lambda
    println("l=$length w=$width, d=$depth")
}
```

*Note*: The compiler uses the order of the properties in the constructor to assign the values to the destructed
variables. So if the order of the properties changes, that can result in a reference with an incorrect value.

---

[Go to next section](../3-data/Recipe.md)



