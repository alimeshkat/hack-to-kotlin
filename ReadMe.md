![](https://github.com/alimeshkat/hack-to-kotlin/actions/workflows/build.yml/badge.svg)

![](recipes/sources/png/HackToKotlinLogo.png)

We assume that you already have some basic knowledge about Kotlin, but you want to go beyond that and learn how to use
it with Spring Boot.
This workshop is here to help you with that.

*Note*: This project is created as part of the hands-on part of the hack-to-kotlin workshop. You may use it outside of the context of the workshop.

## About this workshop

During this workshop you will be migrating a good old Java Spring Boot Rest service to Kotlin by following recipes.
In the recipes we will touch upon:

- the best practices of using Kotlin,
- the Kotlin utilities you get out-of-the box
- the pitfalls of converting Java code straight to Kotlin,
- Kotlin's interoperability with Java
- neat Kotlin features
- and more!

## The Recipes

The recipes explain step-by-step what needs to be done to migrate the Java code.
Follow the recipes in the right order, and you will be done converting the Java code to Kotlin in no time!

1) [project setup](recipes/1-project-setup/Recipe.md)  
2) [domain](recipes/2-domain-models/Recipe.md)  
3) [data](recipes/3-data/Recipe.md)  
4) [application](recipes/4-application/Recipe.md)  
5) [controller](recipes/5-controller/Recipe.md)  
6) [service](recipes/6-service/Recipe.md)  
7) [test](recipes/7-test/Recipe.md)    
8) [finish](recipes/Finish.md)

## What this workshop is not

Please keep in mind that the goal of this workshop is not to explain Java, Spring Boot modules or any other tool or library, but to expand on
certain aspects that come in play when gradually
migrating a Java Spring Boot project to Kotlin. And be aware that this workshop does not cover what Spring offers! We want to give
you a taste of what is
possible and later extend it based on requests from the community. 
This workshop is not perfect! But with your help we can improve it.

---

## Recipe service

The Java Spring Boot project you are going to work on is called the `recipe-service`.
It's a simple REST service that offers crud operation for recipes.

### Api

The `recipe-service` exposes the following endpoints:

````
GET http://localhost:8080/recipes
GET http://localhost:8080/recipes/{id}
POST http://localhost:8080/recipes
PUT http://localhost:8080/recipes
DELETE http://localhost:8080/recipes/{id}
````

### libraries 

And it uses the following modules from Spring:

- Spring Web is used to create the rest endpoints
- Spring Data is used to handle the database communication and connections
- Spring boot validation for validating the beans.

The `Lombok` library is used for generating the constructors and the accessors (getters & setters).
And Java bean validation is used (JSR380) for validating the fields.

As for the database, an in-memory [H2](https://www.h2database.com/html/main.html) database will run.
For creating the database tables, [flyway](https://flywaydb.org/documentation/getstarted/how) migration
tool is run during the startup.
The migration schema can be found [here](recipe-java/src/main/resources/db/migration/V1_0__recipes.sql)

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



In some recipes we have added a section with supplement information about a specific topic.
These section you can recognize by the bulb icon ![](recipes/sources/png/light-bulb-xs.png).  

---

## The Answers to the Recipes

Last thing, we have created a second modules called [recipe-kotlin](recipe-kotlin) with the converted code.
Feel free to compare your code with ours.

**Good luck and have fun!**

## Feedback

Let us know what you think about this workshop by filling in this [form](https://forms.gle/NYLUQQYk4YKRGB5DA).
