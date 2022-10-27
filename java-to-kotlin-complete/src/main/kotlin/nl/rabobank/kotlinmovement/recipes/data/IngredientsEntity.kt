package nl.rabobank.kotlinmovement.recipes.data

import javax.persistence.*

@Entity
@Table(name = "ingredients")
class IngredientsEntity(
    @ManyToOne(optional = false)
    @JoinColumn(name = "recipes_id", nullable = false)
    val recipes: RecipesEntity? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val type: String,
    val weight: Int,
) {

    override fun equals(other: Any?): Boolean {
        return when {
            other !is IngredientsEntity -> false
            this === other -> true
            id == null || other.id == null -> false
            else -> id == other.id
        }
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: super.hashCode()
    }
}
