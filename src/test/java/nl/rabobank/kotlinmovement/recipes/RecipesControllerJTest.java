package nl.rabobank.kotlinmovement.recipes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rabobank.kotlinmovement.recipes.domain.IngredientRequestJ;
import nl.rabobank.kotlinmovement.recipes.domain.IngredientResponseJ;
import nl.rabobank.kotlinmovement.recipes.domain.IngredientTypeJ;
import nl.rabobank.kotlinmovement.recipes.domain.RecipeRequestJ;
import nl.rabobank.kotlinmovement.recipes.domain.RecipeResponseJ;
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

class RecipesControllerJTest {
    @Autowired
    private MockMvc mockMvc;
    static ObjectMapper objectMapper = new ObjectMapper();
    private RecipeRequestJ initRecipe;


    @Test
    @DisplayName("Should be able to get all recipes")
    void test1() throws Exception {
        final RecipeResponseJ actualRecipeResponseJ = getAllRecipePaginatedResponses()[0];
        assertThat(actualRecipeResponseJ.getId()).isNotNull();
        assertThat(actualRecipeResponseJ.getRecipeName()).isEqualTo(initRecipe.getRecipeName());
        doForEach(actualRecipeResponseJ.getIngredients(), (IngredientResponseJ ingredientResponseJ) -> {
            final IngredientRequestJ expected = findIngredientRequestByName(initRecipe, ingredientResponseJ);
            assertThat(ingredientResponseJ.getName()).isEqualTo(expected.getName());
            assertThat(ingredientResponseJ.getType()).isEqualTo(expected.getType());
            assertThat(ingredientResponseJ.getWeight()).isEqualTo(expected.getWeight());
        });
        assertThat(actualRecipeResponseJ.getRecipeName()).isEqualTo(initRecipe.getRecipeName());
    }

    @Test
    @DisplayName("Should be able to get a recipe")
    void test2() throws Exception {
        final RecipeResponseJ first = getAllRecipePaginatedResponses()[0];
        final RecipeResponseJ actualRecipeResponseJ = getRecipeResponses(first.getId());
        assertThat(actualRecipeResponseJ.getId()).isNotNull();
        assertThat(actualRecipeResponseJ.getRecipeName()).isEqualTo(initRecipe.getRecipeName());
        doForEach(actualRecipeResponseJ.getIngredients(), (IngredientResponseJ ingredientResponseJ) -> {
            final IngredientRequestJ expected = findIngredientRequestByName(initRecipe, ingredientResponseJ);
            assertThat(ingredientResponseJ.getName()).isEqualTo(expected.getName());
            assertThat(ingredientResponseJ.getType()).isEqualTo(expected.getType());
            assertThat(ingredientResponseJ.getWeight()).isEqualTo(expected.getWeight());
        });
        assertThat(actualRecipeResponseJ.getRecipeName()).isEqualTo(initRecipe.getRecipeName());
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
        final RecipeResponseJ[] allRecipePaginatedResponses = getAllRecipePaginatedResponses(page, pageSize, sortPropertyValue, sortDirectionDesc);
        assertThat(allRecipePaginatedResponses).hasSize(expectedPageSize);
    }


    @Test
    @DisplayName("Should be able to delete a recipe ")
    void test4() throws Exception {
        final RecipeResponseJ actualRecipeResponseJ = getAllRecipePaginatedResponses()[0];
        deleteRecipe(actualRecipeResponseJ.getId());
        final RecipeResponseJ[] actualRecipeResponseJLatest = getAllRecipePaginatedResponses();
        assertThat(actualRecipeResponseJLatest).isEmpty();
    }

    @Test
    @DisplayName("Should be able to update a recipe")
    void test5() throws Exception {
        final RecipeResponseJ firstRecipe = getAllRecipePaginatedResponses()[0];
        final RecipeRequestJ updateRequest = peperoniPizzaRecipeRequest();
        final Long actualRecipeResponseId = firstRecipe.getId();
        final RecipeResponseJ updatedRecipeResponseJ = updateRecipe(actualRecipeResponseId, updateRequest);

        assertThat(updatedRecipeResponseJ.getId()).isEqualTo(firstRecipe.getId());
        assertThat(updatedRecipeResponseJ.getRecipeName()).isEqualTo(updatedRecipeResponseJ.getRecipeName());
        doForEach(updatedRecipeResponseJ.getIngredients(), (IngredientResponseJ ingredientResponseJ) -> {
            final IngredientRequestJ expected = findIngredientRequestByName(updateRequest, ingredientResponseJ);
            assertThat(ingredientResponseJ.getName()).isEqualTo(expected.getName());
            assertThat(ingredientResponseJ.getType()).isEqualTo(expected.getType());
            assertThat(ingredientResponseJ.getWeight()).isEqualTo(expected.getWeight());
        });

    }

    @Test
    @DisplayName("Should be able create when resource id doesn't not exist")
    void test6() throws Exception {
        final RecipeRequestJ updateRequest = peperoniPizzaRecipeRequest();
        final RecipeResponseJ updatedRecipeResponseJ = updateRecipe(2L, updateRequest);

        assertThat(updatedRecipeResponseJ.getId()).isEqualTo(2L);
        assertThat(updatedRecipeResponseJ.getRecipeName()).isEqualTo(updatedRecipeResponseJ.getRecipeName());
        doForEach(updatedRecipeResponseJ.getIngredients(), (IngredientResponseJ ingredientResponseJ) -> {
            final IngredientRequestJ expected = findIngredientRequestByName(updateRequest, ingredientResponseJ);
            assertThat(ingredientResponseJ.getName()).isEqualTo(expected.getName());
            assertThat(ingredientResponseJ.getType()).isEqualTo(expected.getType());
            assertThat(ingredientResponseJ.getWeight()).isEqualTo(expected.getWeight());
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
        setInitialState();
    }

    private static Stream<Arguments> errorDataParams() throws JsonProcessingException {
        final var emptyRequest = objectMapper.writeValueAsString(new RecipeRequestJ("", Set.of()));
        final var nullRecipeNameRequest = objectMapper.writeValueAsString(new RecipeRequestJ(null, Set.of()));
        final var nullIngredientsRequest = objectMapper.writeValueAsString(new RecipeRequestJ("test", null));
        final var noContent = "{}";
        return Stream.of(
                Arguments.of(noContent),
                Arguments.of(emptyRequest),
                Arguments.of(nullRecipeNameRequest),
                Arguments.of(nullIngredientsRequest)
        );
    }

    private RecipeResponseJ updateRecipe(Long id, RecipeRequestJ recipeRequestJ) throws Exception {
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

    private void deleteRecipe(Long id) throws Exception {
        this.mockMvc.perform(delete("/recipes/{id}", id)).andExpect(status().isNoContent());
    }

    private <T> void doForEach(Set<T> items, Consumer<T> asserts) {
        items.forEach(asserts);
    }

    private IngredientRequestJ findIngredientRequestByName(RecipeRequestJ recipeRequestJ, IngredientResponseJ ingredientResponseJ) {
        return recipeRequestJ.getIngredients()
                .stream()
                .filter(it -> it.getName().equals(ingredientResponseJ.getName()))
                .findFirst().orElseThrow(AssertionError::new);
    }

    RecipeResponseJ[] getAllRecipePaginatedResponses(Integer page, Integer size, String sort, String sortDirectionDesc) throws Exception {
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

    private RecipeResponseJ[] getRecipeResponses(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        var responseArrayString = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();
        return objectMapper.readValue(responseArrayString, RecipeResponseJ[].class);
    }

    private RecipeResponseJ[] getAllRecipePaginatedResponses() throws Exception {
        return getRecipeResponses(get("/recipes"));
    }

    private RecipeResponseJ getRecipeResponses(Long id) throws Exception {
        var responseArrayString = this.mockMvc
                .perform(get("/recipes/{id}", id))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();
        return objectMapper.readValue(responseArrayString, RecipeResponseJ.class);
    }

    private void assertRecipeStatus(MockHttpServletRequestBuilder mockHttpServletRequestBuilder, ResultMatcher statusMatcher) throws Exception {
        this.mockMvc
                .perform(mockHttpServletRequestBuilder)
                .andDo(print())
                .andExpect(statusMatcher);
    }

    private RecipeRequestJ peperoniPizzaRecipeRequest() {
        final Set<IngredientRequestJ> ingredients = Set.of(
                new IngredientRequestJ("Flower", IngredientTypeJ.DRY, 1000),
                new IngredientRequestJ("Water", IngredientTypeJ.WET, 8000),
                new IngredientRequestJ("Salt", IngredientTypeJ.DRY, 20),
                new IngredientRequestJ("Yeast", IngredientTypeJ.DRY, 2),
                new IngredientRequestJ("Peperoni", IngredientTypeJ.DRY, 100),
                new IngredientRequestJ("Tomato sauce", IngredientTypeJ.WET, 100)

        );
        final String newRecipeName = "Pizza Peperoni";
        return new RecipeRequestJ(newRecipeName, ingredients);
    }

    private void setInitialState() throws Exception {
        final Set<IngredientRequestJ> ingredients = getDefaultIngredientRequests();
        initRecipe = new RecipeRequestJ("Pizza", ingredients);
        createRecipes(List.of(initRecipe));
    }

    private Set<IngredientRequestJ> getDefaultIngredientRequests() {
        return Set.of(
                new IngredientRequestJ("Flower", IngredientTypeJ.DRY, 1000),
                new IngredientRequestJ("Water", IngredientTypeJ.WET, 8000),
                new IngredientRequestJ("Salt", IngredientTypeJ.DRY, 20),
                new IngredientRequestJ("Yeast", IngredientTypeJ.DRY, 2)

        );
    }

    private void createRecipes(List<RecipeRequestJ> recipes) {
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

    private List<RecipeRequestJ> generateRecipeRequest(int i) {
        final var recipeRequests = new ArrayList<RecipeRequestJ>(1);
        final String generatedString = generateRandomString();
        while (i > 0) {
            i--;
            recipeRequests.add(new RecipeRequestJ(generatedString, getDefaultIngredientRequests()));
        }
        return recipeRequests;
    }

    private String generateRandomString() {
        final var random = new Random();
        final var generatedString = random.ints(97, 122 + 1)
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }


}
