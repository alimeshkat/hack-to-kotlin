# Hack to Kotlin

Let's learn how to use Kotlin with Spring Boot!
We will start with the most common scenario, with a good old Java Spring Boot WebMvc project with lombok, and continue
from there.
Bottom-up we will convert the Java code to Kotlin and after each refactoring we will run the test suite.

The code base we want you to migrate to Kotlin is an REST API called ``Recipe Api``.   
Recipes can be retrieved, created, updated and removed via the available rest endpoint of the API.

Before you start, read the following documents to get yourself familiar with the project:
- [Recipe Api](docs/SETUP_AND_DESIGN.md)
- [Overal setup and design](docs/SETUP_AND_DESIGN.md)
- [Installation](docs/INSTALLATION_GUIDE.md)

The next step is the first in the migration process. Here we describe how you can configure Kotlin Maven plugin:
- [Maven setup guide](docs/MAVEN_SETUP_GUIDE.md)
