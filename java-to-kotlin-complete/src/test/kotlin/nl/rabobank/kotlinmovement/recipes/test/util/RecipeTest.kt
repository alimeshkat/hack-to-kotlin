package nl.rabobank.kotlinmovement.recipes.test.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeRequestTest
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeResponseTest
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipesErrorResponseTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RecipeTest {
    @Autowired
    protected lateinit var mockMvc: MockMvc
    protected var objectMapper = jacksonObjectMapper()

    @Throws(Exception::class)
    protected fun setInitialState(): RecipeRequestTest {
        val ingredients = RecipeTestData.getDefaultIngredientRequests
        val initRecipe = RecipeRequestTest("Pizza", ingredients)
        createRecipe(initRecipe)
        return initRecipe
    }

    @Throws(Exception::class)
    protected fun getRecipe(id: Long): RecipeResponseTest = mockRequest(
        HttpMethod.GET,
        "/recipes/$id",
        HttpStatus.OK
    )


    @get:Throws(Exception::class)
    protected val allRecipes: Array<RecipeResponseTest>
        get() = mockRequest(
            HttpMethod.GET,
            "/recipes",
            HttpStatus.OK
        )

    @Throws(Exception::class)
    protected fun updateRecipe(id: Long?, recipeRequest: RecipeRequestTest): RecipeResponseTest = mockRequest(
        HttpMethod.PUT,
        "/recipes/$id",
        HttpStatus.OK,
        objectMapper.writeValueAsString(recipeRequest)
    )

    @Throws(Exception::class)
    protected fun badRequestCall(
        httpMethod: HttpMethod,
        url: String,
        body: String?
    ): RecipesErrorResponseTest = mockRequest(
        httpMethod,
        url,
        HttpStatus.BAD_REQUEST,
        body
    )

    @Throws(Exception::class)
    protected fun notFoundCall(
        httpMethod: HttpMethod,
        url: String
    ): RecipesErrorResponseTest = mockRequest(
        httpMethod,
        url,
        HttpStatus.NOT_FOUND,
    )

    @Throws(Exception::class)
    protected fun assertSimpleMockRequest(httpMethod: HttpMethod, url: String, status: HttpStatus) {
        mockMvc
            .perform(request(httpMethod, url))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().`is`(status.value()))
    }

    @Throws(Exception::class)
    private fun createRecipe(recipe: RecipeRequestTest) {
        mockRequest<RecipeResponseTest>(
            HttpMethod.POST, "/recipes",
            HttpStatus.CREATED,
            objectMapper.writeValueAsString(recipe)
        )
    }

    @Throws(Exception::class)
    private inline fun <reified T> mockRequest(
        httpMethod: HttpMethod, url: String, status: HttpStatus, body: String? = null
    ): T {
        val requestBuilder =
            body?.let {
                request(httpMethod, url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            } ?: request(
                httpMethod,
                url
            )
        return mockMvc
            .perform(requestBuilder)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().`is`(status.value()))
            .andReturn()
            .response
            .contentAsString.let {
                objectMapper.readValue(it, T::class.java)
            }
    }

}
