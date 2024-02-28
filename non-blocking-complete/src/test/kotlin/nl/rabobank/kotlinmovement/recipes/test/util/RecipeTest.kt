package nl.rabobank.kotlinmovement.recipes.test.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.runBlocking
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeRequestTest
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeResponseTest
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipesErrorResponseTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RecipeTest {

    protected var objectMapper = jacksonObjectMapper()
    var client: WebClient = WebClient.create("http://localhost:8080")

    protected fun setInitialState(): RecipeRequestTest {
        val ingredients = RecipeTestData.getDefaultIngredientRequests
        val initRecipe = RecipeRequestTest("Pizza", ingredients)
        createRecipe(initRecipe)
        return initRecipe
    }

    protected fun getRecipe(id: Long): RecipeResponseTest = runBlocking {
        mockRequest(
            HttpMethod.GET,
            "/recipes/$id",
            HttpStatus.OK
        )
    }

    protected fun allRecipes(): Array<RecipeResponseTest> = runBlocking {
        mockRequest(
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
    ): RecipesErrorResponseTest = runBlocking {
        mockRequest(
            httpMethod,
            url,
            HttpStatus.BAD_REQUEST,
            body
        )
    }

    protected fun notFoundCall(httpMethod: HttpMethod, uri: String): RecipesErrorResponseTest = runBlocking {
        mockRequest(
            httpMethod,
            uri,
            HttpStatus.NOT_FOUND
        )
    }

    protected fun mockRequestNoContent(httpMethod: HttpMethod, url: String) = runBlocking {
        client.method(httpMethod).uri(url).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
            .awaitExchange {
                if (it.statusCode() == HttpStatus.NO_CONTENT) {
                    return@awaitExchange
                } else {
                    throw AssertionError("Unexpected status ${it.statusCode()}")
                }
            }
    }

    private fun createRecipe(recipe: RecipeRequestTest): RecipeResponseTest = runBlocking {
        mockRequest(
            HttpMethod.POST,
            "/recipes",
            HttpStatus.CREATED,
            objectMapper.writeValueAsString(recipe)
        )
    }

    private suspend inline fun <reified T : Any> mockRequest(
        method: HttpMethod,
        uri: String,
        status: HttpStatus,
        body: String? = null
    ): T {
        val i =
            client.method(method).uri(uri).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        body?.let {
            i.bodyValue(it)
        }

        return i.awaitExchange {
            if (it.statusCode() == status) {

                it.awaitBody<T>().also { b -> println(b) }

            } else {
                throw AssertionError("Unexpected status ${it.statusCode()}")
            }
        }
    }
}


