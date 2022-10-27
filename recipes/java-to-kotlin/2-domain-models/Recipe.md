# Domain Model Recipe

In this recipe we will convert
the [recipe models](../../../java-to-kotlin/src/main/java/nl/rabobank/kotlinmovement/recipes/model) to
kotlin. To keep the Java files concise, we have used `Lombok` library annotations to generate the `getters`
, `contructor`, `equals` and `hashcode`.

## Convert domain models

1) Convert the classes in `model` package to Kotlin (Read the document
   about [converting Java file to Kotlin](CONVERT_JAVA_FILE_TO_KOTLIN.md)).
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



