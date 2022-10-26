package nl.rabobank.kotlinmovement.recipes.data
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "ingredients")
data class IngredientsEntity(
    @Id
    val ingredientId: Long? = null,
    val name: String,
    val type: String,
    val weight: Int,
    val recipeId: Long
)

@Table(name = "recipes")
data class RecipesEntity(
    @Id
    val recipeId: Long? = null,
    val recipeName: String,
)
