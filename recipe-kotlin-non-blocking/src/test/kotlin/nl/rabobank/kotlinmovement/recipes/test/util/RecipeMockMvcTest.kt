package nl.rabobank.kotlinmovement.recipes.test.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
class RecipeMockMvcTest {

    protected var objectMapper = jacksonObjectMapper()


    private var client: WebClient = WebClient.create("http://localhost:8080")


    @Throws(Exception::class)
    protected suspend fun setInitialState(): RecipeRequestTest {
        val ingredients = RecipeTestData.getDefaultIngredientRequests
        val initRecipe = RecipeRequestTest("Pizza", ingredients)
        createRecipe(initRecipe)
        return initRecipe
    }

    @Throws(Exception::class)
    protected suspend fun getRecipe(id: Long): RecipeResponseTest = mockMvcPerformRequest(
        HttpMethod.GET,
        "/recipes/$id",
        HttpStatus.OK
    )

    @Throws(Exception::class)
    protected suspend fun allRecipes(): Array<RecipeResponseTest> = mockMvcPerformRequest(
        HttpMethod.GET,
        "/recipes",
        HttpStatus.OK
    )

    //    @Throws(Exception::class)
//    protected fun updateRecipe(id: Long?, recipeRequest: RecipeRequestTest): RecipeResponseTest {
//        val requestBuilder = MockMvcRequestBuilders.put("/recipes/{id}", id)
//            .content(objectMapper.writeValueAsString(recipeRequest))
//            .contentType(MediaType.APPLICATION_JSON)
//        return mockMvcPerformRequest<RecipeResponseTest>(
//            requestBuilder,
//            MockMvcResultMatchers.status().isOk
//        )
//    }
//
//    @Throws(Exception::class)
//    protected fun badRequestCall(builder: MockHttpServletRequestBuilder): RecipesErrorResponseTest {
//        val requestBuilder = builder.contentType(MediaType.APPLICATION_JSON)
//        return mockMvcPerformRequest<RecipesErrorResponseTest>(
//            requestBuilder,
//            MockMvcResultMatchers.status().isBadRequest
//        )
//    }
//
    @Throws(Exception::class)
    protected suspend fun notFoundCall(httpMethod: HttpMethod, uri: String): RecipesErrorResponseTest =
        mockMvcPerformRequest(
            httpMethod,
            uri,
            HttpStatus.NOT_FOUND
        )


//    @Throws(Exception::class)
//    protected fun mockMvcPerformRequest(requestBuilder: MockHttpServletRequestBuilder, status: ResultMatcher) {
//        mockMvc
//            .perform(requestBuilder)
//            .andDo(MockMvcResultHandlers.print())
//            .andExpect(status)
//            .andReturn()
//            .response
//    }

    @Throws(Exception::class)
    private suspend fun createRecipe(recipe: RecipeRequestTest): RecipeResponseTest = mockMvcPerformRequest(
        HttpMethod.POST,
        "/recipes",
        HttpStatus.CREATED,
        objectMapper.writeValueAsString(recipe)
    )

    @Throws(Exception::class)
    private suspend inline fun <reified T : Any> mockMvcPerformRequest(
        method: HttpMethod,
        uri: String,
        status: HttpStatus,
        body: String? = null
    ): T {
        val i = client.method(method).uri(uri).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        body?.let {
            i.bodyValue(it)
        }

        return i.awaitExchange {
            if (it.statusCode() == status) {
                it.awaitBody()
            } else {
                throw AssertionError("Unexpected status $status")
            }
        }
    }
}

