package nl.rabobank.kotlinmovement.recipes.test.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rabobank.kotlinmovement.recipes.test.util.model.IngredientRequestTest;
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeRequestTest;
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeResponseTest;
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipesErrorResponseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Set;

import static nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.getDefaultIngredientRequests;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RecipeMockMvcTest {
    @Autowired
    protected MockMvc mockMvc;
    protected static ObjectMapper objectMapper = new ObjectMapper();

    protected RecipeRequestTest initRecipeRequest;

    protected RecipeRequestTest setInitialState() throws Exception {
        final Set<IngredientRequestTest> ingredients = getDefaultIngredientRequests;
        RecipeRequestTest initRecipe = new RecipeRequestTest("Pizza", ingredients);
        initRecipeRequest = initRecipe;
        createRecipe(initRecipe);
        return initRecipe;
    }

    protected RecipeResponseTest getRecipe(long id) throws Exception {
        return mockMvcPerformRequest(get("/recipes/{id}", id), RecipeResponseTest.class, status().isOk());
    }

    protected RecipeResponseTest[] getAllRecipes() throws Exception {
        return mockMvcPerformRequest(get("/recipes"), RecipeResponseTest[].class, status().isOk());
    }


    protected RecipeResponseTest updateRecipe(Long id, RecipeRequestTest recipeRequest) throws Exception {
        final MockHttpServletRequestBuilder requestBuilder =
                put("/recipes/{id}", id)
                        .content(objectMapper.writeValueAsString(recipeRequest))
                        .contentType(MediaType.APPLICATION_JSON);
        return mockMvcPerformRequest(requestBuilder, RecipeResponseTest.class, status().isOk());
    }

    protected RecipesErrorResponseTest badRequestCall(MockHttpServletRequestBuilder builder) throws Exception {
        final MockHttpServletRequestBuilder requestBuilder =
                builder.contentType(MediaType.APPLICATION_JSON);
        return mockMvcPerformRequest(requestBuilder, RecipesErrorResponseTest.class, status().isBadRequest());
    }

    protected RecipesErrorResponseTest notFoundCall(MockHttpServletRequestBuilder builder) throws Exception {
        final MockHttpServletRequestBuilder requestBuilder =
                builder.contentType(MediaType.APPLICATION_JSON);
        return mockMvcPerformRequest(requestBuilder, RecipesErrorResponseTest.class, status().isNotFound());
    }

    protected void mockMvcPerformRequest(MockHttpServletRequestBuilder requestBuilder, ResultMatcher status) throws Exception {
        mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status)
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private void createRecipe(RecipeRequestTest recipe) throws Exception {
        mockMvcPerformRequest(
                post("/recipes")
                        .content(objectMapper.writeValueAsString(recipe))
                        .contentType(MediaType.APPLICATION_JSON)
                , RecipeResponseTest.class, status().isCreated());
    }

    private <T> T mockMvcPerformRequest(MockHttpServletRequestBuilder requestBuilder, Class<T> responseType, ResultMatcher status) throws Exception {
        var responseArrayString = mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status)
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readValue(responseArrayString, responseType);
    }

}
