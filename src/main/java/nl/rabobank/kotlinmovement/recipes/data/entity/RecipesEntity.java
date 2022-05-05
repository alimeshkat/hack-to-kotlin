package nl.rabobank.kotlinmovement.recipes.data.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "recipes")
public class RecipesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recipeName;

    @OneToMany(mappedBy = "recipes", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<IngredientsEntity> ingredients = Collections.emptySet();

    @Override
    public String toString() {
       return String.format("RecipesEntity(id=%d, recipeName=%s)", id, recipeName);
    }
}

