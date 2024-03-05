# Clean-up

Now we have to code completely migrated to non-blocking, we can remove the `JPA` dependency from our code base.

## Recipe

1) Remove the files:
   1) From the `service` package:
      1) [RecipesService](../../../java-to-kotlin-complete/src/main/kotlin/nl/rabobank/kotlinmovement/recipes/service/RecipesService.kt)
      2) [RecipesMapper](../../../java-to-kotlin-complete/src/main/kotlin/nl/rabobank/kotlinmovement/recipes/service/RecipesMapper.kt)
   2) From the `data` package:
      1) [IngredientsEntity](../../../java-to-kotlin-complete/src/main/kotlin/nl/rabobank/kotlinmovement/recipes/data/IngredientsEntity.kt)
      2) [RecipesEntity](../../../java-to-kotlin-complete/src/main/kotlin/nl/rabobank/kotlinmovement/recipes/data/IngredientsEntity.kt)
      3) [RecipesRepository](../../../java-to-kotlin-complete/src/main/kotlin/nl/rabobank/kotlinmovement/recipes/data/RecipesRepository.kt)
      4) [IngredientsRepository](../../../java-to-kotlin-complete/src/main/kotlin/nl/rabobank/kotlinmovement/recipes/data/IngredientsRepository.kt)
2) Replace the annotation `@EnableJpaRepositories` from the [Application](../../../java-to-kotlin-complete/src/main/kotlin/nl/rabobank/kotlinmovement/recipes/RecipesApplication.kt) class with `@EnableR2dbcRepositories`.
3) Remove the JPA dependency `spring-boot-starter-data-jpa` from the [pom.xml](../../../java-to-kotlin-complete/pom.xml) file.
4) Remove the `jpa` plugin from the configuration of the `kotlin-maven-plugin` in the [pom.xml](../../../java-to-kotlin-complete/pom.xml) file.
5) We are almost done! Now lets get the test running again.

[Go to next section](../6-test/Recipe.md)       


