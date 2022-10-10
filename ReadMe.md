![](recipes/sources/png/HackToKotlinLogo.png)

We assume that you already have some basic knowledge about Kotlin, but you want to go beyond that and learn how to use
it with Spring Boot.
This workshop is here to help you with that.

While converting a good old Java Spring Boot Rest service to Kotlin, with Lombok, JPA and other populair libraries,
you will learn what will be different in Kotlin. We will go beyond just converting the code and discuss best practices
and pitfalls.
We will start with converting simple POJO's, and move up to somewhat more complicated and interested parts of the
application.
Where pragmatic modern language that Kotlin can really shine!

*Please keep in mind that the goal of this workshop is not to learn Spring Boot modules or Maven, but how to gradually
migrate a Java Spring Boot project to Kotlin. And we are aware that not all Spring offers is covered! We want to give you a taste of what is
possible and later expend on it based on requests from the community. See this as a work in progress and help us improve it.*

---

## Project setup

The project you are going to work on is called `Recipe API`.
Recipe Api is using Spring Boot and the following modules:

- Spring Web is used to create the rest endpoints
- Spring Data is used to handle the database communication and connections
- Spring boot validation for validating the beans.

`Lombok` is used for generating the constructors and the accessors in classes.
And, for validating the incoming request bean validation is uses (JSR380).

As for the database, an in-memory [H2](https://www.h2database.com/html/main.html) database will run.
For creating the database tables, [flyway](https://flywaydb.org/documentation/getstarted/how) migration
tool is run during the startup.
The migration schema can be found [here](recipe-java/src/main/resources/db/migration/V1_0__recipes.sql)

Recipe API has the following endpoints:

````
GET http://localhost:8080/recipes
GET http://localhost:8080/recipes/{id}
POST http://localhost:8080/recipes
PUT http://localhost:8080/recipes
DELETE http://localhost:8080/recipes/{id}
````

---

## Installation

Please have the following software installed:

- Java 11
- Maven v3
- IntelliJ

To test if everything is working execute

```shell 
./mvnw verify
```

This will build the jar and run all tests. You can start the application by running the following command:

```shell
./mvnw spring-boot:run
```

An embedded Tomcat server will start on port ``8080``.

---

## Recipes

To guid you with the migration we have created `recipes` that tells you step-by-step what have to be done.
Follow the recipes in the right order, and you will convert this project to Kotlin in no time!

1) [project setup](recipes/1-project-setup/Recipe.md)
2) [domain](recipes/2-domain-models/Recipe.md)
3) [data](recipes/3-data/Recipe.md)
4) [application](recipes/4-application/Recipe.md)
5) [controller](recipes/5-controller/Recipe.md)
6) [service](recipes/6-service/Recipe.md)
7) [test](recipes/7-test/Recipe.md)

In some recipes we have added a section that provides you with additional information about a specific topic.
These section you can recognize by the bulb icon ![](recipes/sources/png/light-bulb-xs.png).
---

## Answers

Last thing, we have created a second modules called [recipe-kotlin](recipe-kotlin) with the converted code.
Feel free to compare your code with ours.

**Good luck and have fun!**
