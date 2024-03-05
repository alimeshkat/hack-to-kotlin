# Maven Setup

Our first recipe is about configuring the maven module [java-to-kotlin](../../../java-to-kotlin).
Usually IntelliJ can do it for you automatically.
When adding a Kotlin file (.kt) to the Java project for the first time,
you will receive a notification to configure Kotlin in one of the modules.
But because it's important to understand a little what you have to add/configure, we will go through the steps to configure Kotlin in a Java module.

## Recipe

1) Read the [maven setup guide](MAVEN_SETUP_GUIDE.md) and configure the maven module [java-to-kotlin](../../../java-to-kotlin/pom.xml)
   accordingly
2) Build project:

```shell
   (cd ../../.. && ./mvnw clean package -pl :java-to-kotlin)
```

3) To check if the setup is correct, see if `KotlinSetupTestDTO` is compiled as well. 
   The class should be [here](../../../java-to-kotlin/target/classes/nl/rabobank/kotlinmovement/recipes/KotlinSetupTestDTO.class)

[*peek solutions*](../../../java-to-kotlin-complete/pom.xml)  

Note: *You cannot just replace this module's pom with the one from `java-to-kotlin-complete`. Be mindful of the differences. 
For example: the Java module depends on Lombok, that has been removed from the kotlin module.*

[Go to next section](../2-model/Recipe.md)
