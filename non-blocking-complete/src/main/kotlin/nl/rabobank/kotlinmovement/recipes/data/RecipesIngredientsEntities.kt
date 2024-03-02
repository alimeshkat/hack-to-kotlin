package nl.rabobank.kotlinmovement.recipes.data
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.ReadOnlyProperty
import org.springframework.data.relational.core.mapping.Table

@Table(name = "ingredients")
data class IngredientsEntity(
    val recipeId: Long?,
    @Id
    val ingredientId: Long? = null,
    val name: String,
    val weight: Int,
    val type: String
)

@Table(name = "recipes")
data class RecipesEntity(
    @Id
    val recipeId: Long? = null,
    val recipeName: String,
    @ReadOnlyProperty
    val ingredients: Set<IngredientsEntity>  = emptySet()
)
