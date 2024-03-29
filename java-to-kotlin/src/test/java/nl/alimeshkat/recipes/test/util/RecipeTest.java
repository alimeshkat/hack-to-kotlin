package nl.alimeshkat.recipes.test.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.alimeshkat.recipes.test.model.IngredientRequestTest;
import nl.alimeshkat.recipes.test.model.RecipeRequestTest;
import nl.alimeshkat.recipes.test.model.RecipeResponseTest;
import nl.alimeshkat.recipes.test.model.RecipesErrorResponseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Set;

import static nl.alimeshkat.recipes.test.util.RecipeTestData.getDefaultIngredientRequests;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RecipeTest {
    @Autowired
    protected MockMvc mockMvc;
    protected static ObjectMapper objectMapper = new ObjectMapper();

    protected RecipeRequestTest initRecipeRequest;

    protected RecipeResponseTest[] setInitialState(Integer number) throws Exception {
        var recipeResponseTests = new ArrayList<RecipeResponseTest>();

        for (int i = 0; i < number; i++) {
            final Set<IngredientRequestTest> ingredients = getDefaultIngredientRequests;
            RecipeRequestTest initRecipe = new RecipeRequestTest("Pizza", ingredients);
            initRecipeRequest = initRecipe;
            recipeResponseTests.add(createRecipe(initRecipe));
        }
        return recipeResponseTests.toArray(new RecipeResponseTest[0]);
    }

    protected RecipeResponseTest getRecipe(long id) throws Exception {
        final String url = "/recipes/%d".formatted(id);
        return mockRequest(HttpMethod.GET, url, null, RecipeResponseTest.class, HttpStatus.OK);
    }

    protected RecipeResponseTest[] getAllRecipes() throws Exception {
        return mockRequest(HttpMethod.GET, "/recipes", null, RecipeResponseTest[].class, HttpStatus.OK);
    }


    protected RecipeResponseTest updateRecipe(Long id, RecipeRequestTest recipeRequest) throws Exception {
        final String url = "/recipes/%d".formatted(id);
        return mockRequest(
                HttpMethod.PUT,
                url,
                objectMapper.writeValueAsString(recipeRequest),
                RecipeResponseTest.class, HttpStatus.OK
        );
    }

    protected RecipesErrorResponseTest badRequestCall(HttpMethod httpMethod, String url, String body) throws Exception {
        return mockRequest(httpMethod, url, body, RecipesErrorResponseTest.class, HttpStatus.BAD_REQUEST);
    }

    protected RecipesErrorResponseTest notFoundCall(HttpMethod httpMethod, String url) throws Exception {
        return mockRequest(httpMethod, url, null, RecipesErrorResponseTest.class, HttpStatus.NOT_FOUND);
    }

    protected void assertVoidMockRequest(HttpMethod httpMethod, String url, HttpStatus status) throws Exception {
        mockRequest(httpMethod, url, null, Void.class, status);
    }

    private RecipeResponseTest createRecipe(RecipeRequestTest recipe) throws Exception {
        return mockRequest(
                HttpMethod.POST,
                "/recipes",
                objectMapper.writeValueAsString(recipe),
                RecipeResponseTest.class,
                HttpStatus.CREATED);
    }

    private <T> T mockRequest(HttpMethod httpMethod, String url, String body, Class<T> responseType, HttpStatus status) throws Exception {
        final MockHttpServletRequestBuilder request = request(httpMethod, url);

        if (body != null) {
            request.contentType(MediaType.APPLICATION_JSON).content(body);
        }
        var responseArrayString = mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(status.value()))
                .andReturn()
                .getResponse()
                .getContentAsString();
        if(responseType == null || responseType == Void.class){
            return null;
        }
        return objectMapper.readValue(responseArrayString, responseType);
    }

}

