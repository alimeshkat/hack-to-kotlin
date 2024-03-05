![](https://github.com/alimeshkat/hack-to-kotlin/actions/workflows/build.yml/badge.svg) [![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin)

![](recipes/sources/png/HackToKotlinLogo.png)

**We assume that you already have some basic knowledge of Kotlin, but want to go beyond that and learn how to use it
with
the Spring Boot framework. This workshop is here to help you with that.**

Note: *This project was created as part of the hands-on part of the hack-to-kotlin workshop. You may use it outside the
context of the workshop.*

## About this workshop

In this workshop, you will start by migrating a Java Spring Boot Rest service to Kotlin by following recipes.
In these recipes, we will cover:

- Best practices of using Kotlin
- Kotlin utilities available out-of-the-box
- Pitfalls of converting Java code directly to Kotlin
- Kotlin's interoperability with Java
- Neat Kotlin features
- And more!

## What this workshop is not

Please keep in mind that the goal of this workshop is not to explain Java, Spring Boot modules, or any other tool or
library, but rather to expand on certain aspects that come into play when gradually migrating a Java Spring Boot project
to Kotlin. Also, please note that this workshop does not cover what Spring offers. We want to give you a taste of what
ispossible and later extend it based on requests from the community.
This workshop is not perfect, but with your help,
we can improve it.

## The Recipes

### Start converting Java-To-Kotlin

The first recipes in this workshop explain step-by-step what needs to be done to migrate the Java code. Follow the
recipes  [here](recipes/1.java-to-kotlin/Intro.md) in the correct order, and you will be able to convert the Java code to
Kotlin in no time!

---

## Installation

Install the following software:

- Java 21
- Maven v3
- IntelliJ
- Git

Fork this project and clone it. You can also directly clone this project and keep your changes locally.

Run `maven verify` command to test the installation:

```shell 
./mvnw clean verify
```

---

In some recipes we have added a section with supplement information about a specific topic.
These section you can recognize by the bulb icon ![](recipes/sources/png/light-bulb-xs.png).

---

## Feedback

Let us know what you think about this workshop by filling out this [form](https://forms.gle/NYLUQQYk4YKRGB5DA).

