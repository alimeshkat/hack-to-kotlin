# Convert Java File To Kotlin

Converting a Java class, a Java package or a java project can be done in one click. You can do that in different ways:

- Right-click on a Java file/package/project -> `Convert Java File to Kotlin File`
- Open a Java file and press `CTRL+ALT+Shift+K`
- Or with a Java file open, go to the main menu and select `Code`-> `Convert Java File to Kotlin File`

## Clean-up Tips

Converting of Java files to Kotlin is easy, but the generated code is not always the best code! It's important to review and refactor the generated code right after the conversion.
We have to make the code more idiomatic to Kotlin. To help you out with cleaning up the generated code you can make use of the following tips:

- All fields are made nullable by the converter. Dealing with nullable types is
  easy, but it still makes the code more complex.
  So, be mindful about when using a nullable type.
- When you see bang-bang (`!!`) used by the converter, get rid of it!!
  First check, should the reference be of a nullable type? If so, call the member on the nullable reference with the safe call (
  `?.`) and use the elvis operator (`?:`) to handle the `null` scenario.
- Favor read only properties (`val`).
- Favor immutable objects over mutable objects. This means we should avoid 
  setters when possible and use contructors to create objects (and set all values).

[Go back to the recipe](Recipe.md)
