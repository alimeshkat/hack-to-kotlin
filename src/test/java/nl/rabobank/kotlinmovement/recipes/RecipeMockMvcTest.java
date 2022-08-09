package nl.rabobank.kotlinmovement.recipes;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rabobank.kotlinmovement.recipes.model.IngredientRequestTest;
import nl.rabobank.kotlinmovement.recipes.model.IngredientTypeTest;
import nl.rabobank.kotlinmovement.recipes.model.RecipeRequestTest;
import nl.rabobank.kotlinmovement.recipes.model.RecipeResponseTest;
import nl.rabobank.kotlinmovement.recipes.model.RecipesErrorResponseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Set;

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
    MockMvc mockMvc;
    static ObjectMapper objectMapper = new ObjectMapper();

    RecipeRequestTest initRecipeRequest;

    void setInitialState() throws Exception {
        final Set<IngredientRequestTest> ingredients = getDefaultIngredientRequests();
        RecipeRequestTest initRecipe = new RecipeRequestTest("Pizza", ingredients);
        initRecipeRequest = initRecipe;
        createRecipe(initRecipe);
    }

    <T> T mockMvcPerformRequest(MockHttpServletRequestBuilder requestBuilder, Class<T> responseType, ResultMatcher status) throws Exception {
        var responseArrayString = mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status)
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readValue(responseArrayString, responseType);
    }

    void mockMvcPerformRequest(MockHttpServletRequestBuilder requestBuilder, ResultMatcher status) throws Exception {
        mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status)
                .andReturn()
                .getResponse()
                .getContentAsString();
    }


    RecipeResponseTest[] getAllRecipes() throws Exception {
        return mockMvcPerformRequest(get("/recipes"), RecipeResponseTest[].class, status().isOk());
    }


    RecipeResponseTest updateRecipe(Long id, RecipeRequestTest recipeRequest) throws Exception {
        final MockHttpServletRequestBuilder requestBuilder =
                put("/recipes/{id}", id)
                        .content(objectMapper.writeValueAsString(recipeRequest))
                        .contentType(MediaType.APPLICATION_JSON);
        return mockMvcPerformRequest(requestBuilder, RecipeResponseTest.class, status().isOk());
    }

    RecipesErrorResponseTest badRequestCall(MockHttpServletRequestBuilder builder) throws Exception {
        final MockHttpServletRequestBuilder requestBuilder =
                builder.contentType(MediaType.APPLICATION_JSON);
        return mockMvcPerformRequest(requestBuilder, RecipesErrorResponseTest.class, status().isBadRequest());
    }

    RecipesErrorResponseTest notFoundCall(MockHttpServletRequestBuilder builder) throws Exception {
        final MockHttpServletRequestBuilder requestBuilder =
                builder.contentType(MediaType.APPLICATION_JSON);
        return mockMvcPerformRequest(requestBuilder, RecipesErrorResponseTest.class, status().isNotFound());
    }

    RecipeRequestTest peperoniPizzaRecipeRequest() {
        final Set<IngredientRequestTest> ingredients = Set.of(
                new IngredientRequestTest("Flower", IngredientTypeTest.DRY, 1000),
                new IngredientRequestTest("Water", IngredientTypeTest.WET, 8000),
                new IngredientRequestTest("Salt", IngredientTypeTest.DRY, 20),
                new IngredientRequestTest("Yeast", IngredientTypeTest.DRY, 2),
                new IngredientRequestTest("Peperoni", IngredientTypeTest.DRY, 100),
                new IngredientRequestTest("Tomato sauce", IngredientTypeTest.WET, 100)

        );
        final String newRecipeName = "Pizza Peperoni";
        return new RecipeRequestTest(newRecipeName, ingredients);
    }

    private void createRecipe(RecipeRequestTest recipe) throws Exception {
        mockMvcPerformRequest(
                post("/recipes")
                        .content(objectMapper.writeValueAsString(recipe))
                        .contentType(MediaType.APPLICATION_JSON)
                , RecipeResponseTest.class, status().isCreated());
    }

    private Set<IngredientRequestTest> getDefaultIngredientRequests() {
        return Set.of(
                new IngredientRequestTest("Flower", IngredientTypeTest.DRY, 1000),
                new IngredientRequestTest("Water", IngredientTypeTest.WET, 8000),
                new IngredientRequestTest("Salt", IngredientTypeTest.DRY, 20),
                new IngredientRequestTest("Yeast", IngredientTypeTest.DRY, 2)
        );
    }
}
