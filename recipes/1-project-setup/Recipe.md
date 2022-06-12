# Maven Setup Recipe

With this recipe we will configure maven. To check if the setup is right, we have added
a [test Kotlin class](../../src/main/kotlin/nl/rabobank/kotlinmovement/recipes/KotlinSetupTestDTO.kt) to the kotlin
source.  
If the Maven Kotlin configuration has been done correctly, you should be able to find the compiled class in the projects
target directory.

To complete this simple recipe, you just need a couple of things.

## Ingredients

- kotlin-stdlib library
- kotlin-maven-plugin

## Steps

1) Read about the [maven setup](MAVEN_SETUP.md) and configure maven accordingly
2) Build project:

```shell
   (cd ../.. && ./mvnw package)
   ```

4) You should be able to
   find [KotlinSetupTestDTO](../../target/classes/nl/rabobank/kotlinmovement/recipes/KotlinSetupTestDTO.class) in the
   build directory of the project

5) If the Maven configuration was set up correctly, continue to the next section.

[Go to next section](../2-domain-models/Recipe.md)
