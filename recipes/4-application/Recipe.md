# Application

The main method in the application class represents the entry point of our application. After converting it to
Kotlin, you will notice some small changes. For example, the `main()` function can be defined outside the
with `@application`
annotated class (functions are first class citizens). So, let's get into it.

## Convert RecipesApplication

1) Convert
   the [RecipesApplication class](../../recipe-java/src/main/java/nl/rabobank/kotlinmovement/recipes/RecipesApplication.java)
   .
2) To make it more Kotlin like, let's move the `main` function to the outside of the class and place it on the same
   level as the `class`. The generated `@JvmStatic` can be removed now.
3) Replace `SpringApplication.run(RecipesApplication::class.java, *args)` by the Springs own Kotlin extension
   function `runApplication<RecipesApplication>(*args)`
4) And change class type of the `RecipesApplication` which was `object` to `class` *otherwise it won't be able to do
   Spring stuff!!*
5) When ready, run all tests:

```shell
   (cd ../.. && ./mvnw clean verify)
   ```
--- 
![light-bulb](../sources/png/light-bulb-xs.png)  
The `runApplication()` is an [inline function](https://kotlinlang.org/docs/inline-functions.html) which means the function block and it's arguments can be inlined where it's called (call-site).
This can be useful when working with higher-order-functions for performance reasons, and or functions with type parameter (generic types) to circumvent type arasur.
---

[Go to next section](../5-controller/Recipe.md)
