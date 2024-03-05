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
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RecipeTest {
    @Autowired
    protected lateinit var mockMvc: MockMvc
    protected var objectMapper = jacksonObjectMapper()

    protected fun setInitialState(number: Number = 1): Array<RecipeResponseTest> {
        return (1..number.toInt()).map {
            val ingredients = RecipeTestData.getDefaultIngredientRequests
            val initRecipe = RecipeRequestTest(UUID.randomUUID().toString(), ingredients)
            createRecipe(initRecipe)
        }.toTypedArray()
    }

    protected fun getRecipe(id: Long): RecipeResponseTest = mockRequest(
        HttpMethod.GET,
        "/recipes/$id",
        HttpStatus.OK
    )

    protected fun allRecipes(): Array<RecipeResponseTest> = mockRequest(
        HttpMethod.GET,
        "/recipes",
        HttpStatus.OK
    )

    protected fun updateRecipe(id: Long?, recipeRequest: RecipeRequestTest): RecipeResponseTest = mockRequest(
        HttpMethod.PUT,
        "/recipes/$id",
        HttpStatus.OK,
        objectMapper.writeValueAsString(recipeRequest)
    )

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

    protected fun notFoundCall(
        httpMethod: HttpMethod,
        url: String
    ): RecipesErrorResponseTest = mockRequest(
        httpMethod,
        url,
        HttpStatus.NOT_FOUND,
    )

    protected fun voidMockRequest(httpMethod: HttpMethod, url: String, status: HttpStatus): Unit =
        mockRequest(
            httpMethod,
            url,
            status
        )

    private fun createRecipe(recipe: RecipeRequestTest): RecipeResponseTest {
        return mockRequest<RecipeResponseTest>(
            HttpMethod.POST, "/recipes",
            HttpStatus.CREATED,
            objectMapper.writeValueAsString(recipe)
        )
    }

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

        return if (T::class == Unit::class) {
            mockMvc
                .perform(requestBuilder)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().`is`(status.value()))
            Unit as T
        } else {
            mockMvc
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

}
