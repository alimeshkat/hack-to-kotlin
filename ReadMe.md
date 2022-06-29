# Hack to Kotlin

Let's learn how to use Kotlin with Spring Boot!
We will do that while converting a good old Java Spring Boot Rest service, with Lombok, to Kotlin.  
The goal of this workshop is not to learn Spring Boot modules or Maven, but how to use Kotlin with Spring Boot.

## Approach

We will start with converting simple POJO's, and move up to somewhat more complicated
parts of the project containing the business logic.
Converting the `Recipe API` happens in parts,
each part has a `recipe` that will provide you with the necessary information about how to convert that particular part of the project.
Follow the recipes in the following order, and convert this project to Kotlin!

1) [project setup](recipes/1-project-setup/Recipe.md)
2) [domain](recipes/2-domain-models/Recipe.md)
3) [data](recipes/3-data/Recipe.md)
4) [controller](recipes/4-controller/Recipe.md)
5) [service](recipes/5-service/Recipe.md)
6) [test](recipes/6-test/Recipe.md)

## Recipe API

The project we like you to convert to Kotlin is called `Recipe API`.
Recipe Api is powered by Spring Boot:

- Spring Web is used to create the rest endpoints
- Spring Data is used to handle the database communication and connections
- Spring boot validation for validating the beans.

`Lombok` is used for generating the constructors and the accessors in classes.
And, for validating the incoming request bean validation is uses (JSR380).

As for the database, an in-memory [H2](https://www.h2database.com/html/main.html) database will run.
For creating the database tables, [flyway](https://flywaydb.org/documentation/getstarted/how) migration
tool is run during the startup.
The migration schema can be found [here](src/main/resources/db/migration/V1_0__recipes.sql)

Recipe API has the following endpoints:

````
GET http://localhost:8080/recipes
GET http://localhost:8080/recipes/{id}
POST http://localhost:8080/recipes
PUT http://localhost:8080/recipes
DELETE http://localhost:8080/recipes/{id}
````

## Installation

Please have the following software installed:

- Java 11
- Maven v3
- IntelliJ

To test if everything is working execute ``mvn verify`` from the root of the project. 
This will build the jar and run all tests. You can start the application by running the following command:

```shell
./mvnw spring-boot:run
```
An embedded Tomcat server will start on port ``8080``.
