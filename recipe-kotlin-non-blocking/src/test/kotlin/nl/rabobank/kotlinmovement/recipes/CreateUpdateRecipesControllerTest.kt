//package nl.rabobank.kotlinmovement.recipes
//
//import nl.rabobank.kotlinmovement.recipes.test.util.RecipeAssert.assertRecipeResponse
//import nl.rabobank.kotlinmovement.recipes.test.util.RecipeMockMvcTest
//import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.emptyRequest
//import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.emptyRequestIngredient
//import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.errorMessageIncorrectIngredientName
//import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.errorMessageIncorrectIngredientType
//import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.errorMessageIncorrectIngredients
//import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.errorMessageIncorrectRecipe
//import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.errorMessageIncorrectRecipeName
//import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.errorMessageIncorrectWeight
//import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.ingredientMissingName
//import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.ingredientMissingType
//import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.ingredientMissingWeight
//import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.nullIngredientsRequest
//import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.nullRecipeNameRequest
//import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTestData.peperoniPizzaRecipeRequest
//import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeRequestTest
//import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipesErrorResponseTest
//import org.assertj.core.api.AssertionsForClassTypes
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.DisplayName
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.TestInstance
//import org.junit.jupiter.params.ParameterizedTest
//import org.junit.jupiter.params.provider.Arguments
//import org.junit.jupiter.params.provider.MethodSource
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
//import java.util.stream.Stream
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//internal class CreateUpdateRecipesControllerTest : RecipeMockMvcTest() {
//    @Test
//    @DisplayName("Should be able to update a recipe")
//    @Throws(
//        Exception::class
//    )
//    fun test1() {
//        val updateRequest: RecipeRequestTest = peperoniPizzaRecipeRequest
//        val updatedRecipeResponse = updateRecipe(1L, updateRequest)
//        assertRecipeResponse(updateRequest, updatedRecipeResponse)
//    }
//
//    @Test
//    @DisplayName("Should be to able create new recipe when recipe id doesn't not exist")
//    @Throws(
//        Exception::class
//    )
//    fun test2() {
//        val updateRequest: RecipeRequestTest = peperoniPizzaRecipeRequest
//        val updatedRecipeResponse = updateRecipe(2L, updateRequest)
//        assertRecipeResponse(updateRequest, updatedRecipeResponse)
//    }
//
//    @ParameterizedTest
//    @MethodSource("errorDataParams")
//    @DisplayName("Should not be able to create/update if request object is invalid")
//    @Throws(
//        Exception::class
//    )
//    fun test3(recipeRequest: RecipeRequestTest?, errorResponse: RecipesErrorResponseTest?) {
//        val content = objectMapper.writeValueAsString(recipeRequest)
//        val invalidCreateRecipe = badRequestCall(MockMvcRequestBuilders.post("/recipes").content(content))
//        val invalidUpdateRecipe = badRequestCall(MockMvcRequestBuilders.put("/recipes/{id}", 1).content(content))
//        AssertionsForClassTypes.assertThat(invalidCreateRecipe).isEqualTo(errorResponse)
//        AssertionsForClassTypes.assertThat(invalidUpdateRecipe).isEqualTo(errorResponse)
//    }
//
//    @BeforeEach
//    @Throws(Exception::class)
//    fun setup() {
//        setInitialState()
//    }
//
//    private fun errorDataParams(): Stream<Arguments> {
//        return Stream.of(
//            Arguments.of(emptyRequest, errorMessageIncorrectRecipe),
//            Arguments.of(emptyRequestIngredient, errorMessageIncorrectIngredients),
//            Arguments.of(nullRecipeNameRequest, errorMessageIncorrectRecipeName),
//            Arguments.of(nullIngredientsRequest, errorMessageIncorrectIngredients),
//            Arguments.of(ingredientMissingName, errorMessageIncorrectIngredientName),
//            Arguments.of(ingredientMissingType, errorMessageIncorrectIngredientType),
//            Arguments.of(ingredientMissingWeight, errorMessageIncorrectWeight)
//        )
//    }
//
//}
