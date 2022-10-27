package nl.rabobank.kotlinmovement.recipes.data

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
