package nl.rabobank.kotlinmovement.recipes.test.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeRequestTest
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeResponseTest
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipesErrorResponseTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RecipeMockMvcTest {
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
    protected fun getRecipe(id: Long): RecipeResponseTest {
        return mockMvcPerformRequest<RecipeResponseTest>(
            MockMvcRequestBuilders.get("/recipes/{id}", id),
            MockMvcResultMatchers.status().isOk
        )
    }

    @get:Throws(Exception::class)
    protected val allRecipes: Array<RecipeResponseTest>
        get() = mockMvcPerformRequest<Array<RecipeResponseTest>>(
            MockMvcRequestBuilders.get("/recipes"),
            MockMvcResultMatchers.status().isOk
        )

    @Throws(Exception::class)
    protected fun updateRecipe(id: Long?, recipeRequest: RecipeRequestTest): RecipeResponseTest {
        val requestBuilder = MockMvcRequestBuilders.put("/recipes/{id}", id)
            .content(objectMapper.writeValueAsString(recipeRequest))
            .contentType(MediaType.APPLICATION_JSON)
        return mockMvcPerformRequest<RecipeResponseTest>(
            requestBuilder,
            MockMvcResultMatchers.status().isOk
        )
    }

    @Throws(Exception::class)
    protected fun badRequestCall(builder: MockHttpServletRequestBuilder): RecipesErrorResponseTest {
        val requestBuilder = builder.contentType(MediaType.APPLICATION_JSON)
        return mockMvcPerformRequest<RecipesErrorResponseTest>(
            requestBuilder,
            MockMvcResultMatchers.status().isBadRequest
        )
    }

    @Throws(Exception::class)
    protected fun notFoundCall(builder: MockHttpServletRequestBuilder): RecipesErrorResponseTest {
        val requestBuilder = builder.contentType(MediaType.APPLICATION_JSON)
        return mockMvcPerformRequest<RecipesErrorResponseTest>(
            requestBuilder,
            MockMvcResultMatchers.status().isNotFound
        )
    }

    @Throws(Exception::class)
    protected fun mockMvcPerformRequest(requestBuilder: MockHttpServletRequestBuilder, status: ResultMatcher) {
        mockMvc
            .perform(requestBuilder)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status)
            .andReturn()
            .response
    }

    @Throws(Exception::class)
    private fun createRecipe(recipe: RecipeRequestTest) {
        mockMvcPerformRequest<RecipeResponseTest>(
            MockMvcRequestBuilders.post("/recipes")
                .content(objectMapper.writeValueAsString(recipe))
                .contentType(MediaType.APPLICATION_JSON),
            MockMvcResultMatchers.status().isCreated
        )
    }

    @Throws(Exception::class)
    private inline fun <reified T> mockMvcPerformRequest(
        requestBuilder: MockHttpServletRequestBuilder,
        status: ResultMatcher
    ): T = mockMvc
        .perform(requestBuilder)
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status)
        .andReturn()
        .response
        .contentAsString.let {
            objectMapper.readValue(it, T::class.java)
        }

}
