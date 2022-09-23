## Test setup

To test our `Recipe service` we use Spring Boot Test
and [Spring MockMvc](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/web/servlet/MockMvc.html)
.
In the test we spin-up (with help of `Spring Boot Test`) a `Recipe service` and its database.
In each test with `MockMvc` helper methods we call the endpoints and verify the output of each call.
### MockMvc Tests

We have three test classes:

- GetRecipesControllerTest
- CreateUpdateRecipesControllerTest
- DeleteRecipesControllerTest

All the classes mentioned above extend the `RecipeMockMvcTest` class that contains all `MockMvc` related methods used by
the tests.

### Test util

The re-usable code are contained in utility classes to keep the test classes clean as possible.  
you can find the utility classes in [test/util](../../app/src/test/java/nl/rabobank/kotlinmovement/recipes/test/util) package:

- `RecipeAssert`: Contains all the assertion logic
- `RecipeMockMvcTest`: Is the parent class of all the test classes
- `RecipeTestData`: Contains the test data for each test.

### Test util model

Under [test/util/model](../../app/src/test/java/nl/rabobank/kotlinmovement/recipes/test/util/model) you can find the simple
POJO classes that represent the input/output of the `Recipe service`.
They are used by `Jackson ObjectMapper` to serialize/deserialize `MockMvc` requests/responses.
