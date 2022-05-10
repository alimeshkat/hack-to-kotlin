# Data Classes

With `Data Classes` you can declare in one line your data transfer objects (DTO's).
A `Data Class` has for all its properties an implementation of utility functions:

- equals and hashcode
- toString
- componentN() - for destructuring of an object into a number of variables
- copy - copy and change field from an object into a new object

A `Data Class` cannot be `abstract`, `open`, `sealed`, or `inner`, and should have at least one property declared via
the primary constructor.

## Example

Take a look at the `PersonDTO` data class. It has two properties, `name` and `sureName`. Property `name` is read only
because, and property `sureName` can be overwritten. Note that `sureName` is an optional, nullable property with a
default
value `null`.

```Kotlin

data class PersonDTO(val name: String, var sureName: String? = null)

```

The `PersonDTO` object can be constructed the following ways:

```Kotlin

val me = PersonDTO("Morty", "Meson")
val you = PersonDTO("Rick")
val someone = PersonDTO("Jerry", null)

you.sureName = "Smith" //allowed
you.name = "Beth" //not allowed


```

And, in case when instantiated in a Java file the following ways:

```Java

PersonDTO me=new PersonDTO("Morty");
        PersonDTO me=new PersonDTO("Morty",null);
        PersonDTO you=new PersonDTO("Rick","Sanchez");

        me.setSureName("Smith") // a setter is available for the mutable field

```

Data classes are destructible:

```Kotlin
val (name, sureName) = person
```

[Go back to the recipe](Recipe.md)





