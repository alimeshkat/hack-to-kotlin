package nl.rabobank.kotlinmovement.recipes;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rabobank.kotlinmovement.recipes.domain.model.IngredientRequest;
import nl.rabobank.kotlinmovement.recipes.domain.model.IngredientResponse;
import nl.rabobank.kotlinmovement.recipes.domain.model.RecipeRequest;
import nl.rabobank.kotlinmovement.recipes.domain.model.RecipeResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;
import java.util.function.Consumer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RecipesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    private RecipeRequest initRecipe;

    @Test
    @DisplayName("Should be able to get all recipes")
    void test1() throws Exception {
        final RecipeResponse actualRecipeResponse = getAllRecipeResponses()[0];
        assertThat(actualRecipeResponse.getId()).isNotNull();
        assertThat(actualRecipeResponse.getRecipeName()).isEqualTo(initRecipe.getRecipeName());
        doForEach(actualRecipeResponse.getIngredients(), (IngredientResponse ingredientResponse) -> {
            final IngredientRequest expected = findIngredientRequestByName(ingredientResponse);
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
            final IngredientRequest expected = findIngredientRequestByName(ingredientResponse);
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
            final IngredientRequest expected = findIngredientRequestByName(ingredientResponse);
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

    //TODO fix test for update
    @Test
    @DisplayName("Should be able to update a recipe")
    void test5() throws Exception {
        final RecipeResponse actualRecipeResponse = getAllRecipeResponses()[0];
        final Set<IngredientRequest> ingredients = Set.of(
                new IngredientRequest("Flower", "Dry", 1000),
                new IngredientRequest("Water", "Wet", 8000),
                new IngredientRequest("Salt", "Dry", 20),
                new IngredientRequest("Yeast", "Dry", 2),
                new IngredientRequest("Peperoni", "Dry", 100),
                new IngredientRequest("Tomato sauce", "Wet", 100)

        );
        final Long actualRecipeResponseId = actualRecipeResponse.getId();
        final String newRecipeName = "Pizza Peperoni";
        final RecipeResponse updatedRecipeResponse = updateRecipe(actualRecipeResponseId, newRecipeName, ingredients);

        assertThat(updatedRecipeResponse.getId()).isEqualTo(actualRecipeResponse.getId());
        assertThat(updatedRecipeResponse.getRecipeName()).isEqualTo(updatedRecipeResponse.getRecipeName());
//        doForEach(updatedRecipeResponse.getIngredients(), (IngredientResponse ingredientResponse) -> {
//
//            assertThat(ingredientResponse.getName()).isEqualTo(expected.getName());
//            assertThat(ingredientResponse.getType()).isEqualTo(expected.getType());
//            assertThat(ingredientResponse.getWeight()).isEqualTo(expected.getWeight());
//        });

    }


    private RecipeResponse updateRecipe(Long id, String name, Set<IngredientRequest> ingredientRequests) throws Exception {
        final var updateRequest = new RecipeRequest(name, ingredientRequests);

        final String contentAsString = this.mockMvc.perform(
                        put("/recipes/{id}", id)
                                .content(objectMapper.writeValueAsString(updateRequest))
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

    @BeforeEach
    void seedDb() throws Exception {
        final Set<IngredientRequest> ingredients = Set.of(
                new IngredientRequest("Flower", "Dry", 1000),
                new IngredientRequest("Water", "Wet", 8000),
                new IngredientRequest("Salt", "Dry", 20),
                new IngredientRequest("Yeast", "Dry", 2)

        );
        initRecipe = new RecipeRequest("Pizza", ingredients);

        this.mockMvc.perform(post("/recipes")
                .content(objectMapper.writeValueAsString(initRecipe)).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());
    }

    @AfterEach
    void cleanup() throws Exception {
        this.mockMvc.perform(delete("/recipes")
                .content(objectMapper.writeValueAsString(initRecipe)).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());
    }

    private <T> void doForEach(Set<T> ingredients, Consumer<T> asserts) {
        ingredients.forEach(asserts);
    }

    private IngredientRequest findIngredientRequestByName(IngredientResponse ingredientResponse) {
        return initRecipe.getIngredients()
                .stream()
                .filter(it -> it.getName().equals(ingredientResponse.getName()))
                .findFirst().get();
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

}
