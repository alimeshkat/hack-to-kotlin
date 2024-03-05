# test

Previously we used the convenience of the `MockMVC` to test our controllers, but that unfortunately doesn't work with
our reactive stack.
Fortunately, our test setup is easily adaptable to the reactive stack. We will use the `WebTestClient` to test our
controllers.

## Recipe

1) Let's replace the `MockMVC` with the `WebTestClient` in
   the [RecipeTest](../../../java-to-kotlin-complete/src/test/kotlin/nl/rabobank/kotlinmovement/recipes/test/util/RecipeTest.kt)
   class.
    ```kotlin
       var client: WebTestClient = WebTestClient
        .bindToServer()
        .baseUrl("http://localhost:8080")
        .build()
    ```
2) Replace the `mockRequest` method with the following code:
    ```kotlin
         private inline fun <reified T: Any> mockRequest(
            httpMethod: HttpMethod, url: String, 
            status: HttpStatus, body: String? = null
          ): T {
              val requestBuilder =
                  body?.let {
                      client.method(httpMethod).uri(url)
                          .contentType(MediaType.APPLICATION_JSON)
                          .accept(MediaType.APPLICATION_JSON)
                          .bodyValue(it)
                  } ?: client.method(httpMethod).uri(url)
      
              if(T::class == Unit::class) {
                  requestBuilder.exchange()
                      .expectStatus()
                      .isEqualTo(status)
                  return Unit as T
              }
              return requestBuilder.exchange()
                  .expectStatus()
                  .isEqualTo(status)
                  .expectBody(T::class.java).returnResult().responseBody
                  ?: throw AssertionFailedError("Unexpected response body returned from $url")
          }
    ```
   
3) That's it! Now run the tests:
    ```shell
       (cd ../../.. && ./mvnw clean test -pl :java-to-kotlin-complete)
    ```

[*peek solutions*](../../../java-to-kotlin-complete/src/test/kotlin/nl/rabobank/kotlinmovement/recipes/test/util/RecipeTest.kt)