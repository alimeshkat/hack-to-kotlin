package nl.rabobank.kotlinmovement.recipes.data

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "recipes")
class RecipesEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val recipeName: String,
    @OneToMany(mappedBy = "recipes", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val ingredients: Set<IngredientsEntity>? = emptySet()
)
