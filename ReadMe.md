![](https://github.com/alimeshkat/hack-to-kotlin/actions/workflows/build.yml/badge.svg) [![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin)

![](recipes/sources/png/HackToKotlinLogo.png)

## About this project

This project is created to help Java developers to learn how to use Kotlin with Spring Boot. 
It contains a set of `recipes` that will guide you through the process of converting a Java application to Kotlin and making it non-blocking.

**We assume that you already have some basic knowledge of Kotlin, but want to go beyond that and learn how to use it
with the Spring Boot framework. This project is here to help you with that. The goal of this project was not to explain the various Spring Boot modules.**

## The Recipes

### Java to Kotlin

The first set of recipes explain step-by-step what needs to be done to migrate a Java Spring Boot project to Kotlin. Follow the
recipes  [here](recipes/1.java-to-kotlin/0-intro) in the correct order, and you will be able to convert the Java code to
Kotlin in no time!

#### Goals:

- Best practices of using Kotlin
- Kotlin's utilities available out-of-the-box
- Pitfalls of converting Java code directly to Kotlin
- Kotlin's interoperability with Java
- Neat Kotlin features
- And more!

### Blocking to non-blocking

In the second set of recipes we will refactor our blocking application to a non-blocking one. We will use `Kotlin Coroutines` and `Spring Reactor` to achieve this.
Follow the recipes [here](recipes/2.blocking-to-non-blocking/0-intro) in the correct order, and learn how to convert a blocking application to a non-blocking one!

#### Goals:

- Using Kotlin Coroutines with Spring Boot
- Bridging the gap between imperative and reactive programming models

---

## Installation

Install the following software:

- Java 21
- Maven v3
- IntelliJ
- Git

Fork this project and clone it. You can also directly clone this project and keep your changes locally.

Run mvn verify command to test the installation:

```shell 
./mvnw clean verify
```

---

In some recipes we have added a section with supplement information about a specific topic.
These section you can recognize by the bulb icon ![](recipes/sources/png/light-bulb-xs.png).

---

## Feedback

Let us know what you think about this workshop by filling out this [form](https://forms.gle/NYLUQQYk4YKRGB5DA).

