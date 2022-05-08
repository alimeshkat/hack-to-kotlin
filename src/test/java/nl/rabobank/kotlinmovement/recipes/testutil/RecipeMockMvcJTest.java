package nl.rabobank.kotlinmovement.recipes.testutil;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rabobank.kotlinmovement.recipes.domain.IngredientRequestJ;
import nl.rabobank.kotlinmovement.recipes.domain.IngredientResponseJ;
import nl.rabobank.kotlinmovement.recipes.domain.RecipeRequestJ;
import nl.rabobank.kotlinmovement.recipes.domain.RecipeResponseJ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RecipeMockMvcJTest {

    @Autowired
    protected MockMvc mockMvc;
    protected static ObjectMapper objectMapper = new ObjectMapper();

    protected RecipeResponseJ[] getRecipes() throws Exception {
        return getRecipes(get("/recipes"));
    }

    protected RecipeResponseJ getRecipes(Long id) throws Exception {
        var responseArrayString = this.mockMvc
                .perform(get("/recipes/{id}", id))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();
        return objectMapper.readValue(responseArrayString, RecipeResponseJ.class);
    }

    protected RecipeResponseJ[] getPaginatedRecipes(Integer page, Integer size, String sort, String sortDirectionDesc) throws Exception {
        var request = get("/recipes");
        if (page != null) {
            request.param("page", String.valueOf(page));
        }
        if (size != null) {
            request.param("size", String.valueOf(size));
        }

        if (sort != null && sortDirectionDesc != null) {
            request.param("sort", sort + "," + sortDirectionDesc);
        }

        if (sort != null) {
            request.param("sort", sort);
        }
        return getRecipes(request);
    }

    protected void createRecipes(List<RecipeRequestJ> recipes) {
        recipes.forEach(it ->
                {
                    try {
                        this.mockMvc.perform(post("/recipes")
                                .content(objectMapper.writeValueAsString(it)).contentType(MediaType.APPLICATION_JSON)
                        ).andExpect(status().isCreated());
                    } catch (Exception e) {
                        fail("Failed creating recipe");
                    }
                }
        );
    }

    protected RecipeResponseJ updateRecipe(Long id, RecipeRequestJ recipeRequestJ) throws Exception {
        final var contentAsString = this.mockMvc.perform(
                        put("/recipes/{id}", id)
                                .content(objectMapper.writeValueAsString(recipeRequestJ))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readValue(contentAsString, RecipeResponseJ.class);
    }

    protected IngredientRequestJ findRequestedIngredientsByName(RecipeRequestJ recipeRequestJ, IngredientResponseJ ingredientResponseJ) {
        return recipeRequestJ.getIngredients()
                .stream()
                .filter(it -> it.getName().equals(ingredientResponseJ.getName()))
                .findFirst().orElseThrow(AssertionError::new);
    }

    protected void deleteRecipe(Long id) throws Exception {
        this.mockMvc.perform(delete("/recipes/{id}", id)).andExpect(status().isNoContent());
    }

    protected void assertCallStatus(MockHttpServletRequestBuilder request, ResultMatcher statusMatcher) throws Exception {
        this.mockMvc
                .perform(request)
                .andDo(print())
                .andExpect(statusMatcher);
    }

    protected void assertRecipeResponse(long recipeId, RecipeResponseJ updatedRecipeResponseJ, RecipeRequestJ recipeRequestJ) {
        assertThat(updatedRecipeResponseJ.getId()).isEqualTo(recipeId);
        assertThat(updatedRecipeResponseJ.getRecipeName()).isEqualTo(updatedRecipeResponseJ.getRecipeName());
        assertIngredients(updatedRecipeResponseJ, recipeRequestJ);
    }

    private RecipeResponseJ[] getRecipes(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        var responseArrayString = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();
        return objectMapper.readValue(responseArrayString, RecipeResponseJ[].class);
    }

    private void assertIngredients(RecipeResponseJ updatedRecipeResponseJ, RecipeRequestJ recipeRequestJ) {
        doForEach(updatedRecipeResponseJ.getIngredients(), (ingredientResponseJ) -> {
            final IngredientRequestJ expected = findRequestedIngredientsByName(recipeRequestJ, ingredientResponseJ);
            assertThat(ingredientResponseJ.getName()).isEqualTo(expected.getName());
            assertThat(ingredientResponseJ.getType()).isEqualTo(expected.getType());
            assertThat(ingredientResponseJ.getWeight()).isEqualTo(expected.getWeight());
        });
    }

    private  <T> void doForEach(Set<T> items, Consumer<T> asserts) {
        items.forEach(asserts);
    }
}
