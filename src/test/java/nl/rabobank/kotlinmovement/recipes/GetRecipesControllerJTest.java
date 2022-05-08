package nl.rabobank.kotlinmovement.recipes;

import nl.rabobank.kotlinmovement.recipes.domain.RecipeResponseJ;
import nl.rabobank.kotlinmovement.recipes.testutil.RecipeMockMvcJTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static nl.rabobank.kotlinmovement.recipes.testutil.TestData.generateRecipeRequest;
import static nl.rabobank.kotlinmovement.recipes.testutil.TestData.pizzaRecipeJ;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class GetRecipesControllerJTest extends RecipeMockMvcJTest {

    @Test
    @DisplayName("Should be able to get all recipes")
    void test1() throws Exception {
        final RecipeResponseJ[] recipes = getRecipes();
        assertThat(recipes).hasSize(1);
        assertRecipeResponse(1, recipes[0], pizzaRecipeJ);
    }

    @Test
    @DisplayName("Should be able to get a recipe")
    void test2() throws Exception {
        assertRecipeResponse(1, getRecipes(1L), pizzaRecipeJ);
    }

    @Test
    @DisplayName("Should return not found if resource does not exist")
    void test3() throws Exception {
        assertCallStatus(get("/recipes/{id}", 2L), status().isNotFound());

    }

    @ParameterizedTest
    @MethodSource("sortedPagesRecipeDataProvider")
    @DisplayName("Should be able to get sorted paged recipes")
    void test4(Integer page,
               Integer pageSize,
               Integer expectedPageSize,
               String sortPropertyValue,
               String sortDirectionDesc) throws Exception {
        createRecipes(generateRecipeRequest(40));
        final RecipeResponseJ[] allRecipePaginatedResponses = getPaginatedRecipes(page, pageSize, sortPropertyValue, sortDirectionDesc);
        assertThat(allRecipePaginatedResponses).hasSize(expectedPageSize);
    }

    private static Stream<Arguments> sortedPagesRecipeDataProvider() {
        final String sortDescending = "desc";
        final String sortAscending = "asc";
        final String sortProperty = "recipeName";
        return Stream.of(
                Arguments.of(0, 10, 10, sortProperty, sortDescending),
                Arguments.of(1, 10, 10, sortProperty, sortDescending),
                Arguments.of(2, 10, 10, sortProperty, sortAscending),
                Arguments.of(3, 10, 10, sortProperty, sortDescending),
                Arguments.of(3, 10, 10, sortProperty, null),
                Arguments.of(0, null, 20, null, null)
        );
    }

    @BeforeEach
    void setup() {
        createRecipes(List.of(pizzaRecipeJ));
    }

}
