package nl.alimeshkat.recipes.data

import jakarta.persistence.*

@Entity
@Table(name = "recipes")
class RecipesEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val recipeId: Long? = null,
    val recipeName: String,
    @OneToMany(mappedBy = "recipes", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val ingredients: Set<IngredientsEntity>? = emptySet()
)
