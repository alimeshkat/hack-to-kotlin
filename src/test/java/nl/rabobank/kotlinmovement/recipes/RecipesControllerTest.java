package nl.rabobank.kotlinmovement.recipes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rabobank.kotlinmovement.recipes.model.IngredientRequest;
import nl.rabobank.kotlinmovement.recipes.model.IngredientType;
import nl.rabobank.kotlinmovement.recipes.model.RecipeRequest;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

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
class RecipesControllerTest {
    @Autowired
    private MockMvc mockMvc;
    static ObjectMapper objectMapper = new ObjectMapper();
    private RecipeRequest initRecipe;


    @Test
    @DisplayName("Should be able to get all recipes")
    void test1() throws Exception {
        final RecipeResponseTestDTO actualRecipeResponse = getAllRecipePaginatedResponses()[0];
        assertThat(actualRecipeResponse.getId()).isNotNull();
        assertThat(actualRecipeResponse.getRecipeName()).isEqualTo(initRecipe.getRecipeName());
        doForEach(actualRecipeResponse.getIngredients(), (IngredientResponseTestDTO ingredientResponse) -> {
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
        final RecipeResponseTestDTO first = getAllRecipePaginatedResponses()[0];
        final RecipeResponseTestDTO actualRecipeResponse = getRecipeResponses(first.getId());
        assertThat(actualRecipeResponse.getId()).isNotNull();
        assertThat(actualRecipeResponse.getRecipeName()).isEqualTo(initRecipe.getRecipeName());
        doForEach(actualRecipeResponse.getIngredients(), (IngredientResponseTestDTO ingredientResponse) -> {
            final IngredientRequest expected = findIngredientRequestByName(initRecipe, ingredientResponse);
            assertThat(ingredientResponse.getName()).isEqualTo(expected.getName());
            assertThat(ingredientResponse.getType()).isEqualTo(expected.getType());
            assertThat(ingredientResponse.getWeight()).isEqualTo(expected.getWeight());
        });
        assertThat(actualRecipeResponse.getRecipeName()).isEqualTo(initRecipe.getRecipeName());
    }

    @ParameterizedTest
    @MethodSource("sortedPagesRecipeDataProvider")
    @DisplayName("Should be able to get sorted paged recipes")
    void test3(Integer page,
               Integer pageSize,
               Integer expectedPageSize,
               String sortPropertyValue,
               String sortDirectionDesc) throws Exception {
        createRecipes(generateRecipeRequest(40));
        final RecipeResponseTestDTO[] allRecipePaginatedResponses = getAllRecipePaginatedResponses(page, pageSize, sortPropertyValue, sortDirectionDesc);
        assertThat(allRecipePaginatedResponses).hasSize(expectedPageSize);
    }


    @Test
    @DisplayName("Should be able to delete a recipe ")
    void test4() throws Exception {
        final RecipeResponseTestDTO actualRecipeResponse = getAllRecipePaginatedResponses()[0];
        deleteRecipe(actualRecipeResponse.getId());
        final RecipeResponseTestDTO[] actualRecipeResponseLatest = getAllRecipePaginatedResponses();
        assertThat(actualRecipeResponseLatest).isEmpty();
    }

    @Test
    @DisplayName("Should be able to update a recipe")
    void test5() throws Exception {
        final RecipeResponseTestDTO firstRecipe = getAllRecipePaginatedResponses()[0];
        final RecipeRequest updateRequest = peperoniPizzaRecipeRequest();
        final Long actualRecipeResponseId = firstRecipe.getId();
        final RecipeResponseTestDTO updatedRecipeResponse = updateRecipe(actualRecipeResponseId, updateRequest);

        assertThat(updatedRecipeResponse.getId()).isEqualTo(firstRecipe.getId());
        assertThat(updatedRecipeResponse.getRecipeName()).isEqualTo(updatedRecipeResponse.getRecipeName());
        doForEach(updatedRecipeResponse.getIngredients(), (IngredientResponseTestDTO ingredientResponse) -> {
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
        final RecipeResponseTestDTO updatedRecipeResponse = updateRecipe(2L, updateRequest);

        assertThat(updatedRecipeResponse.getId()).isEqualTo(2L);
        assertThat(updatedRecipeResponse.getRecipeName()).isEqualTo(updatedRecipeResponse.getRecipeName());
        doForEach(updatedRecipeResponse.getIngredients(), (IngredientResponseTestDTO ingredientResponse) -> {
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
    void setup() {
        setInitialState();
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

    private RecipeResponseTestDTO updateRecipe(Long id, RecipeRequest recipeRequest) throws Exception {
        final var contentAsString = this.mockMvc.perform(
                        put("/recipes/{id}", id)
                                .content(objectMapper.writeValueAsString(recipeRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readValue(contentAsString, RecipeResponseTestDTO.class);
    }

    private void deleteRecipe(Long id) throws Exception {
        this.mockMvc.perform(delete("/recipes/{id}", id)).andExpect(status().isNoContent());
    }

    private <T> void doForEach(Set<T> items, Consumer<T> asserts) {
        items.forEach(asserts);
    }

    private IngredientRequest findIngredientRequestByName(RecipeRequest recipeRequest, IngredientResponseTestDTO ingredientResponse) {
        return recipeRequest.getIngredients()
                .stream()
                .filter(it -> it.getName().equals(ingredientResponse.getName()))
                .findFirst().orElseThrow(AssertionError::new);
    }

    RecipeResponseTestDTO[] getAllRecipePaginatedResponses(Integer page, Integer size, String sort, String sortDirectionDesc) throws Exception {
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
        return getRecipeResponses(request);
    }

    private RecipeResponseTestDTO[] getRecipeResponses(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        var responseArrayString = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();
        return objectMapper.readValue(responseArrayString, RecipeResponseTestDTO[].class);
    }

    private RecipeResponseTestDTO[] getAllRecipePaginatedResponses() throws Exception {
        return getRecipeResponses(get("/recipes"));
    }

    private RecipeResponseTestDTO getRecipeResponses(Long id) throws Exception {
        var responseArrayString = this.mockMvc
                .perform(get("/recipes/{id}", id))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();
        return objectMapper.readValue(responseArrayString, RecipeResponseTestDTO.class);
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

    private void setInitialState() {
        final Set<IngredientRequest> ingredients = getDefaultIngredientRequests();
        initRecipe = new RecipeRequest("Pizza", ingredients);
        createRecipes(List.of(initRecipe));
    }

    private Set<IngredientRequest> getDefaultIngredientRequests() {
        return Set.of(
                new IngredientRequest("Flower", IngredientType.DRY, 1000),
                new IngredientRequest("Water", IngredientType.WET, 8000),
                new IngredientRequest("Salt", IngredientType.DRY, 20),
                new IngredientRequest("Yeast", IngredientType.DRY, 2)

        );
    }

    private void createRecipes(List<RecipeRequest> recipes) {
        recipes.forEach(it ->
                {
                    try {
                        this.mockMvc.perform(post("/recipes")
                                .content(objectMapper.writeValueAsString(it)).contentType(MediaType.APPLICATION_JSON)
                        ).andExpect(status().isCreated());
                    } catch (Exception e) {
                        fail(e.getMessage());
                    }
                }
        );

    }

    public static Stream<Arguments> sortedPagesRecipeDataProvider() {
        return Stream.of(
                Arguments.of(0, 10, 10, "recipeName", "desc"),
                Arguments.of(1, 10, 10, "recipeName", "desc"),
                Arguments.of(2, 10, 10, "recipeName", "asc"),
                Arguments.of(3, 10, 10, "recipeName", "desc"),
                Arguments.of(3, 10, 10, "recipeName", null),
                Arguments.of(0, null, 20, null, null)
        );
    }

    private List<RecipeRequest> generateRecipeRequest(int i) {
        final var recipeRequests = new ArrayList<RecipeRequest>(1);
        final String generatedString = generateRandomString();
        while (i > 0) {
            i--;
            recipeRequests.add(new RecipeRequest(generatedString, getDefaultIngredientRequests()));
        }
        return recipeRequests;
    }

    private String generateRandomString() {
        final var random = new Random();
        return random.ints(97, 122 + 1)
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }


}
