package nl.rabobank.kotlinmovement.recipes.testutil

import com.fasterxml.jackson.databind.ObjectMapper
import nl.rabobank.kotlinmovement.recipes.testutil.testdata.IngredientRequest
import nl.rabobank.kotlinmovement.recipes.testutil.testdata.IngredientResponse
import nl.rabobank.kotlinmovement.recipes.testutil.testdata.RecipeRequest
import nl.rabobank.kotlinmovement.recipes.testutil.testdata.RecipeResponse
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.TestInstance
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RecipeMockMvcTest {
    var objectMapper = ObjectMapper()

    @Autowired
    lateinit var mockMvc: MockMvc

    val allRecipePaginatedResponses: Array<RecipeResponse>
        get() = MockMvcRequestBuilders.get("/recipes").getRecipeResponses()

    fun updateRecipe(id: Long, recipeRequest: RecipeRequest): RecipeResponse {
        val contentAsString = mockMvc.perform(
            MockMvcRequestBuilders.put("/recipes/{id}", id)
                .content(objectMapper.writeValueAsString(recipeRequest))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
            .response
            .contentAsString
        return objectMapper.readValue(contentAsString, RecipeResponse::class.java)
    }

    fun findIngredientRequestByName(
        recipeRequest: RecipeRequest,
        ingredientResponse: IngredientResponse?
    ): IngredientRequest? {
        return recipeRequest.ingredients?.firstOrNull { it.name == ingredientResponse?.name }
    }

    fun assertRecipeStatus(
        mockHttpServletRequestBuilder: MockHttpServletRequestBuilder,
        statusMatcher: ResultMatcher
    ) {
        mockMvc
            .perform(mockHttpServletRequestBuilder)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(statusMatcher)
    }

    fun createRecipes(recipes: List<RecipeRequest>) {
        recipes.forEach {
            try {
                mockMvc.perform(
                    MockMvcRequestBuilders.post("/recipes")
                        .content(objectMapper.writeValueAsString(it))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isCreated)
            } catch (e: Exception) {
                AssertionsForClassTypes.fail(e.message)
            }
        }
    }

    fun deleteRecipe(id: Long) {
        mockMvc.perform(MockMvcRequestBuilders.delete("/recipes/{id}", id))
            .andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    fun MockHttpServletRequestBuilder.getRecipeResponses(): Array<RecipeResponse> {
        val responseArrayString = mockMvc
            .perform(this)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk).andReturn().response
            .contentAsString
        return objectMapper.readValue(responseArrayString, Array<RecipeResponse>::class.java)
    }


    fun getRecipeResponses(id: Long): RecipeResponse {
        val responseArrayString = mockMvc
            .perform(MockMvcRequestBuilders.get("/recipes/{id}", id))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk).andReturn().response
            .contentAsString
        return objectMapper.readValue(responseArrayString, RecipeResponse::class.java)
    }


    fun getAllRecipePaginatedResponses(
        pageValue: Int?,
        size: Int?,
        sort: String?,
        sortDirectionDesc: String?
    ): Array<RecipeResponse>? {
        return with(MockMvcRequestBuilders.get("/recipes")) {
            pageValue?.let {
                param("page", it.toString())
            }
            size?.let {
                param("size", it.toString())
            }
            if (sort != null && sortDirectionDesc != null) {
                param("sort", "$sort,$sortDirectionDesc")
            }
            sort?.let {
                param("sort", it)
            }
            this
        }.getRecipeResponses()

    }

    fun <T> doForEach(items: Set<T?>, action: (item: T?) -> Unit) {
        items.forEach(action)
    }

}



