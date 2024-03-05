package nl.rabobank.kotlinmovement.recipes.data;


import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "recipes")
public final class RecipesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;
    @NonNull
    private String recipeName;
    @OneToMany(mappedBy = "recipes", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<IngredientsEntity> ingredients = Collections.emptySet();
}

