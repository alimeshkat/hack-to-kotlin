package nl.rabobank.kotlinmovement.recipes.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeResponse {
    private Long id;
    private String recipeName;
    private Set<IngredientResponse> ingredients;
}
