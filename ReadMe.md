# Hack to Kotlin

Let's learn how to use Kotlin with Spring Boot!
We will do that while converting a good old Java Spring Boot Rest service, with Lombok, to Kotlin.  
The goal of this workshop is not to learn Spring Boot modules or Maven, but how to use Kotlin with Spring Boot.

So we expect you have basic knowledge of:

- Java
- Bean validation (JSR 380)
- Lombok
- Spring Boot modules (Spring Web & Spring Data)
- Maven

*Approach*

We will start with converting simple POJO's, and move up to somewhat more complicated
parts of the project containing the business logic.

Converting the project will happen in steps, steps are ordered in sections.
So a section contains one or more recipes that provide the necessary information for completing it.

Complete the recipes in the sections in this order

1) [project setup](docs/1-project-setup/Recipe.md)
2) [domain](docs/2-domain-models/Recipe.md)
3) [controller](docs/3-controller/Recipe.md)
4) [util](docs/4-util/Recipe.md)
5) [data](docs/5-data/Recipe.md)
6) [service](docs/6-service/Recipe.md)
7) [test](docs/7-test/Recipe.md)

## Setup & dependencies

Recipe Api is powered by Spring Boot:

- Spring Web is used to create the rest endpoints
- Spring Data is used to handle the database communication and connections
- Spring boot validation for validating the beans.

`Lombok` is used for generating the constructors and the accessors in classes.
And, for validating the incoming request bean validation is uses (JSR380).

As for the database, an in-memory [H2](https://www.h2database.com/html/main.html) database will run.
For creating the database tables, [flyway](https://flywaydb.org/documentation/getstarted/how) migration
tool is run during the startup.
The migration schema can be found [here](../src/main/resources/db/migration/V1_0__recipes.sql)

## Installation

*Pre-requisite*

- Java 11
- Maven v3

*Build*  
Execute ``mvn verify`` from the root of the project to build the jar and run all tests.

*Run*  
Execute the following command to run the project. An embedded Tomcat server will start on port ``8080``.

```shell
cd ..
./mvnw spring-boot:run
```

## Recipe API

The resource of this API is ``Recipe``. The ``Recipe`` entity has a ``name`` and a list of ``ingredients`` as its
attributes.
An ingredient has a ``name``,  ``weight`` an a ``type``. The ``type`` can be either ``WET`` or ``DRY``.

Endpoints:

````
GET http://localhost:8080/recipes
GET http://localhost:8080/recipes/{id}
POST http://localhost:8080/recipes
PUT http://localhost:8080/recipes
DELETE http://localhost:8080/recipes/{id}
````

Example request for creating/updating a recipe:

````json

{
  "recipeName": "Pizza Dough",
  "ingredients": [
    {
      "name": "Water",
      "type": "WET",
      "weight": 8000
    },
    {
      "name": "Flower",
      "type": "DRY",
      "weight": 1000
    },
    {
      "name": "Yeast",
      "type": "DRY",
      "weight": 2
    },
    {
      "name": "Salt",
      "type": "DRY",
      "weight": 20
    }
  ]
}

````

And the response you will get back:

````json
{
  "id": 1,
  "recipeName": "Pizza",
  "ingredients": [
    {
      "id": 4,
      "name": "Water",
      "type": "WET",
      "weight": 8000
    },
    {
      "id": 2,
      "name": "Flower",
      "type": "DRY",
      "weight": 1000
    },
    {
      "id": 1,
      "name": "Yeast",
      "type": "DRY",
      "weight": 2
    },
    {
      "id": 3,
      "name": "Salt",
      "type": "DRY",
      "weight": 20
    }
  ]
}
