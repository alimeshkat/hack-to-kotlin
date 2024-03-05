package nl.rabobank.kotlinmovement.recipes

import kotlinx.coroutines.runBlocking
import nl.rabobank.kotlinmovement.recipes.test.util.RecipeAssert.assertRecipeResponses
import nl.rabobank.kotlinmovement.recipes.test.util.RecipeTest
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipeResponseTest
import nl.rabobank.kotlinmovement.recipes.test.util.model.RecipesErrorResponseTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.http.HttpMethod

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class GetRecipesControllerTest : RecipeTest() {

    private lateinit var initRecipes: Array<RecipeResponseTest>
    @BeforeEach
    fun setup(): Unit = runBlocking {
        initRecipes = setInitialState(10)
    }
    @Test
    fun `Should be able to get all recipes`() {
        val actual = allRecipes()
        assertRecipeResponses(actual, initRecipes)
    }

    @Test
    fun `Should be able to get a recipe`(){
        assertThat(initRecipes).contains( getRecipe(1L))
    }

    @Test
    fun `Should return not found if resource does not exist`()  {
        val actual = notFoundCall(HttpMethod.GET, "/recipes/102")
        AssertionsForClassTypes.assertThat(actual).isEqualTo(RecipesErrorResponseTest("Recipe 102 not found"))
    }

}
