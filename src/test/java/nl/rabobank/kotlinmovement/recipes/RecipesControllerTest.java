package nl.rabobank.kotlinmovement.recipes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rabobank.kotlinmovement.recipes.domain.IngredientRequest;
import nl.rabobank.kotlinmovement.recipes.domain.IngredientResponse;
import nl.rabobank.kotlinmovement.recipes.domain.IngredientType;
import nl.rabobank.kotlinmovement.recipes.domain.RecipeRequest;
import nl.rabobank.kotlinmovement.recipes.domain.RecipeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RecipesControllerTest {
    @Autowired
    private MockMvc mockMvc;
    static ObjectMapper objectMapper = new ObjectMapper();
    private RecipeRequest initRecipe;

    @Test
    @DisplayName("Should be able to get all recipes")
    void test1() throws Exception {
        final RecipeResponse actualRecipeResponse = getAllRecipeResponses()[0];
        assertThat(actualRecipeResponse.getId()).isNotNull();
        assertThat(actualRecipeResponse.getRecipeName()).isEqualTo(initRecipe.getRecipeName());
        doForEach(actualRecipeResponse.getIngredients(), (IngredientResponse ingredientResponse) -> {
            final IngredientRequest expected = findIngredientRequestByName(initRecipe, ingredientResponse);
            assertThat(ingredientResponse.getName()).isEqualTo(expected.getName());
            assertThat(ingredientResponse.getType()).isEqualTo(expected.getType());
            assertThat(ingredientResponse.getWeight()).isEqualTo(expected.getWeight());
        });
        assertThat(actualRecipeResponse.getRecipeName()).isEqualTo(initRecipe.getRecipeName());
    }

    @Test
    @DisplayName("Should be able to get a recipe")
    void test2() throws Exception {
        final RecipeResponse first = getAllRecipeResponses()[0];
        final RecipeResponse actualRecipeResponse = getRecipeResponses(first.getId());
        assertThat(actualRecipeResponse.getId()).isNotNull();
        assertThat(actualRecipeResponse.getRecipeName()).isEqualTo(initRecipe.getRecipeName());
        doForEach(actualRecipeResponse.getIngredients(), (IngredientResponse ingredientResponse) -> {
            final IngredientRequest expected = findIngredientRequestByName(initRecipe, ingredientResponse);
            assertThat(ingredientResponse.getName()).isEqualTo(expected.getName());
            assertThat(ingredientResponse.getType()).isEqualTo(expected.getType());
            assertThat(ingredientResponse.getWeight()).isEqualTo(expected.getWeight());
        });
        assertThat(actualRecipeResponse.getRecipeName()).isEqualTo(initRecipe.getRecipeName());
    }

    @Test
    @DisplayName("Should be able to get a recipe")
    void test3() throws Exception {
        final RecipeResponse first = getAllRecipeResponses()[0];
        final RecipeResponse actualRecipeResponse = getRecipeResponses(first.getId());
        assertThat(actualRecipeResponse.getId()).isNotNull();
        assertThat(actualRecipeResponse.getRecipeName()).isEqualTo(initRecipe.getRecipeName());
        doForEach(actualRecipeResponse.getIngredients(), (IngredientResponse ingredientResponse) -> {
            final IngredientRequest expected = findIngredientRequestByName(initRecipe, ingredientResponse);
            assertThat(ingredientResponse.getName()).isEqualTo(expected.getName());
            assertThat(ingredientResponse.getType()).isEqualTo(expected.getType());
            assertThat(ingredientResponse.getWeight()).isEqualTo(expected.getWeight());
        });
        assertThat(actualRecipeResponse.getRecipeName()).isEqualTo(initRecipe.getRecipeName());
    }

    @Test
    @DisplayName("Should be able to delete a recipe ")
    void test4() throws Exception {
        final RecipeResponse actualRecipeResponse = getAllRecipeResponses()[0];
        deleteRecipe(actualRecipeResponse.getId());
        final RecipeResponse[] actualRecipeResponseLatest = getAllRecipeResponses();
        assertThat(actualRecipeResponseLatest).isEmpty();
    }

    @Test
    @DisplayName("Should be able to update a recipe")
    void test5() throws Exception {
        final RecipeResponse firstRecipe = getAllRecipeResponses()[0];
        final RecipeRequest updateRequest = peperoniPizzaRecipeRequest();
        final Long actualRecipeResponseId = firstRecipe.getId();
        final RecipeResponse updatedRecipeResponse = updateRecipe(actualRecipeResponseId, updateRequest);

        assertThat(updatedRecipeResponse.getId()).isEqualTo(firstRecipe.getId());
        assertThat(updatedRecipeResponse.getRecipeName()).isEqualTo(updatedRecipeResponse.getRecipeName());
        doForEach(updatedRecipeResponse.getIngredients(), (IngredientResponse ingredientResponse) -> {
            final IngredientRequest expected = findIngredientRequestByName(updateRequest, ingredientResponse);
            assertThat(ingredientResponse.getName()).isEqualTo(expected.getName());
            assertThat(ingredientResponse.getType()).isEqualTo(expected.getType());
            assertThat(ingredientResponse.getWeight()).isEqualTo(expected.getWeight());
        });

    }

    @Test
    @DisplayName("Should be able create when resource id doesn't not exist")
    void test6() throws Exception {
        final RecipeRequest updateRequest = peperoniPizzaRecipeRequest();
        final RecipeResponse updatedRecipeResponse = updateRecipe(2L, updateRequest);

        assertThat(updatedRecipeResponse.getId()).isEqualTo(2L);
        assertThat(updatedRecipeResponse.getRecipeName()).isEqualTo(updatedRecipeResponse.getRecipeName());
        doForEach(updatedRecipeResponse.getIngredients(), (IngredientResponse ingredientResponse) -> {
            final IngredientRequest expected = findIngredientRequestByName(updateRequest, ingredientResponse);
            assertThat(ingredientResponse.getName()).isEqualTo(expected.getName());
            assertThat(ingredientResponse.getType()).isEqualTo(expected.getType());
            assertThat(ingredientResponse.getWeight()).isEqualTo(expected.getWeight());
        });
    }

    @Test
    @DisplayName("Should return not found if resource does not exist")
    void test7() throws Exception {
        assertRecipeStatus(get("/recipes/{id}", 2L), status().isNotFound());
        assertRecipeStatus(delete("/recipes/{id}", 2L), status().isNotFound());
    }

    @ParameterizedTest
    @MethodSource("errorDataParams")
    @DisplayName("Should not be able to create/update if request object is invalid")
    void test8(String recipeRequest) throws Exception {
        assertRecipeStatus(
                post("/recipes")
                        .content(recipeRequest)
                        .contentType(MediaType.APPLICATION_JSON), status()
                        .is4xxClientError()
        );

        assertRecipeStatus(
                put("/recipes/{id}", 1)
                        .content(recipeRequest)
                        .contentType(MediaType.APPLICATION_JSON), status()
                        .is4xxClientError()
        );
    }

    @BeforeEach
    void setup() throws Exception {
        seedDb();
    }

    private static Stream<Arguments> errorDataParams() throws JsonProcessingException {
        final var emptyRequest = objectMapper.writeValueAsString(new RecipeRequest("", Set.of()));
        final var nullRecipeNameRequest = objectMapper.writeValueAsString(new RecipeRequest(null, Set.of()));
        final var nullIngredientsRequest = objectMapper.writeValueAsString(new RecipeRequest("test", null));
        final var noContent = "{}";
        return Stream.of(
                Arguments.of(noContent),
                Arguments.of(emptyRequest),
                Arguments.of(nullRecipeNameRequest),
                Arguments.of(nullIngredientsRequest)
        );
    }

    private RecipeResponse updateRecipe(Long id, RecipeRequest recipeRequest) throws Exception {
        final var contentAsString = this.mockMvc.perform(
                        put("/recipes/{id}", id)
                                .content(objectMapper.writeValueAsString(recipeRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readValue(contentAsString, RecipeResponse.class);
    }

    private void deleteRecipe(Long id) throws Exception {
        this.mockMvc.perform(delete("/recipes/{id}", id)).andExpect(status().isNoContent());
    }

    private <T> void doForEach(Set<T> items, Consumer<T> asserts) {
        items.forEach(asserts);
    }

    private IngredientRequest findIngredientRequestByName(RecipeRequest recipeRequest, IngredientResponse ingredientResponse) {
        return recipeRequest.getIngredients()
                .stream()
                .filter(it -> it.getName().equals(ingredientResponse.getName()))
                .findFirst().orElseThrow(AssertionError::new);
    }

    private RecipeResponse[] getAllRecipeResponses() throws Exception {
        var responseArrayString = this.mockMvc
                .perform(get("/recipes"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();
        return objectMapper.readValue(responseArrayString, RecipeResponse[].class);
    }

    private RecipeResponse getRecipeResponses(Long id) throws Exception {
        var responseArrayString = this.mockMvc
                .perform(get("/recipes/{id}", id))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();
        return objectMapper.readValue(responseArrayString, RecipeResponse.class);
    }

    private void assertRecipeStatus(MockHttpServletRequestBuilder mockHttpServletRequestBuilder, ResultMatcher statusMatcher) throws Exception {
        this.mockMvc
                .perform(mockHttpServletRequestBuilder)
                .andDo(print())
                .andExpect(statusMatcher);
    }

    private RecipeRequest peperoniPizzaRecipeRequest() {
        final Set<IngredientRequest> ingredients = Set.of(
                new IngredientRequest("Flower", IngredientType.DRY, 1000),
                new IngredientRequest("Water", IngredientType.WET, 8000),
                new IngredientRequest("Salt", IngredientType.DRY, 20),
                new IngredientRequest("Yeast", IngredientType.DRY, 2),
                new IngredientRequest("Peperoni", IngredientType.DRY, 100),
                new IngredientRequest("Tomato sauce", IngredientType.WET, 100)

        );
        final String newRecipeName = "Pizza Peperoni";
        return new RecipeRequest(newRecipeName, ingredients);
    }

    private void seedDb() throws Exception {
        final Set<IngredientRequest> ingredients = Set.of(
                new IngredientRequest("Flower", IngredientType.DRY, 1000),
                new IngredientRequest("Water", IngredientType.WET, 8000),
                new IngredientRequest("Salt", IngredientType.DRY, 20),
                new IngredientRequest("Yeast", IngredientType.DRY, 2)

        );
        initRecipe = new RecipeRequest("Pizza", ingredients);

        this.mockMvc.perform(post("/recipes")
                .content(objectMapper.writeValueAsString(initRecipe)).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
    }

}
