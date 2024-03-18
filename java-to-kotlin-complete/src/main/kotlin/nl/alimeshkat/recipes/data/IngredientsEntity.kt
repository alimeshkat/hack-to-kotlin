package nl.alimeshkat.recipes.data

import jakarta.persistence.*

@Entity
@Table(name = "ingredients")
class IngredientsEntity(
    @ManyToOne(optional = false)
    @JoinColumn(name = "recipes_id", nullable = false)
    val recipes: RecipesEntity? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val ingredientId: Long? = null,
    val name: String,
    val type: String,
    val weight: Int,
) {

    override fun equals(other: Any?): Boolean {
        return when {
            other !is IngredientsEntity -> false
            this === other -> true
            ingredientId == null || other.ingredientId == null -> false
            else -> ingredientId == other.ingredientId
        }
    }

    override fun hashCode(): Int {
        return ingredientId?.hashCode() ?: super.hashCode()
    }
}
