# Maven setup guide

This guide will describe step-by-step how to set up maven, so it will enable Spring Boot Reactor, R2DBC and Kotlin
coroutines.

## Dependencies to be ADDED

Add the following dependencies to the [pom](../../../java-to-kotlin-complete/pom.xml) file:

```xml

<dependecies>
    <!-- Kotlin standard library -->
    <dependency>
        <groupId>org.jetbrains.kotlinx</groupId>
        <artifactId>kotlinx-coroutines-core</artifactId>
        <version>${kotlinx.coroutines.version}</version>
    </dependency>

    <dependency>
        <groupId>org.jetbrains.kotlinx</groupId>
        <artifactId>kotlinx-coroutines-reactor</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>

    <!-- Spring Data for R2DBC -->
    <dependency>
        <groupId>org.springframework.data</groupId>
        <artifactId>spring-data-r2dbc</artifactId>
    </dependency>

    <!-- R2DBC H2 database and driver-->
    <dependency>
        <groupId>io.r2dbc</groupId>
        <artifactId>r2dbc-spi</artifactId>
    </dependency>
    <dependency>
        <groupId>io.r2dbc</groupId>
        <artifactId>r2dbc-h2</artifactId>
    </dependency>

</dependecies>

```

## Dependencies to be REMOVED

Remove the following dependencies from the [pom](../../../java-to-kotlin-complete/pom.xml) file:

```xml

<dependecies>

    <!-- This will be replaced by the r2dbc-h2 library -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>

</dependecies>
```

[Go back to the recipe](Recipe.md)

