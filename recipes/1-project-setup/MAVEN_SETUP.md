# Maven setup guide

This guide will describe step-by-step how to set up maven, so it will compile Kotlin next to the Java sources. 

## Dependencies

Add the Kotlin standaard library dependencies to the pom.  

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

## Compiler plugins

Add the Kotlin maven plugin with ``Spring`` & ``JPA`` plugins configured. 
The `Spring` plugin will make sure all spring annotated final classes are `open`.
And, because ``JPA`` classes have to have default constructors, ``jpa`` plugin is added.

Configure the Maven-compiler so that Kotlin-compiler is invoked before the Java-compiler.

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
        <!-- The maven compiler configured to invoke Kotlin compiler before the Java Compiler -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <executions>
                <!-- Replacing default-compile as it is treated specially by maven -->
                <execution>
                    <id>default-compile</id>
                    <phase>none</phase>
                </execution>
                <!-- Replacing default-testCompile as it is treated specially by maven -->
                <execution>
                    <id>default-testCompile</id>
                    <phase>none</phase>
                </execution>
                <execution>
                    <id>java-compile</id>
                    <phase>compile</phase>
                    <goals> <goal>compile</goal> </goals>
                </execution>
                <execution>
                    <id>java-test-compile</id>
                    <phase>test-compile</phase>
                    <goals> <goal>testCompile</goal> </goals>
                </execution>
            </executions>
        </plugin>
    </build>
    ...
</project>

````

To speed up the build enable the compiler plugin incremental compilation by adding this property:

````xml
<properties>
    <kotlin.compiler.incremental>true</kotlin.compiler.incremental>
</properties>
````

reference: 
[Compiler plugin docs](https://kotlinlang.org/docs/maven.html#jar-file)  

[Go back to the recipe](Recipe.md)
