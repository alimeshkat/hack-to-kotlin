# Hack to Kotlin

Let's learn how to use Kotlin with Spring Boot!
We will start with the most common scenario, with a good old Java Spring Boot WebMvc project with lombok, and continue from there.
Bottom-down we will convert the Java code to Kotlin and after each refactoring we will run the test suite.

Let's take a look at the design:

TODO add uml here 


## Installation

*Pre-requisite*
- Java 11 
- Maven

*Build*
Execute ``mvn verify`` from the root of the project to build the jar and run all tests.

*Run*
Execute ``java -jar target/recipes.jar`` from the root of the project. An embedded Tomcat server will start on port ``8080``.

## API

``````curl

//Get 
curl http://localhost:8080/pizza-recipes
TODO 
//POST
//PATCH
//DELETE

``````

## Migration guide

Steps to convert Java Spring Boot project to Kotlin:

1) Add maven Kotlin configuration:

``````xml

<properties>
    <kotlin.version>1.6.20</kotlin.version>
</properties>
        <!--Add Kotlin standard library-->
<dependencies>
<dependency>
    <groupId>org.jetbrains.kotlin</groupId>
    <artifactId>kotlin-stdlib</artifactId>
    <version>${kotlin.version}</version>
</dependency>
</dependencies>

<build>
 <plugin>
    <groupId>org.jetbrains.kotlin</groupId>
    <artifactId>kotlin-maven-plugin</artifactId>
    <version>${kotlin.version}</version>
    <executions>
        <execution>
            <id>compile</id>
            <goals>
                <goal>compile</goal>
            </goals>
            <configuration>
                <jvmTarget>11</jvmTarget>
                <sourceDirs>
                    <sourceDir>${project.basedir}/src/main/kotlin</sourceDir>
                    <sourceDir>${project.basedir}/src/main/java</sourceDir>
                </sourceDirs>
            </configuration>
        </execution>
        <execution>
            <id>test-compile</id>
            <goals>
                <goal>test-compile</goal>
            </goals>
            <configuration>
                <jvmTarget>11</jvmTarget>
                <sourceDirs>
                    <sourceDir>${project.basedir}/src/test/kotlin</sourceDir>
                    <sourceDir>${project.basedir}/src/test/java</sourceDir>
                </sourceDirs>
            </configuration>
        </execution>
    </executions>
    <configuration>
        <compilerPlugins>
            <plugin>spring</plugin>
            <plugin>jpa</plugin>
        </compilerPlugins>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-maven-allopen</artifactId>
            <version>${kotlin.version}</version>
        </dependency>
        <dependency>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-maven-noarg</artifactId>
            <version>${kotlin.version}</version>
        </dependency>
    </dependencies>
 </plugin>
</build>

``````

3) Convert the domain entities
4) Convert the dto's
5) Convert the models
6) Convert the service layer
7) Convert the controller layer
8) Convert tests

