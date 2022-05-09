# Maven Setup Recipe

This recipe will explain how to configure maven, so it would compile Java & Kotlin code and sources.

Ingredients:

- kotlin-stdlib library
- kotlin-maven-plugin

1) Follow the steps described in the [maven setup guide](MAVEN_SETUP_GUIDE.md)
2) run Maven package command to check if the Kotlin class ``KotlinSetupTestDTO`` has been compiled
```shell
   (cd ../.. && ./mvnw package)
   ```
4) You should be able to find [KotlinSetupTestDTO](../../target/classes/nl/rabobank/kotlinmovement/recipes/KotlinSetupTestDTO.class) in the build directory of the project

[Go to next section](/TODO)
