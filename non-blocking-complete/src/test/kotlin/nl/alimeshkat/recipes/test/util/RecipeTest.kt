package nl.alimeshkat.recipes.test.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.runBlocking
import nl.alimeshkat.recipes.test.model.RecipeRequestTest
import nl.alimeshkat.recipes.test.model.RecipeResponseTest
import nl.alimeshkat.recipes.test.model.RecipesErrorResponseTest
import org.opentest4j.AssertionFailedError
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.UUID

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RecipeTest {

    protected var objectMapper = jacksonObjectMapper()
    var client: WebTestClient = WebTestClient
        .bindToServer()
        .baseUrl("http://localhost:8080")
        .build()

    protected fun setInitialState(number: Number = 1): Array<RecipeResponseTest> {
        return (1..number.toInt()).map {
            val ingredients = RecipeTestData.getDefaultIngredientRequests
            val initRecipe = RecipeRequestTest(UUID.randomUUID().toString(), ingredients)
            createRecipe(initRecipe)
        }.toTypedArray()
    }

    protected fun getRecipe(id: Long): RecipeResponseTest {
        return mockRequest(
            HttpMethod.GET,
            "/recipes/$id",
            HttpStatus.OK
        )
    }

    protected fun allRecipes(): Array<RecipeResponseTest> {
        return mockRequest(
            HttpMethod.GET,
            "/recipes",
            HttpStatus.OK
        )
    }

    protected fun updateRecipe(id: Long?, recipeRequest: RecipeRequestTest): RecipeResponseTest = runBlocking {
        mockRequest(
            HttpMethod.PUT,
            "/recipes/$id",
            HttpStatus.OK,
            objectMapper.writeValueAsString(recipeRequest)
        )
    }

    protected fun badRequestCall(
        httpMethod: HttpMethod,
        url: String,
        body: String?
    ): RecipesErrorResponseTest {
        return mockRequest(
            httpMethod,
            url,
            HttpStatus.BAD_REQUEST,
            body
        )
    }

    protected fun notFoundCall(httpMethod: HttpMethod, uri: String): RecipesErrorResponseTest {
        return mockRequest(
            httpMethod,
            uri,
            HttpStatus.NOT_FOUND
        )
    }

    protected fun voidMockRequest(httpMethod: HttpMethod, url: String, status: HttpStatus): Unit =
        mockRequest(
            httpMethod,
            url,
            status
        )

    private fun createRecipe(recipe: RecipeRequestTest): RecipeResponseTest {
        return mockRequest(
            HttpMethod.POST,
            "/recipes",
            HttpStatus.CREATED,
            objectMapper.writeValueAsString(recipe)
        )
    }

    private inline fun <reified T : Any> mockRequest(
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

        if (T::class == Unit::class) {
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
}


