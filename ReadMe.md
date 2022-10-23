![](recipes/sources/png/HackToKotlinLogo.png)

We assume that you already have some basic knowledge about Kotlin, but you want to go beyond that and learn how to use
it with Spring Boot.
This workshop is here to help you with that.

While converting a good old Java Spring Boot Rest service to Kotlin, with Lombok, JPA and other populair libraries,
you will learn how to go about a gradual migration from Java to Kotlin. But this workshop is more than just converting the Java code to Kotlin,
we will discuss

- best practices of using Kotlin,
- the utilities that you get out-of-the box when using Kotlin
- pitfalls of converting Java code straight to Kotlin,
- Kotlin's interoperability with Java
- some advanced Kotlin features
- and more!

You will start with converting simple POJO's, and move up to somewhat more complicated and interested parts of the
application where pragmatic, modern language that Kotlin is can help you make this look simple!

## What this workshop is not

Please keep in mind that the goal of this workshop is not to explain Java, Spring Boot modules or Maven, but to expand on
certain aspects that come in play when gradually
migrating a Java Spring Boot project to Kotlin. And be aware that this workshop does not cover what Spring offers! We want to give
you a taste of what is
possible and later extend it based on requests from the community. 
This workshop is not perfect! But with your help we can improve it.

---

## Project setup

The project you are going to work on is called `Recipe API` and resides in the [recipe-java](recipe-kotlin) module.
`Recipe Api` is using the following modules from Spring:

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

## The Recipes

To guid you with the migration we have created `recipes` that tells you step-by-step what have to be done.
Follow the recipes in the right order, and you will convert this project to Kotlin in no time!

1) [project setup](recipes/java-to-kotlin/1-project-setup/Recipe.md)  
2) [domain](recipes/java-to-kotlin/2-domain-models/Recipe.md)  
3) [data](recipes/java-to-kotlin/3-data/Recipe.md)  
4) [application](recipes/java-to-kotlin/4-application/Recipe.md)  
5) [controller](recipes/java-to-kotlin/5-controller/Recipe.md)  
6) [service](recipes/java-to-kotlin/6-service/Recipe.md)  
7) [test](recipes/java-to-kotlin/7-test/Recipe.md)    
8) [finish](recipes/java-to-kotlin/Finish.md)

In some recipes we have added a section with supplement information about a specific topic.
These section you can recognize by the bulb icon ![](recipes/sources/png/light-bulb-xs.png).  

---

## The Answers to the Recipes

Last thing, we have created a second modules called [recipe-kotlin](recipe-kotlin) with the converted code.
Feel free to compare your code with ours.

**Good luck and have fun!**

## Feedback

Let us know what you think about this workshop by filling in this [form](https://forms.gle/NYLUQQYk4YKRGB5DA).
