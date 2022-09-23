# Data Recipe

The [data](../../recipe-java/src/main/java/nl/rabobank/kotlinmovement/recipes/data) package has two JPA `entities` and
two `repositories`.
As we have configured `JPA` in the `kotlin-maven-plugin`, only thing we have to do is convert the classes and make some
small adjustments to the
Kotlin code.

## Convert Entities & Repositories

1) Convert the `data` package to Kotlin
2) Refactor the `Entity` classes the following way:
    1) Don't use `data` classes for JPA `entities`, because `entities` are not just regulair DTO's. `Entities` need to
       adhere to certain requirement to function properly. Read more about
       it [here](https://www.jpa-buddy.com/blog/best-practices-and-common-pitfalls/)
    2) Add the `entity` properties to the default constructor
    3) Make all properties immutable (`val`)
    4) Make `id` and `ingridients` nullable
3) There is not much to change in the `Repositories` class, just make the `generic-types` of the
   `JpaRepository` not nullable.
4) When ready, run all tests:

```shell
   (cd ../.. && ./mvnw clean verify)
   ```

5) If all tests have passed, continue to the next recipe.


<span style="color:green">**_Tip_:**</span> **check if you didn't miss any warnings from IntelliJ ;)**

![warning](warning.png)  
[Go to next section](../4-application/Recipe.md)
