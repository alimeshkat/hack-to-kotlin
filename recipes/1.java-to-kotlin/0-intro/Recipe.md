# Intro: Java to Kotlin!

It's time to convert the `Java` code to `Kotlin`! In this section, we will convert the `Java` code to `Kotlin` and make
some small adjustments to the `Kotlin` code so that it's idiomatic `Kotlin` code.

# Objectives
- Learn about how to gradually convert `Java` code to `Kotlin`
- Learn how to convert `Java` code to `Kotlin`, what are the pitfalls and how to avoid them
- Learn about the pitfalls of using JPA (Hibernate) with Kotlin `data` classes
- Learn about the awesome `Kotlin` features

## Recipes

with help of recipes, we will guide you step-by-step through the migration of the `java-to-kotlin` module to `Kotlin`.
Follow the recipes in the right order, and you will be done in no time!

1) [project setup](../1-project-setup/Recipe.md)
2) [domain](../2-model/Recipe.md)
3) [data](../3-data/Recipe.md)
4) [application](../4-application/Recipe.md)
5) [controller](../5-controller/Recipe.md)
6) [service](../6-service/Recipe.md)
7) [test](../7-test/Recipe.md)
8) [finish](../Finish.md)

# Recipe service

The Java Spring Boot project you are going to work on is called the `recipe-service`, it resides in the `java-to-kotlin` module.
It's a simple REST service that offers crud operation for recipes.

## Api

The `recipe-service` exposes the following endpoints:

````
GET http://localhost:8080/recipes
GET http://localhost:8080/recipes/{id}
POST http://localhost:8080/recipes
PUT http://localhost:8080/recipes
DELETE http://localhost:8080/recipes/{id}
````

You can start the application by running the following command from the root:

```shell
  (cd ../../.. && ./mvnw package -pl :java-to-kotlin)
```

An embedded Tomcat server will start on port ``8080``.

### Peek solution
When you feel stuck, you can always check out the answers by peeking the solution.

### Nice-To-Know
In most of the recipes we have added a `nice-to-know` section, to provide you with some additional information about some topics.
These section you can recognize by the bulb icon ![](/recipes/sources/png/light-bulb-xs.png).
