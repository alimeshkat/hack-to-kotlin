package nl.rabobank.kotlinmovement.recipes.data

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import javax.persistence.*

@Table(name = "recipes")
class RecipesEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val recipeName: String,
    @OneToMany(mappedBy = "recipes", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val ingredients: Set<IngredientsEntity>? = emptySet()
)
