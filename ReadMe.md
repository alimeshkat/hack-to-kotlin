# Hack to Kotlin

Let's learn how to use Kotlin with Spring Boot!  
We will do that while migrating a good old Java Spring Boot WebMvc project with Lombok to Kotlin.  
Bottom-up we will convert the Java code to Kotlin code. 
After each refactoring we run the test suite to check if everything still is working as expected.

Before you start, read the following documents to get yourself familiar with this project:
- [Recipe Api](docs/SETUP_AND_DESIGN.md)
- [Overal setup and design](docs/SETUP_AND_DESIGN.md)
- [Installation](docs/INSTALLATION_GUIDE.md)

*Migration steps*

The migration of the code can be done in small steps. 

1) We should configure maven, so it will compile the kotlin resources and code. Follow the [Maven setup guide](docs/MAVEN_SETUP_GUIDE.md).

