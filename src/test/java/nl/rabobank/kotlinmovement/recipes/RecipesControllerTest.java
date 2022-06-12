package nl.rabobank.kotlinmovement.recipes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rabobank.kotlinmovement.recipes.model.IngredientRequest;
import nl.rabobank.kotlinmovement.recipes.model.IngredientRequestTest;
import nl.rabobank.kotlinmovement.recipes.model.IngredientResponseTest;
import nl.rabobank.kotlinmovement.recipes.model.IngredientTypeTest;
import nl.rabobank.kotlinmovement.recipes.model.RecipeRequestTest;
import nl.rabobank.kotlinmovement.recipes.model.RecipeResponseTest;
import nl.rabobank.kotlinmovement.recipes.model.RecipesErrorResponseTest;
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
    private RecipeRequestTest initRecipe;


    @Test
    @DisplayName("Should be able to get all recipes")
    void test1() throws Exception {
        final RecipeResponseTest actualRecipeResponse = getAllRecipePaginatedResponses()[0];
        assertThat(actualRecipeResponse.getId()).isNotNull();
        assertThat(actualRecipeResponse.getRecipeName()).isEqualTo(initRecipe.getRecipeName());
        doForEach(actualRecipeResponse.getIngredients(), (IngredientResponseTest ingredientResponse) -> {
            final IngredientRequestTest expected = findIngredientRequestByName(initRecipe, ingredientResponse);
            assertThat(ingredientResponse.getName()).isEqualTo(expected.getName());
            assertThat(ingredientResponse.getType()).isEqualTo(expected.getType());
            assertThat(ingredientResponse.getWeight()).isEqualTo(expected.getWeight());
        });
        assertThat(actualRecipeResponse.getRecipeName()).isEqualTo(initRecipe.getRecipeName());
    }

    @Test
    @DisplayName("Should be able to get a recipe")
    void test2() throws Exception {
        final RecipeResponseTest first = getAllRecipePaginatedResponses()[0];
        final RecipeResponseTest actualRecipeResponse = getRecipeResponses(first.getId());
        assertThat(actualRecipeResponse.getId()).isNotNull();
        assertThat(actualRecipeResponse.getRecipeName()).isEqualTo(initRecipe.getRecipeName());
        doForEach(actualRecipeResponse.getIngredients(), (IngredientResponseTest ingredientResponse) -> {
            final IngredientRequestTest expected = findIngredientRequestByName(initRecipe, ingredientResponse);
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
        final RecipeResponseTest[] allRecipePaginatedResponses = getAllRecipePaginatedResponses(page, pageSize, sortPropertyValue, sortDirectionDesc);
        assertThat(allRecipePaginatedResponses).hasSize(expectedPageSize);
    }


    @Test
    @DisplayName("Should be able to delete a recipe ")
    void test4() throws Exception {
        final RecipeResponseTest actualRecipeResponse = getAllRecipePaginatedResponses()[0];
        deleteRecipe(actualRecipeResponse.getId());
        final RecipeResponseTest[] actualRecipeResponseLatest = getAllRecipePaginatedResponses();
        assertThat(actualRecipeResponseLatest).isEmpty();
    }

    @Test
    @DisplayName("Should be able to update a recipe")
    void test5() throws Exception {
        final RecipeResponseTest firstRecipe = getAllRecipePaginatedResponses()[0];
        final RecipeRequestTest updateRequest = peperoniPizzaRecipeRequest();
        final Long actualRecipeResponseId = firstRecipe.getId();
        final RecipeResponseTest updatedRecipeResponse = updateRecipe(actualRecipeResponseId, updateRequest);

        assertThat(updatedRecipeResponse.getId()).isEqualTo(firstRecipe.getId());
        assertThat(updatedRecipeResponse.getRecipeName()).isEqualTo(updatedRecipeResponse.getRecipeName());
        doForEach(updatedRecipeResponse.getIngredients(), (IngredientResponseTest ingredientResponse) -> {
            final IngredientRequestTest expected = findIngredientRequestByName(updateRequest, ingredientResponse);
            assertThat(ingredientResponse.getName()).isEqualTo(expected.getName());
            assertThat(ingredientResponse.getType()).isEqualTo(expected.getType());
            assertThat(ingredientResponse.getWeight()).isEqualTo(expected.getWeight());
        });

    }

    @Test
    @DisplayName("Should be able create when resource id doesn't not exist")
    void test6() throws Exception {
        final RecipeRequestTest updateRequest = peperoniPizzaRecipeRequest();
        final RecipeResponseTest updatedRecipeResponse = updateRecipe(2L, updateRequest);

        assertThat(updatedRecipeResponse.getId()).isEqualTo(2L);
        assertThat(updatedRecipeResponse.getRecipeName()).isEqualTo(updatedRecipeResponse.getRecipeName());
        doForEach(updatedRecipeResponse.getIngredients(), (IngredientResponseTest ingredientResponse) -> {
            final IngredientRequestTest expected = findIngredientRequestByName(updateRequest, ingredientResponse);
            assertThat(ingredientResponse.getName()).isEqualTo(expected.getName());
            assertThat(ingredientResponse.getType()).isEqualTo(expected.getType());
            assertThat(ingredientResponse.getWeight()).isEqualTo(expected.getWeight());
        });
    }

    @Test
    @DisplayName("Should return not found if resource does not exist")
    void test7() throws Exception {
        assertRecipeResponse(get("/recipes/{id}", 2L), status().isNotFound(),
                objectMapper.writeValueAsString(new RecipesErrorResponseTest("Recipe 2 not found")));
        assertRecipeResponse(delete("/recipes/{id}", 2L), status().isNotFound(),
                objectMapper.writeValueAsString(new RecipesErrorResponseTest("Recipe 2 not found")));
    }

    @ParameterizedTest
    @MethodSource("errorDataParams")
    @DisplayName("Should not be able to create/update if request object is invalid")
    void test8(String recipeRequest, String errorMessage) throws Exception {
        assertRecipeResponse(
                post("/recipes")
                        .content(recipeRequest)
                        .contentType(MediaType.APPLICATION_JSON), status()
                        .is4xxClientError(),
                errorMessage
        );

        assertRecipeResponse(
                put("/recipes/{id}", 1)
                        .content(recipeRequest)
                        .contentType(MediaType.APPLICATION_JSON), status()
                        .is4xxClientError(),
                errorMessage
        );
    }

    @BeforeEach
    void setup() {
        setInitialState();
    }

    private static Stream<Arguments> errorDataParams() throws JsonProcessingException {
        final var noContent = "{}";
        final var emptyRequest = objectMapper.writeValueAsString(new RecipeRequestTest("", Set.of()));
        final var emptyRequestIngredient = objectMapper.writeValueAsString(new RecipeRequestTest("pizza!", Set.of()));
        final var nullRecipeNameRequest = objectMapper.writeValueAsString(new RecipeRequestTest(null, Set.of(new IngredientRequestTest("flower", IngredientTypeTest.DRY, 100))));
        final var nullIngredientsRequest = objectMapper.writeValueAsString(new RecipeRequestTest("test", null));
        final var ingredientMissingName = objectMapper.writeValueAsString(new RecipeRequestTest("test", Set.of(new IngredientRequestTest("", IngredientTypeTest.DRY, 100))));
        final var ingredientMissingType = objectMapper.writeValueAsString(new RecipeRequestTest("test", Set.of(new IngredientRequestTest("yeast", null, 100))));
        final var ingredientMissingWeight = objectMapper.writeValueAsString(new RecipeRequestTest("test", Set.of(new IngredientRequestTest("flower", IngredientTypeTest.DRY, null))));

        final var errorMessageIncorrectRecipe = objectMapper.writeValueAsString(new RecipesErrorResponseTest("Incorrect fields:ingredients,recipeName."));
        final var errorMessageIncorrectRecipeName = objectMapper.writeValueAsString(new RecipesErrorResponseTest("Incorrect fields:recipeName."));
        final var errorMessageIncorrectIngredients = objectMapper.writeValueAsString(new RecipesErrorResponseTest("Incorrect fields:ingredients."));
        final var errorMessageIncorrectIngredientName = objectMapper.writeValueAsString(new RecipesErrorResponseTest("Incorrect fields:ingredient.name."));
        final var errorMessageIncorrectIngredientType = objectMapper.writeValueAsString(new RecipesErrorResponseTest("Incorrect fields:ingredient.type."));
        final var errorMessageIncorrectWeight = objectMapper.writeValueAsString(new RecipesErrorResponseTest("Incorrect fields:ingredient.weight."));


        return Stream.of(
                Arguments.of(noContent, errorMessageIncorrectRecipe),
                Arguments.of(emptyRequest, errorMessageIncorrectRecipe),
                Arguments.of(emptyRequestIngredient, errorMessageIncorrectIngredients),
                Arguments.of(nullRecipeNameRequest, errorMessageIncorrectRecipeName),
                Arguments.of(nullIngredientsRequest, errorMessageIncorrectIngredients),
                Arguments.of(ingredientMissingName, errorMessageIncorrectIngredientName),
                Arguments.of(ingredientMissingType, errorMessageIncorrectIngredientType),
                Arguments.of(ingredientMissingWeight, errorMessageIncorrectWeight)
        );
    }

    private RecipeResponseTest updateRecipe(Long id, RecipeRequestTest recipeRequest) throws Exception {
        final var contentAsString = this.mockMvc.perform(
                        put("/recipes/{id}", id)
                                .content(objectMapper.writeValueAsString(recipeRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readValue(contentAsString, RecipeResponseTest.class);
    }

    private void deleteRecipe(Long id) throws Exception {
        this.mockMvc.perform(delete("/recipes/{id}", id)).andExpect(status().isNoContent());
    }

    private <T> void doForEach(Set<T> items, Consumer<T> asserts) {
        items.forEach(asserts);
    }

    private IngredientRequestTest findIngredientRequestByName(RecipeRequestTest recipeRequest, IngredientResponseTest ingredientResponse) {
        return recipeRequest.getIngredients()
                .stream()
                .filter(it -> it.getName().equals(ingredientResponse.getName()))
                .findFirst().orElseThrow(AssertionError::new);
    }

    RecipeResponseTest[] getAllRecipePaginatedResponses(Integer page, Integer size, String sort, String sortDirectionDesc) throws Exception {
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

    private RecipeResponseTest[] getRecipeResponses(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        var responseArrayString = this.mockMvc
                .perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();
        return objectMapper.readValue(responseArrayString, RecipeResponseTest[].class);
    }

    private RecipeResponseTest[] getAllRecipePaginatedResponses() throws Exception {
        return getRecipeResponses(get("/recipes"));
    }

    private RecipeResponseTest getRecipeResponses(Long id) throws Exception {
        var responseArrayString = this.mockMvc
                .perform(get("/recipes/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readValue(responseArrayString, RecipeResponseTest.class);
    }

    private void assertRecipeResponse(MockHttpServletRequestBuilder mockHttpServletRequestBuilder, ResultMatcher statusMatcher, String expectedBody) throws Exception {
        var responseString = this.mockMvc
                .perform(mockHttpServletRequestBuilder)
                .andDo(print())
                .andExpect(statusMatcher)
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(responseString).isEqualTo(expectedBody);

    }

    private RecipeRequestTest peperoniPizzaRecipeRequest() {
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

    private void setInitialState() {
        final Set<IngredientRequestTest> ingredients = getDefaultIngredientRequests();
        initRecipe = new RecipeRequestTest("Pizza", ingredients);
        createRecipes(List.of(initRecipe));
    }

    private Set<IngredientRequestTest> getDefaultIngredientRequests() {
        return Set.of(
                new IngredientRequestTest("Flower", IngredientTypeTest.DRY, 1000),
                new IngredientRequestTest("Water", IngredientTypeTest.WET, 8000),
                new IngredientRequestTest("Salt", IngredientTypeTest.DRY, 20),
                new IngredientRequestTest("Yeast", IngredientTypeTest.DRY, 2)
        );
    }

    private void createRecipes(List<RecipeRequestTest> recipes) {
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

    private List<RecipeRequestTest> generateRecipeRequest(int i) {
        final var recipeRequests = new ArrayList<RecipeRequestTest>(1);
        final String generatedString = generateRandomString();
        while (i > 0) {
            i--;
            recipeRequests.add(new RecipeRequestTest(generatedString, getDefaultIngredientRequests()));
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
