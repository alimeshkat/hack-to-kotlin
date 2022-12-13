# Convert Java File To Kotlin

Converting a Java class, a Java package or a java project can be done in one click. You can do that in different ways:

- Right-click on a Java file/package/project -> `Convert Java File to Kotlin File`
- Open a Java file and press `CTRL+ALT+Shift+K`
- Or with a Java file open, go to the main menu and select `Code`-> `Convert Java File to Kotlin File`

## Clean-up Tips

**Converting of Java files to Kotlin is easy, but the generated code is not always the best code!** It's important to
review and refactor the generated code right after the conversion.
We have to make the code more idiomatic to Kotlin. To help you out with cleaning up the generated code you can make use
of the following tips:

- All properties, return types and arguments from Java are made nullable by the converter. Dealing with nullable types
  is
  easy, but it still makes the code more complex. That's why we have used the
  annotation `org.jetbrains.annotations.NotNull` to give
  hints to the converter about what should be non-nullable in Kotlin.
- Even then, the converter can have issues with converting members that are generated with Lombok. But you can
  de-lombok the classes first with help of the Lombok plugin in IntelliJ, clean up the class and then convert the class
  to Kotlin. De-lomboking can be achieved by installing the Lombok plugin first and
  after that `right-click on a Java file` -> `refactor` -> `Delombok` -> `All Lombok annotations`
- When you see bang-bang (`!!`) used by the converter, get rid of it!!
  First check, should the reference be of a nullable type? If so, call the member on the nullable reference with the
  safe call (
  `?.`) and use the elvis operator (`?:`) to handle the `null` scenario
- Favor immutable objects over mutable objects
    - make properties and variables `val` or `const val` (in case of constants)
    - avoid setters when possible and use constructors to create objects.
    - use List<T>, Set<T>, Map<T> instead of the immutable counterpart ImmutableList<T>, ImmutableSet<T>,
      ImmutableMap<T>.

[Go back to the recipe](Recipe.md)
