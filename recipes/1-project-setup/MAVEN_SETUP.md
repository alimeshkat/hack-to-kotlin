# Maven setup guide

This guide will describe step-by-step how to set up maven, so it will compile Kotlin next to the Java sources. 

## Dependencies

First, we will add the Kotlin standaard library to our dependencies.  

````xml
<project>
    ...
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
        <dependency>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-reflect</artifactId>
            <version>${kotlin.version}</version>
        </dependency>
    </dependencies>
    ...
</project>
````    

## Plugin

Next we will add the Kotlin maven plugin. We will add some additional configuration to the Kotlin Maven plugin for ``Spring`` & ``JPA``. 
Because Spring annotated classes (``@Configuration`` or ``@Service``) should be non-final (open), and in Kotlin all classes are by default final.
To ``open`` the Spring specific classes, we will add  ``spring`` plugin. 
And, because ``JPA`` classes need a default constructor to be instantiated, we will add ``jpa`` plugin that generates no-arg constructors.
Beneath here you can find a fully configured Kotlin maven plugin that will compile Java & Kotlin sources, and provide support for `Spring` & `JPA`. 

````xml
<project>
    ...
    <build>
        <plugin>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-maven-plugin</artifactId>
            <version>${kotlin.version}</version>
            <executions>
                <execution>
                    <id>compile</id>
                    <phase>process-sources</phase>
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
    ...
</project>

````

[Go back to the recipe](Recipe.md)
