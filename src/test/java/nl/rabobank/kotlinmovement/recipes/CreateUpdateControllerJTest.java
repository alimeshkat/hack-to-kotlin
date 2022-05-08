
package nl.rabobank.kotlinmovement.recipes;

import com.fasterxml.jackson.core.JsonProcessingException;
import nl.rabobank.kotlinmovement.recipes.domain.RecipeRequestJ;
import nl.rabobank.kotlinmovement.recipes.testutil.RecipeMockMvcJTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static nl.rabobank.kotlinmovement.recipes.testutil.TestData.peperoniPizzaRecipeJ;
import static nl.rabobank.kotlinmovement.recipes.testutil.TestData.pizzaRecipeJ;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class CreateUpdateControllerJTest extends RecipeMockMvcJTest {

    @Test
    @DisplayName("Should be able to update a recipe")
    void test1() throws Exception {
        final long recipeId = 1L;
        assertRecipeResponse(
                recipeId,
                updateRecipe(recipeId, peperoniPizzaRecipeJ()),
                peperoniPizzaRecipeJ()
        );
    }

    @Test
    @DisplayName("Should be able create when resource id doesn't not exist")
    void test2() throws Exception {
        final long recipeId = 2L;
        assertRecipeResponse(
                recipeId,
                updateRecipe(recipeId, peperoniPizzaRecipeJ()),
                peperoniPizzaRecipeJ()
        );
    }

    @ParameterizedTest
    @MethodSource("errorDataParams")
    @DisplayName("Should not be able to create/update if request object is invalid")
    void test4(String recipeRequest) throws Exception {
        assertCallStatus(
                post("/recipes")
                        .content(recipeRequest)
                        .contentType(MediaType.APPLICATION_JSON), status()
                        .is4xxClientError()
        );

        assertCallStatus(
                put("/recipes/{id}", 1)
                        .content(recipeRequest)
                        .contentType(MediaType.APPLICATION_JSON), status()
                        .is4xxClientError()
        );
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

    @BeforeEach
    void setup() {
        createRecipes(List.of(pizzaRecipeJ));
    }

}
