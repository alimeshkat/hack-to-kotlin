# Project Setup

In order to make the `Recipe` service fully non-blocking we will have to change our project setup and change the code in
the repository.

## What changes?

1. Spring WebMVC (blocking) -> Spring Reactor (non-blocking)
2. JDBC  (blocking) -> R2DBC (non-blocking)
3. 

## Recipe

1) Read the [maven setup guide](MAVEN_SETUP_GUIDE.md) and configure the maven
   module [java-to-kotlin](../../../java-to-kotlin-complete/pom.xml) accordingly, and if you had started from the
   java-to-kotlin
   module continue from there (i.e. [java-to-kotlin](../../../java-to-kotlin/pom.xml)).
2) You can now **delete** the [db migration folder](../../../java-to-kotlin-complete/src/main/resources/db/migration)
3) Adjust the [application configuration](../../../java-to-kotlin-complete/src/main/resources/application.yaml) 
4) Build project:

```shell
   (cd ../../.. && ./mvnw clean package -pl :java-to-kotlin-complete)
```

5) The build will fail because of an issue with interoperability of R2DBC with JDBC. We will disable the R2DBC for now 
   1) To disable R2DBC, add an exclusion to the `@RecipeApplication` annotation in the [Application](../../../java-to-kotlin-complete/src/main/kotlin/nl/rabobank/kotlinmovement/recipes/RecipesApplication.kt) class:
    ```kotlin
      @SpringBootApplication(exclude = [R2dbcAutoConfiguration::class])
      @EnableJpaRepositories
      class RecipesApplication {}
    ```
6) The build should pass again and in the next section we will continue with the migration to R2DBC.

```shell
   (cd ../../.. && ./mvnw clean package -pl :java-to-kotlin-complete)
```

[Go to next section](../2-data/Recipe.md)